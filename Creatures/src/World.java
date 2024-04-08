
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
    private BufferedImage worldImage;
    private BufferedImage groundImage;
    
    private Timer timer;
    
    private final int width = 100;     
    private final int height = 50;    
    private final Color grassColor = new Color(10, 200, 50);    
    private final Color dirtColor = new Color(50, 20, 30);   
    private final Color blobColor = new Color(10, 20, 110);    
    private final Color lavaColor = new Color(150, 10, 15);    
    private final int startPopulation = 100;
    private final int maxPopulation = 75;
    private final int blobEnergy =10;
    private final int grassEnergy = 5;
    private final int grassRegrowRate = 5;
    
    public World()
    {
        worldImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        groundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        generateWorld();
        timer = new Timer(100, new ActionListener(){ 
            public void actionPerformed(ActionEvent e){
                update();
            }
        });
        timer.start();
    }
    private void update()
    {
        for(Creature creature : creatures)
        {
            creature.update(this);
        }
        
        for(Creature creature : newCreatures)
        {
            creatures.add(creature);
        }
        for(Creature creature : removeCreatures)
        {
            creatures.remove(creature);
        }
        removeCreatures.clear();
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
        //Draw blob
        repaint();
    }
    
    
    private void generateWorld()
    {
        creatures = new ArrayList<>();
        removeCreatures = new ArrayList<>();
        newCreatures = new ArrayList<>();
        Graphics g = groundImage.getGraphics();
        g.setColor(grassColor);
        g.fillRect(0,0,width,height);
        for(int i = 0; i< startPopulation; i++)
        {
            creatures.add(new Creature((int)(300*Math.random()), (int)(200*Math.random())));
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
        
        
        int color = worldImage.getRGB(x, y);
        if(color == grassColor.getRGB())
            return 0.5;
        else if(color==dirtColor.getRGB())
            return 0.0;
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
