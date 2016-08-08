package ch.zhaw.psit4.martin.api.util;
import static org.junit.Assert.*;
import org.junit.Test;


public class PairTest {

    @Test
    public void testPairsAreEqual() {
        Pair<Integer, Integer> pairEqual = new Pair<>(1, 1);
        Pair<Integer, Integer> pairEqual1 = new Pair<>(1, 1);;
        int value = pairEqual.compareTo(pairEqual1);
        assertTrue(value == 0);
    }

    @Test
    public void testPairsAreNotEqual() {
        Pair<Integer, Integer> pairNotEqual = new Pair<>(1, 2);
        Pair<Integer, Integer> pairNotEqual1 = new Pair<>(5, 5);
        int value = pairNotEqual.compareTo(pairNotEqual1);
        assertTrue(value == -1);
    }

}
