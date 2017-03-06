package com.axolotl.kohdneyms;

import java.util.LinkedHashSet;

import com.axolotl.kohdneyms.Kohdneyms.Board;
import com.axolotl.kohdneyms.Kohdneyms.Hint;
import com.axolotl.kohdneyms.Kohdneyms.HintFromServer;
import com.axolotl.kohdneyms.Kohdneyms.PlayerSelection;
import com.axolotl.kohdneyms.Kohdneyms.State;
import com.axolotl.kohdneyms.Kohdneyms.WordSelection;
import com.axolotl.kohdneyms.Kohdneyms.WordSelectionFromServer;

import io.grpc.stub.StreamObserver;

public class KohdneymsService extends GameServiceGrpc.GameServiceImplBase {
	BoardGenerator generator = new BoardGenerator();
	private static LinkedHashSet<StreamObserver<HintFromServer>> hintObservers = new LinkedHashSet();
	private static LinkedHashSet<StreamObserver<WordSelectionFromServer>> wordSelectionObservers = new LinkedHashSet();

	Board board;
	@Override
	public void initialise(PlayerSelection request, StreamObserver<Board> responseObserver) {
		if(board == null){
			board = generator.generate();
		}
		responseObserver.onNext(board);
		responseObserver.onCompleted();
	}
	
	@Override
	public StreamObserver<Hint> giveHint(final StreamObserver<HintFromServer> responseObservers) {
		hintObservers.add(responseObservers);
		
		return new StreamObserver<Kohdneyms.Hint>() {

			public void onCompleted() {
				hintObservers.remove(responseObservers);
			}

			public void onError(Throwable arg0) {
				
			}

			public void onNext(Hint hintFromSpymaster) {
				Kohdneyms.HintFromServer hintFromServer = Kohdneyms.HintFromServer.newBuilder()
						.setHint(hintFromSpymaster)
						.setState(State.SUCCESS)
						.build();
				for(StreamObserver<HintFromServer> observer : hintObservers) {
					observer.onNext(hintFromServer);
				}
			}
		};
	}

	@Override
	public StreamObserver<WordSelection> selectWord(final StreamObserver<WordSelectionFromServer> responseObserver) {
		wordSelectionObservers.add(responseObserver);
		
		return new StreamObserver<Kohdneyms.WordSelection>() {
			
			public void onNext(WordSelection wordSelection) {
				// TODO Add game logic to verify if word is valid or not, setting the state accordingly
				WordSelectionFromServer wordSelectionFromServer = WordSelectionFromServer.newBuilder()
						.setSelection(wordSelection)
						.setState(State.SUCCESS)
						.build();
				for(StreamObserver<WordSelectionFromServer> observer : wordSelectionObservers) {
					observer.onNext(wordSelectionFromServer);
				}
			}
			
			public void onError(Throwable arg0) {
				// TODO Auto-generated method stub
			}
			
			public void onCompleted() {
				wordSelectionObservers.remove(responseObserver);
			}
		};
	}
}
