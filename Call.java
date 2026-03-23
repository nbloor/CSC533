import java.util.ArrayList;

/**
 * Class that reads in a subroutine call in SILLY and exewcutes the body with specified inputs.
 * @author Nick Bloor
 * @version 3/23/26
 */
public class Call extends Statement {
    private String name;
    private ArrayList<Expression> inputs;


    /**
     * Reads in a subroutine call from the specified stream.
     * @param input the stream to be read from
     * @throws Exception if the call is malformed
     */
    public Call(TokenStream input) throws Exception {
        if (!input.next().toString().equals("call")) {
            throw new Exception("SYNTAX ERROR: Malformed subroutine call");
        }
        this.name = input.next().toString();
        if (!input.next().toString().equals("(")) {
            throw new Exception("SYNTAX ERROR: Malformed subroutine call");
        }

        this.inputs = new ArrayList<Expression>();

        while(!input.lookAhead().toString().equals(")")) {
            this.inputs.add(new Expression(input));
        }
        input.next();
    }

    /**
     * Executes the subroutine call by evaluating inputs, creating new scope, and executing the body.
      * @return the status of the execution
      * @throws Exception if the subroutine is not declared or if there is a mismatch in number of parameters
     */
    public Statement.Status execute() throws Exception {
        if (!Interpreter.MEMORY.isSubroutineDeclared(this.name)) {
            throw new Exception("RUNTIME ERROR: Subroutine " + this.name + " not declared");
        }
        if (this.inputs.size() != Interpreter.MEMORY.getSubroutineParameters(name).size()){
            throw new Exception("RUNTIME ERROR: Subroutine " + this.name + " called with different number of parameters");
        }
        Body body = Interpreter.MEMORY.retrieveSubroutine(this.name);
        ArrayList<DataValue> inputValues = new ArrayList<DataValue>();
        for (int i = 0; i < this.inputs.size(); i++) {
            DataValue paramValue = (DataValue) this.inputs.get(i).evaluate();
            inputValues.add(paramValue);
        }
        //Store values of inputs - done above.

        //Push new scope for subroutine call onto stack
        Interpreter.MEMORY.createSubScope();
        //Store parameter names and values in new scope
        ArrayList<Token> paramNames = Interpreter.MEMORY.getSubroutineParameters(this.name);
        for (int i = 0; i < paramNames.size(); i++) {
            Interpreter.MEMORY.declareVariable(paramNames.get(i));
            Interpreter.MEMORY.storeValue(paramNames.get(i), inputValues.get(i));
        }
        Statement.Status results = body.execute();
        //Pop scope for subroutine call off stack
        Interpreter.MEMORY.endSubScope();
        return results;
    }

    /**
     * Returns a string representation of the subroutine call.
     * @return the string representation
     */
    public String toString() {
        return this.name + "(" + this.inputs + ")";
    }
}