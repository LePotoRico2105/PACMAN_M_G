/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * @author david
 */
public class Mur {
    private String type;
    private Jeu jeu;
    private int rotationType;
      
    public Mur(Jeu _jeu, String p_type, int p_rotationType) {
        jeu = _jeu;
        type = p_type;
        rotationType = p_rotationType;
    }
    
    public String getType(){
        return type;
    }
    public int getRotationType(){
        return rotationType;
    }
}
