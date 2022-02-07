package expressionEvaluation;
import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class expressionEvaluation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Declare an ArrayList for the lines in the input file
		ArrayList<String> lines = new ArrayList<>();
		// Attempt to open the input file
		try {
			File inputFile = new File("sampleIn.txt"); // Create File object
			Scanner file = new Scanner(inputFile); // Create Scanner object
			
			while(file.hasNextLine()) { // Read in lines from file and add them to arraylist
				String expression = file.nextLine(); // Individual expression
				lines.add(expression); // Add expression to arraylist
			}
			file.close(); // Close the file
		}
		catch (FileNotFoundException e) { // If the file cannot be found
			System.out.println("File not found");
		}
		
		// Initialize valid and invalid queues
		Queue<String> valid = new LinkedList<>(); // Create queue to store valid expressions
		Queue<String> invalid = new LinkedList<>(); // Create queue to store invalid expressions
		
		// For loop over all lines in sampleIn.txt
		for(String expression: lines) {
			char[] array = new char[expression.length()]; // Create a new array of characters to represent each expression
			for (int i = 0; i < expression.length(); i++) {
				array[i] = expression.charAt(i); // Populate the array
			}
			if(formChecker(array)) { // If the expression is of a valid format
				String string = ""; // Begin with empty string
				for (int i = 0; i < array.length; i++) { // Iterate over the length of the array to populate string
					string += String.valueOf(array[i]);
				}
				valid.add(expression); // Add line to valid queue
			}
			else { // If the expression is of an invalid format
				String string = ""; // Begin with empty string
				for (int i = 0; i < array.length; i++) { // Iterate over the length of the array to populate string
					string += String.valueOf(array[i]);
				}
				invalid.add(expression); // Add line to invalid queue
			}
		}
		
		Queue<String> finalValid = new LinkedList<>(); // Create new Queue to store valid expressions that have been evaluated
		
		while(!valid.isEmpty()) { // While there is stuff in the valid queue
			// Evaluate and write valid queue and answers to output file
			String expression = valid.remove(); // Acquire expression
			ArrayList<Character> exp = new ArrayList<Character>(); // Create arraylist to represent expression
			for (int i = 0; i < expression.length(); i++) { // Iterate over the length of the array to populate the arraylist
				exp.add(i, expression.charAt(i));
			}
			expression += " = " + String.valueOf(arithmetic(exp)); // Add an equal sign to the arraylist
			finalValid.add(expression); // Add the solved expression to the final valid queue
		}
		System.out.println("Evaluating valid queue:"); // Inform user of final valid queue
		while(!finalValid.isEmpty()) { // While there is stuff in the final valid queue
			System.out.println(finalValid.remove()); // Print out the current value of the queue
		}
		System.out.println("\nShowing invalid queue:"); // Inform user of invalid queue
		while(!invalid.isEmpty()) { // While there is stuff in the invalid queue
			System.out.println(invalid.remove() + ": Invalid"); // Print out the current value of the queue
		}
	}
	
	/** A function to perform arithmetic operations on an expression
	 * 
	 * @param expression The expression to evaluate
	 * @return The result of the expression
	 */
	public static int arithmetic(ArrayList<Character> array) {
		// Solve the expression
		// Do parentheses first, then addition and subtraction
		Stack<Character> stack = new Stack<Character>(); // Create new stack to store characters from the expression
		for (int i = 0; i < array.size(); i++) { // Iterate over the size of the expression
			String string = String.valueOf(array.get(i)); // Get the string value of the current character
			if(string.matches("[0-9]")) { // If the current character is a number
				stack.push(array.get(i)); // Push the character to the stack
			}
			else if(string.matches("[+]") && array.get(i+1) != '(' && array.get(i+1) != ')') { // If the current character is a + sign, and the next character is not a parenthese
				int a = Character.getNumericValue(stack.pop()); // Store the integer value of whatever is on top of the stack
				int b = Character.getNumericValue(array.get(i+1)); // Store the integer value of the next character
				array.remove(i); // Remove the plus sign from the array
				int sum = a + b; // Store the sum of the two values
				char c = Character.forDigit(sum, 10); // Convert the sum to a character
				stack.push(c); // Push the sum to the stack
			}
			else if(string.matches("[-]") && array.get(i+1) != '(' && array.get(i+1) != ')') { // If the current character is a - sign, and the next character is not a parenthese
				int a = Character.getNumericValue(stack.pop()); // Store the integer value of whatever is on top of the stack
				int b = Character.getNumericValue(array.get(i+1)); // Store the integer value of the next character
				array.remove(i); // Remove the minus sign from the array
				int sum = a - b; // Store the difference of the two values
				char c = Character.forDigit(sum, 10); // Convert the difference to a character
				stack.push(c); // Push the difference to the stack
			}
			else if(array.get(i) == '+') { // If the current character is a + sign
				stack.push(array.get(i)); // Push the current character to the stack
			}
			else if(array.get(i) == '-') { // If the current character is a - sign
				stack.push(array.get(i)); // Push the current character to the stack
			}
			else if(array.get(i) == '(') { // If the current character is an opening parenthese
				stack.push(array.get(i)); // Push the current character to the stack
			}
			else if(array.get(i) == ')') { // If the current character is a closing parenthese
				char charVal = stack.pop(); // Store the character that is on top
				int value = Character.getNumericValue(charVal); // Get the integer value of charVal
				stack.pop(); // Pop the opening parenthese from the stack
				if(!stack.isEmpty()) { // If there is stuff in the stack
					char operator = stack.pop(); // Store the operator
					if(operator == '+') { // If the operator is a + sign
						int a = Character.getNumericValue(stack.pop()); // Store the integer value of whatever is on top of the stack
						int sum = a + value; // Store the sum
						char c = Character.forDigit(sum, 10); // Get the character value of the sum
						stack.push(c); // Push the character sum to the stack
					}
					else if(operator == '-') { // If the operator is a - sign
						int a = Character.getNumericValue(stack.pop()); // Store the integer value of whatever is on top of the stack
						int sum = a - value; // Store the sum
						char c = Character.forDigit(sum, 10); // Get the character value of the sum
						stack.push(c); // Push the character sum to the stack
					}
				}
				else { // If the stack is empty
					stack.push(charVal); // Push the character value to the stack
				}
			}
		}
		
		return Character.getNumericValue(stack.pop()); // Return whatever is at the top of the stack (Stack size should be 1 anyway)
	}
	
	/** A function to check if a string is correctly formatted for arithmetic operation
	 * 
	 * @param string The string to check
	 * @return Whether or not the string is in a valid format
	 */
	public static boolean formChecker(char[] array) {
		// Check string  to make sure it only contains numbers, (,),+,-, and is balanced. If yes, return true
		if(!balanced(array)) { // If the array is not balanced
			return false; // Return false
		}
		for(char i: array) { // Iterate over the array
			String string = String.valueOf(i); // Store the string value of the current element
			if(string.matches("[a-zA-Z]")) { // If the string value is a letter
				return false; // Return false
			}
		}
		for (int i = 0; i < array.length; i++) { // Iterate over the length of the array
			if(array[i] == '+' || array[i] == '-') { // If the current value is a + or - sign
				String string = String.valueOf(array[i-1]); // Store the string value of the element prior to the current one
				if(!string.matches("[0-9)]")) { // If the prior element is not a number
					return false; // Return false
				}
				string = String.valueOf(array[i+1]); // Store the string value of the element after the current one
				if(!string.matches("[0-9(]")) { // If the next element is not a number
					return false; // Return false
				}
			}
		}
		return true; // Return true
	}
	
	/** A function to check if the parentheses of an expression are balanced
	 * 
	 * @param expression The expression to check
	 * @return Whether or not the expression is balanced
	 */
	public static boolean balanced(char[] expression) {
		Stack<Character> stack = new Stack<Character>(); // Create a stack to store the expression
		for (int i = 0; i < expression.length; i++) { // Iterate over the length of the expression
			if(expression[i] == '(') { // If the current character is an opening parenthese
				stack.push(expression[i]); // Push the character to the stack
			}
			else if(expression[i] == ')') { // If the current character is a closing parenthese
				if(stack.isEmpty()) { // If there is nothing in the stack
					return false; // Return false
				}
				else if(!matchingPair(stack.pop(), expression[i])) { // If the top value of the stack does not match the current character
					return false; // Return false
				}
			}
		}
		if(stack.isEmpty()) { // If the stack is empty
			return true; // Return true
		}
		else { // If the stack is not empty
			return false; // Return false
		}
	}
	
	/** A function to check if two characters are a complete pair of parentheses
	 * 
	 * @param character1 The first character
	 * @param character2 The second character
	 * @return Whether or not the pair is valid
	 */
	public static boolean matchingPair(char character1, char character2) { 
	    if (character1 == '(' && character2 == ')') { // If character1 is an opening parenthese and character2 is a closing parenthese
	    	return true; // Return true
	    }
	    else { // If character1 is not an opening parenthese or character2 is not a closing parenthese
	    	return false; // Return false
	    }
	} 
}
