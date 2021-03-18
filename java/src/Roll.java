// Author : LEFEVRE Florian / ZHANG ANAIS / ESPERN THIBAULT 

package src;

import java.util.ArrayList;

public class Roll {

    public enum RollType {
        NORMAL,
        ADVANTAGE,
        DISADVANTAGE
    }

    // Attributes
    public int diceValue;
    public int nbRoll;
    public int modifier;
    ArrayList<Integer> diceRecountValue = new ArrayList<>();

    // Constructor

    public Roll(String formula) {
        ArrayList<Integer> playValue = new ArrayList<>();
        int diceValue = 0;
        int nbRoll = 0;
        int modifier = 0;
        if(!formula.isEmpty() && formula.matches("(^[1-9]d[0-9]+[+-][0-9]+)|(^[1-9]d[0-9]+)|(^d[0-9])|(^d[0-9]+[+-][0-9]+)")){
            if(!(formula.matches("^[1-9]d[1-9]$")) && !formula.matches("^[1-9]d[0-9]+[+-][0-9]+")){
                formula = "1" + formula;
            }
            String[] temp;
            temp = formula.split("[a-zA-Z+-]");
            for (int i = 0; i < temp.length; i++){
                playValue.add(Integer.parseInt(String.valueOf(temp[i])));
            }
            diceValue = playValue.get(1);
            nbRoll = playValue.get(0);
            if (playValue.size() > 2){
                if(formula.matches("^[1-9]d[0-9]+[-][0-9]+")){
                    modifier = playValue.get(2) * -1;
                }else{
                    modifier = playValue.get(2);
                }
            }
            if(diceValue > 0 && nbRoll >0){
                this.diceRecountValue.add(calculate(diceValue,nbRoll,modifier));
            }else{
                this.diceRecountValue.add(-1);
            }
        }else{
            this.diceRecountValue.add(-1);
        }
    }

    public Roll(int diceValue, int nbRoll, int modifier) {
        int sum = 0;
        if (diceValue > 0 && nbRoll > 0){
            this.diceRecountValue.add(calculate(diceValue,nbRoll,modifier));
        }else{
            this.diceRecountValue.add(-1);
        }
    }

    // Methods

    public int calculate(int diceValue, int nbRoll, int modifier){
        int dice = 0;
        for (int i = 0; i < nbRoll; i++){
            dice += new Dice(diceValue).rollDice();
        }
        if(modifier < 0 && ((dice + modifier) <= 0)){
            dice = 0;
        }else{
            dice = dice + modifier;
        }
        return dice;
    }

    public int extraRoll(int diceValue, int nbRoll, int modifier, RollType rollType){

        ArrayList<Integer> sumTab = new ArrayList<>();
        int temp = 0;

        for(int j = 0; j < 2 ; j++){
            Roll sum = new Roll(diceValue,nbRoll,modifier);
            for(int i = 0 ; i < this.diceRecountValue.size(); i++){
                temp += this.diceRecountValue.get(i);
            }
            sumTab.add(temp);
            temp = 0;
        }

        if(rollType == RollType.ADVANTAGE){
            if(sumTab.get(0) > sumTab.get(1)){
                return sumTab.get(0);
            }else{
                return  sumTab.get(1);
            }
        }

        if(rollType == RollType.DISADVANTAGE){
            if (sumTab.get(0) < sumTab.get(1)){
                return sumTab.get(0);
            }else{
                return sumTab.get(1);
            }
        }

        return 0;

    }

    public int makeRoll(RollType rollType) {
        int returnValue = 0;
        switch (rollType){
            case NORMAL :
                returnValue = this.diceRecountValue.get(0);
                break;
            case ADVANTAGE:
                returnValue = extraRoll(diceValue,nbRoll,modifier,RollType.ADVANTAGE);
                break;
            case DISADVANTAGE:
                returnValue  = extraRoll(diceValue,nbRoll,modifier,RollType.DISADVANTAGE);
                break;
            default:
                returnValue = 0;
                break;


        }
        return returnValue;
    }

}