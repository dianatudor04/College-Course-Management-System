package org.example;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        String In = "src/main/resources/" + args[0] + "/" + args[0] + ".in";
        String Out = "src/main/resources/" + args[0] + "/" + args[0] + ".out";
        Queue<String> lista = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(In))) {
            String linie;
            while ((linie = br.readLine()) != null)
                lista.add(linie);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try (FileWriter fw = new FileWriter(Out, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            Secretariat s = new Secretariat();
            while (lista.peek() != null) {
                String linie = lista.remove();
                List<String> date = List.of(linie.split(" - "));
                switch (date.get(0)) {
                    case "adauga_student":
                        String program_student = date.get(1);
                        String nume_student = date.get(2);
                        s.adaugaStudent(program_student, nume_student, out);
                        break;
                    case "adauga_curs":
                        String program_curs = date.get(1);
                        String nume_curs = date.get(2);
                        Integer capacitate = Integer.parseInt(date.get(3));
                        s.adaugaCurs(program_curs, nume_curs, capacitate);
                        break;
                    case "citeste_mediile":
                        s.citesteMediile("src/main/resources/" + args[0], "note_");
                        break;
                    case "posteaza_mediile":
                        s.posteazaMediile(out);
                        break;
                    case "contestatie":
                        String nume_contestatie = date.get(1);
                        Float medie_contestatie = Float.parseFloat(date.get(2));
                        s.contestatie(nume_contestatie, medie_contestatie);
                        break;
                    case "adauga_preferinte":
                        String nume_stud = date.get(1);
                        s.adaugaPreferinte(nume_stud, date.subList(2, date.size()));
                        break;
                    case "repartizeaza":
                        s.repartizeaza();
                        break;
                    case "posteaza_curs":
                        s.posteazaCurs(date.get(1), out);
                        break;
                    case "posteaza_student":
                        s.posteazaStudent(date.get(1), out);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
