
import java.awt.Color;
import java.awt.Graphics;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 94450
 */
public class Creature 
{
    private Brain brain;
    private int x;
    private int y;
    private Color color;
    private int metabolism;
    private boolean predator;
    
    public static final int maxMetabolism = 100;
    public static final double spawnCost = .5;
    public static final double radiation = .05;
    public static final int spawnRate = 5;
    public static final double spawnLevel = .75;
    public static final int senseRadius = 2;
    
    public Creature(int x, int y)        
    {
        this.x = x;
        this.y = y;
        int inputSize = 2 * (int)Math.pow(2*senseRadius + 1, 2);
        brain = new Brain(new int[]{inputSize, inputSize * 2, 6});
        
        metabolism = maxMetabolism/2;
        predator = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getMetabolism() {
        return metabolism;
    }

    public boolean isPredator() {
        return predator;
    }
    
    public Creature getSpawn()
    {
        int x = this.x + (int)(Math.random() * 6 -3);
        int y = this.y + (int)(Math.random() * 6 -3);
        Creature spawn = new Creature(x,y);
        spawn.brain = brain.mitose(radiation);
        return spawn;
    }
    public double[] getSenses(World world)
    {
        int inputSize = 2 * (int)Math.pow(2 * senseRadius + 1, 2);
        double[] input = new double[inputSize];
        int i = 0;
        for(int x = this.x - senseRadius; x < this.x + senseRadius; x++)
        {
            for(int y = this.y - senseRadius; y < this.y + senseRadius; y++)
            {
                input[i++]=world.getTile(x, y);
                input[i++]=world.getPredator(x, y);
            }
        }
        return input;
    }
    public void update(World world)
    {
        double[] input =  getSenses(world);
        double[] output =  brain.think(input);
        move(output[0],output[1], world);
        int red = (int)(output[2] * 127 + 127);
        int green = (int)(output[3] * 127 + 127);
        int blue = (int)(output[4] * 127 + 127);
        color = new Color(red, green, blue);
        predator = output[5]>0;
        
        metabolism--;
        if(world.getTile(x, y)==.1)
        {
            metabolism = metabolism - (metabolism/8);
        }
        if(predator)
        {
            int dx = convertOutput(output[0]);
            int dy = convertOutput(output[1]);
            Creature other = world.getCreature(x+dx,y+dy);
            if(other != null && other.isPredator())
            {
                metabolism+=other.getMetabolism();
                world.removeCeature(other);
                System.out.println("he is ded :(");
            }
        }
        if(!predator)
        {
            metabolism+=world.eat(x,y);
        }
        if(metabolism >= maxMetabolism * spawnLevel)
        {
            for(int i = 0; i < spawnRate;i++)
            {
                Creature spawn = getSpawn();
                world.addCreature(spawn);
            }
        }
        if(metabolism <=0)
        {
            world.removeCeature(this);
        }
    }
    private int convertOutput(double x)
    {
        if(x<-.33)
            return -1;
        else if(x < .33)
            return 0;
        else 
            return 1;
    }
    
    private void move(double h, double v, World world)
    {
        int dx = convertOutput(h);
        int dy = convertOutput(v);
        
        if(world.canMove(x + dx, y+dy))
        {
            x+=dx;
            y+=dy;
        }
            
    }
    public void draw(Graphics g, double scaleX, double scaleY)
    {
        int x = (int)(this.x * scaleX);
        int y = (int)(this.y * scaleY);
        int w = (int)scaleX;
        int h = (int)scaleY;
        if(predator)
        {
            g.setColor(color);
            g.fillRect(x, y, w, h);
            g.setColor(Color.RED);
            g.drawRect(x, y, w, h);
        }
        else
        {
            g.setColor(color);
            g.fillOval(x, y, w, h);
            g.setColor(Color.WHITE);
            g.drawOval(x, y, w, h);
        }
    }
    
}
