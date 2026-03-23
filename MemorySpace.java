import java.util.Stack;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that defines the memory space for the SILLY interpreter.
 *   @author Dave Reed
 *   Modified by Nick Bloor
 *   @version 3/17/2026
 */
public class MemorySpace {
    private Stack<ScopeRecord> runtimeStack;
    private ArrayList<String> heapSpace;
    private HashMap<String, Body> subroutines;
    private HashMap<String, ArrayList<Token>> subroutineParameters;

    /**
     * Constructs an empty memory space.
     */
    public MemorySpace() {
        this.runtimeStack = new Stack<ScopeRecord>();
        this.runtimeStack.push(new ScopeRecord(null));
        this.heapSpace = new ArrayList<String>();
        this.subroutines = new HashMap<String, Body>();
        this.subroutineParameters = new HashMap<String, ArrayList<Token>>();
    }
    
    /**
     * Adds a new scope to the top of the runtime stack (linked to previous top).
     */
    public void beginScope() {
    	this.runtimeStack.push(new ScopeRecord(this.runtimeStack.peek()));
    }
    
    /**
     * Removes the current scope from the top of the runtime stack.
     */
    public void endScope() {
    	this.runtimeStack.pop();
    }   	

    /**
     * Declares a variable in local scope (without storing an actual value).
     *   @param variable the variable to be declared
     */
    public void declareVariable(Token variable) {
        this.runtimeStack.peek().storeInScope(variable, null);
    }
    
    /** 
     * Determines if a variable is already declared.
     * @param variable the variable to be found
     * @return true if it is declared and/or assigned; else, false
     */
    public boolean isDeclared(Token variable) {
    	return (this.findScopeinStack(variable) != null);
    }

    /**
     * Stores a variable/value in the runtime stack.
     *   @param variable the variable name
     *   @param val the value to be stored under that name
     */
    public void storeValue(Token variable, DataValue val)  {
    	this.findScopeinStack(variable).storeInScope(variable, val);
    }
    
    /**
     * Determines the value associated with a variable in memory.
     *   @param variable the variable to look up
     *   @return the value associated with that variable
     */      
    public DataValue lookupValue(Token variable) {
    	return this.findScopeinStack(variable).lookupInScope(variable);
    }
    
    /**
     * Stores a string value in the heap (if not already stored), returns its index.
     * @param str the string being added
     * @return its index in the heap list
     */
    public int heapStore(String str) {
    	for (int i = 0; i < heapSpace.size(); i++) {
    		if (str.equals(heapSpace.get(i))) {
    			return i;
    		}
    	}
    	heapSpace.add(str);
    	return heapSpace.size()-1;
    }
    
    /**
     * Look up a string value in the heap.
     * @param index the index in the heap list
     * @return the string value stored at that index
     */
    public String heapLookup(int index) {
    	return heapSpace.get(index);
    }
 
    /////////////////////////////////////////////////////////////////////////////
    
    /**
     * Locates the Scope in the stackSegment that contains the specified variable.
     * @param variable the variable being searched for
     * @return the Scope containing that variable
     */
    private ScopeRecord findScopeinStack(Token variable) {
    	ScopeRecord stepper = this.runtimeStack.peek();
    	while (stepper != null && !stepper.declaredInScope(variable)) {
    		stepper = stepper.getParentScope();
    	}
    	return stepper;
    }

    /**
     * Determiens if a variable is declared in the current local scope.
     * @param variable the variable being checked if in scope
     * @return true if variable is declared in the current local scope, otherwise it is false
     */
    public boolean isLocal(Token variable){
        return this.runtimeStack.peek().declaredInScope(variable);
    }

    /**
     * Stores a subroutine definition in the subroutines HashMap.
     * @param name the name of the subroutine
     * @param body the body of the subroutine
     */
    public void storeSubroutine(String name, Body body){
        this.subroutines.put(name, body);
    }

    /**
     * Retrieves subroutine definition from subroutines HashMap.
     * @param name the name of the subroutine
     * @return the body of the subroutine
     */
    public Body retrieveSubroutine(String name){
        return this.subroutines.get(name);
    }

    /**
     * Determines if a subroutine is already declared in the subroutines HashMap.
     * @param name the name of the subroutine being checked
     * @return true if already declared, false otherwise
     */
    public boolean isSubroutineDeclared(String name){
        return this.subroutines.containsKey(name);
    }

    /**
     * Stores the parameter names for a subroutine in the subroutineParameters HashMap.
     * @param name the name of the subroutine
     * @param parameters the list of parameter names for the subroutine
     */
    public void storeSubroutineParameters(String name, ArrayList<Token> parameters){
        this.subroutineParameters.put(name, parameters);
    }

    /**
     * Retrieves the parameter names for a subroutine from the subroutineParameters HashMap.
     * @param name the name of the subroutine
     * @return the list of parameter names for the subroutine
     */
    public ArrayList<Token> getSubroutineParameters(String name){
        return this.subroutineParameters.get(name);
    }

    public void createSubScope(){
        this.runtimeStack.push(new ScopeRecord(null));
    }

    public void endSubScope(){
        this.runtimeStack.pop();
    }
}
