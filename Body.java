/**
 * Class that represents a control statement body (i.e., a sequence of statements).
 *   @author Dave Reed
 *   Modified by Nick Bloor
 *   @version 2/27/2026
 */
import java.util.ArrayList;

public class Body {
    private ArrayList<Statement> stmts;
    
    /**
     * Constructs a a body of statements.
     *   param s the list of statements
     */
    public Body(ArrayList<Statement> s) {
    	this.stmts = new ArrayList<Statement>(s);
    }
    
    /**
     * Exexcutes the body statements in order.
     *   @return the status of execution (OK, BREAK, or EXIT)
     */
    public Statement.Status execute() throws Exception {
    	Interpreter.MEMORY.beginScope();
    	for (Statement s: this.stmts) {
    		Statement.Status status = s.execute();
    		if (status != Statement.Status.OK) {
    			Interpreter.MEMORY.endScope();
    			return status;
    		}
    	}
    	Interpreter.MEMORY.endScope();
    	
    	return Statement.Status.OK;
    }
    
    /**
     * Converts the body into a String.
     *   @return the string representation
     */
    public String toString() {
    	String msg = "";
    	for (Statement s : this.stmts) {
    		msg += s + "\n";
    	}
    	return msg.trim();
    }
}
