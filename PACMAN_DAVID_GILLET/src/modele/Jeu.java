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
    public String[] mapsString = {"", ""};
    private int numMap;
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
   
    
    
    public Jeu() {
        numMap = 0;
    }
    
    public void initialiserJeu(){
        initialisationDesMaps();
        initialisationDesPastilles();
        initialisationDesEntites();
    }
    
    public void choixMap(int _numMap){
        numMap = _numMap;
    }
    
    public int getMap(){
        return numMap;
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
        if (grilleEntites[8][10] == null)deplacerEntite(map.get((Entite)f),new Point(8,10), (Entite)f);
        else if (grilleEntites[9][10] == null) deplacerEntite(map.get((Entite)f),new Point(9,10), (Entite)f);
        else if (grilleEntites[10][10] == null) deplacerEntite(map.get((Entite)f),new Point(10,10), (Entite)f);
        else if (grilleEntites[11][10] == null) deplacerEntite(map.get((Entite)f),new Point(11,10), (Entite)f);
    }
    
    private void initialisationDesEntites() {
        
        //  Initialisation du pacman
        if (pm == null)pm = new Pacman(this);
        else pm = new Pacman(this, pm.getNbVies());
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
    public Object regarderDansLaDirection(Point p, Direction d) {
        return objetALaPosition(calculerPointCible(p, d));
    }
    
    public Point regarderDansLaDirectionPoint(Entite e, Direction d) {
        Point positionEntite = map.get(e);
        return calculerPointCible(positionEntite, d);
    }
    
    public Point regarderDansLaDirectionPoint(Point p, Direction d) {
        return calculerPointCible(p, d);
    }
    
    public Direction[] cheminVersPacman(Fantome f){
        Direction[][] chemin = new Direction[SIZE_X*SIZE_Y][SIZE_X*SIZE_Y];
        int nChemin = 0;
        int n = 0;
        int n2 = 0;
        int m = 0;
        Point p = map.get(f);
        if (regarderDansLaDirection(p, Direction.gauche) instanceof Pacman) {
            chemin[0][0] = Direction.gauche;
            nChemin = 0;
        }
        else if (regarderDansLaDirection(p, Direction.haut) instanceof Pacman) {
            chemin[0][0] = Direction.haut;
            nChemin = 0;
        }
        else if (regarderDansLaDirection(p, Direction.droite) instanceof Pacman) {
            chemin[0][0] = Direction.droite;
            nChemin = 0;
        }
        else if (regarderDansLaDirection(p, Direction.bas) instanceof Pacman) {
            chemin[0][0] = Direction.bas;
            nChemin = 0;
        }
        else {
            Direction[] lesDirections;
            int nDirections = 0;
            if(!(regarderDansLaDirection(p, Direction.gauche) instanceof Mur)) nDirections++;
            if(!(regarderDansLaDirection(p, Direction.haut) instanceof Mur)) nDirections++;
            if(!(regarderDansLaDirection(p, Direction.droite) instanceof Mur)) nDirections++;
            if(!(regarderDansLaDirection(p, Direction.bas) instanceof Mur) && !(p.x == 11 && p.y == 8) && !(p.x == 8 && p.y == 8)) nDirections++;
            lesDirections = new Direction[nDirections];
            int indexDirection = 0;
            if(!(regarderDansLaDirection(p, Direction.gauche) instanceof Mur)) {
                lesDirections[indexDirection] = Direction.gauche;
                indexDirection++;
            }
            if(!(regarderDansLaDirection(p, Direction.haut) instanceof Mur)) {
                lesDirections[indexDirection] = Direction.haut;
                indexDirection++;
            }
            if(!(regarderDansLaDirection(p, Direction.droite) instanceof Mur)) {
                lesDirections[indexDirection] = Direction.droite;
                indexDirection++;
            }
            if(!(regarderDansLaDirection(p, Direction.bas) instanceof Mur) && !(p.x == 11 && p.y == 8) && !(p.x == 8 && p.y == 8)) {
                lesDirections[indexDirection] = Direction.bas;
                indexDirection++;
            }               
            for (Direction d : lesDirections){
                chemin[n2][m] = d;
                n2++;
            }
            n = n2;   
            m++;
            try{
                while (nChemin == 0){
                    for (int i = 0; i < n; i++){
                        p = map.get(f);     
                        for (int j = 0; j < m; j++){
                            try{
                            p = calculerPointCible(p, chemin[i][j]);
                            }catch (Exception e){
                            }
                        } 
                        if (regarderDansLaDirection(p, Direction.gauche) instanceof Pacman) {
                            chemin[i][m] = Direction.gauche;
                            nChemin = i;
                            i = n;
                        }
                        else if (regarderDansLaDirection(p, Direction.haut) instanceof Pacman) {
                            chemin[i][m] = Direction.haut;
                            nChemin = i;
                            i = n;
                        }
                        else if (regarderDansLaDirection(p, Direction.droite) instanceof Pacman) {
                            chemin[i][m] = Direction.droite;
                            nChemin = i;
                            i = n;
                        }
                        else if (regarderDansLaDirection(p, Direction.bas) instanceof Pacman) {
                            chemin[i][m] = Direction.bas;
                            nChemin = i;
                            i = n;
                        }
                        else {
                            nDirections = 0;
                            if(!(regarderDansLaDirection(p, Direction.gauche) instanceof Mur) && chemin[i][m-1] != Direction.droite) nDirections++;
                            if(!(regarderDansLaDirection(p, Direction.haut) instanceof Mur) && chemin[i][m-1] != Direction.bas) nDirections++;
                            if(!(regarderDansLaDirection(p, Direction.droite) instanceof Mur) && chemin[i][m-1] != Direction.gauche) nDirections++;
                            if(!(regarderDansLaDirection(p, Direction.bas) instanceof Mur) && chemin[i][m-1] != Direction.haut && !(p.x == 11 && p.y == 8) && !(p.x == 8 && p.y == 8)) nDirections++;
                            lesDirections = new Direction[nDirections];
                            indexDirection = 0;
                            if(!(regarderDansLaDirection(p, Direction.gauche) instanceof Mur) && chemin[i][m-1] != Direction.droite) {
                                lesDirections[indexDirection] = Direction.gauche;
                                indexDirection++;
                            }
                            if(!(regarderDansLaDirection(p, Direction.haut) instanceof Mur) && chemin[i][m-1] != Direction.bas) {
                                lesDirections[indexDirection] = Direction.haut;
                                indexDirection++;
                            }
                            if(!(regarderDansLaDirection(p, Direction.droite) instanceof Mur) && chemin[i][m-1] != Direction.gauche) {
                                lesDirections[indexDirection] = Direction.droite;
                                indexDirection++;
                            }
                            if(!(regarderDansLaDirection(p, Direction.bas) instanceof Mur) && chemin[i][m-1] != Direction.haut && !(p.x == 11 && p.y == 8) && !(p.x == 8 && p.y == 8)) {
                                lesDirections[indexDirection] = Direction.bas;
                                indexDirection++;
                            }               
                            for (Direction d : lesDirections){
                                if (chemin[i][m] == null)chemin[i][m] = d;
                                else {
                                    System.arraycopy(chemin[i], 0, chemin[n2], 0, m);                            
                                    chemin[n2][m] = d; 
                                    n2++;
                                }
                            }         
                        }
                    }
                    n = n2;
                    m++;
                }
            }catch(Exception e){}
        } 
        return chemin[nChemin];
    }

    
    /** Si le délacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     */
    public boolean deplacerEntite(Entite e, Direction d) {    
        Point pCourant = map.get(e);
        Point pCible = calculerPointCible(pCourant, d);
        if (e instanceof Pacman && objetALaPosition(pCible) instanceof Fantome) {
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
        else{
            if (e instanceof Fantome){
                Fantome f = (Fantome)e;
                if (pCourant.x == 8 && pCourant.y == 9){
                    f.setSorti(true);
                    grilleEntites[8][9] = f;
                }else if (pCourant.x == 11 && pCourant.y == 9){
                    f.setSorti(true);
                    grilleEntites[11][9] = f;
                }
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
        return true;
    }
    
    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        grilleEntites[pCourant.x][pCourant.y] = null;
        grilleEntites[pCible.x][pCible.y] = e;
        map.put(e, pCible);
    }
    
    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;
        if (pCourant.x == 0 && pCourant.y == 10 && d == Direction.gauche)pCible = new Point(19, 10);
        else if (pCourant.x == 19 && pCourant.y == 10 && d == Direction.droite)pCible = new Point(0, 10);
        else{
            switch(d) {
                case haut: pCible = new Point(pCourant.x, pCourant.y - 1); break;
                case bas : pCible = new Point(pCourant.x, pCourant.y + 1); break;
                case gauche : pCible = new Point(pCourant.x - 1, pCourant.y); break;
                case droite : pCible = new Point(pCourant.x + 1, pCourant.y); break;     
            }
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
        if (p.x == -1 && p.y == 10)p = new Point(19, 10);
        else if (p.x == 20 && p.y == 10)p = new Point(0, 10);
        
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
                    if (numMap == 1){
                        if (!(x == 8 && y == 10) && 
                                !(x == 11 && y == 10) && 
                                !(x == 9 && y == 10) && 
                                !(x == 10 && y == 10) && 
                                !(x == 8 && y == 9) && 
                                !(x == 11 && y == 9) &&
                                !(x == 5 && y == 10) &&
                                !(x == 4 && y == 10) && 
                                !(x == 3 && y == 10) && 
                                !(x == 2 && y == 10) && 
                                !(x == 1 && y == 10) && 
                                !(x == 0 && y == 10) && 
                                !(x == 14 && y == 10) &&
                                !(x == 15 && y == 10) && 
                                !(x == 16 && y == 10) && 
                                !(x == 17 && y == 10) &&
                                !(x == 18 && y == 10) &&
                                !(x == 19 && y == 10)) 
                        {
                            pastille = new Pastille(this, "petite");
                            grillePastilles[x][y] = pastille;
                            mapPastilles.put(pastille, new Point(x,y));
                        }
                    }else if (numMap == 2){
                        if (!(x == 8 && y == 10) && 
                                !(x == 11 && y == 10) && 
                                !(x == 9 && y == 10) && 
                                !(x == 10 && y == 10) && 
                                !(x == 8 && y == 9) && 
                                !(x == 11 && y == 9) &&
                                !(x == 0 && y == 10) &&
                                !(x == 1 && y == 10) && 
                                !(x == 2 && y == 10) && 
                                !(x == 19 && y == 10) &&
                                !(x == 18 && y == 10) &&
                                !(x == 17 && y == 10)) 
                        {
                            pastille = new Pastille(this, "petite");
                            grillePastilles[x][y] = pastille;
                            mapPastilles.put(pastille, new Point(x,y));
                        }
                    }
                }
            }
        }
    }
    
    public void initialisationDesMaps(){ 
        String mapClassiqueString = mapsString[0];
        String mapHeadString = mapsString[1];
        Mur mur;
        int index = 1;
        int[][][] mapClassique = new int[SIZE_X][SIZE_Y][2];
        int[][][] mapHead = new int[SIZE_X][SIZE_Y][2];
        
        //creation tableau maps
        for (int y = 0; y < SIZE_Y; y++)
            for (int x = 0; x < SIZE_X; x++){
                mapClassique[x][y][0] = Character.getNumericValue(mapClassiqueString.charAt(index));
                mapHead[x][y][0] = Character.getNumericValue(mapHeadString.charAt(index));
                index = index + 2;
                mapClassique[x][y][1] = Character.getNumericValue(mapClassiqueString.charAt(index));
                mapHead[x][y][1] = Character.getNumericValue(mapHeadString.charAt(index));
                index = index + 2;              
            }
        
        //placements murs
        // 1 droit 2 fin 3 coté 4 angle 5 mur
        if (numMap == 1){
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
        else if (numMap == 2){
        for (int y = 0; y < SIZE_Y; y++)
            for (int x = 0; x < SIZE_X; x++){
            switch (mapHead[x][y][0]) {
                case 1:
                    mur = new Mur(this, "droit", mapHead[x][y][1]);
                    break;
                case 2:
                    mur = new Mur(this, "fin", mapHead[x][y][1]);
                    break;
                case 3:
                    mur = new Mur(this, "cote", mapHead[x][y][1]);
                    break;
                case 4:
                    mur = new Mur(this, "angle", mapHead[x][y][1]);
                    break;
                case 5:
                    mur = new Mur(this, "mur", mapHead[x][y][1]);
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
            map.keySet().forEach((e) -> {
                // déclenchement de l'activité des entités, map.keySet() correspond à la liste des entités
                e.run();
            });
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
