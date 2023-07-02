package com.artformgames.plugin.votepass.lobby.api.data.user;

import com.artformgames.plugin.votepass.lobby.api.data.server.ServerApplication;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class PendingRequest {

    protected final @NotNull ServerApplication application;

    protected final @NotNull SortedMap<Integer, List<String>> answers = new TreeMap<>();
    @Nullable Integer editingQuestion;

    public PendingRequest(@NotNull ServerApplication application) {
        this.application = application;
    }

    public @NotNull ServerApplication getApplication() {
        return application;
    }

    public @Nullable Integer getEditingQuestion() {
        return editingQuestion;
    }

    public void setEditingQuestion(@Nullable Integer editingQuestion) {
        this.editingQuestion = editingQuestion;
    }

    public @NotNull SortedMap<Integer, List<String>> getAnswers() {
        return answers;
    }

    public void applyAnswer(@NotNull List<String> contents) {
        if (getEditingQuestion() != null) {
            applyAnswer(getEditingQuestion(), contents);
            setEditingQuestion(null);
        }
    }

    public void applyAnswer(Integer id, @NotNull List<String> contents) {
        this.answers.put(id, contents);
    }

    public boolean isAnswered(int id) {
        return this.answers.containsKey(id);
    }


}
