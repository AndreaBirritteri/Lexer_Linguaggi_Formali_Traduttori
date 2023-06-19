public class Instruction {
    OpCode opCode;
    int operand;
    String op;

    public Instruction(OpCode opCode) {
        this.opCode = opCode;
    }

    public Instruction(OpCode opCode, int operand) {
        this.opCode = opCode;
        this.operand = operand;
    }

    public Instruction(OpCode opCode, String op) {
        this.opCode = opCode;
        this.op = op;
    }

    public String toJasmin () {
        String temp = "";
        switch (opCode) {
            case ldc : temp = " ldc " + operand + "\n"; break;
            case invokestatic :
               if( operand == 1)
                  temp = " invokestatic " + "Output/print(I)V" + "\n";
               else
                  temp = " invokestatic " + "Output/read()I" + "\n"; break;
            case iadd : temp = " iadd " + "\n"; break;
            case imul : temp = " imul " + "\n"; break;
            case idiv : temp = " idiv " + "\n"; break;
            case isub : temp = " isub " + "\n"; break;
            case ineg : temp = " ineg " + "\n"; break;
            case istore : temp = " istore " + operand + "\n"; break;
            case ior : temp = " ior " + "\n"; break;
            case iand : temp = " iand " + "\n"; break;
            case iload : temp = " iload " + operand + "\n"; break;
            /* if per COND, utilizzo label T */
            case if_icmpeq : temp = " if_icmpeq " + op + "\n"; break;
            case if_icmple : temp = " if_icmple " + op + "\n"; break;
            case if_icmplt : temp = " if_icmplt " + op + "\n"; break;
            case if_icmpne : temp = " if_icmpne " + op + "\n"; break;
            case if_icmpge : temp = " if_icmpge " + op + "\n"; break;
            case if_icmpgt : temp = " if_icmpgt " + op + "\n"; break;
            case ifne : temp = " ifne " + op + "\n"; break;
            case GOto :
            if(operand >= 0)
              temp = " goto L" + operand + "\n";
            else
              temp = " goto START\n" ;
            break;
            case label : temp = "L" + operand + ":\n"; break;
            /* label per if di COND */
            case label1 : temp = "T" + operand + ":\n"; break;
            case start : temp = "START:\n"; break;
        }
    return temp;
    }
}
