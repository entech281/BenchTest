package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

public class LinearAxis{

    private double length;
    private double conversion;
    private double location = 0;
    private double speed;
    private double currentJob = -1;
    private double tolerance;
    private boolean disabled = false;
    private boolean topLimitSwitchPressed;
    private boolean bottomLimitSwitchPressed;
    private List<String> errors   = new ArrayList<String>();

    public LinearAxis(double length, double speed, double conversion, double tolerance){
        this.length = length;
        this.speed = speed;
        this.conversion = conversion;
        this.location = 0;
        this.tolerance = tolerance;
    }

    private double getDiff(){
        return currentJob-location;
    }

    private boolean positionWithinTolerence(){
        return Math.abs(getDiff()) < tolerance;
    }

    public boolean setJob(double position){
        if(position > 0 && position < length){
            currentJob = position;
            return true;
        }
        return false;
    }

    public void cancelJob(){
        this.currentJob = -1;
    }

    public boolean hasErrors(){
        return !errors.isEmpty();
    }

    public List<String> getErrors(){
        return errors;
    }

    public double getVelocity(){
        if(currentJob == -1||positionWithinTolerence()||disabled){
            return 0;
        } else if(getDiff() < 0 && !topLimitSwitchPressed) {
            return -speed;
        } else if(getDiff() > 0 && !bottomLimitSwitchPressed) {
            return speed;
        }
        return 0;
    }

    public void update(int encoderClicks, boolean topLimitSwitch, boolean bottomLimitSwitch){
        
        location  += conversion*encoderClicks;
        topLimitSwitchPressed    = topLimitSwitch;
        bottomLimitSwitchPressed = bottomLimitSwitch;

        if(topLimitSwitch&&bottomLimitSwitch){
            errors.add("Both limit switches seem to be pressed. Stopping.");
        }
        if(location > length + tolerance && !topLimitSwitch){
            //checks to see if length is too
            disabled = true;
            errors.add("Top limit switch not triggered and shuttle past declared length. Stopping.");
        }
        if(location < -tolerance && !bottomLimitSwitch){
            disabled = true;
            errors.add("Bottom limit switch not triggered and shuttle position below zero. Stopping.");
        }
        if(topLimitSwitch && location < length - tolerance){
            this.errors.add("Limit switch pressed before estimated size. Updating size to " + this.location+ ".");
            length = location;
        }
    }

}