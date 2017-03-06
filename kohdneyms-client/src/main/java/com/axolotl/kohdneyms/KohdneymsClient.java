package com.axolotl.kohdneyms;
import com.axolotl.kohdneyms.GameServiceGrpc;
import com.axolotl.kohdneyms.GameServiceGrpc.GameServiceBlockingStub;
import com.axolotl.kohdneyms.GameServiceGrpc.GameServiceStub;
import com.axolotl.kohdneyms.Kohdneyms;
import com.axolotl.kohdneyms.Kohdneyms.Board;
import com.axolotl.kohdneyms.Kohdneyms.Hint;
import com.axolotl.kohdneyms.Kohdneyms.HintFromServer;
import com.axolotl.kohdneyms.Kohdneyms.Piece;
import com.axolotl.kohdneyms.Kohdneyms.PlayerSelection;
import com.axolotl.kohdneyms.Kohdneyms.Role;
import com.axolotl.kohdneyms.Kohdneyms.Team;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class KohdneymsClient {
	public static void main (String [] args) throws InterruptedException {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext(true).build();
		GameServiceStub stub = GameServiceGrpc.newStub(channel);
		GameServiceBlockingStub blockingStub = GameServiceGrpc.newBlockingStub(channel);
		Board board = blockingStub.initialise(PlayerSelection.newBuilder().setRole(Role.SPY).setTeam(Team.RED_TEAM).build());
		for(Piece piece : board.getBoardList()) {
			System.out.println("Type:"+ piece.getType() + " Word:"+ piece.getWord());
		}
		StreamObserver<Hint> hintObserver = stub.giveHint(new StreamObserver<Kohdneyms.HintFromServer>() {
			
			@Override
			public void onNext(HintFromServer arg0) {
				System.out.println(arg0.getHint().getWord());
				System.out.println(arg0.getState());
			}
			
			@Override
			public void onError(Throwable arg0) {
				System.out.println("Disconnected");				
			}
			
			@Override
			public void onCompleted() {
				System.out.println("Disconnected");				
			}
		});
		hintObserver.onNext(Hint.newBuilder().setTeam(Team.BLUE_TEAM).setWord("hint").setNumber("2").build());
		Thread.sleep(10000L);
		hintObserver.onCompleted();
		channel.shutdown();
	}
}
