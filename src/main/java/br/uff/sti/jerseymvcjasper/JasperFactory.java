/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.sti.jerseymvcjasper;

import br.uff.sti.jerseymvcjasper.proxy.JasperProxy;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author marcos
 */
@Singleton
public class JasperFactory {

    private static final String STR_JRXML_EXTENSION = ".jrxml";
    
    @Inject
    private ServletContext servletContext;

    private JasperProxy jasperProxy;

    private final ConcurrentMap<String, JasperReport> cache;

    public void setJasperProxy(JasperProxy jp) {
        this.jasperProxy = jp;
    }
    
    public JasperProxy getJasperProxy(){
        return jasperProxy;
    }

    JasperFactory() {
        this.cache = new ConcurrentHashMap<>();
    }

    public JasperReport compile(String name) throws JRException {
        if (!cache.containsKey(name)) {
            InputStream is = getResource(name);
            JasperReport jr = jasperProxy.compileReport(is);
            cache.putIfAbsent(name, jr);
        }
        return cache.get(name);

    }

    InputStream getResource(String name) {
        if(!name.endsWith(STR_JRXML_EXTENSION)){
            name+=STR_JRXML_EXTENSION;
        }
        
        name = name.replaceFirst("^/+", "");
        
        name = "WEB-INF/"+name;
        
        return servletContext.getResourceAsStream(name);
    }

    public void clearCache() {
        cache.clear();
    }

    public int cacheSize() {
        return cache.size();
    }

    public String getRootResources(){
        return servletContext.getRealPath("");
    }
}
