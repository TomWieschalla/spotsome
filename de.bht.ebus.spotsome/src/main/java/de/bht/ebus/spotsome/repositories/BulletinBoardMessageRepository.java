package de.bht.ebus.spotsome.repositories;

import org.springframework.data.repository.CrudRepository;

import de.bht.ebus.spotsome.model.BulletinBoardMessage;

public interface BulletinBoardMessageRepository extends CrudRepository<BulletinBoardMessage, Long> { }
