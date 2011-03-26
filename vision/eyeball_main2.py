import cv
from cv import *
#from operator import itemgetter, attrgetter
#import commands

#import numpy
import math
#import os
#import random
import sys
import time
#import matlab_syntax

#import socketTest

#s = socketTest.socketTest()
us = sys.argv[1]
assert (us == "yellow" or us == "blue"), "Invalid team choice, must be <yellow> or <blue> (without brackets)"

def camera():
	print "# Starting initialization..."
	#camera capture
	#cap = cv.CaptureFromCAM(0)
	intrinsics = cv.CreateMat(3, 3, cv.CV_64FC1)
	cv.Zero(intrinsics)
	#camera data
	intrinsics[0, 0] = 1100.850708957251072
	intrinsics[1, 1] = 778.955239997982062 
	intrinsics[2, 2] = 1.0
	intrinsics[0, 2] = 348.898495232253822
	intrinsics[1, 2] = 320.213734835526282
	dist_coeffs = cv.CreateMat(1, 4, cv.CV_64FC1)
	cv.Zero(dist_coeffs)
	dist_coeffs[0, 0] = -0.326795877008420
	dist_coeffs[0, 1] = 0.139445565548056
	dist_coeffs[0, 2] = 0.001245710462327
	dist_coeffs[0, 3] = -0.001396618726445
	#pFrame = cv.QueryFrame(cap)
	print "# intrinsics loaded!"

	#prepare memory
	capture = cv.CaptureFromCAM(0)
	src = cv.QueryFrame(capture)
	size = GetSize(src)
	dst0 = cv.CreateImage(size, src.depth, src.nChannels)
	# bg = cv.LoadImage("00000005.jpg")
	image_ROI = (0,70,640,340)
	size = (640,340)

	red = cv.CreateImage(size, 8, 1)
	green = cv.CreateImage(size, 8, 1)
	blue = cv.CreateImage(size, 8, 1)

	hue = cv.CreateImage(size, 8, 1)
	sat = cv.CreateImage(size, 8, 1)
	val = cv.CreateImage(size, 8, 1)
	ball = cv.CreateImage(size, 8, 1)
	yellow = cv.CreateImage(size, 8, 1)

	ballx = 0
	bally = 0

	ballmiss = 0
	yellowmiss = 0
	bluemiss = 0

	dst2 = cv.CreateImage(size, 8, 3)
	hsv = cv.CreateImage(size,8,3)
	print "# base images created..."
#####------------------ajustment data---------------------###############
#shadow
	high = 40
	low = 300

#threshold
	thresBallInit = 116
	thresYellowInit = 94
	thresBlueInit = 18
	ballRangeInit = 8.0
	yellowRangeInit = 5.0
	blueRangeInit = 8.0
	ballRange = ballRangeInit
	yellowRange = yellowRangeInit
	blueRange = blueRangeInit
	ballMinRange = 1.5
	yellowMinRange = 2.5
	blueMinRange = 8.0
	thresBall = thresBallInit
	thresYellow = thresYellowInit
	thresBlue = thresBlueInit

#dilate
	ex = cv.CreateStructuringElementEx(3,3,1,1,cv.CV_SHAPE_RECT)
	ex2 = cv.CreateStructuringElementEx(2,2,1,1,cv.CV_SHAPE_RECT)
	ex5 = cv.CreateStructuringElementEx(5,5,1,1,cv.CV_SHAPE_RECT)

#ball
	ballcount = 15.0
	ballAreaInit = 105.0
	ballAreaRangeInit = 60.0
	ballArea = ballAreaInit
	ballAreaRange = ballAreaRangeInit
	ballMinAreaRange = 40.0
	ballcompact = 3.0

#blue
	bluecount = 30.0
	blueAreaInit = 300.0
	blueAreaRangeInit = 150.0
	blueArea = blueAreaInit
	blueAreaRange = blueAreaRangeInit
	blueMiniAreaRange = 50.0
	bluemaxdepth = 8.0
	blueminidepth = 2.5

#yellow
	yellowcount = 30.0
	yellowAreaInit = 450.0
	yellowAreaRangeInit = 200.0
	yellowArea = yellowAreaInit
	yellowAreaRange = yellowAreaRangeInit
	yellowMinAreaRange = 50.0
	yellowmaxdepth = 10.0
	yellowminidepth = 3.2




