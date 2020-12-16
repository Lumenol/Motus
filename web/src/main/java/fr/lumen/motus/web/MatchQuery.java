package fr.lumen.motus.web;

import java.util.List;

public class MatchQuery {
    private String answer;
    private List<String> propositions = List.of();

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<String> getPropositions() {
        return propositions;
    }

    public void setPropositions(List<String> propositions) {
        this.propositions = propositions;
    }
}
