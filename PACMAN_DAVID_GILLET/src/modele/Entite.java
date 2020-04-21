/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * @author freder
 */
public abstract class Entite implements Runnable {

    protected Jeu jeu;
    protected Direction d;
    protected boolean mort;
    
    public abstract void choixDirection(); // stratégie de déclassement définie dans les sous-classes, concernant Pacman, ce sont les évènements clavier qui définissent la direction
    
    public void avancerDirectionChoisie() {
        jeu.deplacerEntite(this, d);
    }
    
    public Entite(Jeu _jeu) {
        jeu = _jeu;
        mort = false;
    }
    
    public Direction getDirection(){
        return d;
    }
    
    public boolean getMort()
    {
        return mort;
    }
    
    public void setMort(boolean _mort)
    {
        mort = _mort;
    }
    
    @Override
    public void run() {
        choixDirection();
        avancerDirectionChoisie();
    }
}
