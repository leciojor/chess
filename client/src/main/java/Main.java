import chess.*;

import ui.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("♕ A Chess Game Made by Lecio: ♕");
        System.out.println();
        System.out.println();

        ReadEvaluateSourceInput client_loop = new ReadEvaluateSourceInput();
        client_loop.run();

    }
}