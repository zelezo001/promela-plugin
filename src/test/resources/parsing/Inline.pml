int inlinedCall = 1; // 'inlinedCall' identifier not yet taken by inline

inline inlinedCall() {
    int inlined_value = 10;
}

init {
    inlinedCall()
}