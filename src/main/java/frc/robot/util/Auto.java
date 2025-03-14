package frc.robot.util;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.Swerve;

/** Simple wrapper class to load routines. */
public class Auto {
    
    Swerve swerve;

    AutoFactory autoFactory;

    /**
     * Constructs a new Auto object with a swerve object to use for path control.

     * @param swerve - the swerve object to be used
     */
    public Auto(Swerve swerve) {

        this.swerve = swerve;

        autoFactory = new AutoFactory(
			swerve::getPose,
			swerve::resetOdometry, 
            swerve::followTrajectory, 
            false, 
            swerve
        );
    }

    public AutoRoutine fromLeft() {
        DataLogManager.log("Starting Auto Routine: fromLeft");

        AutoRoutine fromLeft = autoFactory.newRoutine("fromLeft");

        AutoTrajectory midFromLeft = fromLeft.trajectory("midFromLeft");
        AutoTrajectory midToScore = fromLeft.trajectory("midToScore");
        AutoTrajectory scoreToCoral = fromLeft.trajectory("leftScoreToAlgae");
        AutoTrajectory alg2 = fromLeft.trajectory("alg2");

        // update current pose of robot to starting point of first trajectory
        if (midFromLeft.getInitialPose().isEmpty()) {
            Elastic.sendNotification(new Elastic.Notification(Elastic.Notification.NotificationLevel.WARNING, "Autonomous", "Could not get initial pose from trajectory!"));
        } else {
            swerve.setPose(midFromLeft.getInitialPose().get());
        }

        fromLeft.active().onTrue(
            Commands.sequence(
                midFromLeft.resetOdometry(),
                midFromLeft.cmd()
            )
        );

        midFromLeft.done().onTrue(midToScore.cmd());
        midToScore.done().onTrue(scoreToCoral.cmd());
        scoreToCoral.done().onTrue(alg2.cmd());

        return fromLeft;
    }

    public AutoRoutine fromRight() {
        DataLogManager.log("Starting Auto Routine: fromRight");

        AutoRoutine fromRight = autoFactory.newRoutine("fromRight");

        AutoTrajectory midFromRight = fromRight.trajectory("midFromRight");
        AutoTrajectory midToScore = fromRight.trajectory("midToScore");
        AutoTrajectory scoreToCoral = fromRight.trajectory("scoreToCoral");

        // update current pose of robot to starting point of first trajectory
        if (midFromRight.getInitialPose().isEmpty()) {
            Elastic.sendNotification(new Elastic.Notification(Elastic.Notification.NotificationLevel.WARNING, "Autonomous", "Could not get initial pose from trajectory!"));
        } else {
            swerve.setPose(midFromRight.getInitialPose().get());
        }

        fromRight.active().onTrue(
            Commands.sequence(
                midFromRight.resetOdometry(),
                midFromRight.cmd()
            )
        );

        midFromRight.done().onTrue(midToScore.cmd());
        midToScore.done().onTrue(scoreToCoral.cmd());

        return fromRight;
    }

    public AutoRoutine fromMid() {
        DataLogManager.log("Starting Auto Routine: fromMid");

        AutoRoutine fromMid = autoFactory.newRoutine("fromMid");

        AutoTrajectory midFromMid = fromMid.trajectory("midFromMid");
        AutoTrajectory midToScore = fromMid.trajectory("midToScore");
        AutoTrajectory scoreToCoral = fromMid.trajectory("scoreToCoral");

        // update current pose of robot to starting point of first trajectory
        if (midFromMid.getInitialPose().isEmpty()) {
            Elastic.sendNotification(new Elastic.Notification(Elastic.Notification.NotificationLevel.WARNING, "Autonomous", "Could not get initial pose from trajectory!"));
        } else {
            swerve.setPose(midFromMid.getInitialPose().get());
        }

        fromMid.active().onTrue(
            Commands.sequence(
                midFromMid.resetOdometry(),
                midFromMid.cmd()
            )
        );
    
        return fromMid;
    }
}
