import java.util.ArrayList;

public class Call extends Statement {
    private String name;
    private ArrayList<Expression> inputs;


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

    public String toString() {
        return this.name + "(" + this.inputs + ")";
    }
}