/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.sti.jerseymvcjasper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import org.glassfish.jersey.server.mvc.Viewable;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import org.testng.Assert;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author marcos
 */
public class JasperViewProcessorTest {

    JasperViewProcessor jasperViewProcessor;
    
    JasperFactory jasperFactory;

    Viewable viewable;
    
    JasperReport jasperReport;
    
    private ServletContext servletContext;

    @BeforeMethod
    public void before() throws JRException {
        jasperViewProcessor = new JasperViewProcessor();

        viewable = mock(Viewable.class);
        jasperFactory = mock(JasperFactory.class);
        servletContext = mock(ServletContext.class);
        jasperReport = mock(JasperReport.class);
        doReturn("/").when(jasperFactory).getRootResources(servletContext);
        doReturn(jasperReport).when(jasperFactory).compile("a", servletContext);
        jasperViewProcessor.setJasperFactory(jasperFactory);
        jasperViewProcessor.setServletContext(servletContext);
    }

    @Test
    public void when_find_jasper_model_with_a_list_then_return_a_jasper_model_with_this_list() {
        List list = ImmutableList.of("a", "b", "c");
        doReturn(list).when(viewable).getModel();
        
        JasperModel jm = this.jasperViewProcessor.findJasperModel(viewable);
        
        assertEquals(jm.getListModels().size(), 3);
        assertArrayEquals(jm.getListModels().toArray(), list.toArray());
        
        assertSame(jm.getListModels(), list);
        
    }
    
    @Test
    public void when_find_jasper_model_with_a_collection_then_return_a_jasper_model_with_a_list() {
        Set set = ImmutableSet.of("a", "b", "c");
        doReturn(set).when(viewable).getModel();
        
        JasperModel jm = this.jasperViewProcessor.findJasperModel(viewable);
        
        assertEquals(jm.getListModels().size(), 3);
        assertArrayEquals(jm.getListModels().toArray(), set.toArray());
        
        assertNotSame(jm.getListModels(), set);  
    }
    
    @Test
    public void when_find_jasper_model_with_a_not_collection_object_then_return_a_jasper_model_with_a_list() {
        String nome = "nome";
        doReturn(nome).when(viewable).getModel();
        
        JasperModel jm = this.jasperViewProcessor.findJasperModel(viewable);
        
        assertEquals(jm.getListModels().size(), 1);
        assertEquals(jm.getListModels().get(0), nome);
    }
    
    @Test
    public void when_find_jasper_model_with_a_jasper_model_then_return_the_same_jasper_model_in_a_list() {
        JasperModel<String> jasperModel = JasperModel.create(String.class).addModels("a");
        doReturn(jasperModel).when(viewable).getModel();
        
        JasperModel jm = this.jasperViewProcessor.findJasperModel(viewable);
        
        assertSame(jm, jasperModel);
    }
    
    @Test
    public void when_not_put_any_param_then_insert_default_params() {
        JasperModel<String> jasperModel = JasperModel.create(String.class).addModels("a");
        doReturn(jasperModel).when(viewable).getModel();
        assertEquals(jasperModel.getParameters().size(), 0);
        
        JasperModel jm = this.jasperViewProcessor.findJasperModel(viewable);
        
        Assert.assertEqualsNoOrder(jasperModel.getParameters().entrySet().toArray(), this.jasperViewProcessor.createWithDefaultProperties().entrySet().toArray());
    }
    
    @Test
    public void when_resolve_ok_then_return_jasper_report(){
        JasperReport jr = this.jasperViewProcessor.resolve("a", MediaType.APPLICATION_JSON_TYPE);
        
        assertEquals(jr, this.jasperReport);
    }
    
    @Test(expectedExceptions={IllegalStateException.class})
    public void when_resolve_fail_then_return_jasper_report() throws JRException{
        doThrow(new IllegalStateException()).when(this.jasperFactory).compile("a", servletContext);
        JasperReport jr = this.jasperViewProcessor.resolve("a", MediaType.APPLICATION_JSON_TYPE);
        
    }
    
}
