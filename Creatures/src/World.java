
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 94450
 */
public class World extends JPanel
{
    private ArrayList<Creature> creatures;
    private ArrayList<Creature> newCreatures;
    private ArrayList<Creature> removeCreatures;
    private ArrayList<Blob> blobs;
    private BufferedImage worldImage;
    private BufferedImage groundImage;
    
    private Timer timer;
    
    private final int width = 120;     
    private final int height = 80;    
    private final Color grassColor = new Color(10, 200, 50);    
    private final Color dirtColor = new Color(50, 20, 30);   
    private final Color blobColor = new Color(10, 20, 110);    
    private final Color lavaColor = new Color(150, 10, 15);    
    private final int startPopulation = 100;
    private final int maxPopulation = 100;
    private final int blobEnergy = 15;
    private final int grassEnergy = 5;
    private final int grassRegrowRate = 5;
    
    public World()
    {
        worldImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        groundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        generateWorld();
        timer = new Timer(10, new ActionListener(){ 
            public void actionPerformed(ActionEvent e){
                update();
            }
        });
        timer.start();
    }
    public int getWorldWidth()
    {
        return width;
    }
    public int getWorldHeight()
    {
        return width;
    }
    private void update()
    {
        for(Creature creature : creatures)
        {
            creature.update(this);
        }
        
        
        if(newCreatures.size()!=0)
        {
            for(Creature creature : newCreatures)
            {
                creatures.add(creature);
            }
            newCreatures = new ArrayList<Creature>();
        }
        if(removeCreatures.size()!=0)
        {
            for(Creature creature : removeCreatures)
            {
                creatures.remove(creature);
            }
            removeCreatures = new ArrayList<Creature>();
        }
        for(Blob blob : blobs)
        {
            blob.update(this);
        }
        for(int i = 0; i < grassRegrowRate; i++)
        {
            int x = (int)(Math.random()*width);
            int y = (int)(Math.random()*height);
            if(groundImage.getRGB(x, y)==dirtColor.getRGB())
            {
                groundImage.setRGB(x, y, grassColor.getRGB());
            }
        }
        Graphics g = worldImage.getGraphics();
        g.drawImage(groundImage,0 , 0, width, height, null);
        
        for(Blob blob : blobs)
        {
            blob.draw(g);
        }
        repaint();
    }
    
    
    private void generateWorld()
    {
        creatures = new ArrayList<>();
        removeCreatures = new ArrayList<>();
        newCreatures = new ArrayList<>();
        blobs = new ArrayList<>();
        
        Graphics g = groundImage.getGraphics();
        g.setColor(grassColor);
        g.fillRect(0,0,width,height);
        generateLava();
        for(int i = 0; i< startPopulation; i++)
        {
            creatures.add(new Creature((int)(300*Math.random()), (int)(200*Math.random())));
        }
        for(int i = 0; i < 4; i ++)
        {
            int x = (int)(Math.random() * width);
            int y = (int)(Math.random() * height);
            int w = (int)(Math.random() * 10 + 10);
            int h = (int)(Math.random() * 10 + 10);
            blobs.add(new Blob(x,y,w,h));
        }
    }
    public void generateLava()
    {
        //Standing on lava takes away metabolism. Eventually, they should start avoiding it.
        Graphics g = groundImage.getGraphics();
        g.setColor(lavaColor);
        int i = 0;
        while(i<6)
        {
            int centerX = (int)(width*Math.random());
            int centerY = (int)(height*Math.random());
            g.fillOval(centerX, centerY, centerX + (int)(2*Math.random()), centerY + (int)(2*Math.random()));i++;
        }
    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(worldImage, 0 ,0, getWidth(), getHeight(), null);
        double scaleX = (double)(getWidth()/width);
        double scaleY = (double)(getHeight()/height);
        for(Creature creature : creatures)
        {
            creature.draw(g, scaleX, scaleY);
        }
    }
    private boolean isValidPos(int x, int y)
    {
        if(y >= height||x>=width || y < 0|| x < 0 )
            return false;
        
        return true;
    }
    public boolean canMove(int x, int y)
    {
        if(!isValidPos(x, y))return false;
        Creature creature = getCreature(x, y);
        return creature == null;
            
    }
    
    
    public double getTile(int x, int y)
    {
        if(!isValidPos(x,y))
            return 0.0;
        
        
        int tileColor = worldImage.getRGB(x, y);
        if(tileColor == grassColor.getRGB())
            return 0.5;
        else if(tileColor==dirtColor.getRGB())
            return 0.0;
        else if(tileColor==Blob.color.getRGB())
            return 1.0;
        else if(tileColor==lavaColor.getRGB())
            return -1.0;
        else
            return 1.0;
    }
    public void addCreature(Creature creature)
    {
        if(creatures.size() < maxPopulation)
        {
            int x = getX();
            int y = getY();
            if(getCreature(x,y)==null)
            {
                newCreatures.add(creature);
            }
        }
    }
    public void removeCeature(Creature creature)
    {
        removeCreatures.add(creature);
    }
    public int eat(int x, int y)
    {
        if(!isValidPos(x,y)) return 0;
        int color = worldImage.getRGB(x,y);
        if(color == grassColor.getRGB())
        {
            groundImage.setRGB(x,y, dirtColor.getRGB());
            return grassEnergy;
        }
        else if(color == blobColor.getRGB())
        {
            return blobEnergy;
        }
        return 0;
        
    }
    
    public Creature getCreature(int x, int y)
    {
        for(Creature creature : creatures)
        {
            if(creature.getX()==x&&creature.getY()==y)
                return creature;
        }
        return null;
    }
    
    public double getPredator(int x, int y)
    {
        if(y >= getHeight() || y < 0|| x>=getWidth() || x < 0)
            return 0.0;
        for(Creature creature : creatures)
        {
            if(creature.getX()==x&&creature.getY()==y)
            {
                if(creature.isPredator())
                    return -1;
                else
                    return 1;
            }
        }
        return 0.0;
    }
            
}
