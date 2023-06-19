/*
Es 3.1
Si scriva un analizzatore sintattico a discesa ricorsiva che parsifichi
espressioni aritmetiche molto semplici, scritte in notazione infissa, e
composte soltanto da numeri non negativi (ovvero sequenze di cifre decimali),
operatori di somma e sottrazione + e -, operatori di moltiplicazione e
divisione * e /, simboli di parentesi ( e ).

Errori:
  )2 start    3/3*4 + 3/3*4 - 3/3*4 EOF
  2+( expr    3/3*4 + 3/3*4 - 3/3*4
  5+) term    3/3*4
  1+2( termp  /3*4
  1*+2 fact   3
  1+2) match ("syntax error")

*/

import java.io.*;
import java.lang.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look; //oggetto di controllo dei token

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    /* stampa del token tramite lexer, avanzamento lettura */
    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    /* metodo per gestire gli errori */
    void error(String s) {
    	throw new Error("Near line " + lex.line + ": " + s);
    }

    /* confronto tag con conseguente move(), più errore */
    void match(int t) {
    	if (look.tag == t) {
    	    if (look.tag != Tag.EOF) move();
    	} else error("Syntax error in match");
    }

    /* <start> ::= <expr> EOF */
    public void start() {
      if(look.tag == '(' || look.tag == Tag.NUM) {  //fact inizia con ( o NUM
        expr(); //richiamo expr
        match(Tag.EOF); //controllo fine del file '-1'
      } else
          error("start error"); //error in start
    }

    /* <expr> ::= <term> <exprp> */
    private void expr() {
      if(look.tag == '(' || look.tag == Tag.NUM) {  //fact inizia con ( o NUM
        term(); //richiamo term
        exprp();  //richiamo exprp
      }
      else
        error("expr error");  //error in expr
    }

    /*
    <exprp> ::=  + <term> <exprp>
              |  - <term> <exprp>
              |  ε
    */
    private void exprp() {
      if(look.tag != Tag.EOF) //controllo che non sono a ;
        switch (look.tag) { //controllo sul tag di look
          /* casi uguali per '+' e '-' */
          case '+':
          case '-':
            move(); //eseguo il move per stampa e proseguimento
            term(); //richiamo term
            exprp();  //richiamo ricorsivo exprp
            break;

          /* casi accettati ma non gestiti, ovvero caso ε */
          case '*':
          case '/':
          case ')':
            break;

          /* error in exprp */
          default:
            error("exprp error");
            break;
         }
    }

    /* <term> ::= <fact> <termp> */
    private void term() {
      if(look.tag == '(' || look.tag == Tag.NUM) {  //fact inizia con ( o NUM
        fact(); //richiamo fact
        termp();  //richiamo termp
      }
      else
        error("term error");  //error in term
    }

    /*
    <termp> ::= * <fact> <termp>
              | / <fact> <termp>
              | ε
    */
    private void termp() {
      if(look.tag != Tag.EOF) //controllo che non sono a ;
        switch (look.tag) {
            /* casi uguali per '*' e '/' */
            case '*':
            case '/':
              move();
              fact();
              termp();
              break;

            /* casi accettati ma non gestiti, ovvero caso ε*/
            case '+':
            case '-':
            case ')':
              break;

            /* error in termp */
            default:
              error("termp error");
              break;
          }
    }

    /* <fact> ::= (<expr>) | NUM */
    private void fact() {
      switch (look.tag) {
        /* caso (<expr>) */
        case '(':
          move(); //uso move
          expr(); //richiamo expr
          match(')'); //controllo )
          break;

        /* caso NUM */
        case Tag.NUM:
          move(); //uso move
          break;

        /* error in fact */
        default:
          error("fact error");
          break;
      }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\Utente\\Desktop\\BirritteriAndrea"
                      +"\\Es 3\\testparser.txt"; //path file lettura
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start(); //richiamo start
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
