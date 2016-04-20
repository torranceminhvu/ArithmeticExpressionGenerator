/*
 *  Program created by Minh Vu
 * 
 * 
 */
package arithmexpr;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @Class:ArithmExpr
 * @Description: Takes as input 3-6 integers, the first integer is the target 
 * integer and the following integers will be a set that the program will use along
 * with the operators +,-,*,/ and () in order to create an expression that will
 * be equal to the target value after evaluation. The set of integers can be from
 * 2-5 and a target integer must be provided
 * @author: Minh Vu
 * @Date: 4/15/2016
 */
public class ArithmExpr {

    // Operators that program will use to create expression
    private static final String[] OPERATORS = new String[]{"*", "+", "/", "-"};
    
    // Output message for incorrect usage and invalid inputs
    private static final String INVALID_INPUT_PROMPT = "Invalid Inputs: Please Input Only Valid Integers";
    private static final String USAGE = "USAGE: \n" + "First Arg: Target Number\n" + "Second Args: Set of Integers With Size of 2-5";

    /** 
     * @Function: main
     * @parameters: String[] args, the input from command terminal
     * @description: Calls private methods from class in order to create an 
     * arithmetic expression that will equal to the provided target value, or 
     * returns none if no such expressions exists.
     */
    public static void main(String[] args) {
        // list to store user input
        List<String> input = new ArrayList<>();
        
        // boolean to determine when an expression was found
        boolean found = false;
        
        // String to store value of target integer
        String target;

        // checks for incorrect number of arguments passed
        if (args.length < 3 || args.length > 6) {
            System.out.println(USAGE);
            return;
        }

        // Loops through the arguments to check if inputs are valid integers
        for (String s : args) {
            if (s.length() == 1) {
                for (char c : s.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        System.out.println(INVALID_INPUT_PROMPT);
                        return;
                    }
                }
            } else {
                char[] carr = s.toCharArray();
                if (carr[0] == '-' || Character.isDigit(carr[0])) {
                    for (int i = 1; i < carr.length; i++) {
                        if (!Character.isDigit(carr[i])) {
                            System.out.println(INVALID_INPUT_PROMPT);
                            return;
                        }
                    }
                }
            }
        }
        
        // set target to be first user input and loop through the rest of user
        // input and store them into input list
        target = args[0];
        for (int i = 1; i < args.length; i++) {
            input.add(args[i]);
        }

