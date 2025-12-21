//Code Crafters
//Build your own shell
// pregress - 4/42

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner userInput = new Scanner(System.in);

        final String EXIT = "exit"; 
        final String ECHO = "echo";


        while (true) {
            System.out.print("$ ");
            String prompt = userInput.nextLine();


            if (prompt.equalsIgnoreCase(EXIT)) {
                break;
            }
            else if (prompt.startsWith(ECHO)) {
                if (prompt.length() <= 4) {
                    System.out.println();
                }
                else{
                    System.out.println(prompt.substring(ECHO.length() + 1));
                }
            }
            else{
                System.out.println(prompt + ": command not found");
            }
        }
        userInput.close();
    }
}
