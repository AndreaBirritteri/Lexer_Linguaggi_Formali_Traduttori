/*
Es 5
Translator.java --> traduzione in IJVM

L’obiettivo di quest’ultima parte di laboratorio e' di quello di realizzare
un traduttore per i programmi scritti nel linguaggio di programmazione semplice,
che chiameremo P, visto nell’esercizio 3.2. I file di programmi del linguaggio
P hanno estensione .lft, come suggerito nelle lezioni di teoria.
Il traduttore deve generare bytecode [4] eseguibile dalla Java Virtual Machine
(JVM). Generare bytecode eseguibile direttamente dalla JVM non e' un’operazione
semplice a causa della complessita' del formato dei file .class (che tra
l’altro e' un formato binario) [3]. Il bytecode verra' quindi generato
avvalendoci di un linguaggio mnemonico che fa riferimento alle istruzioni della
JVM (linguaggio assembler [2]) e che successivamente verra' tradotto nel
formato .class dal programma assembler. Il linguaggio mnemonico utilizzato fa
riferimento all’insieme delle istruzioni della JVM [5] e l’assembler effettua
una traduzione 1-1 delle istruzioni mnemoniche nella corrispondente istruzione
(opcode) della JVM. Il programma assembler che utilizzeremo si chiama Jasmin.
Il file sorgente viene tradotto dal compilatore (oggetto della realizzazione)
nel linguaggio assembler per la JVM. Questo file (che deve avere l’estensione
.j) e poi trasformato in un file .class dal programma assembler Jasmin. Nel
codice presentato in questa sezione, il file generato dal compilatore:

> javac Translator.java

> java Translator

si chiama Output.j, e il comando:

> java -jar jasmin.jar Output.j

e' utilizzato per trasformarlo nel file Output.class, che puo' essere eseguito
con il comando:

> java Output


Grammatica:

     <prog> ::=  <statlist> EOF

 <statlist> ::=  <stat> <statlistp>

<statlistp> ::=  ; <stat> <statlistp>
             |   ε

     <stat> ::=  = ID <expr>
             |   print(<exprlist>)
             |   read(ID)
             |   cond <whenlist> else <stat>
             |   while(<bexpr>) <stat>
             |  {<statlist>}

 <whenlist> ::=  <whenitem> <whenlistp>

<whenlistp> ::=  <whenitem> <whenlistp>
             |   ε

 <whenitem> ::=  when(<bexpr>) do <stat>

    <bexpr> ::=  RELOP <expr> <expr>
             |   && (<bexpr>) (<bexpr>)
             |   || (<bexpr>) (<bexpr>)
             |   ! <bexpr>

     <expr> ::=  + (<exprlist>)
             |   * (<exprlist>)
             |   - <expr> <expr>
             |   / <expr> <expr>
             |   NUM
             |   ID

 <exprlist> ::= <expr> <exprlistp>

<exprlistp> ::=  <expr> <exprlistp>
             |   ε
*/
import java.io.*;

public class Translator {
    private Lexer lex;  //oggetto Lexer
    private BufferedReader pbr; //oggetto lettura file
    private Token look; //codice identificativo della lettura

    SymbolTable st = new SymbolTable(); //oggetto indirizzi variabili
    CodeGenerator code = new CodeGenerator(); //generazione ijvm
    int count = 0;  //contatore variabili

