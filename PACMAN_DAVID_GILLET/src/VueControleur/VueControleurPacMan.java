package VueControleur;


import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;
import modele.Direction;
import modele.Fantome;
import modele.Mur;
import modele.Jeu;
import modele.Pacman;
import modele.Pastille;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 * @author freder
 */
public class VueControleurPacMan extends JFrame implements Observer {

    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX; // taille de la grille affichée
    private int sizeY;

    private ImageIcon icone; // icones affichées dans la grille
    private ImageIcon icoPacman1;
    private ImageIcon icoPacman2;
    private ImageIcon icoPacman3;
    private ImageIcon icoPacman4;
    private ImageIcon icoBleuH;
    private ImageIcon icoBleuD;
    private ImageIcon icoBleuB;
    private ImageIcon icoBleuG;
    private ImageIcon icoRoseH;
    private ImageIcon icoRoseD;
    private ImageIcon icoRoseB;
    private ImageIcon icoRoseG;
    private ImageIcon icoRougeH;
    private ImageIcon icoRougeD;
    private ImageIcon icoRougeB;
    private ImageIcon icoRougeG;
    private ImageIcon icoOrangeH;
    private ImageIcon icoOrangeD;
    private ImageIcon icoOrangeB;
    private ImageIcon icoOrangeG;
    private ImageIcon icoEatable;
    private ImageIcon icoDead;
    private ImageIcon icoPastilleS;
    private ImageIcon icoPastilleL;
    private ImageIcon icoLogoTransparent;
    private ImageIcon icoLogoCouleur;
    private ImageIcon icoMur;
    private ImageIcon icoMurDroit1;
    private ImageIcon icoMurDroit2;
    private ImageIcon icoMurFin1;
    private ImageIcon icoMurFin2;
    private ImageIcon icoMurFin3;
    private ImageIcon icoMurFin4;
    private ImageIcon icoMurCote1;
    private ImageIcon icoMurCote2;
    private ImageIcon icoMurCote3;
    private ImageIcon icoMurCote4;
    private ImageIcon icoMurAngle1;
    private ImageIcon icoMurAngle2;
    private ImageIcon icoMurAngle3;
    private ImageIcon icoMurAngle4;
    private ImageIcon icoCouloir;

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associé à une icône, suivant ce qui est présent dans la partie modèle)
    
    public BufferedImage rotate(BufferedImage image, Double degrees) {
        // Calculate the new size of the image based on the angle of rotaion
        double radians = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int newWidth = (int) Math.round(image.getWidth() * cos + image.getHeight() * sin);
        int newHeight = (int) Math.round(image.getWidth() * sin + image.getHeight() * cos);

        // Create a new image
        BufferedImage rotate = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotate.createGraphics();
        // Calculate the "anchor" point around which the image will be rotated
        int x = (newWidth - image.getWidth()) / 2;
        int y = (newHeight - image.getHeight()) / 2;
        // Transform the origin point around the anchor point
        AffineTransform at = new AffineTransform();
        at.setToRotation(radians, x + (image.getWidth() / 2), y + (image.getHeight() / 2));
        at.translate(x, y);
        g2d.setTransform(at);
        // Paint the originl image
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return rotate;
    }

    public VueControleurPacMan(int _sizeX, int _sizeY) {

        sizeX = _sizeX;
        sizeY = _sizeY;
        

        chargerLesIcones();
        placerLesComposantsGraphiques();

        ajouterEcouteurClavier();
    }

    private void ajouterEcouteurClavier() {

        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                
                switch(e.getKeyCode()) {  // on écoute les flèches de direction du clavier
                    case KeyEvent.VK_LEFT : 
                        if (!(jeu.regarderDansLaDirection(jeu.getPacman(), Direction.gauche) instanceof Mur))
                        jeu.getPacman().setDirection(Direction.gauche); 
                    break;
                    case KeyEvent.VK_RIGHT : 
                        if (!(jeu.regarderDansLaDirection(jeu.getPacman(), Direction.droite) instanceof Mur))
                        jeu.getPacman().setDirection(Direction.droite); 
                    break;
                    case KeyEvent.VK_DOWN : 
                        if (!(jeu.regarderDansLaDirection(jeu.getPacman(), Direction.bas) instanceof Mur))
                        jeu.getPacman().setDirection(Direction.bas);
                    break;
                    case KeyEvent.VK_UP : 
                        if (!(jeu.regarderDansLaDirection(jeu.getPacman(), Direction.haut) instanceof Mur))
                        jeu.getPacman().setDirection(Direction.haut);
                    break;
                }
                
            }

        });

    }

    public void setJeu(Jeu _jeu) {
        jeu = _jeu;
    }

    private void chargerLesIcones() {
        // icones affichées dans la grille
        icone = chargerIcone("Images/icone.png", 0.0);
        icoPacman1 = chargerIcone("Images/pacman.png", 0.0);
        icoPacman2 = chargerIcone("Images/pacman.png", 90.0);
        icoPacman3 = chargerIcone("Images/pacman.png", 180.0);
        icoPacman4 = chargerIcone("Images/pacman.png", 270.0); 
        icoBleuH = chargerIcone("Images/bleuB.png", 0.0);
        icoBleuD = chargerIcone("Images/bleuD.png", 0.0);
        icoBleuB = chargerIcone("Images/bleuB.png", 0.0);
        icoBleuG = chargerIcone("Images/bleuG.png", 0.0);
        icoRoseH = chargerIcone("Images/roseH.png", 0.0);
        icoRoseD = chargerIcone("Images/roseD.png", 0.0);
        icoRoseB = chargerIcone("Images/roseB.png", 0.0);
        icoRoseG = chargerIcone("Images/roseG.png", 0.0);
        icoRougeH = chargerIcone("Images/rougeH.png", 0.0);
        icoRougeD = chargerIcone("Images/rougeD.png", 0.0);
        icoRougeB = chargerIcone("Images/rougeB.png", 0.0);
        icoRougeG = chargerIcone("Images/rougeG.png", 0.0);
        icoOrangeH = chargerIcone("Images/orangeH.png", 0.0);
        icoOrangeD = chargerIcone("Images/orangeD.png", 0.0);
        icoOrangeB = chargerIcone("Images/orangeB.png", 0.0);
        icoOrangeG = chargerIcone("Images/orangeG.png", 0.0);
        icoEatable = chargerIcone("Images/eatable.png", 0.0);
        icoDead = chargerIcone("Images/dead.png", 0.0);
        icoPastilleS = chargerIcone("Images/pastilleS.png", 0.0);
        icoPastilleL = chargerIcone("Images/pastilleL.png", 0.0);
        icoLogoTransparent = chargerIcone("Images/logoTransparent.png", 0.0);
        icoLogoCouleur = chargerIcone("Images/logoCouleur.png", 0.0);
        icoCouloir = chargerIcone("Images/couloir.png", 0.0);
        icoMur = chargerIcone("Images/mur.png", 0.0);
        icoMurDroit1 = chargerIcone("Images/murDroit.png", 0.0);
        icoMurDroit2 = chargerIcone("Images/murDroit.png", 90.0);
        icoMurFin1 = chargerIcone("Images/murFin.png", 0.0);
        icoMurFin2 = chargerIcone("Images/murFin.png", 90.0);
        icoMurFin3 = chargerIcone("Images/murFin.png", 180.0);
        icoMurFin4 = chargerIcone("Images/murFin.png", 270.0);
        icoMurCote1 = chargerIcone("Images/murCote.png", 0.0);
        icoMurCote2 = chargerIcone("Images/murCote.png", 90.0);
        icoMurCote3 = chargerIcone("Images/murCote.png", 180.0);
        icoMurCote4 = chargerIcone("Images/murCote.png", 270.0);
        icoMurAngle1 = chargerIcone("Images/murAngle.png", 0.0);
        icoMurAngle2 = chargerIcone("Images/murAngle.png", 90.0);
        icoMurAngle3 = chargerIcone("Images/murAngle.png", 180.0);
        icoMurAngle4 = chargerIcone("Images/murAngle.png", 270.0);
    }

    private ImageIcon chargerIcone(String urlIcone, Double rotation) {

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurPacMan.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new ImageIcon(rotate(image,rotation));
    }

    private void placerLesComposantsGraphiques(){
        // Mise en place d'une icône
        ImageIcon icon = new ImageIcon("Images/icone.png");
        setIconImage(icon.getImage());
        Color color = new Color(255,255,255,255);   
        // Mise en place de la bande son
        try {
            String soundName = "Sounds/musiqueFond.wav";    
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch(IOException | LineUnavailableException |UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        }    
        // Paramètre de la fenêtre de jeu
        setTitle("PacMan");
        setSize(sizeX*40, sizeY*40);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre
        /*
        // Fenêtre de la page d'accueil
        JFrame fenetreTitre = new JFrame();
        fenetreTitre.setTitle("PacMan");
        fenetreTitre.setSize(sizeX*40, sizeY*40);
        fenetreTitre.setLocationRelativeTo(null);
        fenetreTitre.setResizable(false);
        fenetreTitre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre
        fenetreTitre.setBackground(color);
        
        // Fenêtre des règles du jeu
        JFrame fenetreRegle = new JFrame();
        fenetreRegle.setTitle("PacMan");
        fenetreRegle.setSize(sizeX*40, sizeY*40);
        fenetreRegle.setLocationRelativeTo(null);
        fenetreRegle.setResizable(false);
        fenetreRegle.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre
        
        // Fenêtre du jeu
        JFrame Fjeu = new JFrame();
        Fjeu.setTitle("PacMan");
        Fjeu.setSize(sizeX*40, sizeY*40);
        Fjeu.setLocationRelativeTo(null);
        Fjeu.setResizable(false);
        Fjeu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre
        */
        
        JComponent grilleJLabels = new JPanel(new GridLayout(sizeX, sizeY)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille
        grilleJLabels.setBackground(color);
        tabJLabel = new JLabel[sizeX][sizeY];
        

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

                JLabel jlab = new JLabel();
                tabJLabel[y][x] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
                
            }

        }
        add(grilleJLabels);
    }
    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {
        boolean pacmanPresent = false;
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if(jeu.getGrilleMurs()[x][y] != null){
                    if (null != jeu.getGrilleMurs()[x][y].getType())switch (jeu.getGrilleMurs()[x][y].getType()) {
                        case "droit":
                            if (jeu.getGrilleMurs()[x][y].getRotationType() == 1) tabJLabel[x][y].setIcon(icoMurDroit1);
                            else if (jeu.getGrilleMurs()[x][y].getRotationType() == 2) tabJLabel[x][y].setIcon(icoMurDroit2);
                            break;
                        case "angle":
                        switch (jeu.getGrilleMurs()[x][y].getRotationType()) {
                            case 1:
                                tabJLabel[x][y].setIcon(icoMurAngle1);
                                break;
                            case 2:
                                tabJLabel[x][y].setIcon(icoMurAngle2);
                                break;
                            case 3:
                                tabJLabel[x][y].setIcon(icoMurAngle3);
                                break;
                            case 4:
                                tabJLabel[x][y].setIcon(icoMurAngle4);
                                break;
                            default:
                                break;
                        }
                            break;

                        case "fin":
                        switch (jeu.getGrilleMurs()[x][y].getRotationType()) {
                            case 1:
                                tabJLabel[x][y].setIcon(icoMurFin1);
                                break;
                            case 2:
                                tabJLabel[x][y].setIcon(icoMurFin2);
                                break;
                            case 3:
                                tabJLabel[x][y].setIcon(icoMurFin3);
                                break;
                            case 4:
                                tabJLabel[x][y].setIcon(icoMurFin4);
                                break;
                            default:
                                break;
                        }
                            break;

                        case "cote":
                        switch (jeu.getGrilleMurs()[x][y].getRotationType()) {
                            case 1:
                                tabJLabel[x][y].setIcon(icoMurCote1);
                                break;
                            case 2:
                                tabJLabel[x][y].setIcon(icoMurCote2);
                                break;
                            case 3:
                                tabJLabel[x][y].setIcon(icoMurCote3);
                                break;
                            case 4:
                                tabJLabel[x][y].setIcon(icoMurCote4);
                                break;
                            default:
                                break;
                        }
                            break;

                        case "mur": 
                            if (jeu.getGrilleMurs()[x][y].getRotationType() == 1) tabJLabel[x][y].setIcon(icoMur);
                            break;
                        default:
                            break;

                    
                     }
                }
                    else if (jeu.getGrille()[x][y] instanceof Pacman) { // si la grille du modèle contient un Pacman, on associe l'icône Pacman du côté de la vue 
                        pacmanPresent = true;
                        if ("grande".equals(jeu.getGrillePastilles()[x][y].getType()) && !jeu.getGrillePastilles()[x][y].getEstMange())jeu.getPacman().setBooste(true);
                        jeu.getGrillePastilles()[x][y].mangerPastille();
                        if(null != jeu.getPacman().getDirection())switch (jeu.getPacman().getDirection()) {
                            case droite:
                                tabJLabel[x][y].setIcon(icoPacman1);
                                break;
                            case bas:
                                tabJLabel[x][y].setIcon(icoPacman2);
                                break;
                            case gauche:
                                tabJLabel[x][y].setIcon(icoPacman3);
                                break;
                            case haut:
                                tabJLabel[x][y].setIcon(icoPacman4);
                                break;
                            default:
                                break;
                        } 
                    }
                    else if (jeu.getGrille()[x][y] instanceof Fantome) {
                        Fantome f = (Fantome) jeu.getGrille()[x][y];
                        Pacman p = (Pacman)jeu.getPacman();
                        if (null != f.getColor()){ 
                            if (f.getMort())tabJLabel[x][y].setIcon(icoDead);
                            else if (p.getBoostee())tabJLabel[x][y].setIcon(icoEatable);
                            else switch (f.getColor()) {
                                case "bleu":
                                    tabJLabel[x][y].setIcon(icoBleuB);
                                    break;
                                case "rose":
                                    tabJLabel[x][y].setIcon(icoRoseB);
                                    break;
                                case "rouge":
                                    tabJLabel[x][y].setIcon(icoRougeB);
                                    break;
                                case "orange":
                                    tabJLabel[x][y].setIcon(icoOrangeB);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                else if (jeu.getGrillePastilles()[x][y] instanceof Pastille && !jeu.getGrillePastilles()[x][y].getEstMange()) { // si la grille du modèle contient un Pacman, on associe l'icône Pacman du côté de la vue     
                    if("petite".equals(jeu.getGrillePastilles()[x][y].getType()))tabJLabel[x][y].setIcon(icoPastilleS);
                    else if("grande".equals(jeu.getGrillePastilles()[x][y].getType()))tabJLabel[x][y].setIcon(icoPastilleL);
                }
                else{ // si la grille du modèle contient ni Pacman, ni murs, on associe l'icône Pastille du côté de la vue     
                tabJLabel[x][y].setIcon(icoCouloir); 
                }
                if(jeu.TIME >= 30) {
                    jeu.getPacman().setBooste(false);
                    jeu.TIME = 0;
                }
            }
        }
        if (!pacmanPresent) jeu.getPacman().setMort(true);   
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
/*
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                });*/ 
        }

}
