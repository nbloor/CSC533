/**
 * Derived class that represents an exit statement in the SILLY language.
 * @author Nick Bloor (Inspired by Dave Reed)
 * @version 3/23/2026
 */
public class Exit extends Statement {
    /**
     * Reads in an exit statement from the specified stream.
     * @param input the stream to be read from
     * @throws Exception if the statement is malformed
     */
    public Exit(TokenStream input) throws Exception {
        if (!input.next().toString().equals("exit")) {
            throw new Exception("SYNTAX ERROR: Malformed exit statement");
        }
    }

    /**
     * Executes the current exit statement.
     * @return the Status of EXIT to indicate an exit statement was executed
     */
    public Statement.Status execute() throws Exception {
        return Statement.Status.EXIT;
    }

    /**
     * Converts the current exit statement into a String.
     * @return the String representation of this statement.
     */
    public String toString() {
        return "exit";
    }
}
