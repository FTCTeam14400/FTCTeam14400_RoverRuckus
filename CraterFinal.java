package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;



import org.firstinspires.ftc.robotcore.external.ClassFactory;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;


import java.util.List;
import java.util.concurrent.Delayed;


@Autonomous(name = "State Crater Final", group = "Vuforia")
//@Disabled
public class State_Crater_Final extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private ElapsedTime runtime = new ElapsedTime();
    private static final String VUFORIA_KEY = "AdDM0Jr/////AAABmZv662r2ZUZ+tixJD81gTcEPxDJuraCEzzOlfAG3CH7wle1XvtbizvjiiEa5sX6wa7raR+h7R2cE3usL3IULhZ9mp5ZXGRpAglF3wHlLof5MDAgNsBUlfHy5XuUfAWbHFPjun1YptV5UuPOEux05wTf4V8KXnF9sjvezls/UtfFcZ5y0oA7dpMXA2vH7TjAUDzUZqKCzGmvoawol+69HGxhd69NZDowxfchvysrIOU6rrqUDu26/suorET4FAlDR7uL5Zffv5MH4oL4MVZaNgLm1Rm1xi9APhKXsRcRWM6fU9UN8n3ewn7uvYXF2fzyzqU/qTFPJJxM1VzHE4eBR4p9tB1j17fJJUqdQciKnaOti";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

     private DcMotor backLeft = null;
    private DcMotor backRight = null;
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor lift = null;
    private Servo liftlock = null;
        private DistanceSensor range = null;
        private Servo marker = null;


    static final double     COUNTS_PER_MOTOR_REV    = 1150 ;    // eg: Neverest 40 Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    minerals mineralState = minerals.GOLD_NOT_FOUND;

    public enum minerals {
        GOLD_LEFT,
        GOLD_CENTER,
        GOLD_RIGHT,
        GOLD_NOT_FOUND
    }

    @Override
    public void runOpMode() {
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();
 
 
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
          backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
           lift = hardwareMap.get(DcMotor.class, "lift");
           liftlock = hardwareMap.get(Servo.class, "liftlock" );
           marker = hardwareMap.get(Servo.class, "marker" );

backLeft.setDirection(DcMotor.Direction.REVERSE);
   frontLeft.setDirection(DcMotor.Direction.REVERSE);   

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

       backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
       backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
       frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
       lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
       
       backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
           lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                          backLeft.getCurrentPosition(),
                          backRight.getCurrentPosition(),
                          frontLeft.getCurrentPosition(),
                          frontRight.getCurrentPosition());
        telemetry.update();
        waitForStart();
        runtime.reset();
        if (opModeIsActive()) {

            /** Activate Tensor Flow Object Detection. */
            if (tfod != null) {
                tfod.activate();
            }

            while (opModeIsActive()) {
                int goldMineralX = -100;
                int goldMineralY=-100;
                while ( runtime.seconds() <= 7){
                if (tfod != null)
                {
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null)
                    {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        if (updatedRecognitions.size() == 2 || updatedRecognitions.size() == 3 )
                        {
                            for (Recognition recognition : updatedRecognitions)
                            {
                                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL))
                                {
                                    if (recognition.getTop() > 0)
                                    {


                                        goldMineralX = (int) recognition.getLeft();
                                        goldMineralY = (int) recognition.getTop();
                                        telemetry.addData("Gold Mineral X Value", goldMineralX);
                                        telemetry.addData("Gold Mineral Y Value", goldMineralY);
                                        telemetry.update();


                                    }
                                }
                            }

                            telemetry.addData("Gold Mineral X Value", goldMineralX);
                            telemetry.addData("Gold Mineral Y Value", goldMineralY);
                            if (goldMineralX <= 170 && goldMineralX > 0)
                            {
                                mineralState = minerals.GOLD_LEFT;
                                telemetry.addData("Gold Mineral Position", "Left");

                                telemetry.update();
                                sleep(2000);
                                tfod.shutdown();
                                land();
                                left();
                                   forward(1, 32);
       turnLeft(.4, 12);
       strafeRight(1, 30, 10);

        forward(1, 46);
        marker.setPosition(.85);
        marker.setPosition(.5);
        backward(1,66);
                                
                                
                                stop();
                            } else if (goldMineralX >= 500)
                            {
                                mineralState = minerals.GOLD_RIGHT;
                                telemetry.addData("Gold Mineral Position", "Right");

                                telemetry.update();
                                sleep(2000);
                                tfod.shutdown();
                               land();
                               right();
                                                          forward(1, 32);
       turnLeft(.4, 12);
       strafeRight(1, 30, 10);

        forward(1, 46);
          marker.setPosition(.85);
        marker.setPosition(.5);
        backward(1,66);
                               
                                stop();
                            } else if (goldMineralX >= 100)
                            {
                                mineralState = minerals.GOLD_CENTER;
                                telemetry.addData("Gold Mineral Position", "Center");


                                telemetry.update();
                                sleep(1000);
                                tfod.shutdown();
                               land();
                               center();
                                                 forward(1, 32);
       turnLeft(.4, 12);
       strafeRight(1, 30, 10);

        forward(1, 46);
          marker.setPosition(.85);
        marker.setPosition(.5);
        backward(1,66);
                               
                                stop();
                            }
                        }
                    }
                }
                    telemetry.update();
                }
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
       public void forward(double spd, double fwd) {
        encoderDrive(spd, fwd, fwd, fwd, fwd, 10.0);
        sleep(200);
    }
    
     public void land() {
        lift.setDirection(DcMotorSimple.Direction.REVERSE);
                                lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                                lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                                lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                                lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                                lift.setTargetPosition(8100);
                                lift.setPower(1);
                                while (!!lift.isBusy())
                                {
                                    idle();
                                }
                                lift.setPower(0);
                                lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                                liftlock.setPosition(0.31);
                                sleep(2000);
    }
    
    public void backward(double spd, double back) {
        encoderDrive(spd, -back, -back, -back, -back, 10.0);
        sleep(200);
    }
     public void left() {
        
       strafeRight(.4, 18, 10);
        forward(DRIVE_SPEED, 17);
         strafeRight(.4, 17, 10);
        strafeLeft(.4, 17, 10);
        backward(DRIVE_SPEED, 17);
    }
    public void center() {
        strafeRight(.4, 35, 10);
       strafeLeft(.4, 6, 10);
      
    }
    
    public void right() {
        strafeRight(.4, 18, 10);
        backward(DRIVE_SPEED, 10);
         strafeRight(.4, 11, 10);
        strafeLeft(.4, 11, 10);
        forward(DRIVE_SPEED, 10);
    }
    
     public void turnRight(double spd, double turn) {
        encoderDrive(spd, turn, turn, -turn, -turn,  10.0);
        sleep(200);
    }
    
    public void turnLeft(double spd, double turn) {
        encoderDrive(spd, -turn, turn, -turn, turn,  10.0);
        sleep(200);
    }
    // Speed, BackLeft, BackRight, FrontLeft, FrontRight, Timeout
    public void strafeRight(double spd, double strafe, double timeoutS){
        encoderDrive(spd, -strafe, strafe, strafe, -strafe, timeoutS);

    }
    
    public void strafeLeft(double spd, double strafe, double timeoutS){
        encoderDrive(spd, strafe, -strafe, -strafe, strafe, timeoutS);

    }
    
    
    
    public void encoderDrive(double speed,
                             double BackleftInches, double BackrightInches,
                             double FrontleftInches, double FrontrightInches,
                             double timeoutS) {
        int newBackLeftTarget;
        int newBackRightTarget;
        int newFrontLeftTarget;
        int newFrontRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newBackLeftTarget = backLeft.getCurrentPosition() + (int)(BackleftInches * COUNTS_PER_INCH);
            newBackRightTarget = backRight.getCurrentPosition() + (int)(BackrightInches * COUNTS_PER_INCH);
            newFrontLeftTarget = frontLeft.getCurrentPosition() + (int)(FrontleftInches * COUNTS_PER_INCH);
            newFrontRightTarget = frontRight.getCurrentPosition() + (int)(FrontrightInches * COUNTS_PER_INCH);
           backLeft.setTargetPosition(newBackLeftTarget);
           backRight.setTargetPosition(newBackRightTarget);
           frontLeft.setTargetPosition(newFrontLeftTarget);
           frontRight.setTargetPosition(newFrontRightTarget);

            // Turn On RUN_TO_POSITION
           backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
               frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
           backLeft.setPower(Math.abs(speed));
           backRight.setPower(Math.abs(speed));
           frontLeft.setPower(Math.abs(speed));
           frontRight.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                   (runtime.seconds() < timeoutS) &&
                   (backLeft.isBusy() && backRight.isBusy() && frontLeft.isBusy() && frontRight.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newBackLeftTarget,  newBackRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                                            backLeft.getCurrentPosition(),
                                            backRight.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
           backLeft.setPower(0);
           backRight.setPower(0);
           frontLeft.setPower(0);
           frontRight.setPower(0);

            // Turn off RUN_TO_POSITION
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
           backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}
