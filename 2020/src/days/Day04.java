package days;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;

import utilities.Parser;
import utilities.exceptions.ParsingException;

public class Day04 {
    
    private static class Passport{

        String byr;
        String iyr;
        String eyr;
        String hgt;
        String hcl;
        String ecl;
        String pid;
        String cid;

        int numberOfData;

        public void addData(String field, String value) throws ParsingException{
            try{
                Field attribute = this.getClass().getDeclaredField(field);
                attribute.set(this, value);
            }catch(NoSuchFieldException | IllegalAccessException | IllegalArgumentException exception){
                System.out.println(field);
                throw new ParsingException(String.format("Field with name %s for value %s not found!", field, value));
            }
            this.numberOfData++;
        }

        public boolean checkIfHasAllFields(){
            if(numberOfData == 8){
                return true;
            }
            if(numberOfData == 7 && cid == null){
                return true;
            }
            return false;
        }

        public boolean checkBirth(){
            return Integer.parseInt(this.byr) >= 1920 && Integer.parseInt(this.byr) <= 2002;
        }

        public boolean checkIssue(){
            return Integer.parseInt(this.iyr) >= 2010 && Integer.parseInt(this.iyr) <= 2020;
        }

        public boolean checkExpiration(){
            return Integer.parseInt(this.eyr) >= 2020 && Integer.parseInt(this.eyr) <= 2030;
        }

        public boolean checkHeight(){
            if(this.hgt.contains("in")){
                int value = Integer.parseInt(this.hgt.substring(0, this.hgt.length()-2));
                if(value >= 59 && value <= 76) return true;
                return false;
            }else if(this.hgt.contains("cm")){
                int value = Integer.parseInt(this.hgt.substring(0, this.hgt.length()-2));
                if(value >= 150 && value <= 193) return true;
                return false;
            }
            return false;
        }

        public boolean checkHairColor(){
            if(this.hcl.charAt(0) != '#' || this.hcl.length() != 7){
                return false;
            }
            try{
                int value = Integer.parseInt(this.hcl.substring(1), 16);
                return true;
            }catch(NumberFormatException e){
                return false;
            }
        }

        public boolean checkEyeColor(){
            
            String[] colors = {"amb", "blu", "brn", "gry", "grn", "hzl", "oth"};

            for(String color : colors){
                if(this.ecl.equals(color)){
                    return true;
                }
            }

            return false;
        }

        public boolean checkPassportID(){
            if(this.pid.length() == 9){
                try{
                    int value = Integer.parseInt(this.pid);
                    return true;
                }catch(NumberFormatException e){
                    return false;
                }
            }   
            return false;
        }

    }

    private static int partOne(List<Passport> passports){
        int result = 0;
        for(Passport passport : passports){
            if(passport.checkIfHasAllFields()){
                result++;
            }
        }   
        return result;
    }

    private static int partTwo(List<Passport> passports){

        int numberOfValid = 0;

        for(Passport passport : passports){
            if(passport.checkIfHasAllFields() && passport.checkBirth() && passport.checkIssue() && passport.checkExpiration() && passport.checkHeight() && passport.checkHairColor() && passport.checkEyeColor() && passport.checkPassportID()){
                numberOfValid++;
            }
        }
        return numberOfValid;
    }

    public static void main(String[] args) throws ParsingException{

        Scanner sc = new Scanner(System.in);
        
        List<Passport> passports = new ArrayList<>();

        while(sc.hasNextLine()){
            Passport passport = new Passport();
            String line = sc.nextLine();
            while(!line.equals("")){
                String[] dataFields = line.split(" ");
                for(String dataField : dataFields){
                    String[] data = dataField.split(":");
                    passport.addData(data[0], data[1]);
                }
                if(sc.hasNextLine()) {
                    line = sc.nextLine();
                }else{
                    break;
                }
            }
            passports.add(passport);
            
        }
        System.out.println(partOne(passports));
        System.out.println(partTwo(passports));

        sc.close();
    }
}
