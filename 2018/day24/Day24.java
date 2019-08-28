import java.util.*;
import java.io.*;

public class Day24 extends AdventOfCode{

	private static enum Type{
		slashing,
		radiation,
		fire,
		cold,
		bludgeoning,
	}

	private static enum Atribute{
		immune,
		weak
	}

	private static class Group{

		int numberOfUnits;
		int health;
		int attack;
		Type typeOfAttack;
		ArrayList<Type> bonuses;
		ArrayList<Atribute> atributes;
		int incentive;
		Group target = null;
		boolean alreadyChosen = false;

		public Group(int numberOfUnits, int health, int attack,
								Type typeOfAttack, ArrayList<Type> bonuses,
								ArrayList<Atribute> atributes, int incentive){
			this.numberOfUnits = numberOfUnits;
			this.health = health;
			this.attack = attack;
			this.typeOfAttack = typeOfAttack;
			this.bonuses = bonuses;
			this.atributes = atributes;
			this.incentive = incentive;
		}
	}

	private static class ImmuneSystem extends Group{
		public ImmuneSystem(int numberOfUnits, int health, int attack,
								Type typeOfAttack, ArrayList<Type> bonuses,
								ArrayList<Atribute> atributes, int incentive){
			super(numberOfUnits, health, attack, typeOfAttack, bonuses, atributes, incentive);
		}
	}
	private static class Infection extends Group{
		public Infection(int numberOfUnits, int health, int attack,
								Type typeOfAttack, ArrayList<Type> bonuses,
								ArrayList<Atribute> atributes, int incentive){
			super(numberOfUnits, health, attack, typeOfAttack, bonuses, atributes, incentive);
		}
	}

	@Override
	protected Object partOne(){
		
		ArrayList<Group> groups = backupData();

		while(true){
			while(fight(groups)){
				for(Group g : groups){
					g.alreadyChosen = false;
					g.target = null;
				}
				sort(groups);
				
				//target phase
				for(Group g : groups){

					Group target = findTarget(g, groups);
					if(target != null){
						target.alreadyChosen = true;
					}

					g.target = target;
				}

				sortByI(groups);

				//attack phase
				for(Group g : groups){

					if(g.target != null && g.numberOfUnits > 0){

						int dealtDamage = calculateDamage(g, g.target);

						int n =(int)((double)dealtDamage / (int)g.target.health);
						if(n > g.target.numberOfUnits){
							n = g.target.numberOfUnits;
						}
						g.target.numberOfUnits -= n;
					}
				}

				//clear flags
				for(int i = 0; i < groups.size(); i++){
					Group g = groups.get(i);
					if(g.numberOfUnits <= 0){
						groups.remove(g);
						i=-1;;
					}
				}
			}
			int sum = 0;

			for(Group g : groups){
				sum += g.numberOfUnits;
			}

			return sum;
		}
	
	}

	private boolean fight(ArrayList<Group> groups){

		boolean im = false;
		boolean in = false;

		for(Group g : groups){
			if(g instanceof ImmuneSystem){
				im = true;
			}
			if(g instanceof Infection){
				in = true;
			}
		}
		return im & in;
	}

	private void sortByI(ArrayList<Group> groups){
		for(int i = 0; i < groups.size() - 1; i++){

			int iNaj = i;
			Group max = groups.get(i);
			int iMax = max.incentive;

			for(int j = i+1; j < groups.size(); j++){
				Group iter = groups.get(j);
				int jI = iter.incentive;
				if(jI > iMax){
					iMax = jI;
					max = iter;
					iNaj = j;
				}
			}

			groups.set(iNaj, groups.get(i));
			groups.set(i, max);
		}
	}

	private Group findTarget(Group attacker, ArrayList<Group> groups){

		Group target = null;
		int calculatedDamage = Integer.MIN_VALUE;
		for(int i = 0; i < groups.size(); i++){
			Group g = groups.get(i);
			if(g.alreadyChosen){
				continue;
			}
			if((attacker instanceof ImmuneSystem && g instanceof Infection) || (attacker instanceof Infection && g instanceof ImmuneSystem)){
				if(target == null && calculateDamage(attacker, g) > 0){
					target = g;
					calculatedDamage = calculateDamage(attacker,g);
				}else if(target != null){
					int tempCalculatedDamage = calculateDamage(attacker,g);

					if(tempCalculatedDamage > calculatedDamage){
						calculatedDamage = tempCalculatedDamage;
						target = g;
					}else if(tempCalculatedDamage == calculatedDamage &&
						g.numberOfUnits*g.attack > target.numberOfUnits*target.attack){
						calculatedDamage = tempCalculatedDamage;
						target = g;
					}else if(tempCalculatedDamage == calculatedDamage &&
						(int)g.numberOfUnits*g.attack == (int)target.numberOfUnits*target.attack
						&& g.incentive > target.incentive){
							calculatedDamage = tempCalculatedDamage;
							target = g;
					}
				}
			}
		}
		return target;
	}

	private int calculateDamage(Group attacker, Group target){
		Type attack = attacker.typeOfAttack;
		int dealtDamage = attacker.numberOfUnits*attacker.attack;
		for(int i = 0; i < target.bonuses.size(); i++){
			if(target.bonuses.get(i) == attack){
				if(target.atributes.get(i) == Atribute.immune){
					return 0;
				}
				if(target.atributes.get(i) == Atribute.weak){
					return 2*dealtDamage;
				}
			}
		}
		return dealtDamage;
	}

