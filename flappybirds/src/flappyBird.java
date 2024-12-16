import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;




public class flappyBird extends JPanel implements ActionListener,KeyListener{
    int boardWidth = 360;
    int boardHight = 640;

    //image uploading ---->>
    Image backgroundImg;
    Image birdImg;
    Image topImg;
    Image bottomImg;

    //Birdsetup
    int birdX = boardWidth/8;
    int birdY = boardHight/2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int X = birdX;
        int Y = birdY;
        int Width = birdWidth;
        int Height = birdHeight;
        Image img;

        Bird (Image img)
        {
            this.img = img;
        }       
    } 

    // pipe setup--->
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipewidth = 64;
    int pipeheight = 512;

    class pipe {
        int x = pipeX;
        int y = pipeY;
        int Width = pipewidth;
        int Height = pipeheight;
        Image img;
        boolean passed = false;


        pipe(Image img)
        {
            this.img = img;
        }
    }
    //Game Logic--->

    Bird bird;
    Timer gameLoop;
    Timer placepipesTimer;
    boolean gameover = false;
    double score = 0;


    int velocityX =- 4;//move pipes to left side (simulate birds moving right)
    int velocityY = 0;//move up/down speed;
    int gravity = 1;


    ArrayList<pipe>pipes;
    Random random = new Random();   //For pipes

    flappyBird()
    {
        setPreferredSize(new Dimension(boardWidth,boardHight));
        setFocusable(true);
        addKeyListener(this);


         //image uploading ---->>
         backgroundImg = new ImageIcon(getClass().getResource("./bakground.jpg")).getImage();
         birdImg = new ImageIcon(getClass().getResource("./bird.jpg")).getImage();
         topImg = new ImageIcon(getClass().getResource("./top.jpg")).getImage();
         bottomImg= new ImageIcon(getClass().getResource("./bottom.jpg")).getImage();

        //bird-Game Logic....

         bird = new Bird(birdImg);
         pipes = new ArrayList<pipe>();


         // pipes Timer
         placepipesTimer = new Timer(1500,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                placepipes();
            }
         });
         placepipesTimer.start();


         //Game Timer....
         gameLoop = new Timer(1000/60, this);
         gameLoop.start();

    }
//*************For pipes**********/
    public void placepipes(){

        int randomPipeY =(int) (pipeY-pipeheight/4 - Math.random()*(pipeheight/2));
        int openingSpace = boardHight/4;



        pipe toppipe = new pipe(topImg);
        toppipe.y = randomPipeY;
        pipes.add(toppipe);

        pipe bottompipe = new pipe(bottomImg);
        bottompipe.y = toppipe.y + pipeheight + openingSpace;
        pipes.add(bottompipe);

    }

    //*******Making PAint-Draw component********/
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g)
    {
       
        //background -->
        
        g.drawImage(backgroundImg, 0,0, boardWidth,boardHight,null);

        //Bird
        g.drawImage(bird.img, bird.X, bird.Y,  bird.Width, bird.Height,null);  
        //pipes
        for (int i=0;i<pipes.size();i++)
        {
            pipe pipe = pipes.get(i);
            g.drawImage(pipe.img,pipe.x,pipe.y,pipe.Width,pipe.Height,null);

        }   
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameover)
        {
            g.drawString("Game Over:"+String.valueOf((int)score), 100, 100);
            
        }
        else{
            g.drawString(String.valueOf((int)score),10 ,32);
        }
        
        

    }
    //*****movement of the Elements********/
    public void move()
    {
        //*****Bird move******/
        velocityY += gravity;
        bird.Y += velocityY;
        bird.Y = Math.max(bird.Y, 0);
        
        //**********pipes move**** */
        for (int i=0;i<pipes.size();i++)
        {
            pipe pipe = pipes.get(i);
            pipe.x +=velocityX;

            if(!pipe.passed && bird.X > pipe.x + pipe.Width){
                pipe.passed = true;
                score += 0.5;
            }

            if (collision(bird, pipe)){
                gameover = true;
            }

        }   
        if(bird.Y > boardHight)
        {
            gameover = true;
        }
        
    }

    //*******  A bird Collide with Pipe
    public boolean collision(Bird a,pipe b)
    {
        return a.X <b.x + b.Width &&
        a.X + a.Width > b.x &&
        a.Y < b.y + b.Height &&
        a.Y + a.Height > b.y;
    }



//******************overrite on Action Listener************************* */

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameover)
        {
            placepipesTimer.stop();
            gameLoop.stop();
            
        }
      
    }


    //*******************overrite on KeyListener*********************
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        
            if (e.getKeyCode() == KeyEvent.VK_SPACE){
                velocityY = -9;
            }
            if(gameover)
            {
                bird.Y = birdY;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameover = false;
                gameLoop.start();
                placepipesTimer.start();

            }
        }

    @Override
    public void keyReleased(KeyEvent e) {}
    
}
