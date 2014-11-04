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
import org.testng.annotations.Test;

/**
 *
 * @author marcos
 */
public class JasperCacheServiceUnitarioTest {

    private JasperFactory jasperCacheService;

    private final String PRIMEIRO_STR_JASPER = "a.jrxml";
    private final String NAO_EXISTE_STR_JASPER = "nao_existe.jrxml";
    private final String EXISTE_MAS_FALHA_STR_JASPER = "existe_mas_falha.jrxml";
    private final InputStream PRIMEIRO_IS_JASPER = mock(InputStream.class);
    private final InputStream EXISTE_MAS_FALHA_IS_JASPER = mock(InputStream.class);
    private final JasperReport PRIMEIRO_JASPER = mock(JasperReport.class);
    private final JasperReport EXISTE_MAS_FALHA_JASPER = mock(JasperReport.class);

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
        jasperCacheService.setServletContext(servletContext);

        //mocking JasperProxy
        jasperProxy = criaJasperProxy();
        jasperCacheService.setJasperProxy(jasperProxy);
    }

    @Test(groups = {"unitario"})
    public void quando_obtem_um_jasper_nao_compilado_o_compila() throws JRException {

        JasperReport jr = jasperCacheService.compile(PRIMEIRO_STR_JASPER);

        assertNotNull(jr);
        assertTrue(jr == PRIMEIRO_JASPER);
    }

    @Test(groups = {"unitario"})
    public void quando_obtem_um_jasper_que_nao_existe_entao_gera_relatorio_exception() throws JRException {
        try{
            JasperReport jr = jasperCacheService.compile(NAO_EXISTE_STR_JASPER);
        }catch(JRException re){
            assertEquals(re.getMessage(), String.format("Cannot found jrxml 'nao_existe.jrxml'", NAO_EXISTE_STR_JASPER));
        }
    }
    
    @Test(groups = {"unitario"})
    public void quando_obtem_um_jasper_que_existe_porem_falha_na_hora_de_gerar_entao_gera_relatorio_exception() throws JRException {
        try{
            JasperReport jr = jasperCacheService.compile(EXISTE_MAS_FALHA_STR_JASPER);
        }catch(JRException re){
            assertEquals(re.getMessage(), String.format("falhou", EXISTE_MAS_FALHA_STR_JASPER));
        }
    }
        
    @Test(groups = {"unitario"})
    public void quando_obtem_um_jasper_duas_vezes_entao_compila_uma_unica_vez() throws  JRException {

        JasperReport jr = jasperCacheService.compile(PRIMEIRO_STR_JASPER);

        assertNotNull(jr);
        assertTrue(jr == PRIMEIRO_JASPER);
        
        //segunda vez
        jr = jasperCacheService.compile(PRIMEIRO_STR_JASPER);
        
        assertNotNull(jr);
        assertTrue(jr == PRIMEIRO_JASPER);
        assertEquals(jasperCacheService.cacheSize(), 1);
        verify(jasperProxy, times(1)).compileReport(servletContext.getResourceAsStream("WEB-INF/"+PRIMEIRO_STR_JASPER));
    }

    private ServletContext criaServletContext(){
        ServletContext _servletContext = mock(ServletContext.class);
        doReturn(PRIMEIRO_IS_JASPER).when(_servletContext).getResourceAsStream("WEB-INF/"+PRIMEIRO_STR_JASPER);
        doReturn(EXISTE_MAS_FALHA_IS_JASPER).when(_servletContext).getResourceAsStream("WEB-INF/"+EXISTE_MAS_FALHA_STR_JASPER);
        return _servletContext;
    }
    
    private JasperProxy criaJasperProxy() throws JRException {
        JasperProxy jp = mock(JasperProxy.class);

        when(jp.compileReport(PRIMEIRO_IS_JASPER)).thenReturn(PRIMEIRO_JASPER);
        when(jp.compileReport(EXISTE_MAS_FALHA_IS_JASPER)).thenThrow(new JRException("falhou"));
        return jp;
    }

}
