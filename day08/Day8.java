import java.util.*;
import java.io.*;

public class Day8 extends AdventOfCode{

	private static class Node{

		int id;
		int numberOfChildren;
		int numberOfMetaData;

		ArrayList<Integer> metaData = new ArrayList<>();
		ArrayList<Node> children = new ArrayList<>();

		public Node(int id, int numberOfChildren, int numberOfMetaData){
			this.id = id;
			this.numberOfChildren = numberOfChildren;
			this.numberOfMetaData = numberOfMetaData;
		}

	}


	@Override
	protected Object partOne(){
		
		int sum = 0;

		for(Node n : nodes){
			for(Integer x : n.metaData){
				sum += x;
			}
		}

		return sum;
	}

	private Integer getValueOfNode(Node n){
		
		int value = 0;

		if(n.numberOfChildren == 0){
			for(Integer x : n.metaData){
				value += x;
			}

			return value;
		}

		for(int i = 0; i < n.numberOfMetaData; i++){

			if(n.metaData.get(i) - 1 < n.children.size()){
				value += getValueOfNode(n.children.get(n.metaData.get(i) - 1));
			}
		
		}

		return value;	

	}

	@Override
	protected Object partTwo(){

		Node root = nodes.get(nodes.size() - 1);

		int value = 0;
		for(int i = 0; i < root.numberOfMetaData; i++){
			if(root.metaData.get(i) - 1 < root.children.size()){
				value += getValueOfNode(root.children.get(root.metaData.get(i) - 1));
			}
		}

		return value;
	}

	private ArrayList<Node> nodes;


	protected Node parseInput(Scanner sc, int idCounter, int indexOfChild){

		if(!sc.hasNextInt()){
			return null;
		}

		if(indexOfChild == 0 && idCounter != 0){
			return null;
		}

		int id = idCounter;

		int numberOfChildren = sc.nextInt();
		int numberOfMetaData = sc.nextInt();

		Node n = new Node(id, numberOfChildren, numberOfMetaData);

		if(numberOfChildren == 0){ 

			for(int i = 0; i < numberOfMetaData; i++){
				n.metaData.add(sc.nextInt());
			}

			nodes.add(n);
			parseInput(sc, idCounter+1, 0);
		}else{
			
			for(int i  = 1; i <= numberOfChildren; i++){
				n.children.add(parseInput(sc, idCounter+i, indexOfChild-i));	
			}

			for(int i = 0; i < numberOfMetaData; i++){
				n.metaData.add(sc.nextInt());
			}	
			nodes.add(n);
		}
		return n;
	}

	@Override
	protected void parseInput(String inputFile){
		try{
			
			Scanner sc = new Scanner(new File(inputFile));
			
			int idCounter = 0;
			nodes = new ArrayList<>();

			parseInput(sc, 0, 0);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}