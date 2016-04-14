package de.bht.ebus.spotsome.services;

import de.bht.ebus.spotsome.exceptions.BulletinBoardNotFound;
import de.bht.ebus.spotsome.exceptions.SpotNotFoundException;
import de.bht.ebus.spotsome.model.BulletinBoard;
import de.bht.ebus.spotsome.model.BulletinBoardMessage;
import de.bht.ebus.spotsome.model.Location;
import de.bht.ebus.spotsome.model.User;

public interface BulletinBoardService {

	BulletinBoard getBulletinBoardById(Long id);
	
	Iterable<BulletinBoard> getBulletinBoardsBySpotId(Long id) throws SpotNotFoundException;
	
	Iterable<BulletinBoardMessage> getMessagesFromBulletinBoard(Long id) throws BulletinBoardNotFound;

	Iterable<BulletinBoard> getAllBulletinBoards();

	Iterable<BulletinBoard> getAllBulletinBoardsWithinRange(Location location);

	BulletinBoard createBulletinBoard(String name, Long spotId, User creator) throws SpotNotFoundException;

	void addMessageToBulletinBoard(Long id, BulletinBoardMessage message) throws BulletinBoardNotFound;

}
