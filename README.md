### OpenCamera-based remote recorder with IMU recording support

**This is an Android application to record synchronized IMU and video frames based on [Open Camera Remote application](https://play.google.com/store/apps/details?id=net.sourceforge.opencameraremote&hl=ru).** Initially, it is hard-coded for [Samsung 10e](https://www.samsung.com/global/galaxy/galaxy-s10/specs/), but can be applied for wider range of smartphones that satisfy below mentioned parameters.

OpenCamera Remote is an extended [Open Camera](https://play.google.com/store/apps/details?id=net.sourceforge.opencamera&hl=ru) application that allows to start photo/video recording over network using UDP-packages. It also supports "simultaneous" start of recording from several smartphones by using broadcast UDP messages. "Simultaneous" means that errors in synchronization depend on network bandwidth, anyway they are less than run application manually. Can be useful in experiments that require automatisation of running video recorder.

Our main contribution to this application is adding IMU recording (built-in smartphone gyroscope, accelerometer) and "hardware" **synchronization** IMU timestamps with frame timestamps. Note, that the synchronization will not work on all smartphones, only those one that support [parameter](https://developer.android.com/reference/android/hardware/camera2/CameraMetadata#SENSOR_INFO_TIMESTAMP_SOURCE_REALTIME) `SENSOR_INFO_TIMESTAMP_SOURCE_REALTIME`.

##### Here you can download APK (??? TBD)

#### Good practices and tips to use this application in scientific scenarios

* Check ``SENSOR_INFO_TIMESTAMP_SOURCE_REALTIME`` on your Android smartphone to make sure that IMU timestamps and camera frame timestamps belong to one source, in other case synchronization will not work.
* Check  `CONTROL_VIDEO_STABILIZATION_MODE`  and disable stabilization option (if it is possible for your smartphone) to decrease affection of preprocessing that done by smartphone.
*  Using remote start of recording, make sure that you create local network with small traffic, it is better to disable incoming Internet connections to avoid any load in your network. In that case delay for different recordings will be more predictable and can be used in your setup for synchronization with other devices.
* To remove rolling shutter effect, you may play with exposure time parameter. But this will cause darker frames, so you need to play with ISO or light in your environment. 
* You can play with bitrate to increase the quality of the video, but Open Camera developers don't sure that at high bitrate will be recorded correctly, it is your responsibility, so you need some experiments. It depends on you RAM and video duration.

#### How to record?

To record, install an application from the APK (??? TBD) or build it from the sources. Pay attention, that there is an external dependency from the OpenCV, so you need to download its' SDK and add SDK as module in the project.  

##### Manual recording

1. Choose camera that you need
2. Correct its exposure time parameters and ISO
3. Set up the bitrate
4. Press record and go to the next section

##### Auto recording over network

TBD

#### Where can I find recorded results and how they organized?

All videos recorded by the camera are located in OpenCamera folder on the disk (usually in DCIM).

Additional information are stored in the folder `videoSensors`. Here you will find folders that correspond to the recorded videos. They are named by the date and time of recording to easily find what you need.

In every folder you will find:

* `<date>acc.csv` — csv-file with **accelerometer data** in format `X-data, Y-data, Z-data, timestamp (ns)`
* `<date>gyro.csv` — csv-file with **gyroscope data** in format `X-data, Y-data, Z-data, timestamp (ns)`
*  `<date>frame.csv` — csv-file with **frame timestamps (ns)**  from the recorded video
* `<timestamp>.png` — subset of frames from the recorded video with its timestamp (ns) in the filename. Sometimes, when split recorded video in the frames, their count is greater than timestamps. Usually, recorder add some additional duplicate frames in the end or in the beginning of the video, so saved frames can be useful to restore that mismatching.