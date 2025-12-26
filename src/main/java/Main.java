// Code Crafters
// Build your own shell
// progress - 4/42

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner userInput = new Scanner(System.in);

        ArrayList<String> builtinCommands = new ArrayList<>();
        builtinCommands.add("exit");
        builtinCommands.add("echo");
        builtinCommands.add("type");

        boolean running = true;

        while (running) {
            System.out.print("$ ");
            String prompt = userInput.nextLine();
            String arguments = "";

            boolean commandFound = false;

            
            for (String command : builtinCommands) {

                if (prompt.length() > command.length()) {
                    arguments = prompt.substring(command.length() + 1);
                }

                

                if (prompt.startsWith(command)) {

                    commandFound = true;

                    if (command.equals("exit")) {
                        running = false;
                        
                        break;
                    }

                    else if (command.equals("echo")) {
                        if (prompt.length() <= command.length()) {
                            System.out.println();
                        } else {
                            System.out.println(arguments);
                        }
                        break;
                    }

                    else if (command.equals("type")) {
                        if (builtinCommands.contains(arguments)) {
                            System.out.println(arguments + " is a shell builtin");
                        } else {
                            System.out.println(arguments + ": not found");
                        }
                        break;
                    }
                }
            }
            if (!commandFound) {
                System.out.println(prompt + ": command not found");
            }
        }

        userInput.close();
    }
}
