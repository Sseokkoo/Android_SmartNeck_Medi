package com.smartneck.fit.Fit.CustomerSupport.Item;

public class Fit_CustomerFaqItem {

    String answer;
    String questions;

    public Fit_CustomerFaqItem(String questions, String answer) {
        this.answer = answer;
        this.questions = questions;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestions() {
        return questions;
    }
}
