package de.bht.ebus.spotsome.services;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bht.ebus.spotsome.exceptions.BulletinBoardNotFound;
import de.bht.ebus.spotsome.exceptions.SpotNotFoundException;
import de.bht.ebus.spotsome.model.BulletinBoard;
import de.bht.ebus.spotsome.model.BulletinBoardMessage;
import de.bht.ebus.spotsome.model.Location;
import de.bht.ebus.spotsome.model.Spot;
import de.bht.ebus.spotsome.model.User;
import de.bht.ebus.spotsome.push.PushMessage;
import de.bht.ebus.spotsome.push.PushMessageGenerator;
import de.bht.ebus.spotsome.push.SnsMobilePushService;
import de.bht.ebus.spotsome.repositories.BulletinBoardRepository;

@Service
@Transactional
public class BulletinBoardServiceImpl implements BulletinBoardService {

	private static final Logger logger = LoggerFactory.getLogger(BulletinBoardServiceImpl.class);
	
	@Autowired
	private BulletinBoardRepository bulletinBoardRepository;
	
	@Autowired
	private SpotService spotService;
	
	@Autowired
	private SnsMobilePushService pushService;
	
	@Override
	public Iterable<BulletinBoard> getAllBulletinBoards() {
		return bulletinBoardRepository.findAll();
	}
	
	@Override
	public BulletinBoard getBulletinBoardById(Long id) {
		return bulletinBoardRepository.findOne(id);
	}
	
	@Override
	public Iterable<BulletinBoard> getAllBulletinBoardsWithinRange(
			Location location) {
		final Iterable<Spot> allSpotsWithinRange = spotService.getAllSpotsWithinRange(location);
		final List<Long> spotIdsWithinRange = new LinkedList<Long>();
		for (Spot spot : allSpotsWithinRange) {
			spotIdsWithinRange.add(spot.getId());
		}
		return bulletinBoardRepository.findAllBySpotIds(spotIdsWithinRange);
	}
	
	@Override
	public BulletinBoard createBulletinBoard(String name, Long spotId, User creator) throws SpotNotFoundException {
		final Spot spot = spotService.getSpotById(spotId);
		if (spot == null) {
			logger.warn("No spot with id {} found", spotId);
			throw new SpotNotFoundException("No spot with id " + spotId + " found");
		}
		
		final BulletinBoard bulletinBoard = new BulletinBoard(name, spot, creator);
		return bulletinBoardRepository.save(bulletinBoard);
	}

	@Override
	public Iterable<BulletinBoardMessage> getMessagesFromBulletinBoard(Long id) throws BulletinBoardNotFound {
		final BulletinBoard bulletinBoard = bulletinBoardRepository.findOne(id);
		if (bulletinBoard != null) {
			return bulletinBoard.getMessages();
		} else {
			logger.warn("No bulletin board with id {} found", id);
			throw new BulletinBoardNotFound("No bulletin board with id " + id + " found");
		}
	}

	@Override
	public void addMessageToBulletinBoard(Long id, BulletinBoardMessage message) throws BulletinBoardNotFound {
		final BulletinBoard bulletinBoard = bulletinBoardRepository.findOne(id);
		if (bulletinBoard == null) {
			logger.warn("No bulletin board with id {} found", id);
			throw new BulletinBoardNotFound("No bulletin board with id " + id + " found");
		}
		bulletinBoard.addMessage(message);
		bulletinBoardRepository.save(bulletinBoard);
		sendBulletinBoardMessageAsPushNotification(message);
	}

	@Override
	public Iterable<BulletinBoard> getBulletinBoardsBySpotId(final Long id)
			throws SpotNotFoundException {
		if (spotService.getSpotById(id) == null)
			throw new SpotNotFoundException("Spot with id " + id + " not found.");
		return bulletinBoardRepository.findAllBySpotId(id);
	}
	
	private void sendBulletinBoardMessageAsPushNotification(BulletinBoardMessage bulletinBoardMessage) {
		//TODO: Messagetext could be null
		final PushMessage message = PushMessageGenerator.createAndroidMessage("Spotsome", bulletinBoardMessage.getMessageText());
		pushService.sendAppNotification(message);
	}

}
