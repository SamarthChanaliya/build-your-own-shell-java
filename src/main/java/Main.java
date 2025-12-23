//Code Crafters
//Build your own shell
// progress - 4/42

import java.util.ArrayList;
import java.util.Scanner;

public class Main 
{
    public static void main(String[] args) throws Exception 
    {
        Scanner userInput = new Scanner(System.in);
        boolean running = true;

        ArrayList<String> builtinCommands = new ArrayList<>();

        builtinCommands.add("exit");
        builtinCommands.add("echo");
        builtinCommands.add("type");
        

        while (running) 
        {
            System.out.print("$ ");
            String prompt = userInput.nextLine();
            String arguments = null;
            
            for (String command : builtinCommands) 
            {
                if (prompt.length() > command.length()) 
                {
                    arguments = prompt.substring(command.length() + 1);
                }
                if (prompt.startsWith(command)) 
                {
                    if (command.equalsIgnoreCase("exit")) 
                    {
                        running = false;
                        break;
                    }
                    else if (command.equalsIgnoreCase("echo")) 
                    {
                        if (prompt.length() <= command.length()) 
                        {
                            System.out.println();
                        }
                        else
                        {
                            System.out.println(arguments);
                        }
                    }
                    else if (command.equalsIgnoreCase("type")) 
                    {
                        if (builtinCommands.contains(command)) 
                        {
                            System.out.println(arguments + " is a shell builtin");
                        }
                        else
                        {
                            System.out.println(arguments + ": not found");
                        }
                    }
                    else
                    {
                        System.out.println(prompt + ": command not found");
                    }
                }
            }
        }
        userInput.close();
    }
}
