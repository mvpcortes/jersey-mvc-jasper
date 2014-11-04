/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.sti.jerseymvcjasper;

import br.uff.sti.jerseymvcjasper.proxy.JasperProxy;
import br.uff.sti.jerseymvcjasper.proxy.JasperProxyImpl;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author marcos
 */
public class JasperFactory {

    private static final String STR_JRXML_EXTENSION = ".jrxml";

//    private ServletContext servletContext;

    private JasperProxy jasperProxy;

    private final ConcurrentMap<String, JasperReport> cache;

    public void setJasperProxy(JasperProxy jp) {
        this.jasperProxy = jp;
    }

    public JasperProxy getJasperProxy() {
        return jasperProxy;
    }
//
//    void setServletContext(ServletContext servletContext) {
//        this.servletContext = servletContext;
//    }

    JasperFactory() {
        this.cache = new ConcurrentHashMap<>();
        this.jasperProxy = new JasperProxyImpl();
    }

    public JasperReport compile(String name, ServletContext servletContext) throws JRException {

        name = cleanName(name);

        if (!cache.containsKey(name)) {
            InputStream is = getResource(name, servletContext);
            JasperReport jr = jasperProxy.compileReport(is);
            if (jr != null) {
                cache.putIfAbsent(name, jr);
            } else {
                throw new JRException(String.format("Cannot found jrxml '%s'", name));
            }
        }
        return cache.get(name);

    }

    public String cleanName(String name) {
        name = name.replaceFirst("^/+", "");

        if (name.endsWith(STR_JRXML_EXTENSION)) {
            name = name.substring(0, name.length() - STR_JRXML_EXTENSION.length());
        }
        return name;
    }

    public InputStream getResource(String name, ServletContext servletContext) {
        name += STR_JRXML_EXTENSION;

        name = "/WEB-INF/" + name;

        return servletContext.getResourceAsStream(name);
    }

    public void clearCache() {
        cache.clear();
    }

    public int cacheSize() {
        return cache.size();
    }

    public String getRootResources(ServletContext servletContext) {
        return servletContext.getRealPath("");
    }
}
