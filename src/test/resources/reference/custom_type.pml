typedef struct_t {
    int x = 1;
}

typedef struct2_t {
    int x = 1;
}

init {
    <caret>struct_t struct;
}

typedef struct3_t {
    int x = 1;
}
