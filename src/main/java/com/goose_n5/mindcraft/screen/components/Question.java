package com.goose_n5.mindcraft.screen.components;

import java.util.List;

public class Question{
    String question;
    List<String> answers;
    int correct;

    public String getQuestion() {
        return question;
    }

    public int getCorrect() {
        return correct;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }
}