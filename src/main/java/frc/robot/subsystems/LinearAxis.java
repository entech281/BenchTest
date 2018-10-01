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
        this.tolerance = tolerance;
    }
    
    public double getLength(){
        return length;
    }

    public double getConversion(){
        return conversion;
    }

    public double getLocation(){
        return location;
    }

    public boolean setLocation(double d){
        if(d > 0 && d < length){
            location = d;
            return true;
        }
        return false;
    }

    public double getSpeed(){
        return speed;
    }

    public double getCurrentJob(){
        return currentJob;
    }
    
    public void cancelJob(){
        this.currentJob = -1;
    }
    
    public boolean setCurrentJob(double position){
        if(position > 0 && position < length){
            currentJob = position;
            return true;
        }
        return false;
    }
    public double getTolerence(){
        return tolerance;
    }

    public boolean getDisabled(){
        return disabled;
    }

    public void setDisabledTrue(){
        errors.add("setDisabledTrue was called. Disabling.");
        disabled = true;
    }

    public void setDisabledTrue(String message){
        errors.add("setDisabledTrue was called. The message was: " + message + ". Disabling.");
        disabled = true;
    }

    public boolean getBottomLimitSwitch(){
        return bottomLimitSwitchPressed;
    }

    public boolean getTopLimitSwitch(){
        return topLimitSwitchPressed;
    }

    public List<String> getErrors(){
        return errors;
    }

    private double getDiff(){
        return currentJob-location;
    }
    private boolean positionWithinTolerence(){
        return Math.abs(getDiff()) < tolerance;
    }



    public boolean hasErrors(){
        return !errors.isEmpty();
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