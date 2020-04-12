/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/** La classe Jeu a deux fonctions 
 *  (1) Gérer les aspects du jeu : condition de défaite, victoire, nombre de vies
 *  (2) Gérer les coordonnées des entités du monde : déplacements, collisions, perception des entités, ... 
 *
 * @author freder
 */
public class Jeu extends Observable implements Runnable {

    public int TIME = 0;
    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 20;


    private Pacman pm;
    private Fantome bleu;
    private Fantome rouge;
    private Fantome rose;
    private Fantome orange;

    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées
    private HashMap<Mur, Point> mapMurs = new  HashMap<Mur, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Mur[][] grilleMurs = new Mur[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées
    private HashMap<Pastille, Point> mapPastilles = new  HashMap<Pastille, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Pastille[][] grillePastilles = new Pastille[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées
    
    // TODO : ajouter les murs, couloir, PacGums, et adapter l'ensemble des fonctions (prévoir le raffraichissement également du côté de la vue)
    
    
    public Jeu() throws IOException {
        initialisationDesMaps(2);
        initialisationDesPastilles();
        initialisationDesEntites();
    }
    
    public Entite[][] getGrille() {
        return grilleEntites;
    }
    
    public Point getEmplacementEntite(Entite e) {
        return map.get(e);
    }
    
    public Mur[][] getGrilleMurs() {
        return grilleMurs;
    }
    
    public Pastille[][] getGrillePastilles() {
        return grillePastilles;
    }
    
    public Pacman getPacman() {
        return pm;
    }
    
    public String lireFichierTexte(String nomFichier) throws FileNotFoundException, IOException{
        BufferedReader in = new BufferedReader(new FileReader("Maps/"+ nomFichier + ".txt"));
        String texte = "";
        String line = "";
        while ((line = in.readLine()) != null)
        {
      // Afficher le contenu du fichier
          texte = texte + "\n" + line;
        }
        in.close();
        return texte;
    }
    public void replacerEntites(){
        grilleEntites = new Entite[SIZE_X][SIZE_Y];
        map = new  HashMap<Entite, Point>();
        pm.setMort(false);
        bleu = new Fantome(this, "bleu");
        rose = new Fantome(this, "rose");
        rouge = new Fantome(this, "rouge");
        orange = new Fantome(this, "orange");
        grilleEntites[9][15] = pm;
        map.put(pm, new Point(9, 15));
        grilleEntites[8][10] = bleu;
        grilleEntites[9][10] = rose;
        grilleEntites[10][10] = rouge;
        grilleEntites[11][10] = orange;
        map.put(bleu, new Point(8,10));
        map.put(rose, new Point(9,10));
        map.put(rouge, new Point(10,10));
        map.put(orange, new Point(11,10));
    }
    
    public void replacerFantome(Fantome f){
        if (f.getColor() == "bleu") deplacerEntite(map.get((Entite)f),new Point(8,10), (Entite)f);
        else if (f.getColor() == "rose") deplacerEntite(map.get((Entite)f),new Point(9,10), (Entite)f);
        else if (f.getColor() == "rouge") deplacerEntite(map.get((Entite)f),new Point(10,10), (Entite)f);
        else if (f.getColor() == "orange") deplacerEntite(map.get((Entite)f),new Point(11,10), (Entite)f);
    }
    
    private void initialisationDesEntites() {
        //  Initialisation du pacman
        pm = new Pacman(this);
        grilleEntites[9][15] = pm;
        map.put(pm, new Point(9, 15));
        
        
        // Initialisation des fantomes
        bleu = new Fantome(this, "bleu");
        rose = new Fantome(this, "rose");
        rouge = new Fantome(this, "rouge");
        orange = new Fantome(this, "orange");
        grilleEntites[8][10] = bleu;
        grilleEntites[9][10] = rose;
        grilleEntites[10][10] = rouge;
        grilleEntites[11][10] = orange;
        map.put(bleu, new Point(8,10));
        map.put(rose, new Point(9,10));
        map.put(rouge, new Point(10,10));
        map.put(orange, new Point(11,10));
        
    } 
    
    /** Permet a une entité  de percevoir sont environnement proche et de définir sa strétégie de déplacement 
     * (fonctionalité utilisée dans choixDirection() de Fantôme)
     */
    public Object regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }
    
    public Point regarderDansLaDirectionPoint(Entite e, Direction d) {
        Point positionEntite = map.get(e);
        return calculerPointCible(positionEntite, d);
    }

    
    /** Si le délacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     */
    public boolean deplacerEntite(Entite e, Direction d) {    
        Point pCourant = map.get(e);
        Point pCible = calculerPointCible(pCourant, d);
        if (pCible.x == -1 && pCible.y == 10) deplacerEntite(pCourant, new Point(19, 10), e);
        else if (pCible.x == 20 && pCible.y == 10) deplacerEntite(pCourant, new Point(0, 10), e);
        else if (e instanceof Pacman && objetALaPosition(pCible) instanceof Fantome) {
            Fantome f = (Fantome)getGrille()[pCible.x][pCible.y];
            if(!f.getEatable()){
                if (!f.getMort())getPacman().setMort(true);
            }
            else {
                f.setMort(true);
            }
        }
        else if (e instanceof Fantome && objetALaPosition(pCible) instanceof Pacman){
            Fantome f = (Fantome)e;
            if(!f.getEatable()) {
                if (!f.getMort())getPacman().setMort(true);
            }
            else {
                f.setMort(true);
            }
        }
        else if (contenuDansGrille(pCible)){
            if (e instanceof Fantome){
                Fantome f = (Fantome)e;
                if (pCourant.x == 8 && pCourant.y == 10 && objetALaPosition(new Point(8,9)) == null) {
                    if(!f.getMort() && grilleEntites[8][10].d == Direction.haut){
                        deplacerEntite(pCourant, new Point(8, 9), e);
                    }else grilleEntites[8][10].d = Direction.haut;
                }
                else if (pCourant.x == 11 && pCourant.y == 10 && objetALaPosition(new Point(11,9)) == null) {
                    if(!f.getMort() && grilleEntites[11][10].d == Direction.haut){
                        deplacerEntite(pCourant, new Point(11, 9), e);
                    }else grilleEntites[11][10].d = Direction.haut;
                }
                else if ((pCourant.x == 8 && pCourant.y == 8 && e.d == Direction.bas) || (pCourant.x == 11 && pCourant.y == 8 && e.d == Direction.bas) || objetALaPosition(pCible) != null) grilleEntites[pCourant.x][pCourant.y].choixDirection();
                else if(!f.getMort()) deplacerEntite(pCourant, pCible, e);

            }
            else if (e instanceof Pacman){
                if (!(pCourant.x == 8 && pCourant.y == 8 && e.d == Direction.bas) && !(pCourant.x == 11 && pCourant.y == 8 && e.d == Direction.bas) && objetALaPosition(pCible) == null) deplacerEntite(pCourant, pCible, e);
                else return false;
            }
        }
        else return false;
        return true;
    }
    
    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        grilleEntites[pCourant.x][pCourant.y] = null;
        grilleEntites[pCible.x][pCible.y] = e;
        map.put(e, pCible);
    }
    
    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;   
        switch(d) {
            case haut: pCible = new Point(pCourant.x, pCourant.y - 1); break;
            case bas : pCible = new Point(pCourant.x, pCourant.y + 1); break;
            case gauche : pCible = new Point(pCourant.x - 1, pCourant.y); break;
            case droite : pCible = new Point(pCourant.x + 1, pCourant.y); break;     
        }
        return pCible;
    }
    
    
    /** Vérifie que p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return (p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y);
    }
    
    private Object objetALaPosition(Point p) {
        Object retour = null;
        if (contenuDansGrille(p)) {
            if (grilleMurs[p.x][p.y] != null)retour = grilleMurs[p.x][p.y];
            else retour = grilleEntites[p.x][p.y];
        }
        return retour;
    }
    
    /**
     * Un processus est créé et lancé, celui-ci execute la fonction run()
     */
    public void start() {
        new Thread(this).start();
    }
    
    public void initialisationDesPastilles(){
        Pastille pastille;
        
        for(int x = 0; x < SIZE_X; x++){
            for(int y = 0; y < SIZE_Y; y++)
            {
                
                if (grilleMurs[x][y] == null && ((x == 1 && y == 1)||(x == 1 && y == SIZE_Y-2)||(x == SIZE_X-2 && y == SIZE_Y-2)||(x == SIZE_X-2 && y == 1))){ 
                    pastille = new Pastille(this, "grande");
                    grillePastilles[x][y] = pastille;
                    mapPastilles.put(pastille, new Point(x,y));
                }else if(grilleMurs[x][y] == null){
                    if (!(x == 8 && y == 10) && !(x == 11 && y == 10) && !(x == 9 && y == 10) && !(x == 10 && y == 10)){
                        pastille = new Pastille(this, "petite");
                        grillePastilles[x][y] = pastille;
                        mapPastilles.put(pastille, new Point(x,y));
                    }
                }
            }
        }
    }
    
    public void initialisationDesMaps(int map) throws IOException{ 
        String mapClassiqueString = lireFichierTexte("classique");
        String map2String = lireFichierTexte("2");
        Mur mur;
        int index = 1;
        int[][][] mapClassique = new int[SIZE_X][SIZE_Y][2];
        int[][][] map2 = new int[SIZE_X][SIZE_Y][2];
        
        //creation tableau maps
        for (int y = 0; y < SIZE_Y; y++)
            for (int x = 0; x < SIZE_X; x++){
                mapClassique[x][y][0] = Character.getNumericValue(mapClassiqueString.charAt(index));
                map2[x][y][0] = Character.getNumericValue(map2String.charAt(index));
                index = index + 2;
                mapClassique[x][y][1] = Character.getNumericValue(mapClassiqueString.charAt(index));
                map2[x][y][1] = Character.getNumericValue(map2String.charAt(index));
                index = index + 2;              
            }
        
        //placements murs
        // 1 droit 2 fin 3 coté 4 angle 5 mur
        if (map == 1){
        for (int y = 0; y < SIZE_Y; y++)
            for (int x = 0; x < SIZE_X; x++){
            switch (mapClassique[x][y][0]) {
                case 1:
                    mur = new Mur(this, "droit", mapClassique[x][y][1]);
                    break;
                case 2:
                    mur = new Mur(this, "fin", mapClassique[x][y][1]);
                    break;
                case 3:
                    mur = new Mur(this, "cote", mapClassique[x][y][1]);
                    break;
                case 4:
                    mur = new Mur(this, "angle", mapClassique[x][y][1]);
                    break;
                case 5:
                    mur = new Mur(this, "mur", mapClassique[x][y][1]);
                    break;
                default:
                    mur = null;
                    break;
            }
                if (mur != null){
                    grilleMurs[x][y] = mur;
                    mapMurs.put(mur, new Point(x,y)); 
                }   
            }
        }
        else if (map == 2){
        for (int y = 0; y < SIZE_Y; y++)
            for (int x = 0; x < SIZE_X; x++){
            switch (map2[x][y][0]) {
                case 1:
                    mur = new Mur(this, "droit", map2[x][y][1]);
                    break;
                case 2:
                    mur = new Mur(this, "fin", map2[x][y][1]);
                    break;
                case 3:
                    mur = new Mur(this, "cote", map2[x][y][1]);
                    break;
                case 4:
                    mur = new Mur(this, "angle", map2[x][y][1]);
                    break;
                case 5:
                    mur = new Mur(this, "mur", map2[x][y][1]);
                    break;
                default:
                    mur = null;
                    break;
            }
                if (mur != null){
                    grilleMurs[x][y] = mur;
                    mapMurs.put(mur, new Point(x,y)); 
                }   
            }
        }
    }
    

    @Override
    public void run() {
        while (true) {
            for (Entite e : map.keySet()) { // déclenchement de l'activité des entités, map.keySet() correspond à la liste des entités
                e.run(); 
            }
            setChanged();
            notifyObservers(); // notification de l'observer pour le raffraichisssement graphique
            try {
                Thread.sleep(500); // pause de 0.5s
            } catch (InterruptedException ex) {
                Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    } 
}
