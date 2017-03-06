package com.axolotl.kohdneyms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.axolotl.kohdneyms.Kohdneyms.Board;
import com.axolotl.kohdneyms.Kohdneyms.Piece;
import com.axolotl.kohdneyms.Kohdneyms.PieceType;

public class BoardGenerator {
	private String[] wordlist = "time,year,people,way,day,man,thing,woman,life,child,world,school,state,family,student,group,country,problem,hand,part,place,case,week,company,system,program,question,work,government,number,night,point,home,water,room,mother,area,money,story,fact,month,lot,right,study,book,eye,job,word,business,issue,side,kind,head,house,service,friend,father,power,hour,game,line,end,member,law,car,city,community,name,president,team,minute,idea,kid,body,information,back,parent,face,others,level,office,door,health,person,art,war,history,party,result,change,morning,reason,research,girl,guy,moment,air,teacher,force,education".split(",");
	private List<String> selectedWords = new ArrayList<String>();
	private Random random = new Random();
	private Map<PieceType,Integer> pieceTypeCounts = new HashMap<Kohdneyms.PieceType, Integer>();
	private Map<PieceType,Integer> pieceTypeMaximums = new HashMap<Kohdneyms.PieceType, Integer>();
	
	public BoardGenerator(){
		pieceTypeCounts.put(PieceType.BLUE, 0);
		pieceTypeCounts.put(PieceType.RED, 0);
		pieceTypeCounts.put(PieceType.CIVILIAN, 0);
		pieceTypeCounts.put(PieceType.ASSASSIN, 0);
		pieceTypeMaximums.put(PieceType.BLUE, 8);
		pieceTypeMaximums.put(PieceType.RED, 8);
		pieceTypeMaximums.put(PieceType.CIVILIAN, 8);
		pieceTypeMaximums.put(PieceType.ASSASSIN, 1);
	}

	public Board generate() {
		List<Piece> pieces = new ArrayList<Kohdneyms.Piece>();
		while(pieces.size() < 25){
			pieces.add(generatePiece());
		}
		return Board.newBuilder().addAllBoard(pieces).build();
	}
	
	private Piece generatePiece() {
		PieceType type = generateType();
		String word = generateWord();
		selectedWords.add(word);
		return Piece.newBuilder()
				.setWord(word)
				.setType(type)
				.build();
	}

	private String generateWord() {
		String word = wordlist[random.nextInt(wordlist.length-1)];
		while (selectedWords.contains(word)){
			word = wordlist[random.nextInt(wordlist.length-1)];
		}
		return word;
	}
	
	private PieceType generateType() {		
		PieceType type  = PieceType.valueOf(random.nextInt(4));
		while(pieceTypeCounts.get(type) ==  pieceTypeMaximums.get(type)) {
			type  = PieceType.valueOf(random.nextInt(4));
		}
		int count = pieceTypeCounts.get(type);
		count = count+1;
		pieceTypeCounts.put(type, count);
		return type;
	}
}
