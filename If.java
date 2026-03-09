import java.util.ArrayList;

/**\
 * Derived class that represents an if statement in SILLY
 * @author Nick Bloor (Inspired by Dave Reed)
 * @version 2/27/26
 */
public class If extends Statement {
    private ArrayList<Expression> conditions;
    private ArrayList<Body> bodies;

    /**
     * Reads in an if statement from the specified stream.
     * @param input The stream to be read from
     * @throws Exception if the statement is malformed
     */
    public If(TokenStream input) throws Exception {
        if (!input.next().toString().equals("if")) {
            throw new Exception("SYNTAX ERROR: Malformed if statement");
        }
        
        this.conditions = new ArrayList<Expression>();
        this.bodies = new ArrayList<Body>();
        
        
        this.conditions.add(new Expression(input));
        if (!input.next().toString().equals("then")) {
            throw new Exception("SYNTAX ERROR: Malformed if statement");
        }
        ArrayList<Statement> stmts = new ArrayList<Statement>();
        while (!input.lookAhead().toString().equals("endif") && !input.lookAhead().toString().equals("else") && !input.lookAhead().toString().equals("elseif")) {
        	stmts.add(Statement.getStatement(input));
        }
        this.bodies.add(new Body(stmts));
        
        while(input.lookAhead().toString().equals("elseif") || input.lookAhead().toString().equals("else")) {
        	String keyword = input.next().toString();
        	ArrayList<Statement> elseStmts = new ArrayList<Statement>();
        	
        	if (keyword.equals("elseif")) {
        		this.conditions.add(new Expression(input));
        		if (!input.next().toString().equals("then")) {
        			throw new Exception("SYNTAX ERROR: Malformed elseif statement");
        		}
        	} else {
        		// This menas that we are using else, so there is no condition to check. Add a null condition so number of conditions and bodies are equal.
        		this.conditions.add(null);
        	}
        	
        	while (!input.lookAhead().toString().equals("endif") && !input.lookAhead().toString().equals("else") && !input.lookAhead().toString().equals("elseif")) {
        		elseStmts.add(Statement.getStatement(input));
        	}
        	this.bodies.add(new Body(elseStmts));
        }
        
        while(!input.lookAhead().toString().equals("endif")) {
        	input.next();
        }
        input.next();
    }

    /**
     * Executes the current if statement.
     */
    public Statement.Status execute() throws Exception {
        // Check each condition in order until one is true
        for (int i = 0; i < this.conditions.size(); i++) {
            Expression cond = this.conditions.get(i);
            
            // If condition is null, it's an else clause (always execute)
            if (cond == null) {
                this.bodies.get(i).execute();
                return Statement.Status.OK;
            }
            
            DataValue eVal = cond.evaluate();
            if (eVal.getType() != DataValue.Type.BOOLEAN){
                throw new Exception ("RUNTIME ERROR: if statement requires Boolean values");
            }
            
            if ((Boolean)eVal.getValue()) {
                Statement.Status status = this.bodies.get(i).execute();
                if (status == Statement.Status.BREAK) {
                    return Statement.Status.BREAK;
                }
                return Statement.Status.OK;
            }
        }
        return Statement.Status.OK;
    }

    public String toString() {
        String output = "if " + this.conditions.get(0) + " then" + Statement.indent("\n" + this.bodies.get(0));
        
        // All elseif and else clauses
        for (int i = 1; i < this.conditions.size(); i++) {
            Expression cond = this.conditions.get(i);
            if (cond == null) {
                output += "\nelse";
            } else {
                output += "\nelseif " + cond + " then";
            }
            output += Statement.indent("\n" + this.bodies.get(i));
        }
        
        output += "\nendif";
        return output;
    }
}
