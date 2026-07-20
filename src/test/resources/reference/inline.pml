int x = 10;

inline inlined_before() {
    x = 10;
}

inline inlined_before2() {
x = 10;
}

inline inline_inside() {
    <caret>inlined_before()
}

inline inlined_after() {
    x = 10;
}