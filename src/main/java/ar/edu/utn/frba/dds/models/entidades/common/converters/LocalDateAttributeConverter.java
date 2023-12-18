package ar.edu.utn.frba.dds.models.entidades.common.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDate) {
        return localDate == null? null: Timestamp.valueOf(localDate);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp date) {
        return date == null? null : date.toLocalDateTime();
    }
}
