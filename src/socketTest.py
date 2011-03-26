#client echo
import socket
#simple class to send messages
class socketTest:
	
	global s
	global HOST
	global PORT
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	HOST = 'localhost'
	PORT = 4560
	#connect
	def __init__(self):
		s.connect((HOST, PORT))
	#send something to the server	
	def send(self, ball_x, ball_y, stewie_from_x, stewie_from_y, stewie_to_x, stewie_to_y, lois_from_x, lois_from_y, lois_to_x, lois_to_y, our_goal_x, our_goal_y, their_goal_x, their_goal_y):
		msg = "%f,%f;%f,%f;%f,%f;%f,%f;%f,%f;%f,%f;%f,%f;"%(ball_x, ball_y, stewie_from_x, stewie_from_y, stewie_to_x, stewie_to_y, lois_from_x, lois_from_y, lois_to_x, lois_to_y, our_goal_x,our_goal_y,their_goal_x,their_goal_y)
		s.sendall(msg+"\n")
		#data = s.recv(1024)
		#if(int(data) == 1):
		#	print "message sent correctly"
		#else:
		#	print "message not sent correctly"
	def sendshort(self,message):
		s.sendall(message+"\n")
	#close the connection, have to work some more on this
	def close(self):
		s.close()

		
