package io.github.escapehonbab.spring.cipher;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class BodyConverter extends AbstractHttpMessageConverter<Object> {

    private static final Charset DEFAULT = StandardCharsets.UTF_8;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public BodyConverter() {
        super(MediaType.APPLICATION_JSON, new MediaType("application", "*+json", DEFAULT));
    }

    @Override
    protected boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    protected Object readInternal(Class<?> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return objectMapper.readValue(decrypt(httpInputMessage.getBody()),aClass);
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        httpOutputMessage.getBody().write(encrypt(objectMapper.writeValueAsBytes(o)));
    }

    private InputStream decrypt(InputStream inputStream) {

        try {
            return CipherBase.getInstance().decode(inputStream);
        } catch (Exception ex) {
            return inputStream;
        }
    }

    private byte[] encrypt(byte[] data) {
        try {
            return CipherBase.getInstance().encode(data);
        } catch (Exception ex) {
            return data;
        }
    }

}
