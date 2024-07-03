package com.nhathuy.gameandroid;

public class GameLoop implements Runnable{

    private Thread gameThread;
    private GamePanel gamePanel;
    public GameLoop(GamePanel gamePanel) {
        this.gamePanel=gamePanel;
        gameThread = new Thread(this);
    }

    @Override
    public void run() {
        //fps counter
        long lastFPScheck=System.currentTimeMillis();
        int fps=0;


        //sử lý thời gian lag (thời gian có thể lâu hơn bình thường)
        long lastDelta=System.nanoTime();
        long nanoSec=1_000_000_000;

        while(true){

            long nowDelta=System.nanoTime();
            double timeSinceLastDelta=nowDelta- lastDelta;

            double delta= timeSinceLastDelta /nanoSec;


            gamePanel.update(delta);
            gamePanel.render();

            lastDelta=nowDelta;
            fps++;

//            long now=System.currentTimeMillis();
//            if(now-lastFPScheck>=1000){
//                System.out.println("FPS: "+fps);
//                fps=0;
//                lastFPScheck+=1000;
//            }
        }
    }

    public void startGameLoop(){
        gameThread.start();
    }
}
