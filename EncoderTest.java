// Import this to Final OpMode Folder

package org.firstinspires.ftc.teamcode.Final;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Libraries.Hardware;

import java.util.List;


@Autonomous(name = "Encoder Test", group = "Marvin V7")
//@Disabled
public class EncoderTest extends LinearOpMode {

    private Hardware robot = new Hardware();

  
    private ElapsedTime runtime = new ElapsedTime();

    private Telemetry.Item encoderProjected;
    private Telemetry.Item encoderCurrent;
    private Telemetry.Item liftProjected;
    private Telemetry.Item liftCurrent;

 

    private static final double COUNTS_PER_MOTOR_REV = 1120;
    private static final double DRIVE_GEAR_REDUCTION = 1.0;
    private static final double WHEEL_DIAMETER_INCHES = 6;
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808651328230664709384460955058223172535940812848111745028410270193852110555964462294895493038196442881097566593344612847564823378678316527120190914564856692346034861045432664821339360726024914127372458700660631558817488152092096282925409171536436789259036001133053054882046652138414695194151160943305727036575959195309218611738193261179310511854807446237996274956735188575272489122793818301194912983367336244065664308602139494639522473719070217986094370277053921717629317675238467481846766940513200056812714526356082778577134275778960917363717872146844090122495343014654958537105079227968925892354201995611212902196086403441815981362977477130996051870721134999999837297804995105973173281609631859502445945534690830264252230825334468503526193118817101000313783875288658753320838142061717766914730359825349042875546873115956286388235378759375195778185778053217122680661300192787661119590921642019893809525720106548586327886593615338182796823030195203530185296899577362259941389124972177528347913151557485724245415069595082953311686172785588907509838175463746493931925506040092770167113900984882401285836160356370766010471018194295559619894676783744944825537977472684710404753464620804668425906949129331367702898915210475216205696602405803815019351125338243003558764024749647326391419927260426992279678235478163600934172164121992458631503028618297455570674983850549458858692699569092721079750930295532116534498720275596023648066549911988183479775356636980742654252786255181841757467289097777279);


    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        robot.red.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.blue.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.yellow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.green.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

       

       

        /* Wait for the game to begin */
        waitForStart();

        resetStartTime();

        robot.red.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.blue.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.green.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.yellow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        /* Step 1: Lower Robot */
// lowerRobot();

       

            while (opModeIsActive()) {
               
                          
                    
                }
                // Begin Auto Path Movement
             
                // Step 3. Turn the robot
                encoderDrive(0.5, 12, -12, 4);

                /* Step 4. Square the robot up with the wall */
                encoderDrive(0.2, -6, -6, 4);

                /* Step 5. Drive Forward to position to sample */

                runtime.reset();
                /* Step 6. Sample the Mineral then return to position */
                
                        forward(.2, 19);
                        backward(.2, 8);
                        strafeLeft(.4, 8, 6);
                        turnLeft(.4, 12.25);
                        forward(.5, 6);
                        turnLeft(.4, 6.5);
                        strafeRight(.4, 16, 5);
                        forward(.5, 30);
                        dumpMarker();
                        backward(.6, 30);
                        turnLeft(.4, 27);
                        forward(.4, 12);

                        

                /* End */
                sleep(30000);
            }
        }

       

    }

   
    /*
     * raiseRobot lowers the robots arm to it's starting position thus raising the robot in the air if it is hooked
     */
    public void raiseRobot() {
        encoderlift(0.8, 0, 8);
        sleep(200);
    }

    /*
     * lowerRobot raises the robots arm to its lifting position thus lowering the robot if it is hooked
     */
    public void lowerRobot() {
        encoderlift(0.5, -6850, 10);
        sleep(200);
    }

    public void lowerArm() {
        robot.intakeAdjust.setPower(1);
        sleep(800);
        robot.intakeAdjust.setPower(0);
    }

    /*
     * forward moves the robot forward at the prescribed speed for the prescribed distance
     *
     * @param spd
     * @param fwd
     */
    public void forward(double spd, double fwd) {
        encoderDrive(spd, fwd, fwd, 10.0);
        sleep(200);
    }

    /*
     * backward moves the robot backward at the prescribed speed for the prescribed distance
     *
     * @param spd
     * @param back
     */
    public void backward(double spd, double back) {
        encoderDrive(spd, -back, -back, 5.0);
        sleep(200);
    }

    /*
     * turnRight turns the robot right at the prescribed speed for the prescribed distance
     *
     * @param spd
     * @param turn
     */
    public void turnRight(double spd, double turn) {
        encoderDrive(spd, turn, -turn, 5.0);
        sleep(200);
    }

    /*
     * turnLeft turns the robot right at the prescribed speed for the prescribed distance
     *
     * @param spd
     * @param turn
     */
    public void turnLeft(double spd, double turn) {
        encoderDrive(spd, -turn, turn, 5.0);
        sleep(200);
    }
    //
