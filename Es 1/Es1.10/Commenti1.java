/*
Implementare DFA {/,*,a} che riconosca il linguaggio dei commenti, ogni volta
che apro un commento mi attendo la chiusura. Posso non avere l'apertura del
commento. Accetto stringhe prima e dopo di essi.
*/
public class Commenti1 {
  public static boolean scan(String s) {
    int state = 0, i = 0;

    while (state >= 0 && i < s.length()) {
      final char ch = s.charAt(i++);

      switch(state) {
        /* stato iniziale e finale, accetto a, * o / con possibile apertura */
        case 0:
          if(ch == '/')
            state = 1;  //vado a 1 con possibile apertura
          else if(ch == 'a' || ch == '*')
            state = 0;  //resto in 0 con a, * stringhe pre commento
          else
            state = -1;
          break;

        /* caso / possibile apertura, accetto a, / o * apertura */
        case 1:
          if(ch == '*')
            state = 2;  //vado a 2 con apertura
          else if(ch == 'a' || ch == '/')
            state = 0;  //torno in 0 con a, / stringhe pre commento
          else
            state = -1;
          break;

          /* caso apertura, accetto a, / o * possibile chiusura */
         case 2:
           if(ch == '*')
             state = 3; //vado a 3 con possibile chiusura
           else if(ch == 'a' || ch == '/')
             state = 2; //resto in 2 con a,/ stringhe intra commento
           else
             state = -1;
           break;

         /* caso possibile chiusura,
            accetto a, / chiusura o * possibile chiusura */
         case 3:
           if(ch == '/')
             state = 4; //vado a 4 con chiusura
           else if(ch == 'a')
             state = 2; //torno a 2 con a stringhe intra commento
           else if(ch == '*')
             state = 3; //resto in 3 con possibile chiusura
           else
             state = -1;
           break;

         /* stato finale, caso chiusura, accetto a, * o / possibile apertura */
         case 4:
           if(ch == 'a' || ch == '*')
             state = 0; //torno a 0 con a, * stringhe intra commento
           else if(ch == '/')
             state = 1; //vado in 1 con possibile apertura
           else
             state = -1;
           break;
        }
    }

    return state == 0   //caso testo
        || state == 4;  //caso chiusura commento
  }

  public static void main(String[] args) {
    System.out.println(scan(args[0]) ? "OK" : "NOPE");
  }
}
