package com.agonmati.tackexpenses.trackexpenses.helpers;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by agonmati on 2/21/18.
 */

public class NumeroFjalite {
    public static void main(String [] args){
        Scanner sc = new Scanner(System.in);

        System.out.println("Shtypeni madhesine e vargut");

        int madhesia = sc.nextInt();
        while (madhesia<10 && madhesia%3 != 0){
            System.out.println("Shtypeni madhesine e vargut, madhesia duhet te jete numer me i madh se dhjete i plotpjesueshem me 3");
            madhesia = sc.nextInt();
        }

        String fjalite [] = new String[madhesia];
        for (int i=0;i<fjalite.length;i++){
            System.out.println("Shtyp fjaline e "+ i+1 );
            fjalite[i] = sc.nextLine();
        }

        String [] ktheFjalite = ktheFjalite(fjalite);

    }

    public static String [] ktheFjalite(String [] fjalite) {
        String shenjatEPiksimit [] = new String [3];
        shenjatEPiksimit[0]=".";
        shenjatEPiksimit[1]="!";
        shenjatEPiksimit[2]="?";

        String zanoret [] = new String[7];
        zanoret[0] = "A";
        zanoret[1] = "E";
        zanoret[2] = "Ë";
        zanoret[3] = "I";
        zanoret[4] = "O";
        zanoret[5] = "U";
        zanoret[6] = "Y";

        String fjaliteQePembushinKushtin [] = new String[fjalite.length];
        int totaliIFjaliveQePermbushinKushtin = 0;
        for (int i = 0;i<fjalite.length;i++){
            String karakteriIFunditIFjalise = fjalite[i].substring(fjalite[i].length() - 1);
            if (Arrays.asList(shenjatEPiksimit).equals(karakteriIFunditIFjalise)) { // kushti me shenja tpiksimit
                if (fjalite[i].split(" ").length > 2){//kushti per se paku 3 fjale ne fjali
                    //numrimi i zanoreve ne fjali
                    int countZanoret = 0;
                    for (int j =0;i <zanoret.length;j++){
                        if(fjalite[i].toUpperCase().contains(zanoret[j])){
                            countZanoret++;
                        }
                    }
                    if (countZanoret > 3) { // me shum se tri zanore
                        char [] karakteretEFjalise = fjalite[i].toCharArray();
                        int countNumrat = 0;
                        for (int j=0;j<karakteretEFjalise.length;i++){
                            if (Character.isDigit(karakteretEFjalise[j])){
                                countNumrat++;
                            }
                        }
                        if (countNumrat > (karakteretEFjalise.length*0.2)){ // Mbi 20% e karaktereve të fjalisë të jenë numra,
                            fjaliteQePembushinKushtin[totaliIFjaliveQePermbushinKushtin++] = fjalite[i];
                        }
                    }
                }

            }

        }
        //Array fjaliteQePembushinKushtin mundet me pas hapsira te zbrazta ne fund, per ket arsye
        // po e krijoj nje array te re e cila nuk do te kete hapsira te lira

        String [] fjaliteQePermbushinKushtin2 = new String[totaliIFjaliveQePermbushinKushtin];
        for(int i =0;i<totaliIFjaliveQePermbushinKushtin;i++){
            fjaliteQePermbushinKushtin2[i] = fjaliteQePembushinKushtin[i];
        }

        return fjaliteQePermbushinKushtin2;

    }
}
