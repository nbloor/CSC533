/**
 * Derived class that represents an output statement in the SILLY language.
 *   @author Dave Reed
 *   @version 1/20/26
 */
public class Print extends Statement {
	private Expression expr;

    /**
     * Reads in a print statement from the specified TokenStream.
     *   @param input the stream to be read from
     */
    public Print(TokenStream input) throws Exception {
    	if (!input.next().toString().equals("print")) {
            throw new Exception("SYNTAX ERROR: Malformed print statement");
        } 
    	
    	this.expr = new Expression(input);
    }

    /**
     * Executes the current output statement - note: strings and characters 
     * are printed without quotation marks.
     */
    public Statement.Status execute() throws Exception {
		System.out.println(this.expr.evaluate().toString());
		
		return Statement.Status.OK;
    }
    
    /**
     * Converts the current output statement into a String.
     *   @return the String representation of this statement
     */
    public String toString() {
    	return "print " + this.expr;
    }
}


