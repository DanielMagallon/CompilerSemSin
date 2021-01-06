package Perfomance;

import compiler.Lexico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Semantic
{

    /*
        int[] a = {1,2,3,4};
        boolean contains = IntStream.of(a).anyMatch(x -> x == 4);

        String[] values = {"AB","BC","CD","AE"};
        boolean contains = Arrays.stream(values).anyMatch("s"::equals);
     */

    public static String[] datatypes={"int","char","float"};

    public static String[][] compatibility=
            {
                    {"int",null,"float"},
                    {null,"char",null},
                    {"float",null,"float"},
            };

    private ArrayList<String> componentes;
    private LinkedHashMap<String, ArrayList<String>> dataIdTypes;
    public StringBuilder semanticError;
    private Runnable onError;
    public StringBuilder expresion;

    public Semantic(Runnable error)
    {
        this.onError=error;
        expresion = new StringBuilder();
        semanticError = new StringBuilder();
        componentes = new ArrayList<>();
        dataIdTypes = new LinkedHashMap<>();
    }

    public void reset(){
        componentes.clear();
        dataIdTypes.clear();
        semanticError.setLength(0);
        expresion.setLength(0);
        isAsign=false;
        currentType=null;
    }

    String currentType;
    boolean isAsign;
    String data_type[] = new String[2];


    public int getIndexOf(String type)
    {
        for(int i=0; i<datatypes.length; i++){
            if(type.equals(datatypes[i]))
                return i;
        }

        return -1;
    }

    private boolean evalTypeExpression(){
        Posfix.infixToPostfix(expresion.toString().split(" "));
        Posfix.toArraysExpresion();

        String it1,type1[];
        String it2,type2[];

        if(Posfix.operadores.isEmpty())
        {
            it1 = Posfix.ids.pop();
            type2 = checkExistingVariable(it1);

            System.out.println(Arrays.toString(data_type));
            System.out.println(Arrays.toString(type2));

            if(compatibility[getIndexOf(data_type[1])][getIndexOf(type2[1])]!=null)
            {
                semanticError.append("\nAnalisis semantico finalizado correctamente");
                return true;
            }else{
                semanticError.append(String.format("\nEl tipo de dato de %s:%s y %s no coinciden\n",
                        data_type[0],data_type[1],it1));
                onError.run();
                return false;
            }
        }
        else while(!Posfix.operadores.isEmpty())
        {
            it1 = Posfix.ids.pop();
            it2 = Posfix.ids.pop();

            type1 = checkExistingVariable(it1);
            type2 = checkExistingVariable(it2);


            //si es nulo, es porque el tipo de variable no se encontro
            //(osea no hay variable dentro it1 y hay un tipo de dato en it1)
            if(type1==null)
            {
                int ind;
                if((ind=getIndexOf(it1))!=-1)
                {
                    int ind2 = getIndexOf(type2[1]);
                    String type = compatibility[ind2][ind];

                    if(type==null){
                        semanticError.append(String.format
                                ("Tipo de datos no posibles [%s & %s no compatibles]\n",
                                                type2[1],it1));
                        onError.run();
                        return false;
                    }else{
                        Posfix.ids.push(type);
                    }

                }else{
                    System.err.println("Tipo de dato no encontrado");
                    return false;
                }
            }else{

                int ind;
                if((ind=getIndexOf(type1[1]))!=-1)
                {
                    //Posfix.ids.push(datatypes[ind]);
                    int ind2 = getIndexOf(type2[1]);
                    String type = compatibility[ind2][ind];
                    if(type==null){
                        semanticError.append(String.format("Tipo de datos no posibles [%s & %s no compatibles]\n",
                                type2[1],it1));
                        onError.run();
                        return false;
                    }else{
                        Posfix.ids.push(type);
                    }

                }else{
                    System.err.println("Tipo de dato no encontrado");
                    return false;
                }
            }
            Posfix.operadores.poll();
        }

        if(data_type[1].equals(Posfix.ids.peek())){
            semanticError.append("\nAnalisis semantico finalizado correctamente");
            return true;
        }else{
            semanticError.append(String.format("\nEl tipo de dato de %s:%s y %s no coinciden\n",
                    data_type[0],data_type[1],Posfix.ids.peek()));
            return false;
        }
    }

    public boolean semanticAnalyze(String comp)
    {

        if(comp.equals(";")){
            currentType=null;
            if(isAsign){
                System.out.println("Evaluando expresion: "+expresion);
                isAsign=false;
                return evalTypeExpression();
            }

            return true;
        }else if(comp.equals("=")){
            currentType=null;
            isAsign=true;
            return true;
        }else{

            if(isAsign)
            {
                if(Lexico.isIdentifier(comp)) {
                    if (checkExistingVariable(comp)==null){
                        semanticError.append("No se ha declarado la variable: "+comp);
                        onError.run();
                        return false;
                    }

                }
                expresion.append(comp).append(" ");
            }
            else{

                for (String type : datatypes)
                {
                    if (comp.equals(type)) {
                        currentType = type;
                        if(!dataIdTypes.containsKey(type))
                            dataIdTypes.put(type, new ArrayList<>());
                        return true;
                    }
                }

                //int->currentType
                //y
                if(currentType!=null && Lexico.isIdentifier(comp))
                    return this.checkDataTypes(comp);
                else if(Lexico.isIdentifier(comp)){
                    data_type=checkExistingVariable(comp);
                    if(data_type==null){
                        semanticError.append("No se ha declarado la variable: "+comp);
                        onError.run();
                        return false;
                    }
                }
            }

            return true;
        }
    }

    private String[] checkExistingVariable(String comp){

//        System.out.println("Data types: "+dataIdTypes);
        Set<String> keys = dataIdTypes.keySet();

        for(String k : keys)
        {
            if(dataIdTypes.get(k).contains(comp))
            {
                return new String[]{comp,k};
            }
        };

        return null;
    }

    private boolean checkDataTypes(String comp){
        Set<String> keys = dataIdTypes.keySet();
        AtomicBoolean status= new AtomicBoolean(true);

        keys.forEach(k->{

            if(dataIdTypes.get(k).contains(comp))
            {
                semanticError.append(String.format("Ya existe el identifcador %s declarado con el "+
                        "tipo de dato %s\n",comp,k));
                onError.run();
                status.set(false);
            }
        });

        if(status.get()){
            //dataIdTypes={int->{x,y}}
            dataIdTypes.get(currentType).add(comp);
        }

        return status.get();
    }
}
