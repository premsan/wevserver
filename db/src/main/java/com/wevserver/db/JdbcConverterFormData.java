package com.wevserver.db;

import com.wevserver.lib.FormData;
import java.sql.JDBCType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jdbc.core.mapping.JdbcValue;

public class JdbcConverterFormData {

    @RequiredArgsConstructor
    public static class toJdbcValue implements Converter<FormData, JdbcValue> {

        @Override
        public JdbcValue convert(final FormData source) {

            return JdbcValue.of(source.encode(), JDBCType.VARCHAR);
        }
    }

    @RequiredArgsConstructor
    public static class fromString implements Converter<String, FormData> {

        @Override
        public FormData convert(final String source) {

            return FormData.decode(source);
        }
    }
}
