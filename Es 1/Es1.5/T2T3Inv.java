/*
Progettare e implementare un DFA che, come in Esercizio 1.3, riconosca il
linguaggio di stringhe che contengono matricola e cognome di studenti del
turno 2 o del turno 3 del laboratorio, ma in cui il cognome precede il numero
di matricola (in altre parole, le posizioni del cognome e matricola sono
scambiate rispetto all’Esercizio 1.3). Assicurarsi che il DFA sia minimo.
*/

public class T2T3Inv {
  public static boolean scan(String s) {
    int state = 0, i = 0;

    while (state >= 0 && i < s.length()) {
      final char ch = s.charAt(i++);
      boolean b = false;

      switch(state) {

        /* caso iniziale, accetto lettere maiuscole */
        case 0:
          b = ch >= 'A' && ch <= 'K';
          if(b)
            state = 1;  //vado in 1 se cognome con marticola pari
          else if (ch >= 'L' && ch <= 'Z')
            state = 2;  //vado in 2 se cognome con marticola dispari
          else
            state = -1;
          break;

        /* caso cognome con marticola pari, accetto resto cognome e numeri */
        case 1:
          b = ch >= '0' && ch <= '9';
          if(ch >= 'a' && ch <= 'z')
            state = 1;  //rimango in 1, resto del cognome, lettere minuscole
          else if(b && (int)(ch)%2 == 0)
            state = 3;  //vado a 3 per numero pari
          else if(b && (int)(ch)%2 != 0)
            state = 5;  //vado a 5 per numero dispari
          else
            state = -1;
          break;

        /* caso cognome con marticola dispari, accetto resto cognome e numeri */
        case 2:
          b = ch >= '0' && ch <= '9';
          if(ch >= 'a' && ch <= 'z')
            state = 2;  //rimango in 2, resto del cognome, lettere minuscole
          else if(b && (int)(ch)%2 != 0)
            state = 4;  //vado a 4 per numero pari
          else if(b && (int)(ch)%2 == 0)
            state = 6;  //vado a 6 per numero dispari
          else
            state = -1;
          break;

         /* stato finale, caso A-K marticola pari, accetto numeri */
         case 3:
            b = ch >= '0' && ch <= '9';
            if(b && (int)(ch)%2 == 0)
              state = 3;  //rimango in 3 se pari
            else if(b && (int)(ch)%2 != 0)
              state = 5;  //vado a 5 se dispari
            else
              state = -1;
            break;

          /* stato finale, caso L-Z marticola dispari, accetto numeri */
          case 4:
            b = ch >= '0' && ch <= '9';
            if(b && (int)(ch)%2 != 0)
              state = 4;  //rimango in 4 se pari
            else if(b && (int)(ch)%2 == 0)
              state = 6;  //vado a 6 se dispari
            else
              state = -1;
            break;

          /* caso A-K marticola dispari, accetto numeri */
          case 5:
            b = ch >= '0' && ch <= '9';
            if(b && (int)(ch)%2 != 0)
              state = 5;  //rimango in 5 se dispari
            else if(b && (int)(ch)%2 == 0)
              state = 3;  //vado a 3 se pari
            else
              state = -1;
            break;

           /* caso L-Z marticola pari, accetto numeri */
           case 6:
            b = ch >= '0' && ch <= '9';
            if(b && (int)(ch)%2 == 0)
              state = 6;  //rimango in 6 se pari
            else if(b && (int)(ch)%2 != 0)
              state = 4;  //vado a 4 se dispari
            else
              state = -1;
            break;
      }

    }

    return state == 3   //matricola cognomi A-K
        || state == 4;  //matricola cognomi L-Z
  }

  public static void main(String[] args) {
    System.out.println(scan(args[0]) ? "OK" : "NOPE");
  }
}
