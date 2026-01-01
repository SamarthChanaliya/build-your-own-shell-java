// CodeCrafters
// Build your own shell
// progress - 8/42

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner userInput = new Scanner(System.in);

        List<String> builtinCommands = new ArrayList<>();
        builtinCommands.add("exit");
        builtinCommands.add("echo");
        builtinCommands.add("type");

        String path = System.getenv("PATH");
        String[] dirs = path.split(File.pathSeparator);

        boolean isRunning = true;

        while (isRunning) {
            System.out.print("$ ");
            String prompt = userInput.nextLine();
//            String arguments = "";

            boolean commandFoundInBuiltin = false;

            List<String> tokens = parseTokens(prompt);

            String command = tokens.getFirst();
            List<String> arguments = List.of();
            if (command.length() > 1){
                arguments = tokens.subList(1, tokens.size());
            }

            String parsedArgs = String.join(" ",arguments);




            label:
            for (String builtinCommand : builtinCommands) {

                if (command.equals(builtinCommand)) {

                    commandFoundInBuiltin = true;

                    switch (builtinCommand) {
                        case "exit":
                            isRunning = false;

                            break label;
                        case "echo":
                            if (arguments.isEmpty()) {
                                System.out.println();
                            } else {
                                System.out.println(parsedArgs);
                            }
                            break label;
                        case "type":
                            Path foundPath = findInPath(parsedArgs, dirs);
                            if (builtinCommands.contains(parsedArgs)) {
                                System.out.println(parsedArgs + " is a shell builtin");
                            } else if (foundPath != null) {
                                System.out.println(parsedArgs + " is " + foundPath);
                            } else {
                                System.out.println(parsedArgs + ": not found");
                            }
                            break label;
                    }
                }
            }
            if (!commandFoundInBuiltin) {
                Path externalCommandFound = findInPath(command, dirs);
                if (externalCommandFound != null) {
                    executeCommand(arguments);
                } else {
                    System.out.println(prompt + ": command not found");
                }
            }
        }
        userInput.close();
    }

    public static Path findInPath(String commandName, String[] directories) throws IOException {
        for (String directory : directories) {
            Path path = Path.of(directory, commandName);
            if (Files.exists(path) && Files.isExecutable(path)) {
                return path;
            }
        }
        return null;
    }

    public static void executeCommand(List<String> command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    private static ArrayList<String> parseTokens(String input) {
        final char WHITE_SPACE = ' ';
        final char SINGLE_QUOTE = '\'';
        boolean insideQuotes = false;

        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<String> tokens = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            char inputCharacter = input.charAt(i);
            if (inputCharacter == SINGLE_QUOTE && !insideQuotes){
                insideQuotes = true;
            } else if (inputCharacter == SINGLE_QUOTE) {
                insideQuotes = false;
            }
            if (inputCharacter != WHITE_SPACE && !insideQuotes && inputCharacter != SINGLE_QUOTE) {
                stringBuilder.append(inputCharacter);
            } else if (inputCharacter == WHITE_SPACE && !stringBuilder.isEmpty() && !insideQuotes ) {
                tokens.add(stringBuilder.toString());
                stringBuilder.setLength(0);
            } else if (inputCharacter != WHITE_SPACE && insideQuotes && inputCharacter != SINGLE_QUOTE) {
                stringBuilder.append(inputCharacter);
            } else if (inputCharacter == WHITE_SPACE && insideQuotes) {
                stringBuilder.append(inputCharacter);
            }
        }
        if (!stringBuilder.isEmpty()) {
            tokens.add(stringBuilder.toString());
        }
        return tokens;
    }
}