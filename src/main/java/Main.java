// Code Crafters
// Build your own shell
// progress - 7/42

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

            boolean commandFoundInBuiltin = false;


            label:
            for (String command : builtinCommands) {

                if (prompt.length() > command.length()) {
                    arguments = prompt.substring(command.length() + 1);
                }



                if (prompt.startsWith(command)) {

                    commandFoundInBuiltin = true;

                    switch (command) {
                        case "exit":
                            running = false;

                            break label;
                        case "echo":
                            if (prompt.length() == command.length()) {
                                System.out.println();
                            } else {
                                System.out.println(arguments);
                            }
                            break label;
                        case "type":
                            Path foundPath = findInPath(arguments, dirs);
                            if (builtinCommands.contains(arguments)) {
                                System.out.println(arguments + " is a shell builtin");
                            } else if (foundPath != null) {
                                System.out.println(arguments + " is " + foundPath);
                            } else {
                                System.out.println(arguments + ": not found");
                            }
                            break label;
                    }
                }
            }
            if (!commandFoundInBuiltin) {
                String[] promptArgs = prompt.split(" ");
                String command = promptArgs[0];
                Path externalCommandFound = findInPath(command,dirs);
                if (externalCommandFound != null){
                    executeCommand(promptArgs);
                }
                else{
                    System.out.println(prompt + ": command not found");
                }
            }

        }

        userInput.close();
    }

    public static Path findInPath(String commandName, String[] directories) throws IOException{
        for (String directory : directories) {
            Path path = Path.of(directory,commandName);
            if (Files.exists(path) && Files.isExecutable(path)) {
                return path;
            }
        }
        return null;
    }

    public static void executeCommand(String[] command) throws IOException {
        List<String> commandList = Arrays.asList(command);
        ProcessBuilder processBuilder = new ProcessBuilder(commandList);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine())  != null){
            System.out.println(line);
        }
    }
}