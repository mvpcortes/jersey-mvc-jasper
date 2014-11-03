/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.sti.jerseymvcjasper;

import static br.uff.sti.jerseymvcjasper.JasperViewProcessor.STR_EXT_JRXML;
import br.uff.sti.jerseymvcjasper.proxy.JasperProxy;
import br.uff.sti.jerseymvcjasper.proxy.JasperProxyImpl;
import java.io.InputStream;
import java.io.Reader;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author marcos
 */
public class JasperFactory {

//    private ServletContext servletContext;
    private JasperProxy jasperProxy;
    private ServletContext servletContext;

    JasperFactory(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.jasperProxy = new JasperProxyImpl();
        
    }

    public void setJasperProxy(JasperProxy jp) {
        this.jasperProxy = jp;
    }


    JasperReport compile(Reader reader) {
        return null;
    }

    JasperReport getCompiledJasper(String name) throws JRException {
        InputStream os = this.getResource(name);
        return jasperProxy.compileReport(os);
    }

    public InputStream getResource(String name) {
        if (!name.endsWith(STR_EXT_JRXML)) {
            name += STR_EXT_JRXML;
        }
        
        if(!name.startsWith("/")){
            name = "/" + name;
        }
        
        return servletContext.getResourceAsStream("WEB-INF"+name);
    }

}
