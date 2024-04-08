import ui.*;

public class MainClient {
    public static void main(String[] args) throws Exception {
        System.out.println("♕ A Chess Game Made by Lecio ♕");
        System.out.println();
        System.out.println();

        ReadEvaluateSourceInput client_loop = new ReadEvaluateSourceInput();
        client_loop.run();

    }
}