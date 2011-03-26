import cv
from cv import *
import math
import sys
import time
import socket

us = sys.argv[1]
pitchSet = 0
hostname = 'localhost'
port = 4560
global debug
debug = False
try:
	pitchSet = int(sys.argv[2])
except:
	pitchSet = 0
try:
    hostname = sys.argv[3]
except:
    hostname = 'localhost'
try:
	if(sys.argv[4]):
		debug = True
	else: debug = False
except:
	debug = False

global s
global connected
connected = False

steweis_goal=(0,0)
loiss_goal=(0,0)
assert (us == "yellow" or us == "blue"), "Invalid team choice, must be <yellow> or <blue> (without brackets)"

def find_goals(size,stewie):
	(x,y)= size
	(us_x,us_y)=stewie
	if(abs(us_x-0)<abs(us_x-x)):
		return [(0, y/2),(x, y/2)]
	else:
		return[(x,y/2),(0,y/2)]

def pitch_detect(intrinsics, dist_coeffs, dst0):
	capture = cv.CaptureFromCAM(0)
	src = cv.QueryFrame(capture)
	cv.SetImageROI(dst0,(0,0,640,480))
	cv.Undistort2(src, dst0, intrinsics, dist_coeffs)
	cv.SetImageROI(dst0,image_ROI)
	dst = GetImage(dst0)
	hsv = cv.CreateImage(size,8,3)
	CvtColor(dst,hsv,CV_RGB2HSV)
	cv.Split(hsv,hue,sat,val,None)		
	hist = cv.CreateHist([32,64], CV_HIST_ARRAY, [[0,180], [0,256]], 1)
	cv.CalcHist([hue, sat], hist, 0, None)
	values = cv.GetMinMaxHistValue(hist)
	tweak = values[3][0]
	return tweak

def camera():
	found_goals=False
	print "# Starting initialization..."
	intrinsics = cv.CreateMat(3, 3, cv.CV_64FC1)
	cv.Zero(intrinsics)
	
	#camera data
	intrinsics[0, 0] = 850.850708957251072
	intrinsics[1, 1] = 778.955239997982062 
	intrinsics[2, 2] = 1
	intrinsics[0, 2] = 320.898495232253822
	intrinsics[1, 2] = 380.213734835526282
	dist_coeffs = cv.CreateMat(1, 4, cv.CV_64FC1)
	cv.Zero(dist_coeffs)
	dist_coeffs[0, 0] = -0.226795877008420
	dist_coeffs[0, 1] = 0.139445565548056
	dist_coeffs[0, 2] = 0.001245710462327
	dist_coeffs[0, 3] = -0.001396618726445
	print "# intrinsics loaded!"
	
	#prepare memory
	capture = cv.CaptureFromCAM(0)
	src = cv.QueryFrame(capture)
	size = GetSize(src)
	dst0 = cv.CreateImage(size, src.depth, src.nChannels)
	image_ROI = (0,60,640,340)
	size = (640,340)
	hue = cv.CreateImage(size, 8, 1)
	sat = cv.CreateImage(size, 8, 1)
	val = cv.CreateImage(size, 8, 1)
	ball = cv.CreateImage(size, 8, 1)
	yellow = cv.CreateImage(size, 8, 1)
	blue = cv.CreateImage(size, 8, 1)
	Set2D(hue,4,4,255)
	Set2D(sat,4,4,255)
	Set2D(val,4,4,255)
	Set2D(ball,4,4,255)
	Set2D(yellow,4,4,255)
	Set2D(blue,4,4,255)

	ballx = 0
	bally = 0
	
	print "# base images created..."
#####------------------adjustment data---------------------###############
#shadow
	high = 40
	low = 300

#threshold
	thresred = 160
	thresgreen = 220
	thresblue = 254

#dilate
	ex = cv.CreateStructuringElementEx(3,3,1,1,cv.CV_SHAPE_RECT)
	ex2 = cv.CreateStructuringElementEx(2,2,1,1,cv.CV_SHAPE_RECT)
	ex5 = cv.CreateStructuringElementEx(5,5,1,1,cv.CV_SHAPE_RECT)
	tHack = cv.CreateStructuringElementEx(3,3,1,1,cv.CV_SHAPE_CROSS)
	
#ball
	ballcount = 15
	ballmaxarea = 200
	ballminiarea = 45
	ballcompact = 1.3

