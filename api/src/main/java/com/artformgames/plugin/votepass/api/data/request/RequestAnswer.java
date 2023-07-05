package com.artformgames.plugin.votepass.api.data.request;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public record RequestAnswer(@NotNull String question, @NotNull List<String> answers) {

    public int countWords() {
        return answers.stream().mapToInt(String::length).sum();
    }

    @Override
    public String toString() {
        return "RequestAnswer{" +
                "question='" + question + '\'' +
                ", answers=" + answers +
                '}';
    }

}
