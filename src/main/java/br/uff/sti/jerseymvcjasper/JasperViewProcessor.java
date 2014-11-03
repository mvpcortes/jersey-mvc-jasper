/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.sti.jerseymvcjasper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import org.glassfish.jersey.server.mvc.Viewable;
import org.glassfish.jersey.server.mvc.spi.TemplateProcessor;

/**
 * {@link org.glassfish.jersey.server.mvc.spi.TemplateProcessor Template processor}
 * providing support for Freemarker templates.
 *
 * @author Marcos Côrtes (marcoscortes at id.uff.br)
 */
class JasperViewProcessor implements TemplateProcessor<JasperReport> {

    private final ConcurrentMap<String, JasperReport> cache;
    private final JasperFactory factory;

    public static String STR_EXT_JRXML = ".jrxml";

    @Inject
    public JasperViewProcessor(ServletContext servletContext) {
        this.factory = new JasperFactory(servletContext);
        this.cache = new ConcurrentHashMap();
    }

    /**
     * Reimplement resolve method to replace Reader source to InputStream
     * (Jasper use it).
     *
     * @param name
     * @param mediaType
     * @return
     */
    @Override
    public JasperReport resolve(String name, javax.ws.rs.core.MediaType mediaType) {
       
        if (!cache.containsKey(name)) {
            try{
                JasperReport jr = factory.getCompiledJasper(name);
                cache.putIfAbsent(name, jr);
                return jr;
            }catch(JRException jre){
                throw new IllegalStateException("Houve um erro ao compilar o JasperReport", jre);
            }
        }
        
        return cache.get(name);
    }

    @Override
    public void writeTo(JasperReport jasper, Viewable viewable, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream out) throws IOException {
        
    }
}
