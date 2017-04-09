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
import com.axolotl.kohdneyms.Kohdneyms.Type;

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
	public void testTeamStarter(){
		System.out.println(board.getStarter());
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
		Map<Type,Integer> pieceTypeCounts = new HashMap<Kohdneyms.Type, Integer>();
        pieceTypeCounts.put(Type.BLUE, 0);
        pieceTypeCounts.put(Type.RED, 0);
        pieceTypeCounts.put(Type.CIVILIAN, 0);
        pieceTypeCounts.put(Type.ASSASSIN, 0);
		for(Piece piece : pieces) {
			int count = pieceTypeCounts.get(piece.getType());
			count = count+1;
			pieceTypeCounts.put(piece.getType(),count); 
		}
		assertEquals(Integer.valueOf(1),pieceTypeCounts.get(Type.ASSASSIN));
		assertEquals(Integer.valueOf(8),pieceTypeCounts.get(Type.BLUE));
		assertEquals(Integer.valueOf(8),pieceTypeCounts.get(Type.RED));
		assertEquals(Integer.valueOf(8),pieceTypeCounts.get(Type.CIVILIAN));
		
	}
}
