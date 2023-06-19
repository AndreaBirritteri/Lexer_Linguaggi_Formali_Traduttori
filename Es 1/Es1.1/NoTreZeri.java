/*
  Es 1.1
  Modificare il DFA di esempio 1 in modo che accetti stringhe
  che non contengono tre 0 consecutivi.

  Ex
  "010101" = ok
  "1100011001" = nope
*/
public class NoTreZeri
{
    public static boolean scan(String s)
    {
      	int state = 0, i = 0;

      	while (state >= 0 && i < s.length()) {
      	    final char ch = s.charAt(i++);

      	    switch (state) {
            /* caso iniziale, accetto 0 o 1, avanzo con 0, ritorno con 1 */
      	    case 0:
              		if (ch == '0')
              		    state = 1;
              		else if (ch == '1')
              		    state = 0;
              		else
              		    state = -1;
              		break;

            /* primo 0 trovato, avanzo con secondo 0, torno indietro con 1 */
      	    case 1:
              		if (ch == '0')
              		    state = 2;
              		else if (ch == '1')
              		    state = 0;
              		else
              		    state = -1;
              		break;

            /* secondo 0 trovato, fermo con terzo 0, torno indietro con 1 */
      	    case 2:
              		if (ch == '1')
              		    state = 0;
              		else
              		    state = -1;
              		break;
      	    }
      	}

      	return state == 0 // stato iniziale, valore uno
          || state == 1   // stato con un solo zero,
          || state == 2;  // stato con due zeri consecutivi.
    }

    public static void main(String[] args)
    {
	     System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
