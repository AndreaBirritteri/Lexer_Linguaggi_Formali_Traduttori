/*
Implementare DFA {/,*,a} che riconosca il linguaggio dei commenti, ogni volta
che apro un commento mi attendo la chiusura.
*/
public class Commenti {
  public static boolean scan(String s) {
    int state = 0, i = 0;

    while (state >= 0 && i < s.length()) {
      final char ch = s.charAt(i++);

      switch(state) {
        /* caso iniziale, accetto / */
        case 0:
          if(ch == '/')
            state = 1;  //vado ad 1 con /
          else
            state = -1;
          break;

        /* caso /, accetto * */
        case 1:
          if(ch == '*')
            state = 2;  //vado ad 1 con apertura
          else
            state = -1;
          break;

         /* caso /*, accetto a, / o possibile chiusura con * */
         case 2:
           if(ch == '*')
             state = 3; //vado a 3 nel caso di chiusura
           else if(ch == 'a' || ch == '/')
             state = 2; //resto in 2 con a o /
           else
             state = -1;
           break;

        /* caso * possibile chiusura, accetto a, / o possibile chiusura con * */
         case 3:
           if(ch == '/')
             state = 4; //vado a 4 completamento di chiusura
           else if(ch == 'a')
             state = 2; //torno a 2 con a
           else if(ch == '*')
             state = 3; //resto in 3 nuova possibile chiusura
           else
             state = -1;
           break;

         /* stato finale, caso chiusura */
         case 4:
           if(ch != 0)
             state = -1;
           break;
        }
    }

    return state == 4;  //chiusura del commento
  }

  public static void main(String[] args) {
    System.out.println(scan(args[0]) ? "OK" : "NOPE");
  }
}