    /* costruttore */
    public Translator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move(); //lettura primo carattere
    }

    /* stampa del token tramite lexer, avanzamento lettura */
    void move() {
        look = lex.lexical_scan(pbr); //metodo del lexer, per stampa dei token
        System.out.println("token = "+ look);
    }

    /* metodo per gestire gli errori */
    void error(String s) {
    	throw new Error("Near line " + lex.line + ": " + s);
    }

    /* confronto tag con conseguente move(), più errore */
    void match(int t) {
    	if (look.tag == t) {
    	    if (look.tag != Tag.EOF) move();
    	} else
          error("Syntax error tag: "+ look.tag +" expected: "+ t);
    }

    /* <prog> ::= <statlist> EOF */
    public void prog() {
      /* controllo corrispondenza con stat */
      if(look.tag == '=' || look.tag == Tag.PRINT
         || look.tag == Tag.READ || look.tag == Tag.COND
         || look.tag == Tag.WHILE || look.tag == '{') {

        int l = code.newLabel(); // passaggio e incremento L: label generale
        int t = code.getLabel1(); //passaggio T: label di COND WHEN...
        code.emitStart(); //stampa START, label iniziale
        /* richiamo statlist passando le label e true per stampa label */
        statlist(l, t, true);
        code.emitLabel(l); //stampo L0
        match(Tag.EOF);
        try {
        	code.toJasmin(); //genero codice Jasmin in Output.j
        }
        catch(java.io.IOException e) {
        	System.out.println("IO error\n");
        }
      } else //error stat non trovato
          error("Error in grammar (prog) expected <stat> find "+ look);
    }

    /* <statlist> ::= <stat> <statlistp> */
    private void statlist(int l, int t, boolean emit) {
      /* controllo dopo ; mi aspetto uno stat */
      if(look.tag == '=' || look.tag == Tag.PRINT
         || look.tag == Tag.READ || look.tag == Tag.COND
         || look.tag == Tag.WHILE || look.tag == '{') {
        stat(l, t); //istruzione corrente
        if(emit){ //condizione di stampa label
          l = code.newLabel(); //L istruzione successiva
          code.emitLabel(l); //stampa di L
        }
        statlistp(l, t, emit);  //prossima istruzione
      } else //error stat non trovato
          error("Error in grammar (statlist) expected <stat> find "+ look);
    }

    /* <statlistp> ::=  ; <stat> <statlistp>
                     |  ε
    */
    private void statlistp(int l, int t, boolean emit) {
      /* no error per rispetto condizione ε */
      if(look.tag == ';'){
        move();
        /* controllo dopo ; mi aspetto uno stat */
        if(look.tag == '=' || look.tag == Tag.PRINT
           || look.tag == Tag.READ || look.tag == Tag.COND
           || look.tag == Tag.WHILE || look.tag == '{') {
          stat(l, t); //istruzione corrente
          if(emit){
            l = code.newLabel(); //L istruzione successiva
            code.emitLabel(l); //stampa di L
          }
          statlistp(l, t, emit);  //prossima istruzione, in ricorsione
        } else //error stat non trovato
            error("Error in grammar (statlistp) expected <stat> find "+ look);
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
    public void stat(int l, int t) {
        switch(look.tag) {
            /* = ID <expr> */
            case '=':
              move();
              if (look.tag == Tag.ID) {
                /* assegno indice variabile */
                int id_addr = st.lookupAddress(((NumberTok)look).lexeme);
                if (id_addr == -1) {  //se non ho ancora registrato la variabile
                    id_addr = count;  //assegno il contatore
                    /* aggiungo la variabile alla tabella */
                    st.insert(((NumberTok)look).lexeme,count++);
                }
                match(Tag.ID);
                expr();
                code.emit(OpCode.istore, id_addr);  //<istore id_addr>

              } else
                  error("Error in grammar (stat) after = with "+ look);
              break;

            /* print(<exprlist>) */
            case Tag.PRINT:
                move();
                match('(');
                exprlist();
                match(')');
                /*
                  operando 0 per la read, 1 per la print
                  stampo invokestatic Output/print(I)V
                */
                code.emit(OpCode.invokestatic, 1);
                break;

            /* read(ID) */
            case Tag.READ:
                move();
                match('(');
                if (look.tag == Tag.ID) {
                    /* assegno indice variabile */
                    int id_addr = st.lookupAddress(((NumberTok)look).lexeme);
                    if (id_addr == -1) {  //se non ho ancora registrato la variabile
                        id_addr = count;  //assegno il contatore
                        /* aggiungo la variabile alla tabella */
                        st.insert(((NumberTok)look).lexeme,count++);
                    }
                    match(Tag.ID);
                    match(')');
                    /*
                      operando 0 per la read, 1 per la print
                      stampo invokestatic Output/read(I)V
                    */
                    code.emit(OpCode.invokestatic, 0);
                    code.emit(OpCode.istore, id_addr);  //<istore id_addr>
                } else
                    error("Error in grammar (stat) after <read> with "+ look);
                break;

            /* cond <whenlist> else <stat> */
            case Tag.COND:
                move();
                whenlist(l, t);
                match(Tag.ELSE);
                stat(l, t);
                break;

            /* while(<bexpr>) <stat> */
            case Tag.WHILE:
              move();
              match('(');
              bexpr('L', code.getLabel());
              match(')');
              stat(l, t);
              if(l > 0)
                code.emit(OpCode.GOto, l);  //<goto L>
              else
                code.emit(OpCode.GOto, -1); //nel caso ho un while all'inizio
              break;

            /* {<statlist>} */
            case '{':
              move();
              /* non stampo etichetta, considero come istruzione unica */
              statlist(l, t, false);
              match('}');
              break;

            /* error */
            default:
              error("Error in grammar (stat): "+ look);
              break;
        }
     }

    /* <whenlist> ::= <whenitem> <whenlistp> */
    private void whenlist(int l, int t) {
      if(look.tag == Tag.WHEN){
      t = code.newLabel1(); //passaggio e incremento T
      whenitem(l, t);
      code.emitLabel1(t); //stampa di T
      whenlistp(l, t);
    } else //error when non trovato
          error("Error in grammar (whenlist) expected <when> find "+ look);
    }

    /*
    <whenlistp> ::=  <whenitem> <whenlistp>
                 |  ε
    */
    private void whenlistp(int l, int t) {
      if(look.tag == Tag.WHEN){
        whenitem(l, t);
        t = code.getLabel1()-1; //aggiorno t
        code.emitLabel1(t); //stampa di T
        whenlistp(l, t);
      }
    }

    /* <whenitem> ::= when(<bexpr>) do <stat> */
    private void whenitem(int l, int t) {
        match(Tag.WHEN);
        match('(');
        bexpr('T', t);
        match(')');
        match(Tag.DO);  //non stampo L per scelta
        stat(l, t);
        code.emit(OpCode.GOto, ++l);//salto alla fine di COND
    }

    /*
    RELOP: ">, >=, <, <=, <>, =="
    <bexpr> ::=  RELOP <expr> <expr>
             |   && (<bexpr>)(<bexpr>)
             |   || (<bexpr>)(<bexpr>)
             |   ! <bexpr>
    */
    private void bexpr(char label, int nlabel) {
      String op = "";
      switch(((Word)look).lexeme) {
        /* caso > */
        case ">":
          move();
          if(look.tag == Tag.NUM || look.tag == Tag.ID){
            expr();
            expr();
            op += "" + label + nlabel;
            /* if <= esco per regola di traduzione ijvm */
            code.emit(OpCode.if_icmple, op);
          } else
              error("Error in grammar (bexpr) after > with "+ look);
          break;

        /* caso >= */
        case ">=":
          move();
          if(look.tag == Tag.NUM || look.tag == Tag.ID){
            expr();
            expr();
            op += "" + label + nlabel;
            /* if < esco per regola di traduzione ijvm */
            code.emit(OpCode.if_icmplt, op);
          } else
              error("Error in grammar (bexpr) after >= with "+ look);
          break;

        /* caso < */
        case "<":
          move();
          if(look.tag == Tag.NUM || look.tag == Tag.ID){
            expr();
            expr();
            op += "" + label + nlabel;
            /* if >= esco per regola di traduzione ijvm */
            code.emit(OpCode.if_icmpge, op);
          } else
              error("Error in grammar (bexpr) after < with "+ look);
          break;

        /* caso <= */
        case "<=":
          move();
          if(look.tag == Tag.NUM || look.tag == Tag.ID){
            expr();
            expr();
            op += "" + label + nlabel;
            /* if > esco per regola di traduzione ijvm */
            code.emit(OpCode.if_icmpgt, op);
          } else
              error("Error in grammar (bexpr) after <= with "+ look);
          break;

        /* caso == */
        case "==":
          move();
          if(look.tag == Tag.NUM || look.tag == Tag.ID){
            expr();
            expr();
            op += "" + label + nlabel;
            /* if <> esco per regola di traduzione ijvm */
            code.emit(OpCode.if_icmpne, op);
          } else
              error("Error in grammar (bexpr) after == with "+ look);
          break;

        /* caso <>, diverso */
        case "<>":
          move();
          if(look.tag == Tag.NUM || look.tag == Tag.ID){
            expr();
            expr();
            op += "" + label + nlabel;
            /* if == esco per regola di traduzione ijvm */
            code.emit(OpCode.if_icmpeq, op);
          } else
              error("Error in grammar (bexpr) after <> with "+ look);
          break;

        /* caso iand */
        case "&&":
          move();
          match('(');
          bexpr(label, nlabel); //richiamo ricorsivo ad espressione booleana
          match(')');
          match('(');
          bexpr(label, nlabel); //richiamo ricorsivo ad espressione booleana
          match(')');
          code.emit(OpCode.iand); //stampo codice
          break;

        /* caso ior */
        case "||":
          move();
          match('(');
          bexpr(label, nlabel); //richiamo ricorsivo ad espressione booleana
          match(')');
          match('(');
          bexpr(label, nlabel); //richiamo ricorsivo ad espressione booleana
          match(')');
          code.emit(OpCode.ior); //stampo codice
          break;

        /* caso ineg */
        case "!":
          move();
          bexpr(label, nlabel); //richiamo ricorsivo ad espressione booleana
          code.emit(OpCode.ineg); //stampo codice
          break;

        /* error */
        default:
          error("Error in grammar (bexpr)"+ look);
          break;
       }
     }

     /*
     <expr> ::=  + (<exprlist>)
             |   * (<exprlist>)
             |   - <expr> <expr>
             |   / <expr> <expr>
             |   NUM
             |   ID
     */
    private void expr() {
        switch(look.tag) {
          /* caso iadd */
          case '+':
            move();
            match('(');
            exprlist();
            match(')');
            code.emit(OpCode.iadd); //<iadd>
            break;

          /* caso imul */
          case '*':
            move();
            match('(');
            exprlist();
            match(')');
            code.emit(OpCode.imul); //<imul>
            break;

          /* caso isub */
          case '-':
            move();
            expr();
            expr();
            code.emit(OpCode.isub); //<isub>
            break;

          /* caso idiv */
          case '/':
            move();
            expr();
            expr();
            code.emit(OpCode.idiv); //<idiv>
            break;

          /* caso ldc op */
          case Tag.NUM:
            code.emit(OpCode.ldc, look.getNum()); //<ldc const>
            move();
            break;

          /* caso iload id_addr */
          case Tag.ID:
            int id_addr = st.lookupAddress(((NumberTok)look).lexeme);
            if (id_addr == -1) {
                id_addr = count;
                st.insert(((NumberTok)look).lexeme,count++);
            }
            code.emit(OpCode.iload, id_addr); //<iload id_addr>
            move();
            break;

          /* error */
          default:
            error("Error in grammar (expr): "+ look);
            break;
        }
    }

    /* <exprlist> ::= <expr> <exprlistp> */
    private void exprlist() {
      if(look.tag == Tag.NUM || look.tag == Tag.ID
        || look.tag == '+' || look.tag == '*' || look.tag == '-'
        || look.tag == '/'){
        expr();
        exprlistp();
      } else //error expr non trovata
          error("Error in grammar (exprlist) expected <expr> find "+ look);
    }

    /*
    <exprlistp> ::=  <expr> <exprlistp>
                  |  ε
    */
    private void exprlistp() {
      if(look.tag == Tag.NUM || look.tag == Tag.ID
        || look.tag == '+' || look.tag == '*' || look.tag == '-'
        || look.tag == '/'){
        expr();
        exprlistp();
      }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\Utente\\Desktop\\BirritteriAndrea"
                      +"\\Es 5\\P.lft";

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();  //eseguo prog
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
