import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner userInput = new Scanner(System.in);
        

        while (true) {
            System.out.print("$ ");
            String command = userInput.nextLine();
            if (command.equalsIgnoreCase("q")) {
                break;
            }
            System.out.println(command + ": command not found");
        }
        userInput.close();
    }
}
