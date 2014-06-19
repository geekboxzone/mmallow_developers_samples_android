JobScheduler Sample
====================

This is a demo of the new L dev preview JobScheduler API. It exposes a UI to schedule jobs that
represent the full spectrum of constraints offered by the scheduler.

For example you can specify a job that requires the device to be plugged in, or that will fire when
the device is idle. You can specify that your job only be run when there is a network connection, or
only when that network connection is unmetered. Finally you can impose, in seconds, a minimum
delay after which your job will be considered ready, or a maximum execution deadline by which your
job must run regardless of other constraints.

When the job lands on the app, the UI will flash green. Similarly, when the system calls back to
notify the app that it should stop executing the UI will flash again.

Keep in mind that the scheduler will not set an alarm for shorter than 15 seconds, so any deadline
set lower than that will only time-out after 15 seconds.

Build Instructions
-------------------

This sample uses the Gradle build system. To build this project, use the
"gradlew build" command or use "Import Project" in Android Studio.

To see a list of all available commands, run "gradlew tasks".

Dependencies
-------------

- Android SDK Build-tools v18.1
- Android Support Repository v2

Dependencies are available for download via the Android SDK Manager.

Android Studio is available for download at:
    http://developer.android.com/sdk/installing/studio.html
