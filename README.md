tewie Bot - Team Family Geeks : System Design Project 2011 
===========================================================

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


