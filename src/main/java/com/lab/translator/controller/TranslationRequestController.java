package com.lab.translator.controller;

import com.lab.translator.exception.TranslationException;
import com.lab.translator.model.TranslationResponse;
import com.lab.translator.service.TranslationApiService;
import com.lab.translator.service.TranslationResponseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requests")

public class TranslationRequestController {

    private final TranslationResponseService translationResponseService;
    private final TranslationApiService translationApiService;

    public TranslationRequestController(TranslationResponseService translationResponseService,
                                        TranslationApiService translationApiService) {
        this.translationResponseService = translationResponseService;
        this.translationApiService = translationApiService;
    }

    @GetMapping
    public List<TranslationResponse> getTranslationRequests() {
        return translationResponseService.getTranslationRequests();
    }

    @PostMapping
    public ResponseEntity<String> translate(
            @RequestParam("sourceLanguage") String sourceLanguage,
            @RequestParam("targetLanguage") String targetLanguage,
            @RequestBody String text,
            HttpServletRequest request) {

            String ipAddress = request.getRemoteAddr();

        try {
            String targetText = translationApiService.translateText(sourceLanguage, targetLanguage, text);
            translationResponseService.saveTranslationRequest(ipAddress, text, targetText);
            return ResponseEntity.ok(targetText);
        } catch (TranslationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
}