package frc.robot.subsystems;

import java.util.ArrayList;

public class LinearAxis{

    public double length;
    public double conversion;
    public double location;
    public double speed;

    private double currentJob;
    private double tolerance;
    private boolean topLimitSwitchPressed;
    private boolean bottomLimitSwitchPressed;
    private ArrayList<String> errors;
    private ArrayList<String> warnings;

    public LinearAxis(double length, double speed, double conversion, double tolerance){
        this.length = length;
        this.speed = speed;
        this.conversion = conversion;
        this.location = 0;
        this.tolerance = tolerance;
        this.currentJob = -1;
        this.errors =  new ArrayList<String>();
        this.warnings =  new ArrayList<String>();
    }

    public LinearAxis(double length, double speed, double conversion,double tolerence, double location){
        this.length = length;
        this.speed = speed;
        this.conversion = conversion;
        this.location = location;
        this.tolerance = tolerence;
        this.currentJob = -1;
        this.errors =  new ArrayList<String>();
    }

    private double getDiff(){
        return this.currentJob-this.location;
    }

    private boolean positionWithinTolerence(){
        return Math.abs(getDiff()) < this.tolerance;
    }

    public boolean setJob(double position){
        if(position > 0 && position < this.length){
            this.currentJob = position;
            return true;
        }
        return false;
    }

    public void cancelJob(){
        this.currentJob = -1;
    }

    public boolean hasErrors(){
        return !this.errors.isEmpty();
    }

    public ArrayList<String> getErrors(){
        return this.errors;
    }

    public double getVelocity(){
        if(this.currentJob == -1||positionWithinTolerence()||this.hasErrors()){
            return 0;
        } else if(this.getDiff() < 0 && !this.topLimitSwitchPressed) {
            return -this.speed;
        } else if(this.getDiff() > 0 && !this.bottomLimitSwitchPressed) {
            return this.speed;
        }
        return 0;
    }

    public void update(int encoderClicks, boolean topLimitSwitch, boolean bottomLimitSwitch){
        
        this.location += this.conversion*encoderClicks;
        this.topLimitSwitchPressed    = topLimitSwitch;
        this.bottomLimitSwitchPressed = bottomLimitSwitch;

        if(topLimitSwitch&&bottomLimitSwitch){
            this.errors.add("Both limit switches seem to be pressed. Stopping.");
        }
        if(this.location > this.length + this.tolerance && !topLimitSwitch){
            //checks to see if length is too
            this.errors.add("Top limit switch not triggered and shuttle past declared length. Stopping.");
        }
        if(this.location < -this.tolerance && !bottomLimitSwitch){
            this.errors.add("Bottom limit switch not triggered and shuttle position below zero. Stopping.");
        }
        if(topLimitSwitch && this.location < this.length - this.tolerance){
            //this.errors.add("Limit switch pressed before estimated size. Updating size to " + this.location+ ".");
            this.length = this.location;
        }
    }

}