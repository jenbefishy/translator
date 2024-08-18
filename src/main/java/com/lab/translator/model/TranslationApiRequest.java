package com.lab.translator.model;

import java.util.List;

public class TranslationApiRequest {
    private String sourceLanguageCode;
    private String targetLanguageCode;
    private List<String> texts;

    public TranslationApiRequest() {
    }

    public TranslationApiRequest(String sourceLanguageCode, List<String> texts, String targetLanguageCode) {
        this.targetLanguageCode = targetLanguageCode;
	this.sourceLanguageCode = sourceLanguageCode;
        this.texts = texts;
    }

    public String getSourceLanguageCode() {
        return sourceLanguageCode;
    }

    public void setSourceLanguageCode(String sourceLanguageCode) {
        this.sourceLanguageCode = sourceLanguageCode;
    }

    public String getTargetLanguageCode() {
        return targetLanguageCode;
    }

    public void setTargetLanguageCode(String targetLanguageCode) {
        this.targetLanguageCode = targetLanguageCode;
    }

    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }

}
