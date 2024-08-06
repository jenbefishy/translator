package com.lab.translator.service;

import com.lab.translator.exception.TranslationException;
import com.lab.translator.service.impl.TranslationApiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestPropertySource("classpath:application.yaml")
public class TranslationApiServiceTest {

    @Value("${translation.api.url}")
    private String apiUrl;

    @Value("${translation.api.key}")
    private String apiKey;

    private TranslationApiServiceImpl translationApiService;
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        restTemplate = mock(RestTemplate.class);
        translationApiService = new TranslationApiServiceImpl(restTemplate);
    }

    @Test
    public void testTranslateText_Success() {
        String response = "{\"translations\": [{\"text\": \"Hola\"}]}";
        String sourceText = "Hello";

        when(restTemplate.exchange(eq(apiUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        String result = translationApiService.translateText("en", "es", sourceText);
        assertEquals("Hola", result);
    }

    @Test
    public void testTranslateText_TargetLanguageNotFound() {
        String jsonResponse = """
           {
               "code": 3,
               "message": "unsupported target_language_code: xyz",
               "details": [
                   {
                       "@type": "type.googleapis.com/google.rpc.RequestInfo",
                       "requestId": "id"
                   }
               ]
           }
           """;
        HttpClientErrorException httpClientErrorException = new HttpClientErrorException(
                HttpStatus.BAD_REQUEST, "Bad Request", jsonResponse.getBytes(), null);

        when(restTemplate.exchange(eq(apiUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenThrow(httpClientErrorException);

        TranslationException exception = assertThrows(TranslationException.class, () -> {
            translationApiService.translateText("en", "xyz", "hello");
        });

        assertEquals("Target language not found", exception.getMessage());
    }

    @Test
    public void testTranslateText_SourceLanguageNotFound() {
        String jsonResponse = """
                {
                    "code": 3,
                    "message": "unsupported source_language_code: xyz",
                    "details": [
                        {
                            "@type": "type.googleapis.com/google.rpc.RequestInfo",
                            "requestId": "e94b2e51-a43a-44b1-9f0e-622ea50a8ed3"
                        }
                    ]
                }
           """;
        HttpClientErrorException httpClientErrorException = new HttpClientErrorException(
                HttpStatus.BAD_REQUEST, "Bad Request", jsonResponse.getBytes(), null);

        when(restTemplate.exchange(eq(apiUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenThrow(httpClientErrorException);

        TranslationException exception = assertThrows(TranslationException.class, () -> {
            translationApiService.translateText("xyz", "es", "hello");
        });

        assertEquals("Source language not found", exception.getMessage());
    }

    @Test
    public void testTranslateText_ServerNotAvailable() {
        when(restTemplate.exchange(eq(apiUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new ResourceAccessException("Server is not available"));

        TranslationException thrown = assertThrows(TranslationException.class, () -> {
            translationApiService.translateText("en", "es", "hello");
        });

        assertEquals("Unable to access translation service", thrown.getMessage());
    }

}
