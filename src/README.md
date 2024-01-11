****** TEMA 2 POO ******
->Tudor Diana 324 CB


->Toate functiile si comenzile sunt comentate pe cod.

Structuri folosite:
->Coada:      *pentru a par curge comenzile din input. 
              *Aveam nevoie sa accesez elementul de la inceput.

->Hashmap :   *Pentru a asocia elemente tip cheie-valoare.
              *ex: atribuire medie student la citirea notelor din fisier.

->Arraylist:  *accesarea cu usurinta a elementelor.
              *nu aveam nevoie de operatii precum stergerea, astfel arraylist era mai potrivit decat linkedlist.

Clase utilizate si comenzi:
->Clasele Main, Curs, Secretariat si Student care mosteneste StudentLicenta si StudentMaster.
    ->Main:
        ->Cum am specificat si la coada, am stocat comenzile din input intr-o coada pentru a putea accesa elementul de la inceput.
        ->Dupa aceea am retinut fiecare comanda dupa separatorul "-" si le-am apelat corespunzator numelui lor.
    ->Curs:
        ->am "initializat" getter-erele si setter-ele pentru campurile specificate in enunt.
    ->Secretariat:
        ->am definit 4 liste generice pentru fiecare categorie de student si curs. ( licenta/master)
        ->am creat doua functii auxiliare gasesteStudent si gasesteCurs care cauta studentul/cursul
            dupa nume. Astfel, am usurat cautarile in celelalte functii. (for-uri repetitive)
        ->AdaugaStudent si AdaugaCurs-dupa cum spune si numele, adaugam studentul si cursul in listele corespunzatoare.
            Am tratat si exceptiile specificate in enunt. (ex: duplicat nume).
        ->Functia de repartizare:
            ->Am parcurs toti studentii si am folosit un "if-else mare". Pe ramura de if s-a tratat cazul pentru
            studenti licenta, iar pe ramura de else cazul pentru studenti master.
            ->Pentru a evita un warning, am retinut in cursPref cursul preferat al Studentului.
            ->Parcurgem lista de preferinte pentru fiecare student, apoi parcurgem toata lista de cursuri.
            Identificam preferinta in lista de cursuri si verificam daca mai sunt locuri disponibile.
            If so, adaugam cursPref in lista de cursuri a studentului. 
            ->Mai avem si cazul limita in care un student are media egala cu cea a ultimului student care a intrat
            la acel curs. Atunci adaugam si studentul curent.
            ->Restul functiilor sunt comentate pe cod.
