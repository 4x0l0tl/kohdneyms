package com.axolotl.kohdneyms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.axolotl.kohdneyms.Kohdneyms.Board;
import com.axolotl.kohdneyms.Kohdneyms.Piece;
import com.axolotl.kohdneyms.Kohdneyms.Type;

public class BoardGenerator {
	private String[] wordlist = "time,year,people,way,day,man,thing,woman,life,child,world,school,state,family,student,group,country,problem,hand,part,place,case,week,company,system,program,question,work,government,number,night,point,home,water,room,mother,area,money,story,fact,month,lot,right,study,book,eye,job,word,business,issue,side,kind,head,house,service,friend,father,power,hour,game,line,end,member,law,car,city,community,name,president,team,minute,idea,kid,body,information,back,parent,face,others,level,office,door,health,person,art,war,history,party,result,change,morning,reason,research,girl,guy,moment,air,teacher,force,education".split(",");
	private List<String> selectedWords = new ArrayList<String>();
	private Random random = new Random();
	private Map<Type,Integer> pieceTypeCounts = new HashMap<Kohdneyms.Type, Integer>();
	private Map<Type,Integer> pieceTypeMaximums = new HashMap<Kohdneyms.Type, Integer>();
	
	public BoardGenerator(){
		pieceTypeCounts.put(Type.BLUE, 0);
		pieceTypeCounts.put(Type.RED, 0);
		pieceTypeCounts.put(Type.CIVILIAN, 0);
		pieceTypeCounts.put(Type.ASSASSIN, 0);
		pieceTypeMaximums.put(Type.BLUE, 8);
		pieceTypeMaximums.put(Type.RED, 8);
		pieceTypeMaximums.put(Type.CIVILIAN, 8);
		pieceTypeMaximums.put(Type.ASSASSIN, 1);
	}

	public Board generate() {
		List<Piece> pieces = new ArrayList<Kohdneyms.Piece>();
		while(pieces.size() < 25){
			pieces.add(generatePiece());
		}
		Type starter = Type.valueOf(random.nextInt(2));
		return Board.newBuilder().addAllBoard(pieces).setStarter(starter).build();
	}
	
	private Piece generatePiece() {
		Type type = generateType();
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
	
	private Type generateType() {		
		Type type  = Type.valueOf(random.nextInt(4));
		while(pieceTypeCounts.get(type) ==  pieceTypeMaximums.get(type)) {
			type  = Type.valueOf(random.nextInt(4));
		}
		int count = pieceTypeCounts.get(type);
		count = count+1;
		pieceTypeCounts.put(type, count);
		return type;
	}
}
