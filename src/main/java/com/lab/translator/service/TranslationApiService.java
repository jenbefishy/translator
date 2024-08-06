package com.lab.translator.service;

public interface TranslationApiService {
    String translateText(String sourceLanguage, String targetLanguage, String text);
}