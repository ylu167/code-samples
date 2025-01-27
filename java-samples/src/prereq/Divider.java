package prereq;

import java.util.Scanner;

public class Divider {
	
	//do not change the main method
	public static void main(String[] args) {
		Divider divider = new Divider();
		Scanner input = new Scanner(System.in);
		System.out.println("Enter a divisor greater than 1");
		int divisor = input.nextInt();
		System.out.println("Enter a dividend greater than divisor");
		int dividend = input.nextInt();
		divider.divide(dividend, divisor);
		input.close();
	}
	
	private void divide(int x, int y) {
		//write your code here
		while (y != 0) {
			int quotient = x / y;
			int remainder = x % y;
			System.out.println(x + " = " + quotient + " * " + y + " + " + remainder);
			x = quotient;
			y = remainder;
		}
	}
}
