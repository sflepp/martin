package ch.zhaw.psit4.martin.api.validation;

/**
 * The return values for test results.
 * 
 * OK: Tests successful
 * WARNING: Tests successful, but might not use some functionality.
 * ERROR: Tests unsuccessful
 * 
 * @version 0.0.1-SNAPSHOT
 */
public enum MartinAPITestResult {
    OK(2), WARNING(1), ERROR(0);
    
    private int value;
    MartinAPITestResult() {
        this.value = -1;
    }
    MartinAPITestResult(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
}
