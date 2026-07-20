#DEFINE NUMBER 10000


int value = NUMBER;

#UNDEF NUMBER
#DEFINE NUMBER(x) (x + 10000)

init {
    assert(NUMBER(10) == value)
}