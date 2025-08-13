package com.v2solutions.books.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Converter(autoApply = true)
public class OffsetDateTimeConverter implements AttributeConverter<OffsetDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(OffsetDateTime attribute) {
        return (attribute == null) ? null : Timestamp.from(attribute.toInstant());
    }

    @Override
    public OffsetDateTime convertToEntityAttribute(Timestamp dbData) {
        return (dbData == null) ? null : dbData.toInstant().atOffset(ZoneOffset.UTC);
    }
}