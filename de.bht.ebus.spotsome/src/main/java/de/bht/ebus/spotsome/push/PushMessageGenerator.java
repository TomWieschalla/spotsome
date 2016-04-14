package de.bht.ebus.spotsome.push;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PushMessageGenerator {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static String jsonify(Object message) {
		try {
			return objectMapper.writeValueAsString(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw (RuntimeException) e;
		}
	}

	private static Map<String, String> getData(String message) {
		Map<String, String> payload = new HashMap<String, String>();
		payload.put("message", message);
		return payload;
	}

	public static PushMessage createAppleMessage(String alert, Integer badge, String sound) {
		Map<String, Object> appleMessageMap = new HashMap<String, Object>();
		Map<String, Object> appMessageMap = new HashMap<String, Object>();
//		appMessageMap.put("alert", "You have got email.");
//		appMessageMap.put("badge", 9);
//		appMessageMap.put("sound", "default");
//		appleMessageMap.put("aps", appMessageMap);
		appMessageMap.put("alert", alert);
		appMessageMap.put("badge", badge);
		appMessageMap.put("sound", (sound != null) ? sound :"default");
		appleMessageMap.put("aps", appMessageMap);
		return new PushMessage(jsonify(appleMessageMap), Platform.APNS);
	}

	public static PushMessage createAndroidMessage(String collapseKey, String message) {
		Map<String, Object> androidMessageMap = new HashMap<String, Object>();
//		androidMessageMap.put("collapse_key", "Welcome");
//		androidMessageMap.put("data", getData());
//		androidMessageMap.put("delay_while_idle", true);
//		androidMessageMap.put("time_to_live", 125);
//		androidMessageMap.put("dry_run", false);
		androidMessageMap.put("collapse_key", Objects.requireNonNull(collapseKey, "Collapse key cannot be null."));
		androidMessageMap.put("data", getData(Objects.requireNonNull(message, "Message cannot be null.")));
		androidMessageMap.put("delay_while_idle", true);
		androidMessageMap.put("time_to_live", 125);
		androidMessageMap.put("dry_run", false);
		return new PushMessage(jsonify(androidMessageMap), Platform.GCM);
	}
}