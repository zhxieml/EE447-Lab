from socket import *

serverName = '192.168.0.119'
serverPort = 12000
clientSocket = socket(AF_INET, SOCK_STREAM)
clientSocket.connect((serverName, serverPort))

sentence = input('Input sentence:')
clientSocket.send(bytes(sentence, encoding="utf8"))
checkedSentence = clientSocket.recv(1024)
print("Receive from Server: {}", str(checkedSentence, encoding="utf-8"))
clientSocket.close()
