package Perfomance;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Posfix
{
    public static int Prec(String ch)
    {
        switch (ch)
        {
            case "+":
            case "-":
                return 1;

            case "*":
            case "/":
                return 2;
        }
        return -1;
    }

    public static StringBuilder expresionResult = new StringBuilder();;
    public static Queue<String> operadores = new LinkedList<>();
    public static Stack<String> ids = new Stack<>();

    public static boolean infixToPostfix(String[] exp)
    {
        expresionResult.setLength(0);
        operadores.clear();
        ids.clear();

        Stack<String> stack = new Stack<>();

        for (String c : exp)
        {
            if (c.equals("("))
                stack.push(c);

            else if (c.equals(")"))
            {
                while (!stack.isEmpty() &&
                        !stack.peek().equals("("))
                    expresionResult.append(stack.pop()).append(" ");

                stack.pop();
            }else if (Prec(c)==-1)
                    expresionResult.append(c).append(" ");
                else
                {
                    while (!stack.isEmpty() && Prec(c)
                            <= Prec(stack.peek())){

                        expresionResult.append(stack.pop()).append(" ");
                    }
                    stack.push(c);
                }

        }

        while (!stack.isEmpty()){
            if(stack.peek().equals("("))
                return false;
            expresionResult.append(stack.pop()).append(" ");
        }
        return true;
    }

    public static void toArraysExpresion(){


        for(String comp : expresionResult.toString().split(" "))
        {
            if(Prec(comp)==-1)
            {
                ids.push(comp);
            }else{
                operadores.offer(comp);
            }
        }

    }

    public static void main(String[] args)
    {
        /*String exp="x2 - ( x2 - y / x )";
        if(infixToPostfix(exp.split(" "))){
            System.out.println(Posfix.expresionResult);
        }*/
    }
}
