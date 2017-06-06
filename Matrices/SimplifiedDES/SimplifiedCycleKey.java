package Matrices.SimplifiedDES;

/**
 * Created by Michele Iessi on 06/06/17.
 */
public class SimplifiedCycleKey {

    private String[] cycleKeys;

    private int ITERATIONS;


    public SimplifiedCycleKey(int iterations) {
        this.ITERATIONS = iterations;
        this.cycleKeys = new String[ITERATIONS];
    }

    public void computeCycleKeys(String key) {

        for(int i=0; i<ITERATIONS; i++) {
            cycleKeys[i] = key.substring(i+1) + key.substring(0, i+1);
        }
    }

    public String[] getCycleKeys() {
        return this.cycleKeys;
    }

}
