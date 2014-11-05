package br.uff.sti.jerseymvcjasper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.uff.sti.jerseymvcjasper.proxy.JasperProxy;
import java.io.InputStream;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 *
 * @author marcos
 */
public class JasperFactoryTest {

    private JasperFactory jasperCacheService;

    private final String PRIMEIRO_STR_JASPER = "a.jrxml";
    private final String NAO_EXISTE_STR_JASPER = "nao_existe.jrxml";
    private final String EXISTE_MAS_FALHA_STR_JASPER = "existe_mas_falha.jrxml";
    private final InputStream PRIMEIRO_IS_JASPER = mock(InputStream.class);
    private final InputStream EXISTE_MAS_FALHA_IS_JASPER = mock(InputStream.class);
    private final JasperReport PRIMEIRO_JASPER = mock(JasperReport.class);
    private final JasperReport EXISTE_MAS_FALHA_JASPER = mock(JasperReport.class);

    @Parameters
    @DataProvider(name = "validNames")
    public Object[][] validNames(){
        return new Object[][]{
        {"a", "a"},
        {"a/b", "a/b"},
        {"a/b/c", "a/b/c"},
        {"a/b/c.a", "a/b/c.a"}
        };
    }
    
    @Parameters
    @DataProvider(name = "invalidNames")
    public Object[][] invalidNames(){
        return new Object[][]{
        {"a.jrxml", "a"},
        {"a/b.jrxml", "a/b"},
        {"a/b/c.jrxml", "a/b/c"},
        {"a/b/c.a.jrxml", "a/b/c.a"},
        {"/a.jrxml", "a"},
        {"/a/b.jrxml", "a/b"},
        {"/a/b/c.jrxml", "a/b/c"},
        {"/a/b/c.a.jrxml", "a/b/c.a"}
        };
    }
    
    private JasperProxy jasperProxy;

    private ServletContext servletContext;

    @BeforeTest
    public void beforeAll() {
        FixtureFactoryLoader.loadTemplates("br.uff.sti.diplomaexterno.model.fixture");
    }

    @BeforeMethod
    public void before() throws JRException {

        jasperCacheService = new JasperFactory();

        //mocking ResourceSource
        servletContext = criaServletContext();
//        jasperCacheService.setServletContext(servletContext);

        //mocking JasperProxy
        jasperProxy = criaJasperProxy();
        jasperCacheService.setJasperProxy(jasperProxy);
    }

    @Test()
    public void when_get_jasper_not_compiled_yet_then_compile_it() throws JRException {

        JasperReport jr = jasperCacheService.get(PRIMEIRO_STR_JASPER, this.servletContext);

        assertNotNull(jr);
        assertTrue(jr == PRIMEIRO_JASPER);
    }

    @Test()
    public void when_get_jasper_not_exists_then_throw_exception() throws JRException {
        try{
            JasperReport jr = jasperCacheService.get(NAO_EXISTE_STR_JASPER, this.servletContext);
        }catch(JRException re){
            assertEquals(re.getMessage(), String.format("Cannot found jrxml 'nao_existe'", NAO_EXISTE_STR_JASPER));
        }
    }
    
    @Test()
    public void when_get_jasper_exist_but_not_compile_then_throw_exception() throws JRException {
        try{
            jasperCacheService.get(EXISTE_MAS_FALHA_STR_JASPER, this.servletContext);
        }catch(JRException re){
            assertEquals(re.getMessage(), String.format("falhou", EXISTE_MAS_FALHA_STR_JASPER));
        }
    }
    
    @Test()
    public void when_get_jasper_with_name_without_extension_then_put_the_extension() throws JRException {
        try{
            jasperCacheService.get(EXISTE_MAS_FALHA_STR_JASPER, this.servletContext);
        }catch(JRException re){
            assertEquals(re.getMessage(), String.format("falhou", EXISTE_MAS_FALHA_STR_JASPER));
        }
    }
    
  
    @Test(dataProvider="validNames")
    public void when_send_valid_names_then_not_change_string(String actual, String expected){
        assertEquals(jasperCacheService.cleanName(actual), expected);
    }
    
    @Test(dataProvider="invalidNames")
    public void when_send_invalid_names_then_not_change_string(String actual, String expected){
        assertEquals(jasperCacheService.cleanName(actual), expected);
    }
        
    @Test()
    public void when_get_jasper_two_times_then_compile_only_one_time() throws  JRException {

        JasperReport jr = jasperCacheService.get(PRIMEIRO_STR_JASPER, this.servletContext);

        assertNotNull(jr);
        assertTrue(jr == PRIMEIRO_JASPER);
        
        //segunda vez
        jr = jasperCacheService.get(PRIMEIRO_STR_JASPER, this.servletContext);
        
        assertNotNull(jr);
        assertTrue(jr == PRIMEIRO_JASPER);
        assertEquals(jasperCacheService.cacheSize(), 1);
        verify(jasperProxy, times(1)).compileReport(servletContext.getResourceAsStream("/WEB-INF/"+PRIMEIRO_STR_JASPER));
    }

    private ServletContext criaServletContext(){
        ServletContext _servletContext = mock(ServletContext.class);
        doReturn(PRIMEIRO_IS_JASPER).when(_servletContext).getResourceAsStream("/WEB-INF/"+PRIMEIRO_STR_JASPER);
        doReturn(EXISTE_MAS_FALHA_IS_JASPER).when(_servletContext).getResourceAsStream("/WEB-INF/"+EXISTE_MAS_FALHA_STR_JASPER);
        return _servletContext;
    }
    
    private JasperProxy criaJasperProxy() throws JRException {
        JasperProxy jp = mock(JasperProxy.class);

        when(jp.compileReport(PRIMEIRO_IS_JASPER)).thenReturn(PRIMEIRO_JASPER);
        when(jp.compileReport(EXISTE_MAS_FALHA_IS_JASPER)).thenThrow(new JRException("falhou"));
        return jp;
    }

}
