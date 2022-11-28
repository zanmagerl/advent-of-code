package days;

import java.util.*;

import java.io.*;

import utilities.Array;
import utilities.Parser;

public class Day21{

    private static class Ingridient{
        
        String name;

        public Ingridient(String name){
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Ingridient ingridient = (Ingridient) o;
            return ingridient.name.equals(this.name);
        }

        @Override
        public int hashCode(){
            return this.name.hashCode();
        }

        @Override
        public String toString(){
            return this.name;
        }
        
    }  

    private static class AlergenComparator implements Comparator<Alergen>{
        @Override
        public int compare(Alergen o1, Alergen o2) {
            return o1.name.compareTo(o2.name);
        }
    }

    private static class Alergen{

        String name;

        public Alergen(String name){
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Alergen alergen = (Alergen) o;
            return alergen.name.equals(this.name);
        }

        @Override
        public int hashCode(){
            return this.name.hashCode();
        }

        @Override
        public String toString(){
            return this.name;
        }
    }

    private static int partOne(HashMap<Ingridient, Integer> ingridients, HashMap<Alergen, HashSet<Ingridient>> alergens){

        HashSet<Ingridient> nonAlergenic = new HashSet<>();
        nonAlergenic.addAll(ingridients.keySet());

        for(Alergen alergen : alergens.keySet()){
            nonAlergenic.removeAll(alergens.get(alergen));
        }

        int result = 0;

        for(Ingridient i : nonAlergenic){
            result += ingridients.get(i);
        }

        return result;
    }


    private static String partTwo(HashMap<Alergen, HashSet<Ingridient>> alergens){

        HashMap<Ingridient, Alergen> mapping = new HashMap<>();

        do{

            for(Alergen alergen : alergens.keySet()){
                if(alergens.get(alergen).size() == 1){
                    Ingridient i = alergens.get(alergen).iterator().next();
                    mapping.put(i, alergen);
                    for(Alergen al : alergens.keySet()){
                        alergens.get(al).remove(i);
                    }
                }


            }

        }while(mapping.size() != alergens.keySet().size());
        
        List<Alergen> alergens2 = new ArrayList();
        alergens2.addAll(alergens.keySet());
        alergens2.sort(new AlergenComparator());

        StringBuffer stringBuffer = new StringBuffer();

        for(Alergen al : alergens2){
            for(Ingridient i : mapping.keySet()){
                if(mapping.get(i) == al){
                    stringBuffer.append(i.name);
                    stringBuffer.append(",");
                }
            }
        }
        
        return stringBuffer.toString();
    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        HashMap<Ingridient, Integer> ingridients = new HashMap<>();
        HashMap<Alergen, HashSet<Ingridient>> alergens = new HashMap<>();

        while(sc.hasNextLine()){
            String[] separation = sc.nextLine().split("\\(");
            String[] ingridientList = separation[0].trim().split(" ");
            String[] alergenList = Parser.removeWordsFromString(Parser.removePunctuations(separation[1].trim(), new String[]{")", ","}), new String[]{"contains"}).split(" ");

            HashSet<Ingridient> ingridientsSet = new HashSet<>();
            for(String ing : ingridientList){
                Ingridient in = new Ingridient(ing);
                ingridients.putIfAbsent(in,0);
                ingridients.put(in,ingridients.get(in) + 1);
                ingridientsSet.add(in);
            }

            for(String alergen : alergenList){

                HashSet<Ingridient> copy = new HashSet<>();
                copy.addAll(ingridientsSet);

                Alergen al = new Alergen(alergen);
                alergens.putIfAbsent(al, copy);
                alergens.get(al).retainAll(ingridientsSet);
            }
        }

        System.out.println(partOne(ingridients, alergens));
        System.out.println(partTwo(alergens));

        sc.close();
    }
}
