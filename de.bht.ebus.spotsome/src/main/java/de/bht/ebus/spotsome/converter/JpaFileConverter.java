package de.bht.ebus.spotsome.converter;

import java.io.File;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class JpaFileConverter implements AttributeConverter<File, String> {

	public String convertToDatabaseColumn(File attribute) {
		return attribute.getAbsolutePath();
	}

	public File convertToEntityAttribute(String dbData) {
		return new File(dbData);
	}

}
