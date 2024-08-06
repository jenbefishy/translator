package com.lab.translator.model;

public class TranslationResponse {
    private Long id;
    private String userIp;
    private String sourceText;
    private String targetText;


    public TranslationResponse() {
    }

    public TranslationResponse(Long id, String userIp, String sourceText, String targetText) {
        this.id = id;
        this.userIp = userIp;
        this.sourceText = sourceText;
        this.targetText = targetText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public String getTargetText() {
        return targetText;
    }

    public void setTargetText(String targetText) {
        this.targetText = targetText;
    }
}
