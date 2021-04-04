package com.company;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;

public class AlgoGenTSP {

    int distances[][]; //Le tableau de distances entre chaque ville
    int nbVilles; //Le nombre de villes
    ArrayList<Individu> population;
    int populationSize;


    //Constructor
    public AlgoGenTSP(int dist[][], int nbVilles, int populationSize) {
        this.distances = dist;
        this.nbVilles = nbVilles;
        this.populationSize = populationSize;
        this.population = new ArrayList<>(populationSize);
        initiatePopulation(population);
    }

    public Individu getBestIndividu() {
        Collections.sort(population);
        return population.isEmpty() ? null : population.get(0);
    }

    public Individu createIndividu() {
        return new Individu(nbVilles);
    }

    public Individu createRandomIndividu() {
        int[] chemin = new int[nbVilles - 1];
        TSPMain.genererChemin(chemin, nbVilles);
        return new Individu(nbVilles, chemin, distances);
    }

    void initiatePopulation(ArrayList<Individu> population) {
        for (int i = 0; i < populationSize; i++) {
            population.add(i, createRandomIndividu());
        }
    }

    void printPopulation() {
        System.out.println("Population : ");
        for (int i = 0, size = population.size(); i < size; i++) {
            Individu ind = population.get(i);
            System.out.print("Individu n°" + i + "\t");
            ind.print();
        }
    }

    public void iterer(double kBestPercent, double mutationChance, int mutationTirages) {
        int nBest = (int) (populationSize * kBestPercent);
        Collections.sort(population);
        ArrayList<Individu> newPopulation = new ArrayList<>(populationSize);
        for (int i = 0; i < nBest; i++) {
            newPopulation.add(i, population.get(i)); //On ajoute les n meilleurs individus
        }
        for (int i = nBest; i < populationSize; i++) { //On complète la liste avec des individus qui sont le croisement des n meilleurs avec des potentielles mutations
            int x = (int) (Math.random() * (nBest - 1)); //On prend un élément aléatoire parmis les n meilleurs
            int y = (int) (((Math.random() * (nBest - 2)) + x + 1)) % (nBest - 1); //On prend un second élément aléatoire parmis les n meilleurs différent du premier élément

            Individu ind = newPopulation.get(x).crossOver(newPopulation.get(y));

            for (int j = 0; j < mutationTirages; j++) {
                if (Math.random() < mutationChance) ind.mutation();
            }

            newPopulation.add(i, ind);
            population = newPopulation;
        }
    }

    public void solveByCyle(int nbCycle, double kBestPercent, double mutationChance, int mutationTirages) {
        double start = System.nanoTime();
        for (int i = 0; i < nbCycle; i++) {
            iterer(kBestPercent, mutationChance, mutationTirages);
        }

        double elapsedTime = (System.nanoTime() - start) / 10e6;

        int minutes = (int)((elapsedTime / 1000) / 60);

        // formula for conversion for
        // miliseconds to seconds
        double seconds = (elapsedTime / 1000) % 60;

        String time = elapsedTime + " Milliseconds = "
                + minutes + " minutes and "
                + seconds + " seconds.";

        System.out.print("Meilleure solution après " + nbCycle + " cycles  : ");
        getBestIndividu().print();
        System.out.println("Solution obtenue en : " + time);
    }
}
