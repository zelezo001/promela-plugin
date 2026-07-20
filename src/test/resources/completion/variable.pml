int x_yes1 = 10;

init {
    {
        int x_no1 = 2;
    }
    int x_yes3 = 3;
    {
        int x_yes2 = 3;
    <caret>
        int x_no1 = 3;
    }
    int x_no1 = 3;
}

int x_no1 = 3;