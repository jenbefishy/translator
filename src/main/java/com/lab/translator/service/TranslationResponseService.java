package com.lab.translator.service;

import com.lab.translator.model.TranslationResponse;

import java.util.List;

public interface TranslationResponseService {
    List<TranslationResponse> getTranslationRequests();
    void saveTranslationRequest(String ipAddress, String sourceText, String targetText);
}