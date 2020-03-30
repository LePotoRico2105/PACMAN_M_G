/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.Random;

/**
 *
 * @author david
 */
public class Pastille {
    private String type;
    private boolean estMange;
    private Jeu jeu;
    
    
    public Pastille(Jeu _jeu, String p_type, boolean p_estMange) {
        jeu = _jeu;
        type = p_type;
        estMange = p_estMange;
    }
    
    public String getType(){
        return type;
    }
    
    public boolean getEstMange(){
        return estMange;
    }
}
