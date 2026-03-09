/**
 * Derived class that represents a break statement in the SILLY language.
 * @author Nick Bloor (Inspired by Dave Reed)
 * @version 2/27/2026
 */
public class Break extends Statement {
    /**
     * Reads in a break statement from the specified stream.
     * @param input the stream to be read from
     * @throws Exception if the statement is malformed
     */
    public Break(TokenStream input) throws Exception {
        if (!input.next().toString().equals("break")) {
            throw new Exception("SYNTAX ERROR: Malformed break statement");
        }
    }

    /**
     * Executes the current break statement.
     * @return the Status of BREAK to indicate a break statement was executed
     */
    public Statement.Status execute() throws Exception {
        return Statement.Status.BREAK;
    }

    /**
     * Converts the current break statement into a String.
     * @return the String representation of this statement.
     */
    public String toString() {
        return "break";
    }
}
