package org.example;

public class StudentDuplicatExceptie extends Exception {
    public StudentDuplicatExceptie(String nume) {
        super("Student duplicat: " + nume);
    }
}
