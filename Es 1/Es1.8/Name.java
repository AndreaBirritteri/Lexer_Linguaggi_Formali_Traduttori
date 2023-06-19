/*
Progettare e implementare un DFA che riconosca il linguaggio di stringhe che
contengono il tuo nome e tutte le stringhe ottenute dopo la sostituzione di un
carattere del nome con un altro qualsiasi. Ad esempio, nel caso di uno studente
che si chiama Paolo, il DFA deve accettare la stringa "Paolo" (cio`e il nome
scritto correttamente), ma anche le stringhe "Pjolo","caolo", "Pa%lo", "Paola"
e "Parlo" (il nome dopo la sostituzione di un carattere), ma non "Eva", "Perro",
 "Pietro" oppure "P*o*o".

 Caso Andrea
*/
public class Name {
  public static boolean scan(String s) {
    int state = 0, i = 0, nameLenght = 6;

    if(s.length() == nameLenght){
      while (state >= 0 && i < s.length()) {
        final char ch = s.charAt(i++);

        switch(state) {

          /* caso iniziale, accetto A o qualsiasi carattere*/
          case 0:
            if(ch == 'A')
              state = 1;  //caso carattere normale
            else
              state = 2;  //caso carattere cambiato
            break;

          /* caso A, accetto n o qualsiasi carattere*/
          case 1:
            if(ch == 'n')
              state = 3;  //caso carattere normale
            else
              state = 4;  //caso carattere cambiato
            break;

          /* caso 1 lettera errata prima, accetto n*/
          case 2:
            if(ch == 'n')
              state = 4;  //caso carattere normale
            else
              state = -1;
            break;

          /* caso An, accetto d o qualsiasi carattere*/
          case 3:
            if(ch == 'd')
              state = 5;  //caso carattere normale
            else
              state = 6;  //caso carattere cambiato
            break;

          /* caso 1 lettera errata prima, accetto d*/
          case 4:
            if(ch == 'd')
              state = 6;  //caso carattere normale
            else
              state = -1;
            break;

          /* caso And, accetto r o qualsiasi carattere*/
          case 5:
            if(ch == 'r')
              state = 7;  //caso carattere normale
            else
              state = 8;  //caso carattere cambiato
            break;

          /* caso 1 lettera errata prima, accetto r*/
          case 6:
            if(ch == 'r')
              state = 8;  //caso carattere normale
            else
              state = -1;
            break;

          /* caso Andr, accetto e o qualsiasi carattere*/
          case 7:
            if(ch == 'e')
              state = 9;  //caso carattere normale
            else
              state = 10; //caso carattere cambiato
            break;

          /* caso 1 lettera errata prima, accetto e */
          case 8:
            if(ch == 'e')
              state = 10;
            else
              state = -1;
            break;

          /* caso Andre, accetto qualsiasi carattere */
          case 9:
            state = 11;
            break;

          /* caso 1 lettera errata prima, accetto a */
          case 10:
            if(ch == 'a')
              state = 11;
            else
              state = -1;
            break;
          }
        }
      }
    else
      state = -1;

    return state == 11; //fine caratteri nome
  }

  public static void main(String[] args) {
    System.out.println(scan(args[0]) ? "OK" : "NOPE");
  }
}