#####----------------------------------------




	#create window
	NamedWindow("camera",cv.CV_WINDOW_AUTOSIZE)
	#NamedWindow("ball",cv.CV_WINDOW_AUTOSIZE)
	#NamedWindow("yellow",cv.CV_WINDOW_AUTOSIZE)
	#NamedWindow("blue",cv.CV_WINDOW_AUTOSIZE)

	#NamedWindow("hue",cv.CV_WINDOW_AUTOSIZE)
	#NamedWindow("sat",cv.CV_WINDOW_AUTOSIZE)
	#NamedWindow("val",cv.CV_WINDOW_AUTOSIZE)
	timecount = 0

	onesec =  time.clock()
	storage = cv.CreateMemStorage()
	print "# starting capture..."
	print ''
	capture = cv.CaptureFromCAM(0)
	aa =  time.clock()
	while(True):
		timecount = timecount + 1
		src = cv.QueryFrame(capture)
		
		#barrel undistortion
		cv.Undistort2(src, dst0, intrinsics, dist_coeffs)
		#ROI = Region of Interests, crop the image 
		cv.SetImageROI(dst0,image_ROI)
		dst = GetImage(dst0)
		CvtColor(dst,hsv,CV_RGB2HSV)
		cv.Split(hsv,hue,sat,val,None)

#		ShowImage("hue",hue)
#		ShowImage("val",val)
#		ShowImage("sat",sat)

		# BALL
		cv.Threshold(hue,ball,thresBallInit+ballRange, 255,cv.CV_THRESH_TOZERO_INV)
		cv.Threshold(hue,ball,thresBallInit-ballRange, 255,cv.CV_THRESH_BINARY)
		cv.Erode(ball,ball,ex2,1)
		cv.Dilate(ball,ball,ex2,1)
		ShowImage("ball",ball)
		# YELLOW
		cv.Threshold(hue,yellow,thresYellowInit+yellowRange,255,cv.CV_THRESH_TOZERO_INV)
		cv.Threshold(yellow,yellow,thresYellowInit-yellowRange,255,cv.CV_THRESH_BINARY)
		cv.Erode(yellow,yellow,ex2,1)
		cv.Dilate(yellow,yellow,ex2,1)
		ShowImage("yellow",yellow)

		# BLUE
#		CvtColor(dst,hsv,CV_BGR2HSV)
#		cv.Split(hsv,hue,sat,val,None)

		cv.Threshold(hue,blue,thresBlue+blueRange,255,cv.CV_THRESH_BINARY_INV)
#		cv.Threshold(blue,blue,4,255,cv.CV_THRESH_BINARY)
		cv.Erode(blue,blue,ex2,1)
		cv.Dilate(blue,blue,ex2,1)
		ShowImage("blue",blue)

		cv.Threshold(val,val,80,255,cv.CV_THRESH_BINARY_INV)
		cv.Threshold(sat,sat,80,255,cv.CV_THRESH_BINARY_INV)
		ShowImage("sat2",sat)
		ShowImage("val2",val)
		# Removes the walls
		Sub(blue,val,blue)
		Sub(blue,sat,blue)
		Sub(yellow,val,yellow)
		Sub(yellow,sat,yellow)
		Sub(ball,val,ball)
		Sub(ball,sat,ball)

		Set2D(ball,4,4,255)
		Set2D(blue,4,4,255)
		Set2D(yellow,4,4,255)
		ShowImage("yellow3",yellow)
		ShowImage("ball3",ball)
		ShowImage("blue3",blue)


		#find ball

		seq = cv.FindContours(ball,storage,cv.CV_RETR_LIST, cv.CV_LINK_RUNS)
		if seq != None:
			count = 0
			while (seq != None and count <= ballcount):
				count =count + 1
				area = cv.ContourArea(seq)+0.01
				compact =  ArcLength(seq)*ArcLength(seq)/(4*area*math.pi)
				if (area < 4 or area > (ballArea+ballAreaRange) or area < (ballArea-ballAreaRange)): #or compact >= ballcompact ):
					seq = seq.h_next()
					continue
				else:
					ballx = 0
					bally = 0
					for p in seq:
						ballx = ballx + p[0]
						bally = bally + p[1]
					ballx = int(float(ballx)/len(seq))
					bally = int(float(bally)/len(seq))

###############--------------Auto ajustment
					print "ball area %f" %area
					print "ball hue: %f" %hue[bally,ballx]
#					thresBall = 0.2*hue[bally,ballx]+0.2*thresBall + 0.5*thresBallInit
#					ballArea = area
#					if(ballRange > ballMinRange):
#						ballRange = ballRange -0.1
#					if(ballAreaRange > ballMinAreaRange):
#						ballAreaRange = ballAreaRange -1.0
					cv.Circle(dst,(ballx,bally),4,cv.CV_RGB(255,255,255),2,8,0)
					cv.Circle(dst,(ballx,bally),10,cv.CV_RGB(255,0,0),9,8,0)
					break
			if(count > ballcount or seq == None):
