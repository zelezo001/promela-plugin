#DEFINE NUMBER 10

#IFDEF NUMBER
int value = NUMBER;
#ENDIF

#IF NUMBER > 9
active proctype proc() {
    value = NUMBER;
}
#ELSE
int value = 10;
#ENDIF