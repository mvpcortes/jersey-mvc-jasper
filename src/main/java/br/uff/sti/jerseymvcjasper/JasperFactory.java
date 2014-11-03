/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.sti.jerseymvcjasper;

import br.uff.sti.jerseymvcjasper.proxy.JasperProxy;
import java.io.Reader;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author marcos
 */
public class JasperFactory {

    private ServletContext servletContext;
    
    private JasperProxy jasperProxy;
    
    public void setJasperProxy(JasperProxy jp){
        this.jasperProxy = jp;
    }

    JasperFactory(final ServletContext servletContext) {
            this.servletContext = servletContext;
    }

    JasperReport compile(Reader reader) {
        this.jasperProxy.compileReport(null)
    }

}
