package com.wevserver.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

@Configuration
@RequiredArgsConstructor
public class JdbcConfiguration extends AbstractJdbcConfiguration {

    private final ObjectMapper objectMapper;

    @Override
    protected List<?> userConverters() {

        return Arrays.asList(
                new JdbcConverterStringStringMap.StringStringMapToJdbcValueConverter(objectMapper),
                new JdbcConverterStringStringMap.StringToStringStringMapConverter(objectMapper),
                new JdbcConverterStringCollection.StringCollectionToJdbcValueConverter(
                        objectMapper),
                new JdbcConverterStringCollection.StringToStringCollectionConverter(objectMapper),
                new JdbcConverterReferenceCollection.ReferenceCollectionToJdbcValueConverter(
                        objectMapper),
                new JdbcConverterReferenceCollection.StringToReferenceCollectionConverter(
                        objectMapper),
                new JdbcConverterFormData.toJdbcValue(),
                new JdbcConverterFormData.fromString());
    }
}
