package ch.zhaw.psit4.martin.frontend;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.aiController.AIControllerFacade;
import ch.zhaw.psit4.martin.models.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class FrontendControllerTest {

    @Mock
    private AIControllerFacade aiController;
    
    @Mock
    private MHistoryItem historyItem;
    
    @InjectMocks
    FrontendController controller;
    
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void ElaborateRequestReturnsResponseObjectFromObject(){
        MResponse response = new MResponse("Hallo");
        MResponse response1;
        MRequest request = new MRequest("ciao", false);
        when(aiController.elaborateRequest(request)).thenReturn(response);    
        
        response1 = aiController.elaborateRequest(request);
        assertTrue(response1.equals(response));       
    }
    
    @Test
    public void checkIfHistoryItemIsreturned() {
        List<MHistoryItem> list = new ArrayList<MHistoryItem>();
        List<MHistoryItem> newlist = new ArrayList<MHistoryItem>();
        
        when(aiController.getHistory()).thenReturn(list);
        
        newlist = aiController.getHistory();
        assertEquals(newlist, list);  
    }
    
    
    
    
    
}
