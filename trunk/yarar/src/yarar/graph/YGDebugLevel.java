package yarar.graph;

/** Used by {@link YGAbstractElement#debugLevel}. */
enum YGDebugLevel {
    /** outputs only {@link YGAbstractElement#internalID} */
    MIN,
    /** outputs {@link YGAbstractElement#internalID} and class name */
    MORE,
    /**
     * Outputs the {@link YGElementType}, the {@link YGAbstractElement#internalID} and
     * <code>super.toString()</code> value.
     */
    FULL
}