package com.lab.translator.service;

import com.lab.translator.model.TranslationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TranslationResponseServiceTest {

    private TranslationResponseService translationResponseService;

    @BeforeEach
    void setUp() {
        translationResponseService = mock(TranslationResponseService.class);
    }

    @Test
    void testGetTranslationRequests() {
        List<TranslationResponse> expectedResponses = new ArrayList<>();
        TranslationResponse response1 = new TranslationResponse(1L, "127.0.0.1", "Hello", "Hola");
        TranslationResponse response2 = new TranslationResponse(2L, "127.0.0.2", "It's", "response");
        TranslationResponse response3 = new TranslationResponse(3L, "127.0.0.3", "Number", "3");

        expectedResponses.add(response1);
        expectedResponses.add(response2);
        expectedResponses.add(response3);

        when(translationResponseService.getTranslationRequests()).thenReturn(expectedResponses);
        List<TranslationResponse> actualResponses = translationResponseService.getTranslationRequests();
        assertEquals(expectedResponses, actualResponses);
    }

    @Test
    void testSaveTranslationRequest() {
        String ipAddress = "127.0.0.1";
        String sourceText = "Hello";
        String targetText = "Hola";

        translationResponseService.saveTranslationRequest(ipAddress, sourceText, targetText);
        verify(translationResponseService, times(1)).saveTranslationRequest(ipAddress, sourceText, targetText);
    }
}
