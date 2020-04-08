/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;
import java.util.Random;

/**
 *
 * @author freder
 */
public class Fantome extends Entite {

    private Random r = new Random();
    private String color;
    private boolean mort;

    public Fantome(Jeu _jeu, String p_color) {
        super(_jeu);
        color = p_color;
        mort = false;
        d = Direction.haut;
    }
    
    public String getColor(){
        return color;
    }
    
    public boolean getMort(){
        return mort;
    }
    
    public void setMort(boolean _mort){
        mort = _mort;
    }
    
    @Override
    public void choixDirection() {  
        // développer une stratégie plus détaillée (utiliser regarderDansLaDirection(Entité, Direction) , ajouter murs, etc.)
        if (d == null || jeu.regarderDansLaDirection(this, d) instanceof Mur || jeu.regarderDansLaDirection(this, d) instanceof Fantome)
        switch (r.nextInt(4)) {
              case 0:
                  d = Direction.droite;
                  break;
              case 1:
                  d = Direction.bas;
                  break;
              case 2:
                  d = Direction.gauche;
                  break;
              case 3:
                  d = Direction.haut;
                  break;
        }  
    }
}