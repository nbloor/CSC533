import java.util.ArrayList;

/**
 * Class that reads in a subroutine definition in the SILLY language and stores teh name, body, and paramters. This is an early version where there will be no parameters or exist statements yet.
 * @author Nick Bloor
 * @version 3/17/26
 */
public class Sub extends Statement {
    private String name;
    private Body body;

    /**
     * Reads in a subroutine definition from the specified stream.
     * @param input the stream to be read from
     */
    public Sub(TokenStream input) throws Exception {
        if (!input.next().toString().equals("sub")) {
            throw new Exception("SYNTAX ERROR: Malformed subroutine definition.");
        }
        this.name = input.next().toString();
        if (!input.next().toString().equals("(")) {
            throw new Exception("SYNTAX ERROR: Malformed subroutine definition.");
        }
        if (!input.next().toString().equals(")")) {
            throw new Exception("SYNTAX ERROR: Malformed subroutine definition.");
        }
        if (!input.next().toString().equals("does")) {
            throw new Exception("SYNTAX ERROR: Malformed subroutine definition.");
        }
        ArrayList<Statement> stmts = new ArrayList<Statement>();
        while (!input.lookAhead().toString().equals("endsub")) {
            stmts.add(Statement.getStatement(input));
        }
        this.body = new Body(stmts);
        input.next();
    }

    /**
     * Executes the current subroutine definition. This shouldn'e execute the sub yet, but should modify MemorySpace with definition.
     */
    public Statement.Status execute() throws Exception {        
        if (Interpreter.MEMORY.isSubroutineDeclared(this.name)){
            throw new Exception ("RUNTIME ERROR: Subroutine already declared");
        }
        Interpreter.MEMORY.storeSubroutines(this.name, this.body);
        return Statement.Status.OK;
    }

    /**
     * Returns a string representation of the subroutine definition.
     * @return the string representation
     */
    public String toString(){
        return "sub " + this.name + " do\n" + this.body.toString() + "endsub\n";
    }
}