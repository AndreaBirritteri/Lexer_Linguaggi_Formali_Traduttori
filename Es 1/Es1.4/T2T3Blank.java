/*
ES1.4
Modificare l’automa dell’esercizio precedente in modo che riconosca le
combinazioni di matricola e cognome di studenti del turno 2 o del turno 3
del laboratorio, dove il numero di matricola e il cognome possono essere
separati da una sequenza di spazi, e possono essere precedute e/o seguite da
sequenze eventualmente vuote di spazi.
Si ricorda le regole per suddivisione di studenti in turni:
• Turno T2: iniziale `e compresa tra A e K, e matricola `e pari;
• Turno T3: iniziale `e compresa tra L e Z, e matricola `e dispari;

Esempio accettate: "1234 Bianchi", " 1234De Gasperi " e "12345  Teo  Gasperi".
Esempio non accettate: "123456Bia nchi" e "1234 5Rianchi".

*/

public class T2T3Blank {
  public static boolean scan(String s) {
    int state = 0, i = 0;

    while (state >= 0 && i < s.length()) {
      final char ch = s.charAt(i++);

      switch(state) {
          /* caso iniziale, accetto spazi e numeri */
          case 0:
            if(ch == ' ')
              state = 0;  //spazio iniziale rimango in 0
            else if(Character.isDigit(ch)){
              if((int)(ch)%2 == 0)  //vado in uno se numero pari
                state = 1;
              else  //vado in due se numero dispari
                state = 2;
            } else
                state = -1;
            break;

          /* caso numero pari, accetto A-K, spazi e numeri */
          case 1:
            if(ch == ' ')
              state = 4;  //spazio vado a 4
            else if(ch >= 'A' && ch <= 'K')
              state = 3;  //se trovo lettera vado a 3
            else if(Character.isDigit(ch)){
              if((int)(ch)%2 == 0)
                state = 1;  //resto in uno se numero pari
              else
                state = 2;  //vado in due se numero dispari
            } else
                state = -1;
            break;

          /* caso numero dispari, accetto L-Z, spazi e numeri */
          case 2:
            if(ch == ' ')
              state = 5;  //spazio vado a 5
            else if(ch >= 'L' && ch <= 'Z')
              state = 3;  //se trovo lettera vado a 3
            else if(Character.isDigit(ch)){
              if((int)(ch)%2 == 0)
                state = 1;  //torno in uno se numero pari
              else
                state = 2;  //resto in due se numero dispari
            } else
                state = -1;
            break;

          /* stato finale, caso lettera, accetto le lettere minuscole e spazi*/
          case 3:
            if(ch >= 'a' && ch <= 'z')
              state = 3;  //se trovo lettera vado a 3
            else if (ch == ' ')
              state = 6;  //spazio vado a 6
            else
              state = -1;
            break;

          /* caso spazio matricola pari, accetto A-K e spazi*/
          case 4:
            if(ch >= 'A' && ch <= 'K')
              state = 3;  //se trovo lettera torno a 3
            else if(ch == ' ')
              state = 4;  //spazio resto in 4
            else
              state = -1;
            break;

          /* caso spazio matricola dispari, accetto L-Z e spazi*/
          case 5:
              if(ch >= 'L' && ch <= 'Z')
                state = 3;  //se trovo lettera torno a 3
              else if(ch == ' ')
                state = 5;  //spazio resto in 5
              else
                state = -1;
              break;

          /* stato finale, caso cognome composto, accetto A-Z e spazi*/
          case 6:
             if(ch >= 'A' && ch <= 'Z')
              state = 3;  //se trovo lettera torno a 3
             else if(ch == ' ')
              state = 6;  //spazio resto in 6
             else
              state = -1;
            break;
      }

    }

    return state == 3 //fine cognome
          || state == 6; //cognomi composti
  }

  public static void main(String[] args) {
    System.out.println(scan(args[0]) ? "OK" : "NOPE");

  }
}
