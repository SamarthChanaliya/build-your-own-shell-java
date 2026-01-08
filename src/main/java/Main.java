// CodeCrafters
// Build your own shell
// progress - 9/42

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
    public static void main(String[] args) {

        Scanner userInput = new Scanner(System.in);

        List<String> builtinCommands = new ArrayList<>();
        builtinCommands.add("exit");
        builtinCommands.add("echo");
        builtinCommands.add("type");

        String path = System.getenv("PATH");
        String[] dirs = path.split(File.pathSeparator);

        boolean running = true;

        while (running) {
            System.out.print("$ ");
            String prompt = userInput.nextLine();

            boolean commandFoundInBuiltin = false;

            List<String> tokens = parseTokens(prompt);
            List<String> arguments = List.of();
            String command;

            if (!tokens.isEmpty()) {
                command = tokens.getFirst();
            } else continue;


            if (command.length() > 1) {
                arguments = tokens.subList(1, tokens.size());
            }
            String parsedArgs = String.join(" ", arguments);


            label:
            for (String builtinCommand : builtinCommands) {

                if (command.equals(builtinCommand)) {

                    commandFoundInBuiltin = true;

                    switch (builtinCommand) {
                        case "exit":
                            running = false;

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
                    try {
                        executeCommand(tokens);
                    } catch (IOException e) {
                        System.out.println(command + " is not a valid command");
                    }
                } else {
                    System.out.println(command + ": command not found");
                }
            }
        }
        userInput.close();
    }

    public static Path findInPath(String commandName, String[] directories) {
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
        final char DOUBLE_QUOTE = '\"';
        boolean insideSingleQuotes = false;
        boolean insideDoubleQuotes = false;

        StringBuilder charBuffer = new StringBuilder();
        ArrayList<String> tokenList = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            char inputCharacter = input.charAt(i);

            if (inputCharacter == SINGLE_QUOTE && !insideSingleQuotes && !insideDoubleQuotes) {
                insideSingleQuotes = true;
                continue;
            } else if (inputCharacter == SINGLE_QUOTE && !insideDoubleQuotes) {
                insideSingleQuotes = false;
                continue;
            }
            if (inputCharacter == DOUBLE_QUOTE && !insideDoubleQuotes && !insideSingleQuotes) {
                insideDoubleQuotes = true;
                continue;
            } else if (inputCharacter == DOUBLE_QUOTE && !insideSingleQuotes) {
                insideDoubleQuotes = false;
                continue;
            }
            if (inputCharacter != WHITE_SPACE && !insideSingleQuotes) {
                charBuffer.append(inputCharacter);
            } else if (inputCharacter == WHITE_SPACE && !charBuffer.isEmpty() && !insideSingleQuotes && !insideDoubleQuotes) {
                tokenList.add(charBuffer.toString());
                charBuffer.setLength(0);
            } else if (insideSingleQuotes || insideDoubleQuotes) {
                charBuffer.append(inputCharacter);
            }
        }
        if (!charBuffer.isEmpty()) {
            tokenList.add(charBuffer.toString());
        }
        return tokenList;
    }
}