#blue
	bluecount = 30
	bluemaxarea = 1500
	blueminiarea = 50
	bluemaxdepth = 10
	blueminidepth = 2
	

#yellow 
	yellowcount = 30
	yellowmaxarea = 1000
	yellowminiarea = 50
	yellowmaxdepth = 10
	yellowminidepth = 3.2	




#####----------------------------------------

	aa =  time.time()
	storage = cv.CreateMemStorage()
	first = True
	pitch = 0 # 0 for main pitch, 1 for alt pitch
	countf=0
	print "# starting capture..."
	print ''
	capture = cv.CaptureFromCAM(0)	
	while(True):
		global connected
		if (not connected):
			global s
			s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
			try:
				s.connect((hostname,port))
				connected = True
			except:
				print "java down, waiting"
		src = cv.QueryFrame(capture)
		#ShowImage('src',src)
		cv.SetImageROI(dst0,(0,0,640,480))
		average = cv.CreateImage(size,8,3)
		#barrel undistortion
		cv.Undistort2(src, dst0, intrinsics, dist_coeffs)
		#ROI = Region of Interests, crop the image 
		cv.SetImageROI(dst0,image_ROI)
		dst = GetImage(dst0)
		dst2 = cv.CreateImage(size, 8, 3)
		Set2D(dst2,4,4,255)
		hsv = cv.CreateImage(size,8,3)
		CvtColor(dst,hsv,CV_RGB2HSV)
		cv.Split(hsv,hue,sat,val,None)
		if (first):
			#hist = cv.CreateHist([32,64], CV_HIST_ARRAY, [[0,180], [0,256]], 1)
			#cv.CalcHist([hue, sat], hist, 0, None)
			#values = cv.GetMinMaxHistValue(hist)
			
			#print values
			#tweak = values[3][0]
			#if tweak >= 12:
			#	pitch = 1
			#print ">>> tweak=",tweak,"pitch selected =",pitch
			
			pitch = pitchSet
			if pitch == 1:
				base = cv.LoadImage("base.jpg",cv.CV_LOAD_IMAGE_UNCHANGED)
				baseInv = cv.CreateImage(size,8,1)
				cv.Not(base,baseInv)
				#huecorr = cv.LoadImage("huecorr.jpg",cv.CV_LOAD_IMAGE_UNCHANGED)
				#cv.Smooth(huecorr,huecorr)		
				#ShowImage("base",base)
			#base = cv.CreateImage(size,8,1)
			#base = GetImage(val)
			#cv.Threshold(hue,hue,75,255,cv.CV_THRESH_BINARY_INV)							
			#cv.SaveImage("huecorr.jpg", hue)			
			#cv.Threshold(base,base,110,255,cv.CV_THRESH_BINARY)
			#cv.SaveImage("base.jpg", base)
			
			#cv.WaitKey(-1)
			first = False
		global debug
		if (debug):
			ShowImage("hue",hue)
			ShowImage("sat",sat)
			ShowImage("val",val)
			
		if pitch == 1:
			walls = cv.CreateImage(size,8,1)
			cv.Threshold(val,walls,50,255,cv.CV_THRESH_BINARY_INV)
			Set2D(walls,4,4,255)
			
			# BALL
			# fixed this cause with another robot it was finding the ball on it. seems to work			
			Add(sat,hue,ball)
			Sub(ball,walls,ball)
			#cv.SubS(ball,10,ball,baseInv)
			cv.Threshold(ball,ball,170,255,cv.CV_THRESH_BINARY)
			cv.Erode(ball,ball,ex,1)
			cv.Dilate(ball,ball,ex2,1)
			Set2D(ball,4,4,255)
			
			# YELLOW			
			# cv.Threshold(hue,yellow,80,255,cv.CV_THRESH_BINARY)
			cv.Threshold(val,yellow,250,255,cv.CV_THRESH_BINARY)
			Sub(yellow,walls,yellow)
			cv.Erode(yellow,yellow,ex,1)	
			Set2D(yellow,4,4,255)		
			
			# blue
			cv.Add(walls,hue,blue)
			cv.Threshold(blue,blue,40,255,cv.CV_THRESH_BINARY_INV)
			cv.Erode(blue,blue,ex2,2)
			Set2D(blue,4,4,255)
			cv.Dilate(blue,blue,tHack,2)
			
		if pitch == 0:
			ballcompact=2.0
			walls = cv.CreateImage(size,8,1)
			cv.Threshold(val,walls,50,255,cv.CV_THRESH_BINARY_INV)
			Set2D(walls,4,4,255)
				  
			# BALL
			#cv.Add(sat,val,ball)
			#ShowImage("rawB",ball)
			cv.Threshold(hue,ball,110, 255,cv.CV_THRESH_BINARY)
			cv.Erode(ball,ball,ex2,1)
			cv.Dilate(ball,ball,ex,1)
			
			# YELLOW
			cv.Threshold(val,yellow,240,255,cv.CV_THRESH_BINARY)
			# cv.Threshold(hue,yellow,80,255,cv.CV_THRESH_TOZERO)
			# cv.Threshold(yellow,yellow,105,255,cv.CV_THRESH_TOZERO_INV)
			# cv.Threshold(yellow,yellow,50,255,cv.CV_THRESH_BINARY)
			cv.Erode(yellow,yellow,ex,1)
			cv.Dilate(yellow,yellow,tHack,1)

			# BLUE
			CvtColor(dst,hsv,CV_BGR2HSV)
			cv.Split(hsv,hue,sat,val,None)
			cv.Threshold(hue,blue,80,255,cv.CV_THRESH_BINARY)
			cv.Threshold(val,val,80,255,cv.CV_THRESH_BINARY_INV)
						
			# Removes the walls
			Sub(blue,val,blue)
			Sub(yellow,val,yellow)
			Sub(ball,val,ball)
			cv.Erode(blue,blue,ex,1)
			
			Set2D(ball,4,4,255)
			Set2D(yellow,4,4,255)
			Set2D(blue,4,4,255)
			
		if (debug):
			ShowImage("blue",blue)
			ShowImage("yellow",yellow)
			ShowImage("ball",ball)
		#find ball
		#seq = None		
		seq = cv.FindContours(ball,storage,cv.CV_RETR_LIST, cv.CV_LINK_RUNS)
		if seq != None:
			count = 0
			#print seq
			while seq != None:			
				compact=0				
				count =count + 1
				if(count > ballcount):
					break
				#removed and pitch==0 no idea why it was there
				if (cv.ContourArea(seq) != 0 ):
					compact =  ArcLength(seq)*ArcLength(seq)/(4*cv.ContourArea(seq)*math.pi)
					if compact >= ballcompact:
						print ">> compact: ",compact, ballcompact
						seq = seq.h_next()
						continue
				area = cv.ContourArea(seq)
				if(area == 0 or area  > ballmaxarea or area < ballminiarea): # or compact > ballcompact):
					
					print ">> area: ",area, ballmaxarea, ballminiarea
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
				#	print "compact=%f,area=%f" %(compact,area)
					cv.Circle(dst,(ballx,bally),4,cv.CV_RGB(255,255,255),2,8,0)
					cv.Circle(dst2,(ballx,bally),4,cv.CV_RGB(255,255,255),2,8,0)
					break
			if(count > 15 or seq == None):				
				ballx = -1
				bally = -1
				print "# error: ball not found  "

				
		#find blue
		seq = None
		seq = cv.FindContours(blue,storage,cv.CV_RETR_LIST, cv.CV_LINK_RUNS)
		if seq != None:			
			count = 0
			while seq != None:
				count =count + 1
				if(count > bluecount):
					break
				if(cv.ContourArea(seq) < blueminiarea or cv.ContourArea(seq) > bluemaxarea):
					seq = seq.h_next()
					continue
				else:
					hull = None
					convex = None
					#
					hull =cv.ConvexHull2(seq,storage)
					convex = cv.ConvexityDefects(seq,hull,storage)
					if (len(convex) > 1):
						convex = sorted(convex , key = lambda(k1,k2,k3,k4):k4)#sort by depth of the convex defect
						if (convex[len(convex)-1][3] < blueminidepth or convex[len(convex)-2][3] < blueminidepth or convex[len(convex)-1][3] > bluemaxdepth or convex[len(convex)-2][3] > bluemaxdepth ):
							cv.Line(dst,convex[len(convex)-1][0],convex[len(convex)-1][2],cv.CV_RGB(0,0,255),2,8,0)
							cv.Line(dst,convex[len(convex)-1][2],convex[len(convex)-1][1],cv.CV_RGB(0,255,255),2,8,0)
							cv.Line(dst,convex[len(convex)-2][0],convex[len(convex)-2][2],cv.CV_RGB(0,0,255),2,8,0)
							cv.Line(dst,convex[len(convex)-2][2],convex[len(convex)-2][1],cv.CV_RGB(0,255,255),2,8,0)
							seq = seq.h_next()
							continue
						else:
							#find the T
							blue_start1 = convex[len(convex)-1][0]
							blue_end1 = convex[len(convex)-1][1]
							blue_depth1 = convex[len(convex)-1][2]
					
							#draw the side line of T
							cv.Line(dst,blue_start1,blue_depth1,cv.CV_RGB(0,0,255),2,8,0)
							cv.Line(dst,blue_depth1,blue_end1,cv.CV_RGB(0,255,255),2,8,0)
					
							cv.Line(dst2,blue_start1,blue_depth1,cv.CV_RGB(0,0,255),2,8,0)
							cv.Line(dst2,blue_depth1,blue_end1,cv.CV_RGB(0,255,255),2,8,0)

							blue_start2 = convex[len(convex)-2][0]
							blue_end2 = convex[len(convex)-2][1]
							blue_depth2 = convex[len(convex)-2][2]
							cv.Line(dst,blue_start2,blue_depth2,cv.CV_RGB(0,0,255),2,8,0)
							cv.Line(dst,blue_depth2,blue_end2,cv.CV_RGB(0,255,255),2,8,0)
					
							cv.Line(dst2,blue_start2,blue_depth2,cv.CV_RGB(0,0,255),2,8,0)
							cv.Line(dst2,blue_depth2,blue_end2,cv.CV_RGB(0,255,255),2,8,0)
					
							blue_from = ((blue_depth1[0]+blue_depth2[0])/2,(blue_depth1[1]+blue_depth2[1])/2)#calculate the center of robot
				
							#calculate the end of direction vector, the two end point of the smaller distans
							if math.hypot(blue_start1[0]-blue_end2[0],blue_start1[1]-blue_end2[1])>math.hypot(blue_end1[0]-blue_start2[0],blue_end1[1]-blue_start2[1]):
								blue_to = ((blue_end1[0]+blue_start2[0])/2,(blue_end1[1]+blue_start2[1])/2)
							else:
								blue_to = ((blue_start1[0]+blue_end2[0])/2,(blue_start1[1]+blue_end2[1])/2)
							cv.Line(dst,blue_from,blue_to,cv.CV_RGB(255,0,255),2,8,0)
							cv.Circle(dst,blue_from,1,cv.CV_RGB(255,0,0),2,8,0)
							cv.Circle(dst,blue_to,1,cv.CV_RGB(0,0,0),2,8,0)
					
							cv.Line(dst2,blue_from,blue_to,cv.CV_RGB(255,0,255),2,8,0)
							cv.Circle(dst2,blue_from,1,cv.CV_RGB(255,0,0),2,8,0)
							cv.Circle(dst2,blue_to,1,cv.CV_RGB(255,255,255),2,8,0)
							break
					else:
						seq = seq.h_next()
						continue
			if(count > bluecount or seq == None):
				blue_from = (0,0);
				blue_to = (0,0);
				print "# error: blue not found  "
		#find yellow
		seq = None
		seq = cv.FindContours(yellow,storage,cv.CV_RETR_LIST, cv.CV_LINK_RUNS)
		
		if seq != None:			
			count = 0
			while seq != None:
				count =count + 1
				if(count > yellowcount):
					break
				area = cv.ContourArea(seq)
				if(area < yellowminiarea or area > yellowmaxarea):
					seq = seq.h_next()
					continue
				else:
					hull = None
					convex = None
					#
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
							cv.Line(dst,yellow_start1,yellow_depth1,cv.CV_RGB(0,0,255),2,8,0)
							cv.Line(dst,yellow_depth1,yellow_end1,cv.CV_RGB(0,255,255),2,8,0)
					
							cv.Line(dst2,yellow_start1,yellow_depth1,cv.CV_RGB(0,0,255),2,8,0)
							cv.Line(dst2,yellow_depth1,yellow_end1,cv.CV_RGB(0,255,255),2,8,0)

							yellow_start2 = convex[len(convex)-2][0]
							yellow_end2 = convex[len(convex)-2][1]
							yellow_depth2 = convex[len(convex)-2][2]
							cv.Line(dst,yellow_start2,yellow_depth2,cv.CV_RGB(0,0,255),2,8,0)
							cv.Line(dst,yellow_depth2,yellow_end2,cv.CV_RGB(0,255,255),2,8,0)
					
							cv.Line(dst2,yellow_start2,yellow_depth2,cv.CV_RGB(0,0,255),2,8,0)
							cv.Line(dst2,yellow_depth2,yellow_end2,cv.CV_RGB(0,255,255),2,8,0)
					
							yellow_from = ((yellow_depth1[0]+yellow_depth2[0])/2,(yellow_depth1[1]+yellow_depth2[1])/2)#calculate the center of robot
				
							#calculate the end of direction vector, the two end point of the smaller distans
							if math.hypot(yellow_start1[0]-yellow_end2[0],yellow_start1[1]-yellow_end2[1])>math.hypot(yellow_end1[0]-yellow_start2[0],yellow_end1[1]-yellow_start2[1]):
								yellow_to = ((yellow_end1[0]+yellow_start2[0])/2,(yellow_end1[1]+yellow_start2[1])/2)
							else:
								yellow_to = ((yellow_start1[0]+yellow_end2[0])/2,(yellow_start1[1]+yellow_end2[1])/2)
							# print cv.ContourArea(seq)
							cv.Line(dst,yellow_from,yellow_to,cv.CV_RGB(255,0,255),2,8,0)
							cv.Circle(dst,yellow_from,1,cv.CV_RGB(255,0,0),2,8,0)
							cv.Circle(dst,yellow_to,1,cv.CV_RGB(0,0,0),2,8,0)
					
							cv.Line(dst2,yellow_from,yellow_to,cv.CV_RGB(255,0,255),2,8,0)
							cv.Circle(dst2,yellow_from,1,cv.CV_RGB(255,0,0),2,8,0)
							cv.Circle(dst2,yellow_to,1,cv.CV_RGB(255,255,255),2,8,0)
							break
					else:
						seq = seq.h_next()
						continue
			if(count > yellowcount or seq == None):
				yellow_from = (0,0);
				yellow_to = (0,0);
				print "# error: yellow not found"	
		ballpos = (ballx,bally)
		ShowImage("camera",dst)
		if(found_goals==False):
			if(us=="yellow"):
				goals=find_goals(size,yellow_from)
				stewies_goal=goals[0]
				loiss_goal=goals[1]				
				found_goals=True
			elif(us=="blue"):
				goals=find_goals(size,blue_from)
				stewies_goal=goals[0]
				loiss_goal=goals[1]				
				found_goals=True
		#if (ballx >= 0):
		output(ballpos,blue_from,blue_to,yellow_from,yellow_to,stewies_goal,loiss_goal)
		time_passed=time.time() - aa
		countf+=1				
		if(time_passed>=1):
			print "frame per second: " + str(countf),
			countf=0
			aa=time.time() 
		keyPress = cv.WaitKey(2)
		if( keyPress == 1048608 ):
			break
