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
    private final List<StudentLicenta> studentiLicenta = new ArrayList<StudentLicenta>();
    private final List<StudentMaster> studentiMaster = new ArrayList<StudentMaster>();
    private final List<Curs<StudentLicenta>> cursuriLicenta = new ArrayList<Curs<StudentLicenta>>();
    private final List<Curs<StudentMaster>> cursuriMaster = new ArrayList<Curs<StudentMaster>>();


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
        Curs<? extends Student> curs;
        if (program.equals("licenta")) {
            curs = new Curs<StudentLicenta>(nume, capacitate);
            this.getCursuriLicenta().add((Curs<StudentLicenta>) curs);
        } else if (program.equals("master")) {
            curs = new Curs<StudentMaster>(nume, capacitate);
            this.getCursuriMaster().add((Curs<StudentMaster>) curs);
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
        List<Student> studenti = new ArrayList<>(this.getStudentiLicenta());
        studenti.addAll(this.getStudentiMaster());
        studenti.sort(Comparator.comparing(Student::getMedie).reversed().thenComparing(Student::getNume));
        List<Curs<? extends Student>> cursuri = new ArrayList<>();
        cursuri.addAll(this.getCursuriLicenta());
        cursuri.addAll(this.getCursuriMaster());

        for (Student student: studenti) {
            if (student instanceof StudentLicenta) {
                for (String preferinta : student.getPreferinte()) {
                    Curs<StudentLicenta> cursPref = null;
                    for (Curs<? extends Student> curs : cursuri)
                        if (curs.getNume().equals(preferinta))
                            cursPref = (Curs<StudentLicenta>) curs;
                    assert cursPref != null;
                    if (cursPref.getCapacitate() > cursPref.getStudenti().size()) {
                        cursPref.getStudenti().add((StudentLicenta) student);
                        student.setCurs(cursPref.getNume());
                        break;
                    } else {
                        Student ultimulIntrat = (Student) cursPref.getStudenti().get(cursPref.getStudenti().size() - 1);
                        if (ultimulIntrat.getMedie() - student.getMedie() == 0) {
                            cursPref.getStudenti().add((StudentLicenta) student);
                            student.setCurs(cursPref.getNume());
                            break;
                        }
                    }
                }
            } else {
                for (String preferinta : student.getPreferinte()) {
                    Curs<StudentMaster> cursPref = null;
                    for (Curs<? extends Student> curs : cursuri)
                        if (curs.getNume().equals(preferinta))
                            cursPref = (Curs<StudentMaster>) curs;
                    assert cursPref != null;
                    if (cursPref.getCapacitate() > cursPref.getStudenti().size()) {
                        cursPref.getStudenti().add((StudentMaster) student);
                        student.setCurs(cursPref.getNume());
                        break;
                    } else {
                        Student ultimulIntrat = (Student) cursPref.getStudenti().get(cursPref.getStudenti().size() - 1);
                        if (ultimulIntrat.getMedie() - student.getMedie() == 0) {
                            cursPref.getStudenti().add((StudentMaster) student);
                            student.setCurs(cursPref.getNume());
                            break;
                        }
                    }
                }
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
