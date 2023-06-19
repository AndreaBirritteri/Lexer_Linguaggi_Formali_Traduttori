/*
Progettare e implementare un DFA con alfabeto {a, b} che riconosca il linguaggio
delle stringhe tali che a occorre almeno una volta in una delle prime tre
posizioni della stringa. Il DFA deve accettare anche stringhe che contengono
meno di tre simboli (ma almeno uno dei simboli deve essere a).

Esempi di stringhe accettate: "abb", "abbbbbb", "bbaba", "baaaaaaa", "aaaaaaa",
"a", "ba", "bba", "aa", "bbabbbbbbbb"
Esempi di stringhe non accettate: "bbbababab", "b"
*/

public class PrimeTreA {
  public static boolean scan(String s) {
    int state = 0, i = 0;

    while (state >= 0 && i < s.length()) {
      final char ch = s.charAt(i++);

      switch(state) {
        /* caso iniziale, accetto a o b */
        case 0:
          if(ch == 'b')
            state = 2;  //vado in 2 se b
          else if (ch == 'a')
            state = 1;  //vado in 1 se a
          else
            state = -1;
          break;

        /* caso a prima posizione, accetto a o b */
        case 1:
          if(ch == 'a' || ch == 'b')
            state = 1;  //resto in 1 con a o b
          else
            state = -1;
          break;

        /* caso b prima posizione, cerco a, accetto a o b */
        case 2:
          if(ch == 'b')
            state = 3;  //vado a 3 b
          else if(ch == 'a')
            state = 1;  //vado a 1 ho trovato a in seconda posizione
          else
            state = -1;
          break;

         /* caso b seconda posizione, cerco a, accetto a o b */
         case 3:
           if(ch == 'a')
             state = 1; //vado a 1 ho trovato a in terza posizione
           else
             state = -1;
           break;
        }
    }

    return state == 1;  //a nelle prime tre posizioni
  }

  public static void main(String[] args) {
    System.out.println(scan(args[0]) ? "OK" : "NOPE");
  }
}
