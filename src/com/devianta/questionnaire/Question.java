package com.devianta.questionnaire;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "question", "answer" })
@XmlRootElement
public class Question implements Serializable {

    private static final long serialVersionUID = 2L;
    private String question;
    private List<String> answer = new ArrayList<>();

    public Question() {
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void addAnswer(String answer) {
        this.answer.add(answer);
    }

    @XmlElement
    public String getQuestion() {
        return question;
    }

    @XmlElement
    public List<String> getAnswer() {
        return answer;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((answer == null) ? 0 : answer.hashCode());
        result = prime * result + ((question == null) ? 0 : question.hashCode());
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
        Question other = (Question) obj;
        if (answer == null) {
            if (other.answer != null)
                return false;
        } else if (!answer.equals(other.answer))
            return false;
        if (question == null) {
            if (other.question != null)
                return false;
        } else if (!question.equals(other.question))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "{" + "question='" + question + '\'' + ", answer=" + answer + '}';
    }

}
