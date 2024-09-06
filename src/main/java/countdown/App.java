package countdown;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class App 
{

    /*
     * This is a program for the math part of the UK tv show game Countdown.
     * See: https://en.wikipedia.org/wiki/Countdown_(game_show)
     * 
     * It lists, on the terminal, the possible solutions to the target number that can be reached using a finite set of integers.
     * Both the target and the numbers usable in the equations can be set within the code.
     * This setting is possible in two ways: it can be random (general setup) or we can add spcific numbers (specific setup). Use commenting to toggle.
     * At the end, the program also provides the number of recursions.
     * 
     */


    private static Integer target;
    private static Integer recursionCnt;
    private static Integer equationCnt;
    private static Integer initSize;
    private static Boolean noSolution;


    public static void main( String[] args )
    {   
        //GENERAL SETUP
        int thisMany_number = 5;
        int maxTarget = 500;

        Random rnd = new Random();
        target = rnd.nextInt(1, maxTarget + 1);
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < thisMany_number; i++) {
            numbers.add(rnd.nextInt(1, 11));
        }


        // SPECIFIC SETUP
        // List<Integer> numbers = new ArrayList<>();
        // numbers.add(8);
        // numbers.add(10);
        // numbers.add(2);
        // numbers.add(1);
        // numbers.add(9);
        // target = 423;




        //METHOD CALL
        recursionCnt = 0;
        equationCnt = 0;
        initSize = numbers.size();
        noSolution = true;

        System.out.print("\nNumbers: ");
        for (int i = 0; i < numbers.size(); i++) {
            System.out.print(numbers.get(i) + " ");
        }
        System.out.println("\nTarget:  " + target + "\n");

        letsPlay(numbers, 0, "");
        System.out.println("\nNumber of recursions: " + recursionCnt + "\n");
        int variations = 1;
        for (int i = numbers.size(); i > 1; i--) {
            variations *= i;
            variations *= 4;
        }
        System.out.println("Number of total variations: " + variations);
        System.out.println("This number reflects the possible variations in which numbers and operations can follow each other, not considering divisibility," +
                            "\nand is calculated using the following equation: n! * 4^(n-1), where n = number of numbers, and 4 is the number of possible operations.\n");
    }

    private static void letsPlay(List<Integer> numbers, int partialResult, String equation){
        recursionCnt++;
        if (partialResult == target) {
            addParentheses_andPrint(equation);
            noSolution = false;
            return;
        }
        if (numbers.size()==0) {
            return;
        }

        if (numbers.size() == initSize) {
            for (int i = 0; i < numbers.size(); i++) {
                letsPlay(
                    Stream.concat(
                        numbers.subList(0, i).stream(), 
                        numbers.subList(i+1, numbers.size()).stream())
                        .toList(), 
                    numbers.get(i), 
                    String.valueOf(numbers.get(i)));
            }
            if (noSolution) {
                System.out.println("There is no possible solution.");
            }
        } else{
            for (int i = 0; i < numbers.size(); i++) {      //Second number
                for (int k = 0; k < 4; k++) {                       //Operation selector
                    switch (k) {
                        case 0:{        //ADD
                            letsPlay(
                                Stream.concat(
                                    numbers.subList(0, i).stream(), 
                                    numbers.subList(i+1, numbers.size()).stream())
                                    .toList(), 
                                partialResult + numbers.get(i), 
                                equation + " + " + numbers.get(i));
                            break;
                        }
                        case 1:{        //SUBSTRACT
                            letsPlay(
                                Stream.concat(
                                    numbers.subList(0, i).stream(), 
                                    numbers.subList(i+1, numbers.size()).stream())
                                    .toList(), 
                                partialResult - numbers.get(i), 
                                equation + " - " + numbers.get(i));
                            break;
                        }
                        case 2:{        //MULTIPLY
                            letsPlay(
                                Stream.concat(
                                    numbers.subList(0, i).stream(), 
                                    numbers.subList(i+1, numbers.size()).stream())
                                    .toList(), 
                                partialResult * numbers.get(i),
                                equation + " * " + numbers.get(i));
                            break;
                        }
                        case 3:{        //DIVIDE (only IF divisible)
                            if (partialResult % numbers.get(i) == 0) {
                                letsPlay(
                                    Stream.concat(
                                        numbers.subList(0, i).stream(), 
                                        numbers.subList(i+1, numbers.size()).stream())
                                        .toList(), 
                                    partialResult / numbers.get(i), 
                                    equation + " / " + numbers.get(i));
                            }
                            break;
                        }
                    }           
                }
            }
        }
    }

    private static void addParentheses_andPrint(String equation){
        equationCnt++;
        boolean has_addOrSubstract = false;
        String printable = equation;
        int offset = 0;
        for (int i = 0; i < equation.length(); i++) {
            if (equation.charAt(i) == '+' || equation.charAt(i) == '-') {
                has_addOrSubstract = true;
            }
            if (equation.charAt(i) == '*' || equation.charAt(i) == '/') {
                if (has_addOrSubstract) {
                    printable = "(" + printable.substring(0, i-1+offset) + ")" + printable.substring(i-1+offset, printable.length());
                    offset += 2;
                    has_addOrSubstract = false;
                }
            }
        }
        System.out.printf("%4d.\t%s\n", equationCnt, printable);
    }
}
