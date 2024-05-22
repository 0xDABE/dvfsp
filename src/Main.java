import java.util.Scanner;

public class Main {

    public static String user = "user";
    public static Scanner scanner = new Scanner(System.in);
    public static VFS vfs = new VFS();


    public static void main(String[] args) {
        initTerm();
    }

    public static void initTerm(){
        while (true) {
            ColoredMessage.red(user + "@");
            ColoredMessage.blue(vfs.getCurrentAbsolutePath());
            ColoredMessage.red(" > ");
            String exec =  scanner.nextLine().trim();
            String[] commands = Parce.parceArguments(exec);

            switch (commands[0]){
                case "" -> {}
                case "ls" -> vfs.ls();
                case "touch" -> vfs.touch(exec);
                case "md" -> vfs.md(exec);
                case "cd" -> vfs.cd(exec);
                /**case "rm" -> {
                }**/

                /**case "mv" -> {
                }**/

                case "pwd" -> ColoredMessage.blueLn(vfs.getCurrentAbsolutePath());
                case "exit" -> {return;}
                default -> ColoredMessage.redLn("Error: command " + commands[0] + " not found");
            }
        }
    }
}
