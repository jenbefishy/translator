
package com.lab.translator.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lab.translator.exception.TranslationException;
import com.lab.translator.service.TranslationApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class TranslationApiServiceImpl implements TranslationApiService {


    @Value("${translation.api.url}")
    private String apiUrl;

    @Value("${translation.api.key}")
    private String apiKey;

    private static final int MAX_CONCURRENT_REQUESTS = 10;

    private final RestTemplate restTemplate;
    private final ExecutorService executorService;
    private final ObjectMapper objectMapper;

    @Autowired
    public TranslationApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.executorService = Executors.newFixedThreadPool(MAX_CONCURRENT_REQUESTS);
        this.objectMapper = new ObjectMapper();
    }

    public String translateText(String sourceLanguage, String targetLanguage, String text) {
        String[] words = text.split("\\s+");

        List<CompletableFuture<String>> futures = Arrays.stream(words)
                .map(word -> CompletableFuture.supplyAsync(() -> translateWord(sourceLanguage, targetLanguage, word),
                        executorService))
                .toList();

        return futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        cancelRemainingTasks(futures);
                        throw new TranslationException(e.getCause().getMessage(), e);
                    }
                })
                .collect(Collectors.joining(" "));
    }



    private String translateWord(String sourceLanguage, String targetLanguage, String word) {
        HttpEntity<String> requestEntity = createRequestEntity(sourceLanguage, targetLanguage, word);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
            return extractFieldFromJSON(response.getBody(), "text");
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException(e);
        } catch (ResourceAccessException e) {
            throw new TranslationException("Unable to access translation service", e);
        } catch (RestClientException e) {
            throw new TranslationException("Error during translation request: " + e.getMessage(), e);
        }

        throw new TranslationException("Unexpected error occurred during translation");
    }

    private HttpEntity<String> createRequestEntity(String sourceLanguage, String targetLanguage, String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Api-Key " + apiKey);

        String requestBody = String.format("{\"texts\": [\"%s\"], \"targetLanguageCode\": \"%s\", \"sourceLanguageCode\": \"%s\"}",
                word, targetLanguage, sourceLanguage);

        return new HttpEntity<>(requestBody, headers);
    }


    private void handleHttpClientErrorException(HttpClientErrorException e) {
        String errorMessage = extractFieldFromJSON(e.getResponseBodyAsString(), "message");
        if (errorMessage.contains("unsupported target_language_code")) {
            throw new TranslationException("Target language not found", e);
        } else if (errorMessage.contains("unsupported source_language_code")) {
            throw new TranslationException("Source language not found", e);
        } else {
            throw new TranslationException("Translation error: " + errorMessage, e);
        }
    }

    String extractFieldFromJSON(String jsonString, String field) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode fieldNode = rootNode.findPath(field);
            if (fieldNode.isMissingNode()) {
                throw new TranslationException("Field " + field + " not found in JSON response");
            } else {
                return fieldNode.asText();
            }
        } catch (IOException e) {
            throw new TranslationException("Error parsing JSON response", e);
        }
    }

    private void cancelRemainingTasks(List<CompletableFuture<String>> futures) {
        futures.forEach(future -> future.cancel(true));
    }
}