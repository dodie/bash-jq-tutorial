package hu.advancedweb.blogexample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

// https://stackoverflow.com/questions/34048978/convert-list-in-entity-to-single-string-column-in-database/34061754
@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> list) {
	return String.join(",", list);
    }

    @Override
    public List<String> convertToEntityAttribute(String joined) {
	return new ArrayList<>(Arrays.asList(joined.split(",")));
    }

}