package de.bht.ebus.spotsome.util;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import de.bht.ebus.spotsome.model.Message;

/**
 * Note: this comparator imposes orderings that are inconsistent with equals.
 * 
 * @author Steven Maasch
 *
 */
public class MessageDateComperator implements Comparator<Message>, Serializable {

	private static final long serialVersionUID = 7192152288257703225L;

	@Override
	public int compare(Message msg1, Message msg2) {
		final Date msg1CreationDate = msg1.getCreatedOn();
		final Date msg2CreationDate = msg2.getCreatedOn();
		return msg1CreationDate.compareTo(msg2CreationDate);
	}

}
