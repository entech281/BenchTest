package frc.team281;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import frc.robot.subsystems.LinearAxis;
import org.junit.Test;

/**
 * Tests the LinearAxis Class  
 * 
 * @author plaba
 *
 */
public class LinearAxisWorks {
    @Test
    public void testSetAndGet() {
        LinearAxis la = new LinearAxis(50,1, 1, 1/2);
        la.setJob(10);
        assertEquals(1.0, la.getVelocity(),0.0);
        la.update(11, false, false);
        assertEquals(-1, la.getVelocity(),0.0);
        la.update(-1, false, false);
        assertEquals(0, la.getVelocity(),0.0);
    }
    @Test
    public void testLimits() {
        LinearAxis la = new LinearAxis(50,1 , 1, 1/2);
        assertFalse(la.setJob(-0.2));
        assertFalse(la.setJob(50.3));
        la.setJob(50);
        la.update(25,false,false);
        assertFalse(la.hasErrors());
        la.update(51, false, false);
        assertFalse(!la.hasErrors());
    }
}