	private void print(ArrayList<Group> groups){
		for(Group g : groups){
			System.out.println(g.health + " " + g.numberOfUnits  + " " + g.incentive);
		}
		System.out.println();
	}

	private void sort(ArrayList<Group> groups){
		for(int i = 0; i < groups.size(); i++){

			int iNaj = i;
			Group max = groups.get(i);
			int epMax = max.numberOfUnits * max.attack;

			for(int j = i+1; j < groups.size(); j++){
				Group iter = groups.get(j);
				int ep = iter.numberOfUnits * iter.attack;

				if(ep > epMax){
					epMax = ep;
					max = iter;
					iNaj = j;
				}
				else if(ep == epMax && iter.incentive > max.incentive){
					epMax = ep;
					max = iter;
					iNaj = j;
				}
			}

			groups.set(iNaj, groups.get(i));
			groups.set(i, max);
		}
	}

	private ArrayList<Group> backupData(){
		ArrayList<Group> backupGroup = new ArrayList<>();

		for(Group g : groups){
			if(g instanceof ImmuneSystem){
				ArrayList<Atribute> ats = new ArrayList<>();
				ArrayList<Type> bs = new ArrayList<>();
				for(Atribute a : g.atributes){
					ats.add(a);
				}
				for(Type b : g.bonuses){
					bs.add(b);
				}

				backupGroup.add(new ImmuneSystem(g.numberOfUnits,
				g.health, g.attack, g.typeOfAttack, bs, ats, g.incentive));
			}else{

					ArrayList<Atribute> ats = new ArrayList<>();
					ArrayList<Type> bs = new ArrayList<>();
					for(Atribute a : g.atributes){
						ats.add(a);
					}
					for(Type b : g.bonuses){
						bs.add(b);
					}

					backupGroup.add(new Infection(g.numberOfUnits,
					g.health, g.attack, g.typeOfAttack, bs, ats, g.incentive));

			}
		}
		return backupGroup;
	}

	@Override
	protected Object partTwo(){

		int stevec = 1;

		while(true){

			ArrayList<Group> groups = backupData();

			for(Group g : groups){
				if(g instanceof ImmuneSystem){
					g.attack+=stevec;
				}
			}
			stevec++;

			while(fight(groups)){
				for(Group g : groups){
					g.alreadyChosen = false;
					g.target = null;
				}
				sort(groups);

				//target phase
				for(Group g : groups){

					Group target = findTarget(g, groups);
					if(target != null){
						target.alreadyChosen = true;
					}

					g.target = target;
				}

				sortByI(groups);

				//attack phase
				boolean wasAttack = false;
				for(Group g : groups){

					if(g.target != null && g.numberOfUnits > 0){
						wasAttack = true;
						int dealtDamage = calculateDamage(g, g.target);

						int n =(int)((double)dealtDamage / (int)g.target.health);
						//System.out.println((int)dealtDamage / (int)g.target.health + " " + n);
						if(n > g.target.numberOfUnits){
							n = g.target.numberOfUnits;
						}
						g.target.numberOfUnits -= n;
					}
				}

				//clear flags
				if(!wasAttack){
					break;
				}

				for(int i = 0; i < groups.size(); i++){
					Group g = groups.get(i);
					if(g.numberOfUnits <= 0){
						groups.remove(g);
						i=-1;;
					}
				}

			}
			int sum = 0;

			for(Group g : groups){
				if(g instanceof ImmuneSystem){
					sum += g.numberOfUnits;
				}else{
					sum = 0;
					break;
				}
			}
			if(sum != 0){
				return sum;
			}
			
		}
	}

	ArrayList<ImmuneSystem> immuneSystems;
	ArrayList<Infection> infections;

	ArrayList<Group> groups;

	@Override
	protected void parseInput(String inputFile){

		groups = new ArrayList<>();
		int input = 10;
		try{

			Scanner sc = new Scanner(new File(inputFile));

			sc.nextLine();
			for(int j = 0; j < input; j++){
				String[] line = sc.nextLine().split(" ");
				int n = Integer.parseInt(line[0]);
				int h = Integer.parseInt(line[1]);
				int i = 2;

				ArrayList<Type> b = new ArrayList<>();
				ArrayList<Atribute> at = new ArrayList<>();

				if(line[2].equals("(")){
					i++;
					for( ; !line[i].equals(")"); i++){
						if(i % 2 == 1){
							at.add(Atribute.valueOf(line[i]));
						}else{
							b.add(Type.valueOf(line[i]));
						}
					}
				}else{
					i--;
				}
				int a = Integer.parseInt(line[i+1]);
				Type type = Type.valueOf(line[i+2]);
				int in = Integer.parseInt(line[i+3]);

				groups.add(new ImmuneSystem(n, h, a, type, b, at, in));
			}

			sc.nextLine();
			sc.nextLine();

			for(int j = 0; j < input; j++){
				String[] line = sc.nextLine().split(" ");
				int n = Integer.parseInt(line[0]);
				int h = Integer.parseInt(line[1]);
				int i = 2;

				ArrayList<Type> b = new ArrayList<>();
				ArrayList<Atribute> at = new ArrayList<>();

				if(line[2].equals("(")){
					i++;
					for( ; !line[i].equals(")"); i++){
						if(i % 2 == 1){
							at.add(Atribute.valueOf(line[i]));
						}else{
							b.add(Type.valueOf(line[i]));
						}
					}
				}else{
					i--;
				}
				int a = Integer.parseInt(line[i+1]);
				Type type = Type.valueOf(line[i+2]);
				int in = Integer.parseInt(line[i+3]);

				groups.add(new Infection(n, h, a, type, b, at, in));
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