#				thresBall = thresBallInit
#				ballRange = 0.5*ballRange + 0.5*ballRangeInit
#				ballArea = ballArea + 10.0
#				ballAreaRange = ballAreaRange + 0.1
				print ballAreaRange
				ballx = 0
				bally = 0
				ballmiss = ballmiss + 1
				print "\r# error: ball not found  "



		#find blue
		seq = cv.FindContours(blue,storage,cv.CV_RETR_LIST, cv.CV_LINK_RUNS)
		if seq != None:
			count = 0
			while (seq != None and count <= bluecount):
				count =count + 1
				area = cv.ContourArea(seq)
				if(area < blueArea-blueAreaRange or area > blueArea+blueAreaRange):
					seq = seq.h_next()
					continue
				else:
					hull = None
					convex = None
					hull =cv.ConvexHull2(seq,storage)
					convex = cv.ConvexityDefects(seq,hull,storage)
					if (len(convex) > 1):
						convex = sorted(convex , key = lambda(k1,k2,k3,k4):k4)#sort by depth of the convex defect
						if (convex[len(convex)-1][3] < blueminidepth or convex[len(convex)-2][3] < blueminidepth or convex[len(convex)-1][3] > bluemaxdepth or convex[len(convex)-2][3] > bluemaxdepth ):
							seq = seq.h_next()
							continue
						else:
							#find the T
							blue_start1 = convex[len(convex)-1][0]
							blue_end1 = convex[len(convex)-1][1]
							blue_depth1 = convex[len(convex)-1][2]

							#draw the side line of T


							blue_start2 = convex[len(convex)-2][0]
							blue_end2 = convex[len(convex)-2][1]
							blue_depth2 = convex[len(convex)-2][2]


							blue_from = ((blue_depth1[0]+blue_depth2[0])/2,(blue_depth1[1]+blue_depth2[1])/2)#calculate the center of robot

							#calculate the end of direction vector, the two end point of the smaller distans
							if math.hypot(blue_start1[0]-blue_end2[0],blue_start1[1]-blue_end2[1])>math.hypot(blue_end1[0]-blue_start2[0],blue_end1[1]-blue_start2[1]):
								blue_to = ((blue_end1[0]+blue_start2[0])/2,(blue_end1[1]+blue_start2[1])/2)
							else:
								blue_to = ((blue_start1[0]+blue_end2[0])/2,(blue_start1[1]+blue_end2[1])/2)
							cv.Line(dst,blue_from,blue_to,cv.CV_RGB(255,0,255),2,8,0)
							cv.Circle(dst,blue_from,1,cv.CV_RGB(255,0,0),2,8,0)
							cv.Circle(dst,blue_to,1,cv.CV_RGB(0,0,0),2,8,0)
							cv.Circle(dst,blue_from,10,cv.CV_RGB(255,0,0),9,8,0)

#######---------------------------Auto Ajusting
							print "blue area %f" %area
#							print "blue hue: %f" %hue[blue_from[1],blue_from[0]]
#							thresBlue = hue[blue_from[1],blue_from[0]] #+ 0.4*thresBlue + 0.5*thresBlueInit
#							blueArea = area
#							if(blueAreaRange > blueMiniAreaRange):
#								blueAreaRange = blueAreaRange -1.0
#							if(blueRange > blueMinRange):
#								blueRange = blueRange - 0.1
							break
					else:
						seq = seq.h_next()
						continue
			if(count > bluecount or seq == None):
