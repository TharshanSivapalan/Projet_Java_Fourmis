package com.company;

import java.util.*;
import javax.swing.JFrame;


public class Simulation extends JFrame {

    private ArrayList<Fourmi> fourmis = new ArrayList<Fourmi>();
    private ArrayList<Food> foods = new ArrayList<Food>();
    private ArrayList<Pheromone> pheromones = new ArrayList<Pheromone>();
    private Fourmiliere fourmiliere;

    int limit = 0;

    int vitessePheromone = 3;

    public Simulation(int taille, int nbFourmis, int nbFood, int vitessePheromone) {

        this.limit = taille;
        fourmiliere = new Fourmiliere(limit);
        this.fillFourmisList(nbFourmis);
        this.fillFoodsList(nbFood);
    }

    public void nextStep() {
        this.deplacement();
    }

    /**
     * init fourmis list
     * @param nbFourmis
     */
    private void fillFourmisList(int nbFourmis){
        for (int i = 0; i < nbFourmis; i++) {
            fourmis.add(new Fourmi(fourmiliere, limit));
        }
    }

    /**
     * init foods list
     * @param nbFood
     */
    private void fillFoodsList(int nbFood){
        for (int i = 0; i < nbFood; i++) {
            foods.add(new Food(limit));
        }
    }

    private ArrayList<Position> allPositions(Fourmi fourmi){
        ArrayList<Position> positions = new ArrayList<Position>();

        // Nord
        positions.add(new Position("N", fourmi.getPosY() - 1, fourmi.getPosX(), limit, pheromones));

        // Nord-Ouest
        positions.add(new Position("NO", fourmi.getPosY() - 1, fourmi.getPosX() + 1, limit, pheromones));

        // Ouest
        positions.add(new Position("O", fourmi.getPosY(), fourmi.getPosX() + 1, limit, pheromones));

        // Sud-Ouest
        positions.add(new Position("SO", fourmi.getPosY() + 1, fourmi.getPosX() + 1, limit, pheromones));

        // Sud
        positions.add(new Position("S", fourmi.getPosY() + 1, fourmi.getPosX(), limit, pheromones));

        // Sud-Est
        positions.add(new Position("SE", fourmi.getPosY() + 1, fourmi.getPosX() - 1, limit, pheromones));

        // Est
        positions.add(new Position("E", fourmi.getPosY(), fourmi.getPosX() - 1, limit, pheromones));

        // Nord-Est
        positions.add(new Position("NE", fourmi.getPosY() - 1, fourmi.getPosX() - 1, limit, pheromones));

        Collections.sort(positions, new Comparator<Position>() {
            @Override
            public int compare(Position p1, Position p2) {
                    return p1.compareTo(p2);
            }
        });

        int score = positions.get(0).getScore();

        Iterator<Position> i = positions.iterator();
        while (i.hasNext()) {
            Position pos = i.next();
            if(pos.getScore() < score){
                i.remove();
            }
        }

        return positions;
    }


    private void deplacement () {

        for (Fourmi fourmi : this.fourmis) {

            if (fourmi.getNourriture() == 0) {

                ArrayList<Position> positions = allPositions(fourmi);
                // il faut shuffle avant car l'aleatoire pas suffisant
                Collections.shuffle(positions);
                Random randomGenerator = new Random();
                int rnd = randomGenerator.nextInt(positions.size());

                Position newPosition = positions.get(rnd);

                fourmi.setPosX(newPosition.getX());
                fourmi.setPosY(newPosition.getY());
                checkFood(fourmi);

            } else {
                goToFourmilliere(fourmi);
            }
        }

        Iterator<Food> i = foods.iterator();
        while (i.hasNext()) {
            Food food = i.next();
            if(food.getSize() <= 0){
                i.remove();
            }
        }

    }

    public void checkFood(Fourmi fourmi){
        if (fourmi.getNourriture() == 0){
            for (Food food : foods) {
                if (
                        (fourmi.getPosX() == food.getX() || fourmi.getPosX() + 4 == food.getX() || (fourmi.getPosX() < food.getX() && fourmi.getPosX() + 4 > food.getX())) &&
                                (fourmi.getPosY() == food.getY() || fourmi.getPosY() + 4 == food.getY() || (fourmi.getPosY() < food.getY() && fourmi.getPosY() + 4 > food.getY()))
                        ) {
                    food.takeNourriture();
                    fourmi.setNourriture(1);
                }
            }


        }
    }

    public void goToFourmilliere(Fourmi fourmi) {

        if ((fourmi.getPosX() == fourmiliere.getX() || fourmi.getPosX() + 4 == fourmiliere.getX() || (fourmi.getPosX() < fourmiliere.getX() && fourmi.getPosX() + 4 > fourmiliere.getX())) &&
                (fourmi.getPosY() == fourmiliere.getY() || fourmi.getPosY() + 4 == fourmiliere.getY() || (fourmi.getPosY() < fourmiliere.getY() && fourmi.getPosY() + 4 > fourmiliere.getY()))){
            fourmiliere.addNourriture();
            fourmi.setNourriture(0);
        } else {

            if (fourmiliere.getX() > fourmi.getPosX()){
                fourmi.setPosX(fourmi.getPosX() + 1);
            } else if (fourmiliere.getX() < fourmi.getPosX()) {
                fourmi.setPosX(fourmi.getPosX() - 1);
            }

            if (fourmiliere.getY() > fourmi.getPosY()){
                fourmi.setPosY(fourmi.getPosY() + 1);
            } else if (fourmiliere.getY() < fourmi.getPosY()) {
                fourmi.setPosY(fourmi.getPosY() - 1);
            }

            // pose pheromone
        }
    }

    public void pheromonePropagation(){

    }

    public ArrayList<Fourmi> getFourmis() {
        return fourmis;
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public Fourmiliere getFourmiliere() {
        return fourmiliere;
    }

    public ArrayList<Pheromone> getPheromones() {
        return pheromones;
    }
}