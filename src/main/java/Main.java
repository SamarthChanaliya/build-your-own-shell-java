// Code Crafters
// Build your own shell
// progress - 4/42

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner userInput = new Scanner(System.in);

        ArrayList<String> builtinCommands = new ArrayList<>();
        builtinCommands.add("exit");
        builtinCommands.add("echo");
        builtinCommands.add("type");

        String path = System.getenv("PATH");
        String[] dirs = path.split(File.pathSeparator);

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
                        Path foundPath = findInPath(arguments, dirs);
                        if (builtinCommands.contains(arguments)) {
                            System.out.println(arguments + " is a shell builtin");
                        } else if (foundPath != null) {
                            System.out.println(arguments + " is " + foundPath);
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

    // use Files.walk to find the command name in the array of directories
    public static Path findInPath(String commandName, String[] directories) throws IOException{
        for (String directory : directories) {
            Path path = Path.of(directory);
            if (Files.exists(path) || Files.isExecutable(path)) {
                return path;
            }
        }
        return null;
    }
}