#				thresBlue = thresBlueInit
#				blueAreaRange = blueAreaRange + 10.0
#				blueArea = blueArea + 10.0
#				blueRange = 0.5*blueRange + 0.5*blueRangeInit
				bluemiss = bluemiss + 1
				blue_from = (0,0);
				blue_to = (0,0);
				print "\r# error: blue not found  "

		#find yellow
		seq = cv.FindContours(yellow,storage,cv.CV_RETR_LIST, cv.CV_LINK_RUNS)
		if seq != None:			
			count = 0
			while (seq != None and count <= yellowcount):
				count =count + 1
				area = cv.ContourArea(seq)
				if(area < yellowArea-yellowAreaRange or area > yellowArea + yellowAreaRange):
					seq = seq.h_next()
					continue
				else:
					hull = None
					convex = None
					hull =cv.ConvexHull2(seq,storage)
					convex = cv.ConvexityDefects(seq,hull,storage)
					if (len(convex) > 1):
						convex = sorted(convex , key = lambda(k1,k2,k3,k4):k4)#sort by depth of the convex defect
						if (convex[len(convex)-1][3] < yellowminidepth or convex[len(convex)-2][3] < yellowminidepth or convex[len(convex)-1][3] > yellowmaxdepth or convex[len(convex)-2][3] > yellowmaxdepth ):
							seq = seq.h_next()
							continue
						else:
							#find the T
							yellow_start1 = convex[len(convex)-1][0]
							yellow_end1 = convex[len(convex)-1][1]
							yellow_depth1 = convex[len(convex)-1][2]
					
							#draw the side line of T

							yellow_start2 = convex[len(convex)-2][0]
							yellow_end2 = convex[len(convex)-2][1]
							yellow_depth2 = convex[len(convex)-2][2]
					
							yellow_from = ((yellow_depth1[0]+yellow_depth2[0])/2,(yellow_depth1[1]+yellow_depth2[1])/2)#calculate the center of robot
				
							#calculate the end of direction vector, the two end point of the smaller distans
							if math.hypot(yellow_start1[0]-yellow_end2[0],yellow_start1[1]-yellow_end2[1])>math.hypot(yellow_end1[0]-yellow_start2[0],yellow_end1[1]-yellow_start2[1]):
								yellow_to = ((yellow_end1[0]+yellow_start2[0])/2,(yellow_end1[1]+yellow_start2[1])/2)
							else:
								yellow_to = ((yellow_start1[0]+yellow_end2[0])/2,(yellow_start1[1]+yellow_end2[1])/2)


###########------------------------------Auto Ajusting
#							print cv.ContourArea(seq)
							print "yellow area %f" %area
							print "yellow hue: %f" %hue[yellow_from[1],yellow_from[0]]
#							thresYellow = hue[yellow_from[1],yellow_from[0]] #+ 0.4*thresYellow + 0.5*thresYellowInit
#							yellowArea = area
#							if(yellowRange > yellowMinRange):
#								yellowRange = yellowRange -0.1
#							if(yellowAreaRange > yellowMinAreaRange):
#								yellowAreaRange = yellowAreaRange - 1.0
#							yellow_miss = ((yellow_from[0]+yellow_to[0])/2,(yellow_from[1]+yellow_to[1])/2)

							cv.Line(dst,yellow_from,yellow_to,cv.CV_RGB(255,0,255),2,8,0)
							cv.Circle(dst,yellow_from,1,cv.CV_RGB(255,0,0),2,8,0)
							cv.Circle(dst,yellow_to,1,cv.CV_RGB(0,0,0),2,8,0)
							cv.Circle(dst,yellow_from,10,cv.CV_RGB(255,0,0),9,8,0)
							break
					else:
						seq = seq.h_next()
						continue
			if(count > yellowcount or seq == None):
				#thresYellow = thresYellowInit
#				yellowRange = 0.5*yellowRange + 0.5*yellowRangeInit
				#yellowArea = yellowArea
#				yellowAreaRange = yellowAreaRange + 10.0
				print "area:%d" %yellowArea
				yellowmiss = yellowmiss + 1
				yellow_from = (0,0);
				yellow_to = (0,0);
				print "\r# error: yellow not found"
		ballpos = (ballx,bally)
		#output(ballpos,blue_from,blue_to,yellow_from,yellow_to)
		ShowImage("camera",dst)
		cv.SetImageROI(dst0,(0,0,640,480))
#		ShowImage("camera",dst0)
		
		if( cv.WaitKey(2) >= 0 ):
			bb =  time.clock()
			print "frame rate: %f" %(timecount/(bb-aa))
			print "ball miss rate: %f" %(ballmiss)
			print "blue miss rate: %f" %(bluemiss)
			print "yellow miss rate: %f" %(yellowmiss)
			break;
def output(ballpos,blue_from,blue_to,yellow_from,yellow_to):
 	if (us == "yellow"):
 		print ballpos,yellow_from,blue_from
# 		s.send(ballpos[0],ballpos[1],yellow_from[0],yellow_from[1],yellow_to[0],yellow_to[1],blue_from[0],blue_from[1],blue_to[0],blue_to[1])
 	elif(us == "blue"):
 		print ballpos,blue_from,yellow_from
# 		s.send(ballpos[0],ballpos[1],blue_from[0],blue_from[1],blue_to[0],blue_to[1],yellow_from[0],yellow_from[1],yellow_to[0],yellow_to[1])
	
if __name__ == "__main__":
	camera();

