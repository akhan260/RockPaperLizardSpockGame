import static org.junit.jupiter.api.Assertions.*;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServerTest {

//	RPSLSClient serverTest;
	Consumer<Serializable> callback;
//	Socket socket;
//	ClientThread thread;
	String p1move, p2move;
	GameInfo info;
//	Server check;
	Client check;

	@BeforeEach
	void init() {
//		serverTest = new RPSLSClient();
//		thread = new ClientThread(socket, 8);
		p1move = "rock";
		p2move = "scissors";
		info = new GameInfo();
//		check = new Server(callback, 5555);
//		check = new Clidata -> {
//			Platform.runLater(()->{"hi");
//
//		};
//		check = new Client();

//		server.
	}

	//checking callback null case
	@Test
	void callBackTest()
	{
		assertNull(callback);
	}

	//checking server connection
	@Test
	void testServerConnection() {
		assertEquals(false, info.have2Players, "wrong");
	}


}
