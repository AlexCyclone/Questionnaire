package com.devianta.questionnaire;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "name", "QList" })
@XmlRootElement
public class Questionnaire implements Serializable {

    private static final long serialVersionUID = 2L;
    private String name;
    private List<Question> qList = new ArrayList<>();

    public Questionnaire() {
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement(name = "quest")
    public List<Question> getQList() {
        return qList;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((qList == null) ? 0 : qList.hashCode());
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
        Questionnaire other = (Questionnaire) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (qList == null) {
            if (other.qList != null)
                return false;
        } else if (!qList.equals(other.qList))
            return false;
        return true;
    }

    @Override
    public String toString() {
        String s = name + " " + System.lineSeparator();
        for (Question question : qList) {
            s += question + System.lineSeparator();
        }
        return s;
    }

}
