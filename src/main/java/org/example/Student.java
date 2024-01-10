package org.example;

import java.util.List;
import java.util.Objects;

public class Student {
    private String nume;
    private Float medie;
    private List<String> preferinte;
    private String curs;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public List<String> getPreferinte() {
        return preferinte;
    }

    public void setPreferinte(List<String> preferinte) {
        this.preferinte = preferinte;
    }

    public Float getMedie() {
        return medie;
    }

    public void setMedie(Float medie) {
        this.medie = medie;
    }

    public String getCurs() {
        return curs;
    }

    public void setCurs(String curs) {
        this.curs = curs;
    }

    public Student(String nume) {
        this.nume = nume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return nume.equals(student.getNume());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume);
    }

    @Override
    public String toString() {
        return this.getNume() + " - " + this.getMedie();
    }

    public static void main(String[] args) {
    }
}
