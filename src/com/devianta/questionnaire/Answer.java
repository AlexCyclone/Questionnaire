package com.devianta.questionnaire;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Answer implements Serializable {

    private static final long serialVersionUID = 2L;
    private String respondent;
    private List<Integer> answers = new LinkedList<>();

    public Answer(String respondent) {
        this.respondent = respondent;
    }

    public Answer(String respondent, List<Integer> answers) {
        this.respondent = respondent;
        this.answers = answers;
    }

    public String getRespondent() {
        return respondent;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((respondent == null) ? 0 : respondent.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Answer other = (Answer) obj;
        if (respondent == null) {
            if (other.respondent != null)
                return false;
        } else if (!respondent.equals(other.respondent))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Answer [respondent=" + respondent + ", answers=" + answers + "]";
    }

}
