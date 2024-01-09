package org.example;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Secretariat {
    private List<StudentLicenta> studentiLicenta = new ArrayList<StudentLicenta>();
    private List<StudentMaster> studentiMaster = new ArrayList<StudentMaster>();
    private List<Curs<StudentLicenta>> cursuriLicenta = new ArrayList<Curs<StudentLicenta>>();
    private List<Curs<StudentMaster>> cursuriMaster = new ArrayList<Curs<StudentMaster>>();


    public List<StudentLicenta> getStudentiLicenta() {
        return studentiLicenta;
    }

    public List<StudentMaster> getStudentiMaster() {
        return studentiMaster;
    }

    public List<Curs<StudentLicenta>> getCursuriLicenta() {
        return cursuriLicenta;
    }

    public List<Curs<StudentMaster>> getCursuriMaster() {
        return cursuriMaster;
    }

    public void adaugaStudent(String program, String nume, PrintWriter out) {
        try {
            StudentLicenta studentLicenta = new StudentLicenta(nume);
            StudentMaster studentMaster = new StudentMaster(nume);

            for (StudentLicenta student: this.getStudentiLicenta())
                if (student.equals(studentLicenta))
                    throw new StudentDuplicatExceptie(nume);
            for (StudentMaster student: this.getStudentiMaster())
                if (student.equals(studentMaster))
                    throw new StudentDuplicatExceptie(nume);

            if (program.equals("licenta"))
                this.getStudentiLicenta().add(studentLicenta);
            else if (program.equals("master"))
                this.getStudentiMaster().add(studentMaster);
        } catch (StudentDuplicatExceptie e) {
            out.println("***");
            out.println(e.getMessage());
        }
    }

    public void adaugaCurs(String program, String nume, Integer capacitate) {
        Curs curs;
        if (program.equals("licenta")) {
            curs = new Curs<StudentLicenta>(nume, capacitate);
            this.getCursuriLicenta().add(curs);
        } else if (program.equals("master")) {
            curs = new Curs<StudentMaster>(nume, capacitate);
            this.getCursuriMaster().add(curs);
        }
    }

    public Student gasesteStudent(String nume) {
        for (StudentLicenta studentLicenta: this.getStudentiLicenta())
            if (studentLicenta.getNume().equals(nume))
                return studentLicenta;
        for (StudentMaster studentMaster: this.getStudentiMaster())
            if (studentMaster.getNume().equals(nume))
                return studentMaster;
        return null;
    }

    public void citesteMediile(String cale, String prefix) {
        Path director = Paths.get(cale);
        HashMap<String, Float> map = new HashMap<>();

        try (DirectoryStream<Path> fisiere = Files.newDirectoryStream(director, prefix + "*")) {
            for (Path fisier: fisiere) {
                try (BufferedReader br = Files.newBufferedReader(fisier)) {
                    String linie;
                    while ((linie = br.readLine()) != null) {
                        map.put(linie.split(" - ")[0], Float.parseFloat(linie.split(" - ")[1]));
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String key: map.keySet())
            gasesteStudent(key).setMedie(map.get(key));
    }

    public void posteazaMediile(PrintWriter out) {
        out.println("***");
        List<Student> studenti = new ArrayList<>(this.getStudentiLicenta());
        studenti.addAll(this.getStudentiMaster());
        studenti.sort(Comparator.comparing(Student::getNume));
        studenti.sort(Comparator.comparing(Student::getMedie).reversed());
        for (Student student: studenti)
            out.println(student);
    }

    public void contestatie(String nume, Float medie) {
        Student student = gasesteStudent(nume);
        student.setMedie(medie);
    }

    public void adaugaPreferinte(String nume, List<String> preferinte) {
        Student student = gasesteStudent(nume);
        student.setPreferinte(preferinte);
    }

    public void repartizeaza() {
        List<StudentLicenta> studentiLicenta = new ArrayList<>(this.getStudentiLicenta());
        studentiLicenta.sort(Comparator.comparing(Student::getMedie).reversed().thenComparing(Student::getNume));
        for (StudentLicenta student: studentiLicenta)
            for (String preferinta: student.getPreferinte()) {
                Curs<StudentLicenta> curs = null;
                for (Curs<StudentLicenta> cursLicenta: this.getCursuriLicenta())
                    if (cursLicenta.getNume().equals(preferinta))
                        curs = cursLicenta;
                assert curs != null;
                if (curs.getCapacitate() - curs.getStudenti().size() > 0) {
                    curs.getStudenti().add(student);
                    student.setCurs(curs.getNume());
                    break;
                } else if (curs.getStudenti().get(curs.getCapacitate() - 1).getMedie() - student.getMedie() == 0) {
                    curs.getStudenti().add(student);
                    student.setCurs(curs.getNume());
                    break;
                }
            }


        List<StudentMaster> studentiMaster = new ArrayList<>(this.getStudentiMaster());
        studentiMaster.sort(Comparator.comparing(Student::getMedie).reversed().thenComparing(Student::getNume));
        for (StudentMaster student: this.getStudentiMaster())
            for (String preferinta: student.getPreferinte()) {
                Curs<StudentMaster> curs = null;
                for (Curs<StudentMaster> cursMaster: this.getCursuriMaster())
                    if (cursMaster.getNume().equals(preferinta))
                        curs = cursMaster;
                assert curs != null;
                if (curs.getCapacitate() - curs.getStudenti().size() > 0) {
                    curs.getStudenti().add(student);
                    student.setCurs(preferinta);
                    break;
                } else if (curs.getStudenti().get(curs.getCapacitate() - 1).getMedie() - student.getMedie() == 0) {
                    curs.getStudenti().add(student);
                    student.setCurs(preferinta);
                    break;
                }
            }
    }

    public Curs<? extends Student> gasesteCurs(String nume) {
        for (Curs<? extends Student> cursLicenta: this.getCursuriLicenta())
            if (cursLicenta.getNume().equals(nume))
                return cursLicenta;
        for (Curs<? extends Student> cursMaster: this.getCursuriMaster())
            if (cursMaster.getNume().equals(nume))
                return cursMaster;
        return null;
    }

    public void posteazaCurs(String nume, PrintWriter out) {
        Curs<? extends Student> curs = gasesteCurs(nume);
        out.println("***");
        out.println(curs.getNume() + " (" + curs.getCapacitate() + ")");
        curs.getStudenti().sort(Comparator.comparing(Student::getNume));
        for (Student student: curs.getStudenti())
            out.println(student);
    }

    public void posteazaStudent(String nume, PrintWriter out) {
        Student student = gasesteStudent(nume);
        String preferinta;
        if (student instanceof StudentLicenta)
            preferinta = "Licenta";
        else
            preferinta = "Master";
        out.println("***");
        out.println("Student " + preferinta + ": " + student.getNume() +
                " - " + student.getMedie() + " - " + student.getCurs());
    }

    public static void main(String[] args) {
    }
}
