Stewie Bot - Team Family Geeks : System Design Project 2011 
===========================================================

This is the code repo for Team 10's SDP Project 2010/11, who's robot 
was named Stewie. Using a camera situated on top of the pitch, objects
are identified using the OpenCV Python library, then fed into the java
system by a TCP socket.

## Members:

* Maxim Cramer
* Diana Bicazan
* Denes Findrik
* Alex Gouvatsos
* Alessandro La Bianca
* Ewan Leaver
* Alex Shearn [shearn89@github](http://github.com/shearn89)
* Daniel Stanoesc [inquire@github](http://github.com/inquire)
* Peter Walsh
* Chenyang Xia

Supervisor: Dominik Glodzik

## Installation Requirements

- [Lejos](http://lejos.sourceforge.net/) - in order to compile the code and connect to the bot and issue commands
- [OpenCV](http://opencv.willowgarage.com/wiki/) - so you can process the camerea feed

## Programming Languages Used

- Java
- Python

## Usage

1. Grab a terminal.
2. Compile and link all the Java packages: `ant`
3. Connect to the robot: `./run_me.sh`
4. Grab another terminal, and run the vision system: `cd src/vision/`
    - `python merge.py <t-shape color> <0-1 for pitch selection> <java server IP> <anything for debug on>`
5. Watch your robot win, or have a :beer:

## Awesome features

- He tweets, if set up correctly for a different twitter account (you'll have to get your own API key).
- He has a nifty pause interface, allowing you to pick him up easily.
- He has a penalty defense mode.
- He's awesome.