def output(ballpos,blue_from,blue_to,yellow_from,yellow_to,stewies_goal,loiss_goal):
	global connected
	global s
 	if (us == "yellow"):
 		print ballpos,yellow_from,yellow_to,blue_from,blue_to,stewies_goal,loiss_goal
		if (connected):
			try:
				msg = "%f,%f;%f,%f;%f,%f;%f,%f;%f,%f;%f,%f;%f,%f;"%(ballpos[0],ballpos[1],yellow_from[0],yellow_from[1],yellow_to[0],yellow_to[1],blue_from[0],blue_from[1],blue_to[0],blue_to[1],stewies_goal[0],stewies_goal[1],loiss_goal[0],loiss_goal[1])
				s.sendall(msg+"\n")
			except:
				print "connection lost"
 				connected = False
				s.close()
 	elif(us == "blue"):
 		print ballpos,blue_from,yellow_from,stewies_goal,loiss_goal
 		if (connected):
 			try:
				msg = "%f,%f;%f,%f;%f,%f;%f,%f;%f,%f;%f,%f;%f,%f;"%(ballpos[0],ballpos[1],blue_from[0],blue_from[1],blue_to[0],blue_to[1],yellow_from[0],yellow_from[1],yellow_to[0],yellow_to[1],stewies_goal[0],stewies_goal[1],loiss_goal[0],loiss_goal[1])
				s.sendall(msg+"\n")
			except:
				print "connection lost"
 				connected = False
 				s.close()
		
if __name__ == "__main__":
	camera();

