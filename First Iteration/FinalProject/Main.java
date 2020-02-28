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
import java.util.Random;//
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;//
import java.util.logging.Level;
import java.util.logging.Logger;

import java.lang.*;
import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

///////////////////////////////////////////////////////////////////////////////////////
//
//      Created by Thomas Braun with assistance from Paul Jackson
//      2019
//
//
//
//
///////////////////////////////////////////////////////////////////////////////////////


public class Main extends JPanel implements KeyListener, MouseListener, ActionListener {
    public static final int WIDTH = 610;
    public static final int HEIGHT = 900;
    public static final int FPS = 60;
    private BufferedImage im;
    private BufferedImage imb;
    private BufferedImage ball;
    private BufferedImage bar;
    private BufferedImage splash;
    public static Rack user;
    public static Rack comp;
    int speed;
    int barX;
    boolean move;
    public static JButton test;
    boolean testb;
    long startTime;
    double ballx;
    double bally;
    boolean sp;
    
    
    public Main() {
        addMouseListener(this);
        addKeyListener(this);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        barX = 305;
        startTime = System.currentTimeMillis();

        /* Code for a button that would start the game
        test = new JButton("Ready to play");
        test.addActionListener(this);
        add(test);
        test.setAlignmentX(10f);
        test.setAlignmentY(700);
        */
        move = true;
        sp = false;
    }
    
    public void run() {
        while(true) {
            ballx-=1*.25;
            bally-=7*.25;
            spee();
            repaint();
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
        super.paintComponent(g);   
        Graphics2D g2 = (Graphics2D) g; 	
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.drawString("Welcome to CupPong. Designed and created by Paul Jackson and Thomas Braun.", 50, 25);
        g.drawString("Click on the white top of the cup you would like to shoot at", 50, 37);
        g.drawString("Pick a number within the given range to shoot.", 50, 49);
        g.drawString("When you have an appropriate amount of cups, click the 'R' key to rerack", 50, 61);
        try { 
            im = ImageIO.read(new File("redcup.png"));
            imb = ImageIO.read(new File("bluecup.png"));
            bar = ImageIO.read(new File("bar.png"));
            ball = ImageIO.read(new File("ball.png"));
            splash = ImageIO.read(new File("splash.png"));
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
        for (int i = 9; i >=0 ; i--) {
            if(user.rac[i].alive){
                int[] a = locationUSER(user.rac[i].location);
                g.drawImage(im, a[0]+140, a[1], 55, 72, this);
            } else{}
        }
        for (int j = 0; j < 10; j++) {
            if(comp.rac[j].alive){
                int[] b = locationCOMP(comp.rac[j].location);
                g.drawImage(imb, b[0]+140, b[1], 55, 72, this);
            } else{}
        }
        if(!testb) {
            //g.drawImage(splash, 0, 0,  this);
        }
        if(testb) { 
         //   remove(test);
        }
        g.drawImage(ball, 285+(int)(ballx), 620+(int)(bally), 25, 25, this);
    }

//////////////////////////////////////////////KEYLISTENER METHODS///////////////////////
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        System.out.println("You pressed down: " + c);
        select(e);
    }
    public void keyReleased(KeyEvent e) {
        char c=e.getKeyChar();
        System.out.println("You let go of: " + c);
    }
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        System.out.println("You typed: " + c);
    }
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }   

//////////////////////////////////////////////MOUSELISTENER METHODS/////////////////////////
    public void mousePressed(MouseEvent e) {
        System.out.println("pressed: x = "+ e.getX() +"and Y = " +e.getY());
        cupPressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("released");
    }
    
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {
        //Point p1 = e.getPoint();
        System.out.println("clicked: x = "+ e.getX() +"and Y = " +e.getY());
    }
//////////////////////////////////////////REGISTERS CUP PRESSES//////////////////////////////////
    public static int cupPressed(MouseEvent e) {
        System.out.println("Cup selected");
        return 0;
    }
