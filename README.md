Stewie Bot - Team Family Geeks : System Design Project 2011 
===========================================================

This is the project page of a Lego Mindstorm robot that plays football. The robot gets information about the surrounding environment through sensors and a camera which is placed on top of a pitch. The vision system developed in OpenCV processes the feed from the camera giving the robot information about the different objects that reside in the pitch. The opponent robot, and a ball. The goal of the robot is to avoid the oponnent robot, take control of the ball and score in the opponents goal.

## Members:

* Maxim Cramer
* Daniel Stanoescu
* Alex Shearn
* Alessandro La Bianca
* Diana Bicazan
* Denes Findrik
* Ewan Leaver
* Peter Walsh
* Chenyang Xia
* Alex Gouvatsos

Superviser: Dominik Glodzik

## Instalatation Requirements

- Lejos: http://lejos.sourceforge.net/ - in order to compile the code and connect to the bot and issue commands
- OpenCV: http://opencv.willowgarage.com/wiki/ - so you can process the camerea feed

## Programming Languages Used

- Java
- Python
- LeJos (Java for the Mindstorm NXT brick)

## How To Run:

1. Grab a terminal.

2. Compile and link all the Java packages:

- `ant`

3. Connect to the robot and wait for the vision client to connect:

- `./run_me.sh`

4. Grab another terminal. And run the vision system:

- `cd src/vision/`
- `python merge.py <t-shape color> <0-1 for pitch selection>`

5. Have fun!


