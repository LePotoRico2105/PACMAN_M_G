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
    private boolean eatable;
    private boolean sorti;
    private int spawntime;

    public Fantome(Jeu _jeu, String p_color) {
        super(_jeu);
        color = p_color;
        eatable = false;
        sorti = false;
        spawntime = 0;
    }
    
    public String getColor(){
        return color;
    }
    
    public boolean getEatable(){
        return eatable;
    }
    
    public void setEatable(boolean _eatable){
        eatable = _eatable;
    }
    
    public boolean getSorti(){
        return sorti;
    }
    
    public void setSorti(boolean _sorti){
        sorti = _sorti;
    }
    
    public int getSpawntime(){
        return spawntime;
    }
    
    public void setSpawntime(int _spawntime){
        spawntime = _spawntime;
    }
    
    @Override
    public void choixDirection() {  
        // développer une stratégie plus détaillée (utiliser regarderDansLaDirection(Entité, Direction) , ajouter murs, etc.)
        if ("rouge".equals(getColor()) && getEatable() == false && sorti == true && !(jeu.regarderDansLaDirection(this, d) instanceof Fantome)){
            Direction[] chemin = jeu.cheminVersPacman(this);
            /*for (int i = 0; i < chemin.length; i++)System.out.print(chemin[i] + " - ");
            System.out.println("\n");*/
            d = chemin[0];
            
        }
        else if (d == null || jeu.regarderDansLaDirection(this, d) instanceof Mur || jeu.regarderDansLaDirection(this, d) instanceof Fantome)
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
        else if (jeu.regarderDansLaDirectionPoint(this, d).x == 8 && jeu.regarderDansLaDirectionPoint(this, d).y == 9 || jeu.regarderDansLaDirectionPoint(this, d).x == 11 && jeu.regarderDansLaDirectionPoint(this, d).y == 9)
        switch (r.nextInt(3)) {
              case 0:
                  d = Direction.droite;
                  break;
              case 1:
                  d = Direction.haut;
                  break;
              case 2:
                  d = Direction.gauche;
                  break;
        }  
    }
}
