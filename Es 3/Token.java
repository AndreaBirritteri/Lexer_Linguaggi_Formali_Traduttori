public class Token {
    public final int tag;
    public Token(int t) { tag = t; }
    public int getNum() {return -1;}  //-1 error per NumberTok
    public String toString() {return "<" + tag + ">";}
    public static final Token
      //rimozione not per gestione di es 5.2
    	lpt = new Token('('),
    	rpt = new Token(')'),
    	lpg = new Token('{'),
    	rpg = new Token('}'),
    	plus = new Token('+'),
    	minus = new Token('-'),
    	mult = new Token('*'),
    	div = new Token('/'),
    	semicolon = new Token(';'),
    	assign = new Token('=');
}
