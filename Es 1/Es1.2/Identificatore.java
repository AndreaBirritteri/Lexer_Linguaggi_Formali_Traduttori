/*
Es1.2
Progettare e implementare un DFA che riconosca il linguaggio degli
identificatori in un linguaggio in stile Java: un identificatore e' una
sequenza non vuota di lettere, numeri, ed il simbolo di underscore _ che
non comincia con un numero e che non può essere composto solo dal simbolo _.

Esempi: "x", "flag1", "x2y2", "x_1",
        "lft__lab", "_temp",
        "x_1_y_2", "x___", "__5".
*/

public class Identificatore {
  public static boolean scan(String s) {
    int state = 0, i = 0;

    while (state >= 0 && i < s.length()) {
      final char ch = s.charAt(i++);

      switch(state) {
        /*caso iniziale, non accetto numeri*/
        case 0:
          if(ch == '_')
            state = 1;
          else if(Character.isLetter(ch))
            state = 2;
          else
            state = -1;
          break;

        /*caso che prende '_', avanzo con numeri o lettere*/
        case 1:
          if(ch == '_')
            state = 1;
          else if(Character.isLetter(ch)
                  || Character.isDigit(ch))
            state = 2;
          else
            state = -1;
          break;

        /*caso finale che accetta lettere, numeri o '_'*/
        case 2:
          if(Character.isLetter(ch)
             || Character.isDigit(ch)
             || ch == '_')
            state = 2;
          else
            state = -1;
          break;
      }

    }

    return state == 2; /*stato finale accettato dal DFA*/
  }

  public static void main(String[] args) {
    System.out.println(scan(args[0]) ? "OK" : "NOPE");
  }
}
