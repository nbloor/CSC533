public class Call extends Statement {
    private String name;

    public Call(TokenStream input) throws Exception {
        if (!input.next().toString().equals("call")) {
            throw new Exception("SYNTAX ERROR: Malformed subroutine call");
        }
        this.name = input.next().toString();
        if (!input.next().toString().equals("(") || !input.next().toString().equals(")")) {
            throw new Exception("SYNTAX ERROR: Malformed subroutine call");
        }
    }

    public Statement.Status execute() throws Exception {
        if (!Interpreter.MEMORY.isSubroutineDeclared(this.name)) {
            throw new Exception("RUNTIME ERROR: Subroutine " + this.name + " not declared");
        }
        Body body = Interpreter.MEMORY.retrieveSubroutine(this.name);
        return body.execute();
    }

    public String toString() {
        return this.name + "()";
    }
}