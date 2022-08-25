package pac;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Model extends JPanel implements ActionListener {

	protected Dimension d;
	protected final Font smallFont = new Font("Arial", Font.BOLD, 14); 
	protected boolean inGame = false;
	protected boolean dying = false;

	protected final int BLOCK_SIZE = 24;
	protected final int N_BLOCKS = 15;
	protected final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
	protected final int MAX_GHOSTS = 12;
	protected final int PACMAN_SPEED =6 ;

	protected int N_GHOSTS = 6;
	protected int lives, score;
	protected int[] dx, dy;
    protected int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;

    protected Image heart, ghost;
    protected Image up, down, left, right;

    protected int pacman_x, pacman_y, pacmand_x, pacmand_y;
    protected int req_dx, req_dy;

    protected int levelCounter = -1;    
    
    protected final short levelData[] = {
    		19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            25, 24, 24, 24, 28, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
            0,  0,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
            19, 18, 18, 18, 18, 18, 16, 16, 16, 16, 24, 24, 24, 24, 20,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
            17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
            17, 16, 16, 16, 24, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 18, 18, 18, 18, 20,
            17, 24, 24, 28, 0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 20,
            21, 0,  0,  0,  0,  0,  0,   0, 17, 16, 16, 16, 16, 16, 20,
            17, 18, 18, 22, 0, 19, 18, 18, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            25, 24, 24, 24, 26, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };
    
    protected final short level1Data[] = {
			19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
	        17, 16, 24, 24, 24, 16, 16, 16, 16, 16, 24, 24, 24, 16, 20,
	        17, 20,  0,  0,  0, 17, 16, 24, 16, 20,  0,  0,  0, 17, 20,
	        17, 20,  0,  0,  0, 17, 20,  0, 17, 20,  0,  0,  0, 17, 20,
	        17, 20,  0,  0,  0, 17, 20,  0, 17, 20,  0,  0,  0, 17, 20,
	        17, 16, 18, 18, 18, 16, 20,  0, 17, 16, 18, 18, 18, 16, 20,
	        17, 16, 16, 24, 24, 24, 28,  0, 24, 24, 24, 24, 16, 16, 20,
	        17, 16, 20,  0,  0,  0,  0,  0,  0,  0,  0,  0, 17, 16, 20,
	        17, 16, 16, 18, 18, 18, 22,  0, 19, 18, 18, 18, 16, 16, 20,
	        17, 16, 24, 24, 24, 16, 20,  0, 17, 16, 24, 24, 24, 16, 20,
	        17, 20,  0,  0,  0, 17, 20,  0, 17, 20,  0,  0,  0, 17, 20,
	        17, 20,  0,  0,  0, 17, 20,  0, 17, 20,  0,  0,  0, 17, 20,
	        17, 20,  0,  0,  0, 17, 16, 18, 16, 20,  0,  0,  0, 17, 20,
	        17, 16, 18, 18, 18, 16, 16, 16, 16, 16, 18, 18, 18, 16, 20,
	        25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28	
		
	};
	
	protected final short level2Data[] = {
			19, 26, 26, 26, 18, 18, 26, 18, 26, 18, 18, 26, 26, 26, 22,
	    	21,  0,  0,  0, 17, 20,  0, 21,  0, 17, 20,  0,  0,  0, 21,
	    	21,  0, 19, 18, 16, 20,  0, 21,  0, 17, 16, 18, 22,  0, 21,
	    	21,  0, 25, 24, 24, 20,  0, 21,  0, 17, 24, 16, 20,  0, 21,
	    	21,  0,  0,  0,  0, 17, 18, 16, 18, 20,  0, 17, 20,  0, 21,
	    	17, 18, 18, 22,  0, 17, 24, 24, 24, 20,  0, 17, 16, 18, 20,
	    	17, 16, 16, 20,  0, 21,  0,  0,  0, 21,  0, 17, 16, 16, 20,
	    	17, 16, 16, 20,  0, 21,  0,  0,  0, 21,  0, 17, 16, 16, 20,
	    	17, 16, 16, 20,  0, 21,  0,  0,  0, 21,  0, 17, 16, 16, 20,
	    	17, 24, 16, 20,  0, 17, 18, 18, 18, 20,  0, 25, 24, 24, 20,
	    	21,  0, 17, 20,  0, 17, 16, 16, 16, 20,  0,  0,  0,  0, 21,
	    	21,  0, 17, 16, 18, 16, 24, 16, 24, 16, 18, 18, 22,  0, 21,
	    	21,  0, 25, 24, 16, 20,  0, 21,  0, 17, 16, 24, 28,  0, 21,
	    	21,  0,  0,  0, 17, 20,  0, 21,  0, 17, 20,  0,  0,  0, 21,
	    	25, 26, 26, 26, 24, 24, 26, 24, 26, 24, 24, 26, 26, 26, 28
		
	};
	
	protected final short level3Data[] = {
			19, 22,  0, 19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
		    17, 20,  0, 17, 16, 16, 16, 16, 24, 24, 16, 16, 24, 24, 20,
		    17, 20,  0, 17, 16, 16, 16, 20,  0,  0, 17, 28,  0,  0, 21,
		    17, 20,  0, 17, 16, 16, 16, 20,  0,  0, 21,  0,  0,  0, 21,
		    17, 16, 18, 16, 16, 24, 16, 16, 18, 18, 16, 18, 18, 18, 20,
		    17, 16, 16, 16, 20,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
		    17, 16, 24, 24, 28,  0, 25, 16, 16, 16, 16, 16, 16, 16, 20,
		    17, 20,  0,  0,  0,  0,  0, 17, 16, 16, 24, 16, 16, 16, 20,
		    17, 16, 18, 18, 22,  0, 19, 16, 16, 20,  0, 17, 16, 16, 20,
		    17, 16, 16, 16, 20,  0, 17, 16, 16, 20,  0, 17, 16, 16, 20,
		    17, 16, 16, 16, 20,  0, 17, 16, 16, 20,  0, 25, 24, 24, 20,
		    17, 24, 24, 16, 28,  0, 17, 16, 16, 20,  0,  0,  0,  0, 21,
		    21,  0,  0, 21,  0,  0, 17, 16, 16, 20,  0, 19, 18, 18, 20,
		    17, 18, 18, 16, 18, 18, 16, 16, 16, 20,  0, 17, 16, 16, 20,
		    25, 24, 24, 24, 24, 24, 24, 24, 24, 24, 26, 24, 24, 24, 28
		
	};
	
	protected final short level4Data[] = {
			19, 26, 18, 18, 18, 26, 18, 18, 18, 18, 26, 26, 26, 26, 22,
		    21,  0, 17, 16, 20,  0, 17, 24, 24, 20,  0,  0,  0,  0, 21,
		    21,  0, 25, 24, 16, 26, 20,  0,  0, 17, 18, 18, 22,  0, 21,
		    21,  0,  0,  0, 21,  0, 21,  0, 27, 24, 24, 24, 20,  0, 21,
		    17, 18, 18, 18, 20,  0, 21,  0,  0,  0,  0,  0, 21,  0, 21,
		    17, 24, 24, 24, 24, 26, 16, 26, 26, 26, 22,  0, 21,  0, 21,
		    21,  0,  0,  0,  0,  0, 21,  0,  0,  0, 21,  0, 21,  0, 21,
		    17, 26, 18, 18, 18, 18, 20,  0,  0, 19, 16, 18, 20,  0, 21,
		    21,  0, 25, 16, 16, 16, 20,  0,  0, 17, 16, 16, 16, 18, 20,
		    17, 22,  0, 25, 16, 16, 24, 26, 18, 16, 24, 24, 24, 24, 20,
		    17, 16, 22,  0, 25, 20,  0,  0, 17, 20,  0,  0,  0,  0, 21,
		    17, 24, 16, 26,  0, 21,  0,  0, 17, 20,  0,  0,  0,  0, 21,
		    21,  0, 29,  0, 19, 20,  0,  0, 25, 24, 26, 18, 26, 26, 20,
		    17, 22,  0, 19, 16, 20,  0,  0,  0,  0,  0, 21,  0,  0, 21,
		    25, 24, 26, 24, 24, 24, 26, 26, 26, 26, 26, 24, 26, 26, 28
		
	};

    protected final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    protected final int maxSpeed = 6;

    protected int currentSpeed = 3;
    protected short[] screenData;
    protected Timer timer;

    public Model() {// Constructor of the class that start everything

        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();
    }
    
    
    private void loadImages() {//This uses the absolute path in the PC, for other pc's it needs change
    	up = new ImageIcon("C:\\Users\\togag\\eclipse-workspace\\PacTest\\src\\Images\\up.gif").getImage();
		down = new ImageIcon("C:\\Users\\togag\\eclipse-workspace\\PacTest\\src\\Images\\down.gif").getImage();
		left= new ImageIcon("C:\\Users\\togag\\eclipse-workspace\\PacTest\\src\\Images\\left.gif").getImage();
		right = new ImageIcon("C:\\Users\\togag\\eclipse-workspace\\PacTest\\src\\Images\\right.gif").getImage();
		ghost = new ImageIcon("C:\\Users\\togag\\eclipse-workspace\\PacTest\\src\\Images\\ghost.gif").getImage();
		heart = new ImageIcon("C:\\Users\\togag\\eclipse-workspace\\PacTest\\src\\Images\\heart.png").getImage();
		
    }
       private void initVariables() {// initializes all the variables

        screenData = new short[N_BLOCKS * N_BLOCKS];
        d = new Dimension(400, 400);
        ghost_x = new int[MAX_GHOSTS];
        ghost_dx = new int[MAX_GHOSTS];
        ghost_y = new int[MAX_GHOSTS];
        ghost_dy = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        dx = new int[4];
        dy = new int[4];
        
       // timer = new Timer(40, this);
        //timer.start();
    }
       
       private void HardnessInit() {// this method uses the tick rate of the timer to make the game faster so it will be harder to play
    	   switch(levelCounter) {
   		
   		case 1:
   		 timer = new Timer(1000, this);
         timer.start();
   			break;
   			
   		case 2:
   		 timer = new Timer(50, this);
         timer.start();
   			break;
   			
   		case 3:
   		 timer = new Timer(30, this);
         timer.start();
   			break;	
   			
   		case 4:
   		 timer = new Timer(20, this);
         timer.start();
   			break;	
   		case 5:
   		 timer = new Timer(60, this);
         timer.start();
  			break;
  			
   		case 6:
   		 timer = new Timer(40, this);
         timer.start();
  			break;
  			
   		case 7:
   		 timer = new Timer(30, this);
         timer.start();
  			break;	
  			
   		case 8:
   		 timer = new Timer(20, this);
         timer.start();
  			break;	
   		
   		default:	
   		 timer = new Timer(40, this);
         timer.start();
   			
   			break;
   		} 
    	   
    	   
       }
       

    private void playGame(Graphics2D g2d) {// checks if pac is dead otherwise runs the game

        if (dying) {

            death();

        } else {
        	
            movePacman();
            drawPacman(g2d);
            moveGhosts(g2d);
            checkMaze();
        }
    }

    private void showIntroScreen(Graphics2D g2d) {//shows the intro screen 
 
    	String start = "Press ENTER to start";
    	String ability = "Press Q to use TimeStop";
    	String quit = "Press X to Quit";
        g2d.setColor(Color.YELLOW);
        g2d.drawString(start, (SCREEN_SIZE)/4, 150);
        g2d.drawString(ability, (SCREEN_SIZE)/4, 170);
        g2d.drawString(quit, (SCREEN_SIZE)/4, 195);
    }
    private void showEndScreen(Graphics2D g2d) {// shows the end screen
    	 
    	String end = "Enough games played,now let some one else play!";
        g2d.setColor(Color.yellow);
        g2d.drawString(end, (SCREEN_SIZE)/40, 150);
    }

    private void drawScore(Graphics2D g) {// draws the score part and the hearts
        g.setFont(smallFont);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);

        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }

    private void checkMaze() {// cheks if any point left if all eaten restarts

        int i = 0;
        boolean finished = true;

        while (i < N_BLOCKS * N_BLOCKS && finished) {// check if any point left

            if ((screenData[i]) != 0) {
                finished = false;
            }

            i++;
        }

        if (finished) {// if all points are eaten we move to the next level

            score += 50;

            if (N_GHOSTS < MAX_GHOSTS) {
                N_GHOSTS++;
            }

            if (currentSpeed < maxSpeed) {
                currentSpeed++;
            }

            initLevel();
        }
    }

    private void death() {// checks if pac is dead

    	lives--;

        if (lives == 0) {
            inGame = false;
        }

        continueLevel();
    }

    private void moveGhosts(Graphics2D g2d) {// the method that moves the ghosts

        int pos;
        int count;

        for (int i = 0; i < N_GHOSTS; i++) {
        	//this chooses which square to move to next but after finishing moving in that square
            if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) {
                pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE);

                count = 0;
                
                /*
                 * these use the border numbers ( left-1 right-4 top-2 down-8 ) to decide and set the direction of the movement
                 * the number chosen above is used under to move ghost in one direction
                 */               
                
                if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }
                
              //this determines the position of the ghosts and checks if no obstacle and no movement to one side then it will move to the other
             // in basic word it bounces the ghosts from the walls               

                if (count == 0) {

                    if ((screenData[pos] & 15) == 15) {
                        ghost_dx[i] = 0;
                        ghost_dy[i] = 0;
                    } else {
                        ghost_dx[i] = -ghost_dx[i];
                        ghost_dy[i] = -ghost_dy[i];
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    ghost_dx[i] = dx[count];
                    ghost_dy[i] = dy[count];
                }

            }

            ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
            ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);
            drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1);

          //this line is used for checking if pacman touches a ghost or not if touches than pac loses a heart
            if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12)
                    && pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12)
                    && inGame) {

                dying = true;
            }
        }
    }

    private void drawGhost(Graphics2D g2d, int x, int y) {//draws the ghost
    	g2d.drawImage(ghost, x, y, this);
        }

    private void movePacman() {// the method that moves the pacman

        int pos;
        short ch;

        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {//gets the position
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);
            ch = screenData[pos];

            if ((ch & 16) != 0) {// checks if array 16 equals the pos then if yes increases the score
                screenData[pos] = (short) (ch & 15);
                score++;
            }

            if (req_dx != 0 || req_dy != 0) {//checks if there is borders on the way if yes pac cant move
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                }
            }

            // Check for standstill
            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }
        } 
        pacman_x = pacman_x + PACMAN_SPEED * pacmand_x;
        pacman_y = pacman_y + PACMAN_SPEED * pacmand_y;
    }

    private void drawPacman(Graphics2D g2d) {//draws the image of the pac according to the direction of pac

        if (req_dx == -1) {
        	g2d.drawImage(left, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dx == 1) {
        	g2d.drawImage(right, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dy == -1) {
        	g2d.drawImage(up, pacman_x + 1, pacman_y + 1, this);
        } else {
        	g2d.drawImage(down, pacman_x + 1, pacman_y + 1, this);
        }
    }

    private void drawMaze(Graphics2D g2d) {// draws the maze 

        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {// draws the x and y axis
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

                g2d.setColor(new Color(0,72,251));
                g2d.setStroke(new BasicStroke(5));// thickness
                
                
                
                switch(levelCounter) {// picks the color for the levels and the right level design
        		
        		case 1:
         			
        			 if ((level1Data[i] == 0)) { 
                     	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
        			 }
        			break;
        			
        		case 2:
        			 g2d.setColor(new Color(0,204,0));//Green
                     g2d.setStroke(new BasicStroke(5));
                     
        			if ((level2Data[i] == 0)) { 
                     	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
        			 }
        			break;
        			
        		case 3:
        			
        			 g2d.setColor(new Color(102,0,153));//Purple
                     g2d.setStroke(new BasicStroke(5));
        			
        			if ((level3Data[i] == 0)) { 
                     	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
        			 }
        			break;	
        			
        		case 4:
        			
        			g2d.setColor(new Color(204,0,0));//Red
                    g2d.setStroke(new BasicStroke(5));
        			
        			if ((level4Data[i] == 0)) { 
                     	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
        			 }
        			break;	
        		case 5:
        			
        			 g2d.setColor(new Color(0,72,251));//Blue
                     g2d.setStroke(new BasicStroke(5));
                     
       			 if ((level1Data[i] == 0)) { 
                    	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
       			 }
       			break;
       			
        		case 6:
        			
        			g2d.setColor(new Color(0,204,0));//Green
                    g2d.setStroke(new BasicStroke(5));
        			
       			if ((level2Data[i] == 0)) { 
                    	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
       			 }
       			break;
       			
        		case 7:
        			
        			 g2d.setColor(new Color(102,0,153));//Purple
                     g2d.setStroke(new BasicStroke(5));
        			
       			if ((level3Data[i] == 0)) { 
                    	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
       			 }
       			break;	
       			
        		case 8:
        			
        			g2d.setColor(new Color(204,0,0));//Red
                    g2d.setStroke(new BasicStroke(5));
        			
       			if ((level4Data[i] == 0)) { 
                    	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
       			 }
       			break;	
        		
        		default:	
        			
        			g2d.setColor(new Color(0,72,251));//Blue
                    g2d.setStroke(new BasicStroke(5));
        			if ((levelData[i] == 0)) { 
                     	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
        			 }
        			break;
        		} 

                if ((screenData[i] & 1) != 0) { 
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 2) != 0) { 
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }

                if ((screenData[i] & 4) != 0) { 
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 8) != 0) { 
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 16) != 0) { 
                    g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x + 10, y + 10, 6, 6);
               }

                i++;
            }
        }
    }

    private void initGame() {// initializes the game sets the data

    	lives = 3;
        score = 0;
        HardnessInit();
        initLevel();
        N_GHOSTS = 6;
        currentSpeed = 3;
        
    }
    private void EndGameStarter() { // freezes the game while calling the EndGame method 
    	if(levelCounter > 8 ) {
    		try {
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
    		EndGame();
    	}
    }
    
    private void EndGame() {// Ends the game 
    	 try {
				Thread.currentThread();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
          System.exit(0);
    }

    private void initLevel() {// initializes the levels according to the counter which select which dedicated level design will me moved into screenData
    	levelCounter++;
        int i;
switch(levelCounter) {
		
		case 1:
			for(i = 0; i<(N_BLOCKS*N_BLOCKS); i++) {
				screenData[i] = level1Data[i];
			}
			break;
			
		case 2:
			for(i = 0; i<(N_BLOCKS*N_BLOCKS); i++) {
				screenData[i] = level2Data[i];
			}
			break;
			
		case 3:
			for(i = 0; i<(N_BLOCKS*N_BLOCKS); i++) {
				screenData[i] = level3Data[i];
			}
			break;	
			
		case 4:
			for(i = 0; i<(N_BLOCKS*N_BLOCKS); i++) {
				screenData[i] = level4Data[i];
			}
			break;	
		case 5:
			for(i = 0; i<(N_BLOCKS*N_BLOCKS); i++) {
				screenData[i] = level1Data[i];
			}
			break;
			
		case 6:
			for(i = 0; i<(N_BLOCKS*N_BLOCKS); i++) {
				screenData[i] = level2Data[i];
			}
			break;
			
		case 7:
			for(i = 0; i<(N_BLOCKS*N_BLOCKS); i++) {
				screenData[i] = level3Data[i];
			}
			break;	
			
		case 8:
			for(i = 0; i<(N_BLOCKS*N_BLOCKS); i++) {
				screenData[i] = level4Data[i];
			}
			break;	
		
		default:	
			for(i = 0; i<(N_BLOCKS*N_BLOCKS); i++) {
				screenData[i] = levelData[i];
			}
			break;
		}
	//levelCounter = levelCounter+1;
        continueLevel();
        //levelCounter = levelCounter+1;
    }

    private void continueLevel() {//defines the position of the ghosts and assign random speed
    	if(levelCounter > 9) {
    		EndGameStarter();
    		
    	}else {

    	int dx = 1;
        int random;

        for (int i = 0; i < N_GHOSTS; i++) {

            ghost_y[i] = 4 * BLOCK_SIZE; //start position
            ghost_x[i] = 4 * BLOCK_SIZE;
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;//seperate run 
            random = (int) (Math.random() * (currentSpeed + 1));

            if (random > currentSpeed) {
                random = currentSpeed;
            }

            ghostSpeed[i] = validSpeeds[random];
        }

        pacman_x = 7 * BLOCK_SIZE;  //start position
        pacman_y = 11 * BLOCK_SIZE;
        pacmand_x = 0;	//reset direction move
        pacmand_y = 0;
        req_dx = 0;		// reset direction controls
        req_dy = 0;
        dying = false;
    	}
    }

 
    public void paintComponent(Graphics g) {// paints and draws all the components needed
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze(g2d);
        drawScore(g2d);

        if (inGame) {
            playGame(g2d);
        }else if (levelCounter > 8) {       	
        	showEndScreen(g2d); 
        	//EndGameStarter();
        } else {
            showIntroScreen(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    
    

    //controls
    class TAdapter extends KeyAdapter {// the class and method that uses KeyAdapter to take input from the keyboard and then move the pacman

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (inGame) {
                if (key == KeyEvent.VK_A) {//the button to go left
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_D) {//the button to go right
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_W) {//the button to go up
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_S) {//the button to go down
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {//Ends the level
                    inGame = false;
                }else if (key == KeyEvent.VK_Q) {//special ability
                	try {
        				Thread.currentThread();
        				Thread.sleep(1000);
        			} catch (InterruptedException x) {				
        				x.printStackTrace();
        			}
                } else if (key == KeyEvent.VK_X) {// the button of alt-F4 :D
                   System.exit(0);
                }
                
            } else {// the method that start the game with ENTER
                if (key == KeyEvent.VK_ENTER) {
                    inGame = true;
                    initGame();
                } else if (key == KeyEvent.VK_X) {// the button of alt-F4 :D
                    System.exit(0);
                 }
            }
        }
}

	
    @Override
    public void actionPerformed(ActionEvent e) {// repaints everything every time a key is pressed
        repaint();
    }
		
	}


