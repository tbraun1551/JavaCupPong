import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.util.Random;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.*;
import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;

//////////////////////////////////////////////////////////////////////////////////////////////////
/** 
		This is a game of Cup Pong						            
		May 3rd, 2019   																			
		Computer Science 112 Final Project														
		@author Thomas S. Braun -- tbraun22@amherst.edu 	    								
	    @author Paul Jackson   	-- pjackson22@amherst.edu										
**/
/////////////////////////////////////////////////////////////////////////////////////////////////


public class Main extends JPanel implements KeyListener, ActionListener {

    public static final int WIDTH = 610;
    public static final int HEIGHT = 900;
    public static final int FPS = 60;
    public static JButton test;
    private BufferedImage im;
    private BufferedImage imb;
    private BufferedImage ball;
    private BufferedImage bar;
    private BufferedImage splash;
    private BufferedImage comwin;
    private BufferedImage youwin;
    public static Rack user;
    public static Rack comp;
    int speed;
    int barX;
    int cup;
    long startTime;
    long endTime;
    boolean sp;
    boolean hit;
    boolean miss;
    boolean testb;
    boolean move;
    boolean right;
    boolean uTurn;
    boolean cupSelected;
    boolean alreadyP;
    boolean outBounds;
    boolean shot;
    public boolean shooting;
    boolean cHit;
    boolean cTurn;
    boolean cWin;
    boolean uWin;
    int sy;
    int sx;
    int a;
    int b;
    int compDelay;

    
    public Main() {
        addKeyListener(this);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        barX = 305;
        startTime = System.currentTimeMillis();
        
        test = new JButton("DON'T PRESS THIS");
        test.addActionListener(this);
        test.setAlignmentX(10f);
        test.setAlignmentY(700);
        alreadyP = false;
        move = true;
        sp = false;

        sx = 285;
        sy = 650;
        
        right = true;
        shot = false;
        compDelay = 120;
    }

    public void run() {
        while(true) {
            spee();
            if (shooting) {
                evolve();
            }
            compDelayer();
            computer();
            repaint();
            game();
            try {
                Thread.sleep(1000/FPS);
            }
            catch(InterruptedException e){} 
        }
    }

