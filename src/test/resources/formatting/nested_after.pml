int x = 100;

init {
    // test
    atomic {
        {
            x = 10
        }
    }
    for (i : 1..5) {
        {
            {
                {
                    /* nested for loop */
                    i > 100
                }
            }
        }
    }
}
