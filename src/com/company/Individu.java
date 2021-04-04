package com.company;

import javax.management.relation.RelationNotFoundException;
import javax.management.relation.RoleNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.function.IntUnaryOperator;

public class Individu implements Comparable<Individu> {

    int[] chemin;
    int nbVilles;
    int cout;
    int[][] distances;

    Individu(int nbVilles) {
        this.chemin = new int[nbVilles - 1];
    }

    Individu(int nbVilles, int[] chemin, int[][] distances) {
        this.chemin = chemin;
        this.nbVilles = nbVilles;
        this.distances = distances;
        calculerCout();
    }

    public int compareTo(Individu compare) {
        // a remplir retourne 0 si egaux, negatif si plus petit, positif si plus grand
        return this.cout - compare.cout;
    }

    public void print() {
        System.out.print("Chemin : 0->");
        for (int x = 0; x < nbVilles - 2; x++) {
            System.out.print(chemin[x] + "->");
        }
        System.out.println(chemin[nbVilles - 2] + "->0 ; Cout : " + cout);
    }

    public void setChemin(int[] chemin) {
        this.chemin = chemin;
        calculerCout();
    }

    public void calculerCout() {
        this.cout = TSPMain.calculerCout(distances, chemin, nbVilles);
    }

    public Individu crossOver(Individu partenaire) {
        ArrayList<Integer> nouveauChemin = new ArrayList<>(nbVilles - 1);
        Random rand = new Random();
        int pivot = rand.nextInt(nbVilles - 1);
        for (int i = 0; i < pivot + 1; i++) {
            nouveauChemin.add(chemin[i]);
        }
        for (int j = 0; j < nbVilles - 1; j++) {
            if (nouveauChemin.contains(partenaire.chemin[j])) continue;
            nouveauChemin.add(partenaire.chemin[j]);
        }
        return new Individu(nbVilles, nouveauChemin.stream().mapToInt(i -> i).toArray(), distances);
    }

    public void mutation() {
        //On échange deux destinations aléatoirement
        int x = (int) (Math.random() * (nbVilles - 2));
        int y = x + 1;
        int temp = chemin[x];
        chemin[x] = chemin[y];
        chemin[y] = temp;
        calculerCout(); //On pense à recalculer le coût
    }
}
