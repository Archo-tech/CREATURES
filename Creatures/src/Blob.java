
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
public class Blob 
{
    private double x;
    private double y;
    private int width;
    private int height;
    private double vx;
    private double vy;
    private int countdown;
    
    public static final Color color = new Color(10,40,180);
    
    public Blob(int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        countdown = (int)(Math.random()*50+50);
        changeVelocity();
    }
    public Color getColor()
    {
        return color;
    }
    private void changeVelocity()
    {
        this.vx = Math.random()*2-1;
        this.vy = Math.random()*2-1;
    }
    
    public void update(World world)
    {
        countdown--;
        if(countdown==0){
            changeVelocity();
            countdown = (int)(Math.random()*50+50);
        }
        x+=vx;
        y+=vy;
        if(x<0)x=0;
        if(y<0)y=0;
        if(x + width > world.getWorldWidth())
            x=world.getWorldWidth()-width;
        if(y + height > world.getWorldHeight())
            x=world.getWorldHeight()-height;
    }
    public void draw(Graphics g)
    {
        g.setColor(color);
        g.fillOval((int)x, (int)y, width, height);
    }
}
