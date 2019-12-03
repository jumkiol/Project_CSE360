class SetBoundary {

    private static float lowerBound;
    private static float higherBound;
    private static SetBoundary boundaryObject;

    private SetBoundary(float low, float high) {
        lowerBound = low;
        higherBound = high;
    }

    static void updateBoundaries(float low, float high) {
        if (boundaryObject == null) {
            boundaryObject = new SetBoundary(low, high);
        } else {
            SetBoundary.setLowerBound(low);
            SetBoundary.setHigherBound(high);
        }

        MainPage.updateReport("Set Bounds to: " + low + ", " + high + "\n");
    }

    static float getLowerBound() {
        return lowerBound;
    }


    private static void setLowerBound(float lowerBound) {
        SetBoundary.lowerBound = lowerBound;
    }

    static float getHigherBound() {
        return higherBound;
    }

    private static void setHigherBound(float higherBound) {
        SetBoundary.higherBound = higherBound;
    }
}
