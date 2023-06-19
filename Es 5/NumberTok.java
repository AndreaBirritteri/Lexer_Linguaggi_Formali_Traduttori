public class NumberTok extends Token {
  public String lexeme = "";  //stringa per ottenimento valore
  public void setLexeme(String s) { lexeme = s;}  //imposta valore
  public int getNum() {return Integer.parseInt(lexeme);}  //casting per i num

  public NumberTok(int tag, String s) { super(tag); lexeme = s; }

  public String toString() {return "<" + tag + ", " + lexeme + ">";}

  public static final NumberTok
   num = new NumberTok(Tag.NUM, ""), //oggetto numero: tag e stringa
   id = new NumberTok(Tag.ID, "");  //oggetto identificatore: tag e stringa
}
