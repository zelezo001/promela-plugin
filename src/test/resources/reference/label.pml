init {
    int x = 10;
L1:
    x=1

    {
        int xxxx =1
LN1:
        false
    }

    {
        {
            goto <caret>LN1;
        }
    }

L3:
    skip
}

int xxx;

init {
L2:
    printf("hello there")
    goto L2;
}