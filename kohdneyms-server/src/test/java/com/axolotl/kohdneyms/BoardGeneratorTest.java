package com.axolotl.kohdneyms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.axolotl.kohdneyms.Kohdneyms.Board;
import com.axolotl.kohdneyms.Kohdneyms.Piece;
import com.axolotl.kohdneyms.Kohdneyms.PieceType;

@RunWith(JUnit4.class)
public class BoardGeneratorTest {

	BoardGenerator generator;
	Board board;
	@Before
	public void setUp() {
		generator = new BoardGenerator();
		board = generator.generate();
	}
	@Test
	public void test() {
		assertNotNull(board);
		assertEquals(25,board.getBoardList().size());
	}
	
	@Test
	public void testThatWordsAreUnique(){
		List<String> words = new ArrayList<String>();
		List<Piece> pieces = board.getBoardList();
		for(Piece piece : pieces) {
			assertTrue(!words.contains(piece.getWord()));
			words.add(piece.getWord());
		}
	}
	
	@Test
	public void testWordDistribution() {
		List<Piece> pieces = board.getBoardList();
		Map<PieceType,Integer> pieceTypeCounts = new HashMap<Kohdneyms.PieceType, Integer>();
        pieceTypeCounts.put(PieceType.BLUE, 0);
        pieceTypeCounts.put(PieceType.RED, 0);
        pieceTypeCounts.put(PieceType.CIVILIAN, 0);
        pieceTypeCounts.put(PieceType.ASSASSIN, 0);
		for(Piece piece : pieces) {
			int count = pieceTypeCounts.get(piece.getType());
			count = count+1;
			pieceTypeCounts.put(piece.getType(),count); 
		}
		assertEquals(Integer.valueOf(1),pieceTypeCounts.get(PieceType.ASSASSIN));
		assertEquals(Integer.valueOf(8),pieceTypeCounts.get(PieceType.BLUE));
		assertEquals(Integer.valueOf(8),pieceTypeCounts.get(PieceType.RED));
		assertEquals(Integer.valueOf(8),pieceTypeCounts.get(PieceType.CIVILIAN));
		
	}
}
