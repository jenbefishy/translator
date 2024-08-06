package com.lab.translator.service.impl;

import com.lab.translator.model.TranslationResponse;
import com.lab.translator.repository.TranslationResponseRepository;
import com.lab.translator.service.TranslationResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class TranslationResponseServiceImpl implements TranslationResponseService {

    private final TranslationResponseRepository translationResponseRepository;

    @Autowired
    public TranslationResponseServiceImpl(TranslationResponseRepository translationResponseRepository) {
        this.translationResponseRepository = translationResponseRepository;
    }

    @Override
    public List<TranslationResponse> getTranslationRequests() {
        return translationResponseRepository.getAll();
    }

    @Override
    public void saveTranslationRequest(String ipAddress, String sourceText, String targetText) {
        TranslationResponse record = new TranslationResponse();
        record.setUserIp(ipAddress);
        record.setSourceText(sourceText);
        record.setTargetText(targetText);
        translationResponseRepository.save(record);
    }
}