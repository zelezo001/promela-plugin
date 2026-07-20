// global chan
chan global_output = [0] of {
int
}

#define ARRAY_SIZE 14+32

#define PRINT_END (-1)

typedef int_tuple {
    int first, second
}

typedef defined_type {
    int size;
// struct comment
    int_tuple range;
}

defined_type defined_type_array[ARRAY_SIZE]

#define DISABLED 1

#ifdef DISABLED
//this is dissabled code
int disabled_value
#if DISABLED == 2
int nested_disabled_value
#endif
#else
// but this is enabled
int enabled_value;
#endif

// mtype documentation
mtype = {
generic_mtype_1, generic_mtype_2
}

// mtype:mtype_name documentation
mtype:mtype_name = {
enumeration_value_1, enumeration_value_1
}

// argument must be int comparable
inline inlined_code(argument) {
    if
    :: argument == 2 -> printf("inlined code called\n")
    fi
}

// prints ints from input
proctype print_channel(chan input) {
    int output_value // value read from input
    mtype mtype_instance = enumeration_value_1
    do
    :: input?output_value;
        if
        :: output_value == -1 -> break
        :: else -> printf("printing value: %d", output)
        fi
    od
    inlined_code(output_value)
}

active proctype main() priority 1 provided(1) {
    int x;
    run print_channel(global_output)
    for (x : 1 .. 10) {
        global_output !x
    }
    // struct field access
    defined_type_array[10].range.first
    global_output !-1
}