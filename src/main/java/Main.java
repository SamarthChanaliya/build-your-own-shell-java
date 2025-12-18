import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: Uncomment the code below to pass the first stage
        System.out.print("$ ");

        Scanner userInput = new Scanner(System.in);
        String command = userInput.nextLine();

        System.out.println(command + ": command not found");

        userInput.close();

    }
}
