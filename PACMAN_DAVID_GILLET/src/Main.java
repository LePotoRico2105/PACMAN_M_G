
import VueControleur.VueControleurPacMan;
import java.io.IOException;
import modele.Jeu;
import modele.Pacman;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author freder
 */
public class Main {
    public static void main(String[] args) throws IOException {      
        Jeu jeu = new Jeu();

        VueControleurPacMan vc = new VueControleurPacMan(Jeu.SIZE_X, Jeu.SIZE_Y);
        
        jeu.addObserver(vc);
        vc.setJeu(jeu);
        
        vc.setVisible(true);
        jeu.choixMap(vc.choisirMap());
        jeu.initialiserJeu();
        jeu.start();
    }
}