    public static void main(String[] args) {     
        user = new Rack();
        comp = new Rack();
        //Graphics Generator
        JFrame frame = new JFrame("CupPong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Main mainInstance = new Main();
        frame.setContentPane(mainInstance);
        frame.pack();
        frame.setVisible(true);
        mainInstance.run();

        Main abc = new Main();
    }
  
    public void paintComponent(Graphics g) {
        //Paints all the methods for the graphics
        super.paintComponent(g);   
        Graphics2D g2 = (Graphics2D) g; 	
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.drawString("Welcome to CupPong. Designed and created by Paul Jackson and Thomas Braun.", 50, 25);
        g.drawString("Click a number between 0-9 to select which cup you would like to shoot at", 50, 37);
        g.drawString("Pick a number within the given range to shoot.", 50, 49);
        try {
            im = ImageIO.read(new File("redcup.png"));
            imb = ImageIO.read(new File("bluecup.png"));
            bar = ImageIO.read(new File("bar.png"));
            ball = ImageIO.read(new File("ball.png"));
            splash = ImageIO.read(new File("splash.png"));
            comwin = ImageIO.read(new File("lose.png"));
            youwin = ImageIO.read(new File ("win.png"));
        } catch (IOException ex) {
            // handle exception...
            System.out.println("error");
        }

        //////////////////////////////////////BAR DRAWER////////////////////////
        g.drawImage(bar, 120, 670, 370, 30, this);
        
        g2.setStroke(new BasicStroke(2));
        g.setColor(Color.ORANGE);
        g.drawLine(300, 664, 305, 669);
        g.drawLine(305, 669, 310, 664);
        g.drawLine(300, 705, 305, 700);
        g.drawLine(305, 700, 310, 705);

        g2.setStroke(new BasicStroke(3));
        g.setColor(Color.magenta);
        g.drawLine(barX, 660, barX, 710);

        //////////////////////////////////////RACK DRAWER///////////////////////
        /*
        for(Cup t: user.rac) {
            System.out.println("testing this for loop");
        }
        */
        for (int i = 9; i >=0 ; i--) {
            if(user.rac[i].alive){
                ArrayList<Integer> a = locationUSER(user.rac[i].location);
                int x = a.get(0);
                int y = a.get(1);
                g.drawImage(im, x + 140, y, 55, 72, this);
            }
        }
        for (int j = 0; j < 10; j++){
            if(comp.rac[j].alive){
                ArrayList<Integer> a = locationCOMP(comp.rac[j].location);
                int x = a.get(0);
                int y = a.get(1);
                g.drawImage(imb, x + 140, y, 55, 72, this);
            }
        }
        ////////////////////////////////////MISCELLANEOUS DRAWERS///////////////
        if(!testb) {
            g.setColor(new Color(175, 192, 200));
            g.fillRect(0, 0, 700, 1000);
            g.drawImage(splash, 0, 0, this);
            uTurn = false;
        }
        if(testb) { 
            remove(test);
        }

        if(hit) {
            g.setColor(new Color(0, 245, 155));
            g.drawString("Congrats, you hit the cup! :-)", 120, 800);
        }
        if (uTurn) {
            g.setColor(Color.CYAN);
            g.drawString("It is currently your turn. Select what cup you would like to shoot at", 10, 275);
            g.drawString("by picking a number 0-9 then hit space to shoot.", 10, 287);
            g.drawString("The cups are ordered front to back left to right.", 10, 299);
        }
        if(cupSelected) {
            g.drawString("You have selected cup: " + cup, 10, 780);
        }
        if(outBounds) {
            g.drawString("The cup you have selected does not exist. Please try a number between 0 and 9", 10, 792);
        }
        if(alreadyP) {
            g.drawString("You have already hit this cup. Please try again", 10, 792);
        }
        if(shot && hit) {
            shooting = true;
            g.drawImage(ball, sx, sy, 25, 25, this);
            
        }   
        if (!uTurn) {
            g.drawString("Computers turn", 10, 275); 
        }
        if (cHit) {
            g.setColor(Color.RED);
            g.drawString("The computer hit their last cup", 10, 312);
        }

        if(cWin) {
            g.drawImage(comwin, 0, 0, this);
            g.setColor(Color.BLACK);
            g.drawString("It took you " + (endTime - startTime) + " to win.", 50, 700);

        }
        if(uWin) {
            g.drawImage(youwin, 0, 0, this);
        }
    }
    
//////////////////////////////////////////////KEYLISTENER METHODS///////////////////////////////
    public void keyPressed(KeyEvent e) {
        select(e);
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }   

//////////////////////////////////////////RETURNS THE LOCATION FOR PRINTING CUPS/////////////////
    public ArrayList<Integer> locationUSER(int l) {
            ArrayList<Integer> a = new ArrayList<Integer>(); 
            if (l == 1) {
                a.add(131);
                a.add(166);//316
            }
            if (l == 2) {
                a.add(159+3);
                a.add(144);//244
            }
            if (l == 3) {
                a.add(104-3);
                a.add(144);//244
            }
            if (l == 4) {
                a.add(77-5);
                a.add(122);//172
            }
            if (l == 5) {
                a.add(132);
                a.add(122);//172
            }
            if (l == 6) {
                a.add(187+5);
                a.add(122);//172
            }
            if (l == 7) {
                a.add(50-8); //50
                a.add(100);//100
            }
            if (l == 8) {
                a.add(105-3); //105
                a.add(100);
            }
            if (l == 9) {
                a.add(160+3); //160
                a.add(100);
            }
            if (l == 10) {
                a.add(215+8);
                a.add(100);
            }
            return a;
    }
    public ArrayList<Integer> locationCOMP(int l) {
        ArrayList<Integer> a = new ArrayList<Integer>();
            if (l == 1) {
                a.add(131);
                a.add(466);
            }
            if (l == 2) {
                a.add(159+3);
                a.add(538-50);
                
            }
            if (l == 3) {
                a.add(104-3);
                a.add(538-50);
            }
            if (l == 4) {
                a.add(77-5);
                a.add(610-100);
            }
            if (l == 5) {
                a.add(132);
                a.add(610-100);
            }
            if (l == 6){
                a.add(187+5);
                a.add(610-100);
            }
            if (l == 7) {
                a.add(50-8);
                a.add(682-150);
            }
            if (l == 8) {
                a.add(105-3);
                a.add(682-150);
            }
            if (l == 9) {
                a.add(160+3);
                a.add(682-150);
            }
            if (l == 10) {
                a.add(215+8);
                a.add(682-150);
            }
            return a;
        }
//////////////////////////////////////////STOPS THE BAR FROM MOVING//////////////////////////////
    public int speedSelect() {
        //Sets the speed and difficulty of the game. 
        int a = 0;
        speed = user.cupCount;
        if(speed == 1){a = 2;}
        if(speed == 2){a = 3;}
        if(speed == 3){a = 4;}
        if(speed == 4){a = 5;}
        if(speed == 5){a = 6;}
        if(speed == 6){a = 7;}
        if(speed == 7){a = 8;}
        if(speed == 8){a = 9;}
        if(speed == 9){a = 9;}
        if(speed == 0){a = 1;}
        return a;
    }      
    public void spee() {
        //Puts the bar in motion
        int a = speedSelect();
        if(sp) {
            if(barX > 490) {
                right = false;
                barX = 488;
            }
            if (barX < 120) {
                right = true;
                barX = 122;
            }
            if(right) {
                barX += a;
            }
            if(right == false) {
                barX = barX - a;
            }
        }
    }
/////////////////////////////////////////HANDLES KEYBOARD INPUTS////////////////////////////////
    public void select(KeyEvent e) {
        //Handles all the keyboard inputs made by the user
        char c = e.getKeyChar();
        if (c == ' ') {
            if(!testb){
                testb = true; uTurn = true;
            } else {
                spaced();
            }  
        }
        if(move && uTurn) {
            outBounds = false;
            if (c == '0') {
                cup = 0;
                if(user.rac[0].alive) {
                    sp = true;
                    alreadyP = false;
                }
                else{
                    alreadyP = true;
                }
                
            }
            if (c == '1') {
                cup = 1;
                if(user.rac[1].alive) {
                    sp = true;
                    alreadyP = false;
                }
                else {
                    alreadyP = true;
                }            
            }
            if (c == '2') {
                cup = 2;
                if(user.rac[2].alive) {
                    sp = true;
                    alreadyP = false;
                } else {
                    alreadyP = true;
                }            
            }
            if (c == '3') {
                cup = 3;
                if(user.rac[3].alive) {
                    sp = true;
                    alreadyP = false;
                } else {
                    alreadyP = true;
                }            
            }
            if (c == '4') {
                cup = 4;
                if(user.rac[4].alive) {
                    sp = true;
                    alreadyP = false;
                } else {
                    alreadyP = true;
                }            
            }
            if (c == '5') {
                cup = 5;
                if(user.rac[5].alive){
                    sp = true;
                    alreadyP = false;
                } else{
                    alreadyP = true;
                }            
            }
            if (c == '6') {
                cup = 6;
                if(user.rac[6].alive) {
                    sp = true;
                    alreadyP = false;
                } else{
                    alreadyP = true;
                    
                }            
            }
            if (c == '7') {
                cup = 7;
                if(user.rac[7].alive) {
                    sp = true;
                    alreadyP = false;
                } else {
                    alreadyP = true;
                }            
            }
            if (c == '8') {
                cup = 8;
                if(user.rac[8].alive) {
                    sp = true;
                    alreadyP = false;

                } else {
                    alreadyP = true;
                }            
            }
            if (c == '9') {
                cup = 9;
                if(user.rac[9].alive){
                    sp = true;
                    alreadyP = false;
                } else {
                    alreadyP = true;
                }
            }
            cupSelected = true;
        }
    }
    public void evolve() {
        //While this method does not function as it was initially supposed to as the balls move to fast. It properly ends the users turn and starts the computers. 
        if (sy < b) {
            shot = false;
            hit = false;
            shooting = false;
            uTurn = false;
            cTurn = true;
            user.makeDead(cup);
            barX = 305;
            cupSelected = false;
        }
        if (cup == 6) {
            a=209;
            b=105;
            sx = 209;
            sy -= sy*7*.25;
        }
        if (cup == 7) {
            a=271;
            b=105;
            sx -= sx*.25;
            sy -= sy*39*.25; 
        }
        if (cup == 8) {
            a=330;
            b=105;
            sx += sx*.25; 
            sy -= sy*12*.25;
        }
        if (cup == 9) {
            a=388;
            b=105;
            sx += sx*.25; 
            sy -= sy*12*.25;
        }
        if (cup == 3) {
            a=235;
            b=125;
            sx -= sx*.25; 
            sy -= sy*10.5*.25;
        }
        if (cup == 4) {
            a=300;
            b=125;
            sx += sx*.25; 
            sy -= sy*35*.25;
        }
        if (cup == 5){
            a=358;
            b=125;
            sx += sx*.25; 
            sy -= sy*7.2*.25;
        }
        if (cup == 2){
            a=330;
            b=150;
            sx += sx*.25; 
            sy -= sy*11.11*.25;
        }
        if (cup == 1){
            a=266;
            b=150;
            sx -= sx*.25; 
            sy -= sy*26.3*.25;
        }
        if (cup == 0){
            a=295;
            b=170;
            sx += sx +1;
            sy -= sy - 3;
        }
    }
    public void actionPerformed (ActionEvent e) {
        testb = true;
        move = true;
        setFocusable(false);
    }
    public void spaced(){
        //Lets the user stop the bar from moving
        if(sp){
            sp = false;
            barTest();
        }
    }
    public void barTest(){
        //Sees if the user made or missed their shot
        if (barX<=330 && barX>=277){
            hit = true;
            miss = false;
            shot = true;
        }
        else{
            miss = true;
            hit = false;
            shot = true;
            cTurn = true;
            uTurn = false;
            barX = 305;
            cupSelected = false;;;
        }
    }
/////////////////////////////////////////COMPUTER METHODS//////////////////////////////////////
    public void compDelayer(){
        //Sets a delay before the computer takes its 'shot'
        if(!cTurn){
            compDelay = 90;
        }
        if(cTurn){
            compDelay--;
        }
    }
    public void computer(){
        //takes the 'shot' for the computer
        if(compDelay <= 0){
            Random r = new Random();
            Random t = new Random();
            int n;
            int guess = 900;
            int c = comp.cupCount;
            if (cTurn){
                n = t.nextInt(2);
                if(c == 0){
                    guess = r.nextInt(2);
                }
                if(c == 1){
                    guess = r.nextInt(2);
                }
                if(c == 2){
                    guess = r.nextInt(2);
                }
                if(c == 3){
                    guess = r.nextInt(3);
                }
                if(c == 4){
                    guess = r.nextInt(3);
                }
                if(c == 5){
                    guess = r.nextInt(3);
                }
                if(c == 6){
                    guess = r.nextInt(3);
                }
                if(c == 7){
                    guess = r.nextInt(4);
                }
                if(c == 8){
                    guess = r.nextInt(4);
                }
                if(c == 9){
                    guess = r.nextInt(4);
                }
                if(guess == n){
                    cHit = true;
                    uTurn = true;
                    cTurn = false;
                    comp.makeDead(c);
                }
                if(guess != n){
                    cHit = false;
                    uTurn = true;
                    cTurn = false;
                }
            }
        }
    }
    public void game(){
        //Keeps track of whether either user or computer has won
        if(user.cupCount==10){
                uWin=true;
                endTime = System.currentTimeMillis();

        }
        if(comp.cupCount==10){
                cWin=true;
                endTime = System.currentTimeMillis();
        }  
    }
}