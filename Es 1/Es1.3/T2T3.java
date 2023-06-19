/*
ES1.3
Progettare e implementare un DFA che riconosca il linguaggio di stringhe che
contengono un numero di matricola seguito (subito) da un cognome, dove la
combinazione di matricola e cognome corrisponde a studenti del turno 2 o del
turno 3 di LFT.
Nel contesto di questo esercizio, un numero di matricola non ha un numero
prestabilito di cifre (ma deve essere composto di almeno una cifra). Un cognome
corrisponde a una sequenza di lettere, e deve essere composto di almeno una
lettera.
Si ricorda le regole per suddivisione di studenti in turni:
• Turno T2: iniziale `e compresa tra A e K, e matricola `e pari;
• Turno T3: iniziale `e compresa tra L e Z, e matricola `e dispari;

Esempio accettate: "123456Bianchi", "654321Rossi", "2Bianchi" e "122B"
Esempio non accettate: "654321Bianchi", "123456Rossi", "654322" e "Rossi".
*/

public class T2T3 {

  public static boolean scan(String s) {
    int state = 0, i = 0;

    while (state >= 0 && i < s.length()) {
      final char ch = s.charAt(i++);

      switch(state) {
        /* caso iniziale, accetto numeri */
        case 0:
          if(Character.isDigit(ch)){
              if((int)(ch)%2 == 0)
                state = 1;  //vado in uno se numero pari
              else
                state = 2;  //vado in due se numero dispari
          } else
              state = -1;
          break;

        /* caso numero pari, accetto A-K */
        case 1:
          if(ch >= 'A' && ch <= 'K')
            state = 3;  //se trovo lettera vado a 3
          else if(Character.isDigit(ch)){
            if((int)(ch)%2 == 0)
              state = 1;  //resto in uno se numero pari
            else
              state = 2;  //vado in due se numero dispari
          } else
              state = -1;
          break;

        /* caso numero pari, accetto L-Z */
        case 2:
          if(ch >= 'L' && ch <= 'Z')
            state = 3;  //se trovo lettera vado a 3
          else if(Character.isDigit(ch)){
            if((int)(ch)%2 == 0)
              state = 1;  //torno in uno se numero pari
            else
              state = 2;  //resto in due se numero dispari
          } else
              state = -1;
          break;

          /* caso lettera, accetto le lettere minuscole */
          case 3:
            if(ch >= 'a' && ch <= 'z')
              state = 3;  //se lettera minuscola rimango in 3
            else
              state = -1;
            break;
      }

    }

    return state == 3;  //stato finale completamento cognome
  }

  public static void main(String[] args) {
    System.out.println(scan(args[0]) ? "OK" : "NOPE");
  }
}
