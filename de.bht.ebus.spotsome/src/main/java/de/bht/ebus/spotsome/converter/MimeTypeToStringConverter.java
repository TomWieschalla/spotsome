package de.bht.ebus.spotsome.converter;

import org.apache.tika.mime.MimeType;

import com.fasterxml.jackson.databind.util.StdConverter;

public class MimeTypeToStringConverter extends StdConverter<MimeType, String> {

	@Override
	public String convert(MimeType mimeType) {
		return mimeType.toString();
	}

}
