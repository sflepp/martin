package ch.zhaw.psit4.martin.pluginlib;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.zhaw.psit4.martin.api.Feature;

public class MartinContextAccessorTest {
    private Feature mockedFeatures[];

    @Before
    public void setUp() {
        // initialize the mocked feature list
        mockedFeatures = new Feature[20];
        for (int i = 0; i < mockedFeatures.length; i++) {
            mockedFeatures[i] = Mockito.mock(Feature.class);
            Mockito.when(mockedFeatures[i].getRequestID()).thenReturn((long) i);
        }
    }

    @Test
    public void testRegisterWorkItem() {
        // fill list
        MartinContextAccessor context = new MartinContextAccessor();
        for (int i = 0; i < mockedFeatures.length; i++) {
            context.registerWorkItem(mockedFeatures[i]);
        }
        assertEquals(context.getNumberOfFeatures(), mockedFeatures.length);
    }

    @Test
    public void testRegisterInvalidWorkItem() {
        // create mock and classes
        MartinContextAccessor context = new MartinContextAccessor();
        Feature mockedFeature = Mockito.mock(Feature.class);
        Mockito.doNothing().when(mockedFeature).setID(0);

        // test if mocked Feature has been rejected
        context.registerWorkItem(mockedFeature);
        assertEquals(context.getNumberOfFeatures(), 1);
    }

    @Test
    public void testClearWorkList() {
        // fill list
        MartinContextAccessor context = new MartinContextAccessor();
        for (int i = 0; i < mockedFeatures.length; i++) {
            context.registerWorkItem(mockedFeatures[i]);
        }
        assertEquals(context.getNumberOfFeatures(), mockedFeatures.length);

        // clear list
        context.clearWorkList();
        assertEquals(context.getNumberOfFeatures(), 0);
    }

    @Test
    public void testfetchWorkItem() {
        // fill list
        MartinContextAccessor context = new MartinContextAccessor();
        for (int i = 0; i < mockedFeatures.length; i++) {
            context.registerWorkItem(mockedFeatures[i]);
        }
        assertEquals(context.getNumberOfFeatures(), mockedFeatures.length);

        // test not existing item
        Feature feature = context.fetchWorkItem(10000);
        assertNull(feature);

        // get items and check id
        for (int i = 0; i < mockedFeatures.length; i++) {
            feature = context.fetchWorkItem(i);
            assertNotNull(feature);
        }
    }
}
