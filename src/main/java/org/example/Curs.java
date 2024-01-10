package org.example;

import java.util.ArrayList;
import java.util.List;

public class Curs<T extends Student> {
    private String nume;
    private Integer capacitate;
    private final List<T> studenti = new ArrayList<>();

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Integer getCapacitate() {
        return capacitate;
    }

    public void setCapacitate(Integer capacitate) {
        this.capacitate = capacitate;
    }

    public List<T> getStudenti() {
        return studenti;
    }

    public Curs(String nume, Integer capacitate) {
        this.nume = nume;
        this.capacitate = capacitate;
    }

    public static void main(String[] args) {
    }
}
