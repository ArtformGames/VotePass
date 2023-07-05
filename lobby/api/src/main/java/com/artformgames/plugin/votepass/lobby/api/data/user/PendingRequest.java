package com.artformgames.plugin.votepass.lobby.api.data.user;

import com.artformgames.plugin.votepass.api.data.request.RequestAnswer;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerQuestion;
import com.artformgames.plugin.votepass.lobby.api.data.server.ServerSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class PendingRequest {

    protected final @NotNull ServerSettings application;

    protected final @NotNull SortedMap<Integer, RequestAnswer> answers = new TreeMap<>();
    @Nullable Integer editingQuestion;

    public PendingRequest(@NotNull ServerSettings application) {
        this.application = application;
    }

    public @NotNull ServerSettings getSettings() {
        return application;
    }

    public @Nullable Integer getEditingQuestion() {
        return editingQuestion;
    }

    public void setEditingQuestion(@Nullable Integer editingQuestion) {
        this.editingQuestion = editingQuestion;
    }

    public @NotNull SortedMap<Integer, RequestAnswer> getAnswers() {
        return answers;
    }

    public void applyAnswer(@NotNull List<String> contents) {
        if (getEditingQuestion() != null) {
            applyAnswer(getEditingQuestion(), contents);
            setEditingQuestion(null);
        }
    }

    public void applyAnswer(Integer id, @NotNull List<String> contents) {
        ServerQuestion question = getSettings().questions().get(id);
        this.answers.put(id, new RequestAnswer(question.title(), contents));
    }


    public boolean isAnswered(int id) {
        return this.answers.containsKey(id);
    }


}
