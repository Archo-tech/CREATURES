
import java.util.Arrays;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 94450
 */
public class BrainTester {
    public static void main(String[] args)
    {
        World world = new World();
        Creature creature = new Creature(110,20);
        Creature creature2 = new Creature(111,20);
        world.addCreature(creature);
        world.addCreature(creature2);
        double[] sense = creature.getSenses(world);
        System.out.println(Arrays.toString(sense));
    }
}
