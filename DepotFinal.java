           frontRight.setPower(0);
                                       collector.setPower(0);

           sleep(30000);
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
       strafeLeft(.4, 17, 10);
      
    }
    
    public void right() {
        strafeRight(.4, 18, 10);
        backward(DRIVE_SPEED, 10);
         strafeRight(.4, 14, 10);
        strafeLeft(.4, 11, 10);
        forward(1, 10);
    }
    public void auto() {
         strafeRight(.4, 18, 10);
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
           frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
           frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}
