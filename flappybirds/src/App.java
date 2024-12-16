import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 360;
        int boardHight = 640;

        //frame setup

         JFrame frame =new JFrame("Flappy Bird");
        //    frame.setVisible(true);
           frame.setSize(boardWidth,boardHight);
           frame.setLocationRelativeTo(null); 
           frame.setResizable(false);
           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

           //background setup
           flappyBird flapbird = new flappyBird();
           frame.add(flapbird);
           frame.pack();
           flapbird.requestFocus();          
           frame.setVisible(true);


    }
}
