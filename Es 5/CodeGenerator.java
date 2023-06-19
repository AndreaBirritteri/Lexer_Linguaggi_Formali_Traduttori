import java.util.LinkedList;
import java.io.*;

public class CodeGenerator {

    LinkedList <Instruction> instructions = new LinkedList <Instruction>();

    /* label = 'L', label1 = 'T' */
    int label=0, label1=0;

    /* stampe delle instructions, in aggiunta String operand per if */
    public void emit(OpCode opCode) {
        instructions.add(new Instruction(opCode));
    }

    public void emit(OpCode opCode , int operand) {
        instructions.add(new Instruction(opCode, operand));
    }

    public void emit(OpCode opCode , String operand) {
        instructions.add(new Instruction(opCode, operand));
    }

    /* stampe delle label */
    public void emitLabel(int operand) {
        emit(OpCode.label, operand);
    }

    public void emitLabel1(int operand) {
        emit(OpCode.label1, operand);
    }

    public void emitStart() {
        emit(OpCode.start);
    }

    /* metodi ottenimento label corrente, con successivo incremento */
    public int newLabel() {
        return label++;
    }

    public int newLabel1() {
        return label1++;
    }

    /* metodi ottenimento label corrente */
    public int getLabel(){
      return label;
    }

    public int getLabel1(){
      return label1;
    }

    public void toJasmin() throws IOException{
        PrintWriter out = new PrintWriter(new FileWriter("Output.j"));
        String temp = "";
        temp = temp + header;
        while(instructions.size() > 0){
            Instruction tmp = instructions.remove();
            temp = temp + tmp.toJasmin();
        }
        temp = temp + footer;
        out.println(temp);
        out.flush();
        out.close();
    }

    private static final String header = ".class public Output \n"
        + ".super java/lang/Object\n"
        + "\n"
        + ".method public <init>()V\n"
        + " aload_0\n"
        + " invokenonvirtual java/lang/Object/<init>()V\n"
        + " return\n"
        + ".end method\n"
        + "\n"
        + ".method public static print(I)V\n"
        + " .limit stack 2\n"
        + " getstatic java/lang/System/out Ljava/io/PrintStream;\n"
        + " iload_0 \n"
        + " invokestatic java/lang/Integer/toString(I)Ljava/lang/String;\n"
        + " invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V\n"
        + " return\n"
        + ".end method\n"
        + "\n"
        + ".method public static read()I\n"
        + " .limit stack 3\n"
        + " new java/util/Scanner\n"
        + " dup\n"
        + " getstatic java/lang/System/in Ljava/io/InputStream;\n"
        + " invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V\n"
        + " invokevirtual java/util/Scanner/next()Ljava/lang/String;\n"
        + " invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I\n"
        + " ireturn\n"
        + ".end method\n"
        + "\n"
        + ".method public static run()V\n"
        + " .limit stack 1024\n"
        + " .limit locals 256\n\n";

    private static final String footer = " return\n"
        + ".end method\n"
        + "\n"
        + ".method public static main([Ljava/lang/String;)V\n"
        + " invokestatic Output/run()V\n"
        + " return\n"
        + ".end method\n";
}
