/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.Random;

/**
 *
 * @author freder
 */
public class Fantome extends Entite {

    private Random r = new Random();
    private String color;

    public Fantome(Jeu _jeu, String p_color) {
        super(_jeu);
        color = p_color;
    }
    
    public String getColor(){
        return color;
    }
    
    @Override
    public void choixDirection() {  
        // développer une stratégie plus détaillée (utiliser regarderDansLaDirection(Entité, Direction) , ajouter murs, etc.)
        switch (r.nextInt(2)) {
            case 0:
                d = Direction.droite;
                break;
            case 1:
                d = Direction.bas;
                break;
        }
    }
}