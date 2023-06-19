/*
Es 3.2
Si scriva un analizzatore sintattico a discesa ricorsiva che parsifichi
espressioni aritmetiche molto semplici, scritte in notazione prefissa invece
che infissa come l'esercizio precedente.
*/

import java.io.*;
import java.lang.*;

public class Parser2 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser2(Lexer l, BufferedReader br) {
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
    	throw new Error("near line " + lex.line + ": " + s);
    }

    /* confronto tag con conseguente move(), più errore */
    void match(int t) {
    	if (look.tag == t) {
    	    if (look.tag != Tag.EOF) move();
    	} else
          error("syntax error tag: "+ look.tag);
    }

    /* <prog> ::= <statlist> EOF */
    public void prog() {
      statlist(); //richiamo statlist
      match(Tag.EOF); //controllo fine del file '-1'
    }

    /* <statlist> ::= <stat> <statlistp> */
    private void statlist() {
      stat(); //richiamo stat
      statlistp();  //richiamo statlistp
    }

    /* <statlistp> ::=  ; <stat> <statlistp>
                     |  ε
    */
    private void statlistp() {
      if(look.tag == ';'){  //controllo carattere ';'
        move(); //eseguo il move
        stat(); //richiamo stat
        statlistp();  //richiamo ricorsivo su statlistp
      }
    }

    /*
    <stat> ::= = ID <expr>
            |  print(<exprlist>)
            |  read(ID)
            |  cond <whenlist> else <stat>
            |  while(<bexpr>) <stat>
            | {<statlist>}
    */
    private void stat() {
      switch (look.tag) {
        /* = ID <expr> */
        case '=':
          move();
          match(Tag.ID);
          expr();
          break;

        /* print(<exprlist>) */
        case Tag.PRINT:
          move();
          match('(');
          exprlist();
          match(')');
          break;

        /* read(ID) */
        case Tag.READ:
          move();
          match('(');
          match(Tag.ID);
          match(')');
          break;

        /* cond <whenlist> else <stat> */
        case Tag.COND:
          move();
          whenlist();
          match(Tag.ELSE);
          stat();
          break;

        /* while(<bexpr>) <stat> */
        case Tag.WHILE:
          move();
          match('(');
          bexpr();
          match(')');
          stat();
          break;

        /* {<statlist>} */
        case '{':
          move();
          statlist();
          match('}');
          break;

        /* error in stat */
        default:
          error("stat error tag: "+ look.tag);
          break;
      }
    }

    /* <whenlist> ::= <whenitem> <whenlistp> */
    private void whenlist() {
      whenitem();
      whenlistp();
    }

    /*
    <whenlistp> ::=  <whenitem> <whenlistp>
                  |  ε
    */
    private void whenlistp() {
      if(look.tag == Tag.WHEN){ //eseguirò il move nella chiamata successiva
        whenitem();
        whenlistp();  //richiamo ricorsivo whenlistp
      }
    }

    /* <whenitem> ::= when(<bexpr>) do <stat> */
    private void whenitem() {
      if(look.tag == Tag.WHEN){
        move();
        match('(');
        bexpr();
        match(')');
        match(Tag.DO);
        stat();
      } else
          error("whenitem error tag: "+ look.tag);  //error in whenitem
    }

    /* <bexpr> ::= RELOP <expr> <expr>*/
    private void bexpr() {
      if(look.tag == Tag.RELOP){
        move();
        expr();
        expr();
      } else
          error("bexpr error tag: "+ look.tag); //error in bexpr
    }

    /*
    <expr> ::=  + (<exprlist>)
            |   * (<exprlist>)
            |   read(ID)
            |   - <expr> <expr>
            |   / <expr> <expr>
            |   NUM
            |   ID
    */
    private void expr() {
      switch (look.tag) {
        /*
          + (<exprlist>)
          * (<exprlist>)
        */
        case '+':
        case '*':
          move();
          match('(');
          exprlist();
          match(')');
          break;

        /*
          - <expr> <expr>
          / <expr> <expr>
        */
        case '-':
        case '/':
          move();
          expr();
          expr();
          break;

        /*
          NUM
          ID
        */
        case Tag.NUM:
        case Tag.ID:
          move();
          break;

        /* error in expr */
        default:
          error("expr error tag: "+ look.tag);
          break;
      }
    }

    /* <exprlist> ::= <expr> <exprlistp> */
    private void exprlist() {
      expr();
      exprlistp();
    }

    /*
    <exprlistp> ::=  <expr> <exprlistp>
                  |  ε
    */
    private void exprlistp() {
      if(look.tag == Tag.NUM || look.tag == Tag.ID
        || look.tag == '+' || look.tag == '*'
        || look.tag == '-' || look.tag == '/') {
        expr();
        exprlistp();
      }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\Utente\\Desktop\\BirritteriAndrea"
                      +"\\Es 3\\testparser2.txt"; //path file lettura
        try {
            BufferedReader br =
              new BufferedReader(new FileReader(path));
            Parser2 parser2 = new Parser2(lex, br);
            parser2.prog(); //richiamo prog
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
