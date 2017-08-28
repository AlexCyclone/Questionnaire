package com.devianta.questionnaire;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AnswerDB implements Serializable {

    private static final long serialVersionUID = 2L;
    private Set<Answer> answerSet = new HashSet<>();
    private Questionnaire quenr;

    public AnswerDB() {
    }

    public void setQuenr(Questionnaire quenr) {
        this.quenr = quenr;
    }

    public void addAnswer(Answer answer) {
        if (answerSet.contains(answer)) {
            answerSet.remove(answer);
        }
        answerSet.add(answer);
    }

    public Questionnaire getQuenr() {
        return quenr;
    }

    public boolean contains(Answer answer) {
        return answerSet.contains(answer);
    }

    public Integer getRespCount() {
        return answerSet.size();
    }

    public Integer[] getUserAnswer(Answer answer) {
        Integer[] stat = null;
        if (contains(answer)) {
            List<Answer> anList = new LinkedList<>(answerSet);
            Answer a = anList.get(anList.indexOf(answer));
            stat = new Integer[a.getAnswers().size()];
            stat = a.getAnswers().toArray(stat);
        }
        return stat;
    }

    public Integer[][] getStatistic() {
        Integer[][] stat = getStatArray();

        for (Answer answer : answerSet) {
            List<Integer> answerList = answer.getAnswers();
            for (int i = 0; i < answerList.size(); i++) {
                int j = answerList.get(i);
                stat[i][j] += 1;
            }
        }
        return stat;
    }

    private Integer[][] getStatArray() {
        Integer[][] stat = new Integer[quenr.getQList().size()][];

        for (int i = 0; i < stat.length; i++) {
            Question que = quenr.getQList().get(i);
            stat[i] = new Integer[que.getAnswer().size()];
            Arrays.fill(stat[i], 0);
        }
        return stat;
    }

    @Override
    public String toString() {
        return "AnswerDB (" + (quenr == null ? null : quenr.getName()) + ") [answerSet=" + answerSet + "]";
    }

}