//////////////////////////////////////////RETURNS THE LOCATION FOR PRINTING CUPS/////////////////
    public static int[] locationUSER(int l) {
            int[] a = new int[2]; 
            if (l == 1){
                a[0] = 131;
                a[1] = 166;//316
            }

            if (l == 2){
                a[0] = 159+3;
                a[1] = 144;//244
            }
            if (l == 3){
                a[0] = 104-3;
                a[1] = 144;//244
            }
            if (l == 4){
                a[0] = 77-5;
                a[1] = 122;//172
            }
            if (l == 5){
                a[0] = 132;
                a[1] = 122;//172
            }
            if (l == 6){
                a[0] = 187+5;
                a[1] = 122;//172
            }
            if (l == 7){
                a[0] = 50-8; //50
                a[1] = 100;//100
            }
            if (l == 8){
                a[0] = 105-3; //105
                a[1] = 100;
            }
            if (l == 9){
                a[0] = 160+3; //160
                a[1] = 100;
            }
            if (l == 10){
                a[0] = 215+8;
                a[1] = 100;
            }
            return a;
    }
    public static int[] locationCOMP(int l) {
            int[] a = new int[2];
            if (l == 1){
                a[0] = 131;
                a[1] = 466;
            }
            if (l == 2){
                a[0] = 159+3;
                a[1] = 538-50;
            }
            if (l == 3){
                a[0] = 104-3;
                a[1] = 538-50;
            }
            if (l == 4){
                a[0] = 77-5;
                a[1] = 610-100;
            }
            if (l == 5){
                a[0] = 132;
                a[1] = 610-100;
            }
            if (l == 6){
                a[0] = 187+5;
                a[1] = 610-100;
            }
            if (l == 7){
                a[0] = 50-8;
                a[1] = 682-150;
            }
            if (l == 8){
                a[0] = 105-3;
                a[1] = 682-150;
            }
            if (l == 9){
                a[0] = 160+3;
                a[1] = 682-150;
            }
            if (l == 10){
                a[0] = 215+8;
                a[1] = 682-150;
            }
            return a;
        }
    public static int[] locationRack1 (int l) {
        int[] a = new int[10];
        return a;
    }
//////////////////////////////////////////STOPS THE BAR FROM MOVING//////////////////////////////
    //public void stop(){
        //move = false;
    //}

    public int speedSelect() {  //Provides the speed at which the bar moves for the corresponding level
        int a = 100;
        speed = user.cupCount;
        if(speed == 1){a = 2;}
        if(speed == 2){a = 3;}
        if(speed == 3){a = 4;}
        if(speed == 4){a = 5;}
        if(speed == 5){a = 6;}
        if(speed == 6){a = 7;}
        if(speed == 7){a = 8;}
        if(speed == 8){a = 9;}
        if(speed == 9){a = 10;}
        if(speed == 0){a = 1;}
        return a;
    }      
    public void spee() {
        int a = speedSelect();
        boolean up = true;
        while(sp) {
            if(barX > 490) {
                //a = -a;
                barX = 488;
                up = false;
                //System.out.println(a); 
            }
            if (barX < 120) {
                //a = Math.abs(a);
                up = true;
                barX = 122;
                //System.out.println(a);
            }
            if(up) {
                barX += a;
            }
            if(!up) {
                barX -= a;
            }
        }
    }


    /////////////////////////////////////picking cups
    public void select(KeyEvent e){
        char c = e.getKeyChar();
        if (c == ' '){
            System.out.println("spaced");
        }
        if (c == 'c'){
            sp = true;
            System.out.println("speed ready");
        }
        if(move==true){
            if (c == '0') {
                System.out.println("You have selected the first cup");            
            }
            if (c == '1') {
                System.out.println("You have selected the first cup in the second row");            
            }
            if (c == '2') {
                System.out.println("You have selected the second cup in the second row");           
            }
            if (c == '3') {
                System.out.println("You have selected the first cup in the third row");
            }
            if (c == '4') {
                System.out.println("You have selected the second cup in the third row"); 
            }
            if (c == '5') {
                System.out.println("You have selected the third cup in the third row");
            }
            if (c == '6') {
                System.out.println("You have selected the first cup in the fourth row");           
            }
            if (c == '7') {
                System.out.println("You have selected the second cup in the fourth row");   
            }
            if (c == '8') {
                System.out.println("You have selected the third cup in the fourth row");         
            }
            if (c == '9') {
                System.out.println("You have selected the third cup in the fourth row");          
            }
        }
    }
    
    public void actionPerformed (ActionEvent e) {
        //System.out.println("test");
        testb = true;
        move = true;
    }

//public void bartest (int bt){

   // if (bt<=685 && bt>=305){
     //   bally[b].shotin();
    //}
    //if (bt>=685 && bt<=305){
       // bally[b].shotout();
    //}
}



//////////////////////////////////////////RERACKING////////////////////////////////////////

//public void rerack(KeyEvent e){
  //  return;
//}
///////////////////////////////////////////////////////////////////////////NEW CLASSES////////////////////////////////////////////////////////////


class Ball{ //Provides the location for the ball during the animation
    public double x;
    public double y;
    public static final int FPS = 60;
    public double time=1/FPS;

    public Ball(){
        x = 285.0;
        y = 650.0;
    }

    public void toss(char cup){
       if (cup==0){x=x-1;y=y-7;}
       if (cup==0){}
       if (cup==0){}
       if (cup==0){}
       if (cup==0){}
       if (cup==0){}
       if (cup==0){}  
   }   
}




