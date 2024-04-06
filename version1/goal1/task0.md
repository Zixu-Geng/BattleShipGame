Goal 1: Minimal end to end system
---------------------------------


## Task 0. Project Setup
- You should already have your development environment setup from the previous tutorial.
- You already have a GitHub repository for the homework and you need to create the `build.gradle`.
- Now clone that project to your computer.
- Change into that directory (use `cd`)
- Initialize the gradle project (`gradle init`).  
    - Choose application, Java, Groovy, and JUNit Jupiter.  
    - Make the projectname "battelship" and the source `package edu.duke.YOURNETID.battleship`
    [replace YOURNETID with your netid]

This is what my interaction with gradle init looked like:
```
Select type of project to generate:
  1: basic
  2: application
  3: library
  4: Gradle plugin
Enter selection (default: basic) [1..4] 2

Select implementation language:
  1: C++
  2: Groovy
  3: Java
  4: Kotlin
  5: Swift
Enter selection (default: Java) [1..5] 3

Select build script DSL:
  1: Groovy
  2: Kotlin
Enter selection (default: Groovy) [1..2] 1

Select test framework:
  1: JUnit 4
  2: TestNG
  3: Spock
  4: JUnit Jupiter
Enter selection (default: JUnit 4) [1..4] 4
Project name (default: src): battleship
Source package (default: battleship): edu.duke.adh39.battleship
```

- Now, edit `build.gradle` (review more details on hwk 1 guide)
    - (1) in "plugins" put `id 'com.bmuschko.clover' version '2.2.3'`
    - (2) in "dependencies" add
    ```
    testImplementation 'org.junit.platform:junit-platform-launcher:1.6.2'
    clover 'org.openclover:clover:4.4.1'
    ```
    - (3) at the top level (outside of any other block put)
    ```
    test {
        testLogging {
            showStandardStreams = true
            exceptionFormat = 'full'
        }
    }
    clover {
        report{
            html = true
        }
        compiler {
        debug = true }
    }
    ```
- Before you proceed, run `./gradlew  dependencies`
- We aren't so much interested in the output, but it will make sure your build.gradle file
is valid, and check the dependency structure.  This mostly will help you identify any problems
before you proceed.
- Setup `.gitignore`: make sure all this is on your `.gitignore` file (check hwk 1 guide)
```
# Ignore Gradle project-specific cache directory
.gradle

# Ignore Gradle build output directory
build

app/build
app/bin

# Ignore backup and lock files
*~
```


>[!IMPORTANT]
> - Add all the files to source control (GitHub) using `git add -A`
> - Commit and Push 

>[!NOTE]
> - You can now proceed to [Task 1](./task1.md)