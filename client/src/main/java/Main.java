import chess.*;

import ui.*;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws IOException, DeploymentException, URISyntaxException {
        System.out.println("♕ A Chess Game Made by Lecio ♕");
        System.out.println();
        System.out.println();

        ReadEvaluateSourceInput client_loop = new ReadEvaluateSourceInput();
        client_loop.run();

    }
}