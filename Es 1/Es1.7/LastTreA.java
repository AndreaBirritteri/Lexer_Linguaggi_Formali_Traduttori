/*
Progettare e implementare un DFA con alfabeto {a, b} che riconosca il
linguaggio delle stringhe tali che a occorre almeno una volta in una delle
ultime tre posizioni della stringa.
Come nell’esercizio 1.6, il DFA deve accettare anche stringhe che
contengono meno di tre simboli (ma almeno uno dei simboli deve essere a).

Esempi di stringhe accettate: "abb", "bbaba", "baaaaaaa", "aaaaaaa",
"a", "ba", "bba","aa", "bbbababab"
Esempi di stringhe non accettate: "abbbbbb", "bbabbbbbbbb", "b"
*/

public class LastTreA {
  public static boolean scan(String s) {
    int state = 0, i = 0;

    while (state >= 0 && i < s.length()) {
      final char ch = s.charAt(i++);

      switch(state) {

        /* caso iniziale, accetto b ed a */
        case 0:
          if(ch == 'b')
            state = 0;  //rimango in 0 se trovo b
          else if (ch == 'a')
            state = 1;  //vado ad 1 se trovo a
          else
            state = -1;
          break;

        /* caso prima a, accetto a e b, controllo ultime 3 posizioni */
        case 1:
          if(ch == 'a')
            state = 1;  //rimango in 1 se trovo a
          else if(ch == 'b')
            state = 2;  //vado a 2 se trovo b
          else
            state = -1;
          break;

         /* caso penultima posizione con b, accetto a e b */
         case 2:
           if(ch == 'a')
             state = 1; //torno a 1 se trovo a
           else if(ch == 'b')
             state = 3; //vado a 3 se trovo b
           else
             state = -1;
           break;

         /* stato finale, caso ultima posizione accettabile con a,
            accetto a e b */
         case 3:
           if(ch == 'a')
             state = 1; //torno a 1 se trovo a
           else if(ch == 'b')
             state = 0; //torno a 0 se trovo b
           else
             state = -1;
           break;
        }
    }

    return state == 1   //caso a terzultima pos
        || state == 2   //caso a penultima pos
        || state == 3;  //caso a ultima pos
  }

  public static void main(String[] args) {
    System.out.println(scan(args[0]) ? "OK" : "NOPE");
  }
}
