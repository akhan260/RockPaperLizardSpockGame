import static org.junit.jupiter.api.Assertions.*;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServerTest {

	RPSLSServer serverTest;
	Consumer<Serializable> callback;
	Socket socket;
	ClientThread thread;
	String p1move, p2move;
	GameInfo info;
	Server check;

	@BeforeEach
	void init() {
		serverTest = new RPSLSServer();
		thread = new ClientThread(socket, 8);
		p1move = "rock";
		p2move = "scissors";
		info = new GameInfo();
		check = new Server(callback, 5555);

//		server.
	}

	//test the server name
	@Test
	void testClass() {

		assertEquals("RPSLSServer", serverTest.getClass().getName(), "Wrong..error");
	}

	//tests if the callback method is null
	@Test
	void callBackTest()
	{
		assertNull(callback);
	}


	@Test
	void testThreadSocket() {
		//test to see if the thread is null
		assertNull(this.thread.connection);
	}

	//tests to see threads amount
	@Test
	void testClientThreads() {
		assertEquals(8, this.thread.count, "wrong amount of threads");
	}

	//tests the constructor of the GameInfo
	@Test
	void testServerConnection() {
		assertEquals(false, info.have2Players, "wrong");
	}


}
