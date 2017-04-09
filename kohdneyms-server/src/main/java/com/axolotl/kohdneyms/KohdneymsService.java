package com.axolotl.kohdneyms;

import java.util.LinkedHashSet;

import com.axolotl.kohdneyms.Kohdneyms.Board;
import com.axolotl.kohdneyms.Kohdneyms.Hint;
import com.axolotl.kohdneyms.Kohdneyms.HintFromServer;
import com.axolotl.kohdneyms.Kohdneyms.Piece;
import com.axolotl.kohdneyms.Kohdneyms.PlayerSelection;
import com.axolotl.kohdneyms.Kohdneyms.State;
import com.axolotl.kohdneyms.Kohdneyms.Type;
import com.axolotl.kohdneyms.Kohdneyms.WordSelection;
import com.axolotl.kohdneyms.Kohdneyms.WordSelectionFromServer;

import io.grpc.stub.StreamObserver;

public class KohdneymsService extends GameServiceGrpc.GameServiceImplBase {
	BoardGenerator generator = new BoardGenerator();
	private static LinkedHashSet<StreamObserver<HintFromServer>> hintObservers = new LinkedHashSet();
	private static LinkedHashSet<StreamObserver<WordSelectionFromServer>> wordSelectionObservers = new LinkedHashSet();
	private Type current;
	private int remainingGuesses;
	
	Board board;
	@Override
	public void initialise(PlayerSelection request, StreamObserver<Board> responseObserver) {
		if(board == null){
			board = generator.generate();
			current = board.getStarter();
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
				Kohdneyms.HintFromServer hintFromServer;
				if(!hintFromSpymaster.getTeam().equals(current)){
					hintFromServer = Kohdneyms.HintFromServer.newBuilder()
							.setState(State.FAILURE)
							.build();
				}
				if(isHintLegal(hintFromSpymaster.getWord())) {
					hintFromServer = Kohdneyms.HintFromServer.newBuilder()
							.setHint(hintFromSpymaster)
							.setState(State.SUCCESS)
							.build();
					remainingGuesses = hintFromSpymaster.getNumber();
					current = hintFromSpymaster.getTeam();
				} else {
					hintFromServer = Kohdneyms.HintFromServer.newBuilder()
								.setHint(hintFromSpymaster)
								.setState(State.FAILURE)
								.build();
				}
				
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
				WordSelectionFromServer wordSelectionFromServer;
				Type wordType = getTypeForWord(wordSelection.getWord());
				if(wordSelection.getTeam().equals(wordType)) {
					wordSelectionFromServer = WordSelectionFromServer.newBuilder()
							.setSelection(wordSelection)
							.setState(State.SUCCESS)
							.build();
				} else if (wordType.equals(Type.ASSASSIN)) {
					wordSelectionFromServer = WordSelectionFromServer.newBuilder()
							.setSelection(wordSelection)
							.setState(State.END)
							.build();
				} else {
					wordSelectionFromServer = WordSelectionFromServer.newBuilder()
							.setSelection(wordSelection)
							.setState(State.FAILURE)
							.build();
				}
				
				// TODO Add game logic to verify if word is valid or not, setting the state accordingly
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
	
	private boolean isHintLegal(String hint) {
		for(Piece piece : board.getBoardList()) {
			if(piece.getWord().equalsIgnoreCase(hint)){
				return false;
			}
		}
		return true;
	}
	
	private Type getTypeForWord(String word) {
		for(Piece piece : board.getBoardList()) {
			if(piece.getWord().equalsIgnoreCase(word)){
				return piece.getType();
			}
		}
		return null;
	}
}
