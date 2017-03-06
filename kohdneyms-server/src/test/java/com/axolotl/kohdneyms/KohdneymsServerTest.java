package com.axolotl.kohdneyms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.axolotl.kohdneyms.GameServiceGrpc.GameServiceBlockingStub;
import com.axolotl.kohdneyms.GameServiceGrpc.GameServiceStub;
import com.axolotl.kohdneyms.Kohdneyms.Board;
import com.axolotl.kohdneyms.Kohdneyms.Hint;
import com.axolotl.kohdneyms.Kohdneyms.HintFromServer;
import com.axolotl.kohdneyms.Kohdneyms.PlayerSelection;
import com.axolotl.kohdneyms.Kohdneyms.Role;
import com.axolotl.kohdneyms.Kohdneyms.Team;

import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.stub.StreamObserver;

@RunWith(JUnit4.class)
public class KohdneymsServerTest {
	private static final Logger logger = Logger.getLogger(KohdneymsServerTest.class.getName()); 
	InProcessServer<KohdneymsService> server;
	private ManagedChannel channel;
	private GameServiceBlockingStub blockingStub;
	private GameServiceStub asyncStub;

	@Before
	public void beforeEachTest() throws InstantiationException, IllegalAccessException, IOException {
		server = new InProcessServer<KohdneymsService>(KohdneymsService.class);
		server.start();
		channel = InProcessChannelBuilder.forName("test").directExecutor().usePlaintext(true).build();
		blockingStub = GameServiceGrpc.newBlockingStub(channel);
		asyncStub = GameServiceGrpc.newStub(channel);
	}

	@After
	public void afterEachTest() {
		channel.shutdownNow();

		server.stop();
	}
	
	@Test
	public void test(){
		PlayerSelection selection = PlayerSelection.newBuilder().setRole(Role.SPYMASTER).setTeam(Team.BLUE_TEAM).build();
		Board board = blockingStub.initialise(selection);
		assertNotNull(board);
		assertEquals(25,board.getBoardCount());
	}
	
	@Test
	public void testGiveHint() {
		StreamObserver<HintFromServer> responseObserver = new StreamObserver<Kohdneyms.HintFromServer>() {
			
			public void onNext(HintFromServer arg0) {
				assertNotNull(arg0);
				assertEquals("btest",arg0.getHint().getWord());
			}
			
			public void onError(Throwable arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void onCompleted() {
				// TODO Auto-generated method stub
				
			}
		};
		StreamObserver<Hint> requestObserver  = asyncStub.giveHint(responseObserver);
		requestObserver.onNext(Hint.newBuilder().setWord("test").setTeam(Team.BLUE_TEAM).setNumber("2").build());
		requestObserver.onCompleted();
		responseObserver.onCompleted();
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}
}
