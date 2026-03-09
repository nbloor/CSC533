import java.util.ArrayList;

/**
 * Derived class that represents a repeat statement in the SILLY language.
 * @author Nick Bloor (Inspired by Dave Reed)
 * @version 2/27/2026
 */
public class Repeat extends Statement {
    private Expression expr;
    private Body body;

    /**
     * Reads in a repeat statement from the specified stream.
     * @param input the stream to be read from
     */
    public Repeat(TokenStream input) throws Exception{
        if (!input.next().toString().equals("repeat")) {
            throw new Exception("SYNTAX ERROR: Malformed repeat statement");
        }
        this.expr = new Expression(input);  
        if (!input.next().toString().equals("times")) {
            throw new Exception("SYNTAX ERROR: Malformed repeat statement");
        }
        ArrayList<Statement> stmts = new ArrayList<Statement>();
        while (!input.lookAhead().toString().equals("endrepeat")) {
        	stmts.add(Statement.getStatement(input));
        }
        this.body = new Body(stmts);
        input.next();
    }

    /**
     * Executes the current repeat statement.
     */
    public Statement.Status execute() throws Exception {
        if (this.expr.evaluate().getType() != DataValue.Type.INTEGER) {
            throw new Exception("RUNTIME ERROR: repeat statement requires integer test.");
        }
        DataValue numTimes = this.expr.evaluate();
        int count = (Integer)numTimes.getValue();
        for (int i = 0; i <count; i++) {
            Statement.Status status = this.body.execute();
            if (status == Statement.Status.BREAK) {
                break;
            }
        }
        return Statement.Status.OK;
    }

    /**
     * Converts the current repeat statement into a String.
     * @return the String representation of this statement.
     */
    public String toString() {
        return "repeat " + this.expr + "times" + Statement.indent("\n"+this.body) + "\nendrepeat";
    }
    
}
