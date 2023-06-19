/*
Esercizio 4
Modificare l’analizzatore sintattico dell’esercizio 3.1 in modo
da valutare le espressioni aritmetiche semplici, facendo
riferimento allo schema di traduzione diretto dalla sintassi
seguente
*/

import java.io.*;
import java.lang.*;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) {
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

    /* <start> ::= {print(expr.val)} */
    public void start() {
      int expr_val = 0;
      if(look.tag == '(' || look.tag == Tag.NUM){
        expr_val = expr();
        match(Tag.EOF);
        System.out.println("\nexpr_val: "+ expr_val);
      } else
          error("start error tag: "+ look.tag);
    }

    /*
      <expr> ::= {exprp.i = term.val} {expr.val = exprp.val}
    */
    private int expr() {
      int term_val = 0, expr_val = 0;
      if(look.tag == '(' || look.tag == Tag.NUM){
        term_val = term();
        expr_val = exprp(term_val);
      } else
        error("expr error tag: "+ look.tag);

      return expr_val;
    }

    /*
      <exprp> ::= {exprp1.i = exprp.i+term.val} {exprp.val = exprp1.val}
               |  {exprp1.i = exprp.i-term.val} {exprp.val = exprp1.val}
               |  {exprp.val = exprp.i}
    */
    private int exprp(int exprp_i) {
      int term_val = 0, exprp_val = 0;
      switch (look.tag) {
        case '+':
          move();
          term_val = term();
          exprp_val = exprp(exprp_i + term_val);
          break;
        case '-':
          move();
          term_val = term();
          exprp_val = exprp(exprp_i - term_val);
          break;
        case '*':
        case '/':
        case ')':
        case Tag.EOF:
          exprp_val = exprp_i;
          break;
        default:
          error("exprp error tag: "+ look.tag);
          break;
      }

      return exprp_val;
    }

    /* <term> ::= {termp.i = fact.val} {term.val = termp.val} */
    private int term() {
      int fact_val = 0, term_val = 0;
      if(look.tag == '(' || look.tag == Tag.NUM){
        fact_val = fact();
        term_val = termp(fact_val);
      } else
        error("term error tag: "+ look.tag);

      return term_val;
    }

    /*
      <termp> ::= {termp1.i = termp.i*fact.val} {termp.val = termp1.val}
               |  {termp1.i = termp.i/fact.val} {termp.val = termp1.val}
               |  {termp.val = termp.i}
    */
    private int termp(int termp_i) {
      int fact_val = 0, termp_val = 0;
      switch (look.tag) {

        case '*':
          move();
          fact_val = fact();  //assegno a fact_val fact
          termp_val = termp(termp_i * fact_val);  //operazione * in termp_val
          break;

        case '/':
          move();
          fact_val = fact();  //assegno a fact_val fact
          termp_val = termp(termp_i / fact_val);  //operazione / in termp_val
          break;

        case '+':
        case '-':
        case ')':
        case Tag.EOF:
          termp_val = termp_i;  //assegno a termp_val termp_i
          break;

        default:
          error("termp error tag: "+ look.tag); //error in termp
          break;
      }

      return termp_val;
    }

    /*
       <fact> ::= {fact.val = expr.val}
               |  {fact.val = NUM.val}
    */
    private int fact() {
      int fact_val = 0;
      if(look.tag != Tag.EOF)
        switch (look.tag) {

          case '(':
            move();
            fact_val = expr();  //assegno a fact_val expr
            match(')');
            break;

          case Tag.NUM:
            fact_val = look.getNum(); //assegno valore numerico di lexeme
            move();
            break;

          default:
            error("fact error tag: "+ look.tag);  //error in fact
            break;
        }
      return fact_val;
    }

    public static void main(String[] args) {
      Lexer lex = new Lexer();
      String path = "C:\\Users\\Utente\\Desktop\\BirritteriAndrea"
                    +"\\Es 3\\testparser.txt"; // path del file
      try {
        BufferedReader br = new BufferedReader(new FileReader(path));
        Valutatore valutatore = new Valutatore(lex, br);
        valutatore.start(); //eseguo start
        br.close();
      } catch (IOException e) {e.printStackTrace();}
    }
}
