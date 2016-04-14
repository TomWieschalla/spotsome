package de.bht.ebus.spotsome.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.Transactional;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.google.common.collect.Lists;

import de.bht.ebus.spotsome.exceptions.ChatNotFoundException;
import de.bht.ebus.spotsome.exceptions.SpotNotFoundException;
import de.bht.ebus.spotsome.model.Chat;
import de.bht.ebus.spotsome.model.ChatMessage;
import de.bht.ebus.spotsome.model.Message;
import de.bht.ebus.spotsome.model.Spot;
import de.bht.ebus.spotsome.model.User;
import de.bht.ebus.spotsome.push.PushMessage;
import de.bht.ebus.spotsome.push.PushMessageGenerator;
import de.bht.ebus.spotsome.push.SnsMobilePushService;
import de.bht.ebus.spotsome.repositories.ChatRepository;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

	private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);
	
	@Autowired
	private SnsMobilePushService pushService;
	
    /**
     * Timeout for long polling connections in milliseconds
     */
    public static final Long LONG_POLLING_TIMEOUT = 120000L;
    
    /**
     * Response body for connection timeout
     */
    private static final List<Message> LONG_POLLING_TIMEOUT_RESPONSE = new ArrayList<Message>(0);
	
    private final Map<DeferredResult<List<ChatMessage>>, ImmutablePair<Long, Long>> chatRequests =
    		new ConcurrentHashMap<DeferredResult<List<ChatMessage>>, ImmutablePair<Long, Long>>();
    
	@Autowired
	private ChatRepository chatRepository;
	
	@Autowired
	private SpotService spotService;
	
	@Override
	public Chat getChatById(Long id) throws ChatNotFoundException {
		final Chat chat = chatRepository.findOne(id);
		if (chat == null) {
			final String msg = "No chat with id " + id + " found";
			logger.warn(msg);
			throw new ChatNotFoundException(msg);
		}
		return chat;
	}

	@Override
	public Iterable<Chat> getChatsBySpotId(Long spotId)
			throws SpotNotFoundException {
		if (spotService.getSpotById(spotId) == null)
			throw new SpotNotFoundException("Spot with id " + spotId + " not found.");
		return chatRepository.findAllBySpotId(spotId);
	}

	@Override
	public Chat createChat(String name, Long spotId, User creator) throws SpotNotFoundException {
		final Spot spot = spotService.getSpotById(spotId);
		if (spot == null) {
			logger.warn("No spot with id {} found", spotId);
			throw new SpotNotFoundException("No spot with id " + spotId + " found");
		}
		
		final Chat chat = new Chat(name, spot, creator);
		return chatRepository.save(chat);
	}

	@Override
	public void addMessageToChat(Long chatId, ChatMessage message)
			throws ChatNotFoundException {
		final Chat chat = getChatById(chatId);
		chat.addMessage(message);
		chatRepository.save(chat);
		sendResponseToAsyncRequestsByChatId(chatId);
		sendChatMessageAsPushNotification(message);
	}

	@Override
	public DeferredResult<List<ChatMessage>> getMessagesByChatIdAfterMessageIndexAsync(
			Long chatId, Long messageIndex) throws ChatNotFoundException {
		
		final DeferredResult<List<ChatMessage>> deferredResult =
				new DeferredResult<List<ChatMessage>>(LONG_POLLING_TIMEOUT, LONG_POLLING_TIMEOUT_RESPONSE);
		
		synchronized (chatRequests) {
			chatRequests.put(deferredResult, new ImmutablePair<Long, Long>(chatId, messageIndex));
		}
		
		deferredResult.onCompletion(new Runnable() {
			
			@Override
			public void run() {
				synchronized (chatRequests) {
					chatRequests.remove(deferredResult);
				}
			}
		});
		
		List<ChatMessage> chatMessages =
				Lists.newArrayList(
						getAllMessagesByChatIdAndGreaterThanMessageIdOrderByMessageId(chatId, messageIndex));
		if (!chatMessages.isEmpty()) {
			deferredResult.setResult(chatMessages);
		}
		
		return deferredResult;
		
	}

	private void sendResponseToAsyncRequestsByChatId(Long chatId) throws ChatNotFoundException {
		synchronized (chatRequests) {
			for (Entry<DeferredResult<List<ChatMessage>>, ImmutablePair<Long, Long>> entry : chatRequests.entrySet()) {
				final Long entryChatId = entry.getValue().left;
				final Long entryMessageIndex = entry.getValue().right;
				if (chatId.equals(entryChatId)) {
					final List<ChatMessage> chatMessages =
							Lists.newArrayList(
									getAllMessagesByChatIdAndGreaterThanMessageIdOrderByMessageId(entryChatId, entryMessageIndex));
					if (!chatMessages.isEmpty()) {
						entry.getKey().setResult(chatMessages);
					}
				}
			}
		}

	}

	@Override
	public Iterable<ChatMessage> getAllMessagesByChatId(Long chatId)
			throws ChatNotFoundException {
		getChatById(chatId);
		return chatRepository.findAllChatMessagesByChatIdOrderByCreatedOn(chatId);
	}
	
	@Override
	public Iterable<ChatMessage> getAllMessagesByChatIdAfterCreatedOnOrderByCreatedOn(Long chatId, Date after)
			throws ChatNotFoundException {
		getChatById(chatId);
		return chatRepository.findAllChatMessagesByChatIdAndAfterCreatedOnOrderByCreatedOn(chatId, after);
	}

	@Override
	public Iterable<ChatMessage> getAllMessagesByChatIdAndGreaterThanMessageIdOrderByMessageId(
			Long chatId, Long messageId) throws ChatNotFoundException {
		getChatById(chatId);
		return chatRepository.findAllChatMessagesByChatIdAndGreaterThanChatMessageIdOrderByChatMessageId(chatId, messageId);
	}
	
	private void sendChatMessageAsPushNotification(ChatMessage chatMessage) {
		//TODO: Messagetext could be null
		final PushMessage message = PushMessageGenerator.createAndroidMessage("Spotsome", chatMessage.getMessageText());
		pushService.sendAppNotification(message);
	}
	
}
