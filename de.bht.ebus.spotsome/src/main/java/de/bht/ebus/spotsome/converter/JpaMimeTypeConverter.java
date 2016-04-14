package de.bht.ebus.spotsome.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

@Converter(autoApply = true)
public class JpaMimeTypeConverter implements AttributeConverter<MimeType, String>{

	public String convertToDatabaseColumn(MimeType attribute) {
		return attribute.toString();
	}

	public MimeType convertToEntityAttribute(String dbData) {
		try {
			return MimeTypes.getDefaultMimeTypes().forName(dbData);
		} catch (MimeTypeException e) {
			throw new RuntimeException("Could not convert dbData " + dbData + " to valid mime type");
		}
	}

}
