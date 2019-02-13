/* State Hardware, Import this to teamcode.Libraries */

package org.firstinspires.ftc.teamcode.Libraries;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Hardware
{
    /* Public OpMode members. */

    //Motors
    public DcMotor red, blue, yellow, green, lift, rotate,;

    //Servos
    public Servo camera, liftlock, mainClaw;

    //Sensors

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public Hardware(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        //Motor hardware mapping
       red  = hwMap.get(DcMotor.class, "red");
        blue = hwMap.get(DcMotor.class, "blue");
       yellow  = hwMap.get(DcMotor.class, "yellow");
        green = hwMap.get(DcMotor.class, "green");
        lift = hwMap.get(DcMotor.class, "lift");
        rotate = hwMap.get(DcMotor.class, "rotate");
        



        //Servo hardware mapping
        camera = hwMap.get(Servo.class, "camera");
        liftlock = hwMap.get(Servo.class, "liftlock");
        mainClaw = hwMap.get(Servo.class, "mainClaw");

        //Sensor hardware mapping

        //Motor set powers
        red.setPower(0);
        blue.setPower(0);
        green.setPower(0);
        yellow.setPower(0);
        lift.setPower(0);
        rotate.setPower(0);
       
        //Continuous Servo set powers

        //Run motors without encoder
        red.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        blue.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        green.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        yellow.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rotate.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rotate.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }
}
