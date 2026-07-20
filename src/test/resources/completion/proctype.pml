int pc4_fake = 10;

proctype pc1() {
    int x = 1;
}
proctype pc2() {
    int x = 1;
    run <caret>
}

inline inlined() {
    x=1
}

proctype pc3() {
    int x = 1;
}
proctype pc4() {
    int x = 1;
}