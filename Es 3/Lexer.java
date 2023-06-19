import java.io.*;
import java.util.*;

public class Lexer {

    public static int line = 1; //variabile conteggio linee
    private char peek = ' ';  //carattere di scorrimento

    /* metodo per lettera del carattere da file */
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    /* metodo di assegnamento token */
    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br); //leggo il carattere
        }

        switch (peek) { //controllo sul carattere

            /* per (, ), {, }, +, -, * e ; valgono le stesse operazioni */

            case '(':
                peek = ' '; //metto il carattere a ' ' per proseguire il ciclo
                return Token.lpt; //ritorno il token di riferimento

            case ')':
                peek = ' ';
                return Token.rpt;

            case '{':
                peek = ' ';
                return Token.lpg;

            case '}':
                peek = ' ';
                return Token.rpg;

            case '+':
                peek = ' ';
                return Token.plus;

            case '-':
                peek = ' ';
                return Token.minus;

            case '*':
                peek = ' ';
                return Token.mult;

            case ';':
                peek = ' ';
                return Token.semicolon;

            /* caso / controllo se e' commento oppure operazionem sul seguente
               carattere */
            case '/':
                readch(br); //guardo il prossimo
                if (peek == '/'){ // caso commento riga singola
                  while(peek != '\n'){  //ciclo finche' non vado a capo
                    if(peek != (char)-1)
                      readch(br); //leggo carattere/i dentro commento
                    else
                      return new Token(Tag.EOF);  //ritorno EOF
                  }
                  return new Token(Tag.NULL); //tag a 0 per non leggere commenti
                } else if(peek == '*'){ //caso commento riga multipla
                  readch(br); //guardo il prossimo
                  while(peek != '*'){ //mi aspetto la chiusura del commento
                      readch(br); //leggo carattere/i dentro commento
                  }
                  readch(br); //guardo il prossimo
                  if(peek == '/'){  //caso chiusura riga multipla
                    peek = ' '; //setto a spazio
                    return new Token(Tag.NULL); //ritorno il token 0
                  } else {
                      System.err.println("Erroneous character"
                              + " after / : "  + peek );
                      return null;  //ritorno null come errore
                  }
                } else {
                    return Token.div; //caso operatore, ritorno token
                }


            /* caso &&, mi aspetto due &&*/
            case '&':
                readch(br); //controllo seguente
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {  //error
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }

            /* caso ||, mi aspetto due ||*/
            case '|':
                readch(br); //controllo seguente
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {  //error
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }

            /* caso !, gestione per es 5.2, utilizzo classe WORD
               per medesimo utilizzo delle altre operazioni logiche
            */
            case '!':
                peek = ' ';
                return Word.neg;


            /* caso <, <=, <> mi aspetto numero o lettera, = o >*/
            case '<':
                readch(br); //controllo seguente
                if (peek == '=') {  //caso <=
                    peek = ' ';
                    return Word.le;
                } else if(peek == '>') {  //caso <>
                  peek = ' ';
                  return Word.ne;
                } else if(Character.isLetter(peek) || Character.isDigit(peek)
                    || peek == ' ') { //caso <
                  return Word.lt;
                } else {  //error
                    System.err.println("Erroneous character"
                            + " after < : "  + peek );
                    return null;
                }

            /* caso >, >= mi aspetto numero o lettera, = */
            case '>':
                readch(br); //controllo seguente
                if (peek == '=') {  //caso >=
                    peek = ' ';
                    return Word.ge;
                } else if(Character.isLetter(peek) || Character.isDigit(peek)
                    || peek == ' ') {  //caso >
                  return Word.gt;
                } else {  //error
                    System.err.println("Erroneous character"
                            + " after > : "  + peek );
                    return null;
                }

            /* caso =, == mi aspetto numero o lettera, = */
            case '=':
                readch(br); //controllo seguente
                if (peek == '=') {  //caso >=
                    peek = ' ';
                    return Word.eq;
                } else if(Character.isLetter(peek) || Character.isDigit(peek)
                    || peek == ' ') {  //caso =
                  return Token.assign;
                } else {  //error
                    System.err.println("Erroneous character"
                            + " after = : "  + peek );
                    return null;
                }

            /* caso :+, mi aspetto = */
            case ':':
                readch(br); //controllo seguente
                if (peek == '=') {  //caso :=
                    peek = ' ';
                    return Word.assign;
                } else {  //error
                    System.err.println("Erroneous character"
                            + " after : : "  + peek );
                    return null;
                }

            /* caso EOF ovvero '-1' */
            case (char)-1:
                return new Token(Tag.EOF);

            /*  casi cond, when, then, while, do, seq, print, read */
            default:
                if (Character.isLetter(peek) || peek == '_') {
                  String id = "";
                  int c = 0, i = 0;

                  while(Character.isLetter(peek) || Character.isDigit(peek)
                      || peek == '_'){
                    id += peek; //compongo la stringa id
                    readch(br);
                  }

                  if(id != ""){ //se id ha valore
                    /* assegnamento token rispettivo */
                    if(id.equals("cond")){
                      return Word.condtok;
                    } else if (id.equals("when")){
                      return Word.when;
                    } else if (id.equals("then")){
                      return Word.then;
                    } else if (id.equals("else")){
                      return Word.elsetok;
                    } else if (id.equals("while")){
                      return Word.whiletok;
                    } else if (id.equals("do")){
                      return Word.dotok;
                    } else if (id.equals("print")){
                      return Word.print;
                    } else if (id.equals("read")){
                      return Word.read;
                    } else{
                      while(i <= id.length() - 1){
                        if(id.charAt(i) == '_') //controllo sugli '_'
                          c++;  //conto gli '_'
                        i++;
                      }
                      /* controllo che non sia composto da soli '_' e che il
                         primo carattere non sia un numero */
                      if(c < id.length() && !Character.isDigit(id.charAt(0))){
                          NumberTok.id.setLexeme(id); //imposto valore lexeme
                          return NumberTok.id;  //imposto token
                        }
                      else {  //error
                          System.err.println("Only character: "
                                             + peek );
                          return null;
                        }
                    }
                  } else{ //error
                      System.err.println("Erroneous id: "
                                          + id);
                      return null;
                  }

                } else if (Character.isDigit(peek)) { //caso numero
                  String num = "";  //stringa d'appoggio per assegnamento
                  while(Character.isDigit(peek)){ //concateno i numeri in num
                    num += peek;
                    readch(br);
                  }
                  NumberTok.num.setLexeme(num); //imposto stringa concatenata
                  return NumberTok.num;

                } else {  //error
                        System.err.println("Erroneous character: "
                                + peek );
                        return null;
                }
         }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();  //oggetto lexer
        String path = "C:\\Users\\Utente\\Desktop\\BirritteriAndrea"
                      +"\\Es 2\\testlexer.txt"; // file's path
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br); //richiamo metodo su lex
                if(tok.tag != 0) //controllo se
                  System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
