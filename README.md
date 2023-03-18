# Load App

> In this project, there is an app to download a file from Internet by clicking on a custom-built button where:
 - The width of the button gets animated from left to right.
 - The text gets changed based on different states of the button.
 - The circle gets be animated from 0 to 360 degrees.

> A notification will be sent once the download is complete. When a user clicks on notification, the user lands on detail activity and the notification gets dismissed. In detail activity, the status of the download will be displayed and animated via MotionLayout upon opening the activity.

[The final look of the app](https://gph.is/g/Zywmnre)


## Getting Started

> After downloading the starter code, I started to create the options radio-buttons list for the 3 links. After that, I created a custom downloading button with a meaningful downloading animation. Finally, I made a notification to notify the user after the download is finished successfully and enabled in the notification action to go to the download details page (fragment).

### Dependencies
> This app is working on Android versions from Android 7.0 (Nougat) to Android 11 (Red Velvet Cake).

Gradle Versions:
 - Android Gradle Plugin Version: 4.0.1
 - Gradle Version: 6.1.1

> There are the other dependencies to enable them in the build.gradle (module) file:

```
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation 'androidx.appcompat:appcompat:1.0.2'
    implementation 'androidx.core:core-ktx:1.0.2'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test:runner:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.1.1'
```

### Installation

Step by step explanation of how to get a dev environment running.

List out the steps:

```
1. Open Android Studio Application
2. Choose "Open an existing Android Studio Project"
3. In the opened finder find `nd940-c3-advanced-android-programming-project-starter` folder
4. Click on the folder and select `starter` folder and click on "Open" button
5. Once the project is opened in Android studio, go to File -> Sync Project with gradle files
6. Click on "Run" button in Android Studio to install the project on the phone or emulator
```

## Built With

* [Android Studio](https://developer.android.com/studio) - Default IDE used to build android apps
* [Kotlin](https://kotlinlang.org/) - Default language used to build this project

Include all items used to build project.

## License
Please review the following [license agreement](https://bumptech.github.io/glide/dev/open-source-licenses.html)

## Project Screens' Images


![1](https://user-images.githubusercontent.com/57845885/226131056-ee622154-907e-47ff-b0ff-79b4005e6bed.png)
![2](https://user-images.githubusercontent.com/57845885/226131065-cfd6dcca-850c-4061-9d9e-5caa3c57e4c0.png)
![3](https://user-images.githubusercontent.com/57845885/226131070-752982c5-6e8b-42fd-90fd-e1629696e401.png)
![4](https://user-images.githubusercontent.com/57845885/226131077-dc54244f-6c97-415a-b28c-a365a33e535b.png)
![5](https://user-images.githubusercontent.com/57845885/226131086-c8b04184-5dab-4078-b66d-4158d9f7076b.png)
