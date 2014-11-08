MAIN_FILE = Main
ARGS = 
PACKAGE = 

JFLAGS = -g
JC = javac
RUN = java #-Xmx1024m
WORKSPACE = '/Users/matslexell/Documents/Mats/IT/Programmering/workspace


.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

default: clean compile run

clean:
	$(RM) $(PACKAGE)*.class

compile:
	javac $(PACKAGE)*.java

run:
	$(RUN) $(MAIN_FILE) $(ARGS)