// public void strafeLeft(double spd, double strafe, double angle){
// strafeDrive(spd, strafe, angle, 'L');
//
// }
//
// public void strafeRight(double spd, double strafe, double angle){
// strafeDrive(spd, strafe, angle, 'R');
// }
    public void strafeLeft(double spd, double strafe, double timeoutS){
        strafeDrive(spd, strafe, strafe, -strafe, -strafe, timeoutS);

    }

    public void strafeRight(double spd, double strafe, double timeoutS){
        strafeDrive(spd, -strafe, -strafe, strafe, strafe, timeoutS);
    }


    /*
     * Dump the team marker
     */
    public void dumpMarker() {
        //robot.servoIntake.setPower(1);
        sleep(3200);
        // robot.servoIntake.setPower(0);
    }



    /*
     * encoderDrive allows the robot to go at a certain speed for a certain distance based
     * on ticks of the encoder. This allows the robot to be very precise in it's movements
     *
     * @param speed
     * @param leftInches
     * @param rightInches
     * @param timeoutS
     */

    public void encoderDrive(double speed, double leftInches, double rightInches, double timeoutS) {

        int newredTarget;
        int newblueTarget;
        int newgreenTarget;
        int newyellowTarget;
        encoderProjected = telemetry.addData("Projected Path", 0);
        encoderCurrent = telemetry.addData("Current Path", 0);


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newredTarget = robot.red.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            newblueTarget = robot.blue.getCurrentPosition() + (int) (-leftInches * COUNTS_PER_INCH);
            newgreenTarget = robot.green.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            newyellowTarget = robot.yellow.getCurrentPosition() + (int) (-leftInches * COUNTS_PER_INCH);

            robot.red.setTargetPosition(newredTarget);
            robot.blue.setTargetPosition(newblueTarget);
            robot.green.setTargetPosition(newgreenTarget);
            robot.yellow.setTargetPosition(newyellowTarget);
            // Turn On RUN_TO_POSITION
            robot.red.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.blue.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.green.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.yellow.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.red.setPower(Math.abs(speed));
            robot.blue.setPower(Math.abs(speed));
            robot.green.setPower(Math.abs(speed));
            robot.yellow.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop. This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.red.isBusy() && robot.blue.isBusy() && robot.green.isBusy() && robot.yellow.isBusy())) {

                // Display it for the driver.
// telemetry.addData("Path1", "Running to %7d :%7d :%7d :%7d", newredTarget, newblueTarget,
// newgreenTarget, newyellowTarget);

// telemetry.addData("Path2", "Running at %7d :%7d :%7d :%7d",
// robot.red.getCurrentPosition(),
// robot.blue.getCurrentPosition(),
// robot.green.getCurrentPosition(),
// robot.yellow.getCurrentPosition());
                encoderProjected.setValue("Running to %7d :%7d :%7d :%7d", newredTarget, newblueTarget,
                        newgreenTarget, newyellowTarget);
                encoderCurrent.setValue("Running at %7d :%7d :%7d :%7d",
                        robot.red.getCurrentPosition(),
                        robot.blue.getCurrentPosition(),
                        robot.green.getCurrentPosition(),
                        robot.yellow.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.red.setPower(0);
            robot.blue.setPower(0);
            robot.green.setPower(0);
            robot.yellow.setPower(0);


            // Turn off RUN_TO_POSITION
            robot.red.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.blue.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.green.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.yellow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    public void encoderlift(double speed, int liftTarget, double timeoutS) {


        liftProjected = telemetry.addData("Projected lift Position", 0);
        liftCurrent = telemetry.addData("Current lift Position", 0);
        telemetry.update();
        if (opModeIsActive()) {


            robot.lift.setTargetPosition(liftTarget);

            robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            robot.lift.setPower(Math.abs(speed));

            while (opModeIsActive() && (runtime.seconds() < timeoutS) && robot.lift.isBusy()) {

// telemetry.addData("Path1", "Running to %7d", liftTarget);
// telemetry.addData("Path2", "Running at %7d", robot.lift.getCurrentPosition());
                liftProjected.setValue("Running to %7d", liftTarget);
                liftCurrent.setValue("Running at %7d", robot.lift.getCurrentPosition());
                telemetry.update();

            }

            robot.lift.setPower(0);

            robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

    }

    public void strafeDrive(double speed, double blueInches, double redInches, double yellowInches, double greenInches, double timeoutS) {

        int newredTarget;
        int newblueTarget;
        int newgreenTarget;
        int newyellowTarget;
        encoderProjected = telemetry.addData("Projected Path", 0);
        encoderCurrent = telemetry.addData("Current Path", 0);


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newredTarget = robot.red.getCurrentPosition() + (int) (redInches * COUNTS_PER_INCH);
            newblueTarget = robot.blue.getCurrentPosition() + (int) (blueInches * COUNTS_PER_INCH);
            newgreenTarget = robot.green.getCurrentPosition() + (int) (greenInches * COUNTS_PER_INCH);
            newyellowTarget = robot.yellow.getCurrentPosition() + (int) (yellowInches * COUNTS_PER_INCH);

            robot.red.setTargetPosition(newredTarget);
            robot.blue.setTargetPosition(newblueTarget);
            robot.green.setTargetPosition(newgreenTarget);
            robot.yellow.setTargetPosition(newyellowTarget);
            // Turn On RUN_TO_POSITION
            robot.red.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.blue.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.green.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.yellow.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.red.setPower(Math.abs(speed));
            robot.blue.setPower(Math.abs(speed));
            robot.green.setPower(Math.abs(speed));
            robot.yellow.setPower(Math.abs(speed));


            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.red.isBusy() && robot.blue.isBusy() && robot.green.isBusy() && robot.yellow.isBusy())) {

                // Display it for the driver.
                encoderProjected.setValue("Running to %7d :%7d :%7d :%7d", newredTarget, newblueTarget,
                        newgreenTarget, newyellowTarget);
                encoderCurrent.setValue("Running at %7d :%7d :%7d :%7d",
                        robot.red.getCurrentPosition(),
                        robot.blue.getCurrentPosition(),
                        robot.green.getCurrentPosition(),
                        robot.yellow.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.red.setPower(0);
            robot.blue.setPower(0);
            robot.green.setPower(0);
            robot.yellow.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.red.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.blue.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.green.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.yellow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
}
