package com.smartneck.fit.CustomerSupport.Item;

public class CustomerFaqItem {

    String answer;
    String questions;

    public CustomerFaqItem(String questions, String answer) {
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