        // Calls function generateAllNumPerm to return a list of list of strings
        // which will be all the different possible permutations of the integers
        List<List<String>> numPermList = new ArrayList<>(generateAllNumPerm(input));
        for (List<String> l : numPermList) {
            /* for each permutation from the list of all possible permutations
            * call the function generateAllRPN which will generate a list of a list
            * of strings which will contain all the valid permutation of the
            * reverse polish notation
            */
            List<List<String>> RPNList = new ArrayList<>(generateAllRPN(l));
            for (List<String> l1 : RPNList) {
                /* for each permutation of reverse polish notation, call function
                * evaluate on it and compare it with target value,if it is true
                * then call the print method on that permutation to print out
                * the expression and to return. If user wants to print all possible
                * combination of valid expressions, then take comment out line
                * 105 and 108-110.
                */
                if (evaluate(l1) == Double.valueOf(target)) {
                    found = true;
                    print(l1);
                    System.out.println(" = " + target);
                    break;
                }
            }
            // break out of loop to return only one expression
            if (found == true) {
                break;
            }
        }
        // checks to see if expression exists, if not return none
        if (found == false) {
            System.out.println("none");
        }
    }

    /** 
     * @Function: print
     * @parameters: List<String> input, the Reverse Polish Notation expr
     * @description: Prints out the arithmetic expression from the reverse
     * polish notation expression using a stack
     */
    private static void print(List<String> input) {
        // stack and string array store inputs
        Stack<String> stack = new Stack<>();
        String[] stringArr = input.toArray(new String[0]);

        // loops through the String array
        for (String input1 : stringArr) {
            char check = input1.charAt(0);
            // if input from string is an operator, pop expressions from stack and 
            // push the new expression back on
            if ((check == '+' || check == '-' || check == '*' || check == '/') && input1.length() == 1) {
                String b = stack.pop();
                String a = stack.pop();
                stack.push("(" + a + " " + check + " " + b + ")");
            } else { // for cases when inputs are integers, push the expression on the stack
                stack.push(input1);
            }
        }
        // Print the last thing on the stack
        System.out.print(stack.pop());
    }

    /** 
     * @Function: evaluate
     * @parameters: List<String> input, the Reverse Polish Notation expr
     * @description: solve and evaluates the reverse polish notation expression 
     * using a stack and returns a double as an answer
     */
    private static double evaluate(List<String> input) {
        // lcoal variables for storing
        Stack<String> stack = new Stack<>();
        String[] stringArr = input.toArray(new String[0]);
        double temp = 0;
        String result;
        
        // for each element in the array (operator or operand)
        for (String input1 : stringArr) {
            char check = input1.charAt(0);
            /* element is an operand, so push onto stack, else it is an operator
             * then pop two operand from stack and match operator and call appropriate
             * mathematical function on it, then push the result back onto stack
             */
            if (input1.length() > 1 || Character.isDigit(check)) {
                stack.push(input1);
            } else if (input1.length() == 1 && !Character.isDigit(check)) {
                double b = Double.valueOf(stack.pop());
                double a = Double.valueOf(stack.pop());
                switch (check) {
                    case '+':
                        temp = a + b;
                        break;
                    case '-':
                        temp = a - b;
                        break;
                    case '*':
                        temp = a * b;
                        break;
                    case '/':
                        temp = a / b;
                        break;
                }
                stack.push(String.valueOf(temp));
            }
        }
        // return the last element on the stack as a double
        result = stack.pop();
        return Double.valueOf(result);
    }

    /** 
     * @Function: generateAllRPN
     * @parameters: List<String> operands, the list of operands by itself
     * @description: Returns a list of a list of string which will be all the
     * possible valid permutation of the reverse polish notation for that
     * given set of operand inputs
     */
    private static List<List<String>> generateAllRPN(List<String> operands) {
        // local var to store information
        List<List<String>> allPermutations = new ArrayList<>();
        Stack<List<String>> stack = new Stack<>();
        List<String> start = new ArrayList<>();
        
        // push empty list onto stack to start algorithm
        stack.push(start);
        
        // retrieves the amount of operands and valid reverse polish notation length
        int sizeOfOperands = operands.size();
        int validRPNLength = 2 * sizeOfOperands - 1;

        while (!stack.isEmpty()) {
            // pop from stack and check length of expression
            List<String> currentExpr = new ArrayList<>(stack.pop());
            int currentExprLength = currentExpr.size();

            // if length is a valid length, add list of string to result list and
            // continue with loop
            if (currentExprLength == validRPNLength) {
                allPermutations.add(currentExpr);
                continue;
            }

            // keeps track of number of operands available for valid concat of another
            // operator and used number of operands
            int numOfOperandsAvail = 0;
            int numOfOperandsUsed = 0;
            for (String s : currentExpr) {
                char check = s.charAt(0);
                // for every operator encountered, decrement available
                if ((check == '+' || check == '-' || check == '*' || check == '/') && s.length() == 1) {
                    numOfOperandsAvail -= 1;

                } else { // for every operand, increment availble and used
                    numOfOperandsAvail += 1;
                    numOfOperandsUsed += 1;
                }
            }

            // if there are still operands to be used, create a new copy of the
            // current list and add another operand and push to stack
            if (numOfOperandsUsed != sizeOfOperands) {
                List<String> addNewOperand = new ArrayList<>(currentExpr);
                addNewOperand.add(operands.get(numOfOperandsUsed));
                stack.push(addNewOperand);
            }

            // if true, then an operator is allowed to be added to the end of the
            // expression, and can be added to the stack
            if (numOfOperandsAvail > 1) {
                for (String op : OPERATORS) {
                    List<String> addNewOperator = new ArrayList<>(currentExpr);
                    addNewOperator.add(op);
                    stack.push(addNewOperator);
                }
            }
        }
        
        // return list of list of string, which are the permutations of RPN
        return allPermutations;
    }

    /** 
     * @Function: generateAllNumPerm
     * @parameters: List<String> input, input from user command terminal
     * @description: generates all possible permutation from input
     */
    private static List<List<String>> generateAllNumPerm(List<String> input) {
        // local variables
        List<List<String>> allPermutations = new ArrayList<>();
        List<String> building = new ArrayList<>();
        
        // delegates to dfs function
        dfs(input, allPermutations, building, input.size());
        
        // return list of list of string which are all the permutation of input
        // integers
        return allPermutations;
    }

    /** 
     * @Function: dfs
     * @parameters: List<String> input, the Reverse Polish Notation expr
     * List<List<String>> allPermutations, list to store result to return from previous function
     * List<String> building, the current permutation being built
     * int size, size of original input list
     * @description: DFS algorithm that finds all possible combination of integers
     * and store them in allPermutations
     */
    private static void dfs(List<String> input, List<List<String>> allPermutations, List<String> building, int size) {
        // if the size of the current permutation being built it same as original input
        // add it to list of result for previous function to return
        if (size == building.size()) {
            List<String> ansCopy = new ArrayList<>(building);
            allPermutations.add(ansCopy);
        } else { // build permutation
            // for each integer in original list of input
            for (int i = 0; i < input.size(); i++) {
                // store current integer from current input to add back in after recursive call
                String stored = input.get(i);
                
                // add integer to current permutation being built and remove it
                // from input list
                building.add(stored);
                input.remove(i);
                
                // recursively call dfs with current permutation being built
                dfs(input, allPermutations, building, size);
                
                // Return from previous dfs call, so remove last element from 
                // current permutation list and add element that was originally
                // removed from input list back in
                building.remove(building.size() - 1);
                input.add(i, stored);
            }
        }
    }
}
