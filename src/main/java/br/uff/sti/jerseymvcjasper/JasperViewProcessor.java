/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.sti.jerseymvcjasper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.glassfish.jersey.server.mvc.Viewable;
import org.glassfish.jersey.server.mvc.spi.TemplateProcessor;

/**
 * {@link org.glassfish.jersey.server.mvc.spi.TemplateProcessor Template processor}
 * providing support for Freemarker templates.
 *
 * @author Marcos Côrtes (marcoscortes at id.uff.br)
 */
@Provider
class JasperViewProcessor implements TemplateProcessor<JasperReport> {

    private static final String STR_PATH_RESOURCES = "PATH_RESOURCES";

    private JasperFactory factory;

    @Context
    ServletContext servletContext;

    public void JasperViewProcessor(){
        factory = new JasperFactory();
    }
    
    void setJasperFactory(JasperFactory jasperFactory) {
        this.factory = jasperFactory;
    }
    
    JasperFactory getJasperFactory(){
        if(factory == null){
            factory = new JasperFactory();
        }
        return factory;
    }

    void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public JasperReport resolve(String name, MediaType mediaType) {
        try {
            return getJasperFactory().compile(name, servletContext);
        } catch (JRException ex) {
            throw new IllegalStateException(String.format("Cannot compile %s", name), ex);
        }
    }

    @Override
    public void writeTo(JasperReport jr, Viewable vwbl, MediaType mt, MultivaluedMap<String, Object> mm, OutputStream outputStream) throws IOException {
        JasperModel jasperModel = findJasperModel(vwbl);
        try {
            JasperPrint print = getJasperFactory().getJasperProxy().fillReport(jr,
                    jasperModel.getParameters(),
                    jasperModel.getListModels());
            factory.getJasperProxy().exportReportToPDFStream(print, outputStream);
        } catch (JRException jre) {
            throw new IOException("Houve um problema ao gerar a ficha do snapshot/processo", jre);
        }
    }

    JasperModel findJasperModel(Viewable vwbl) {
        final Object obj = vwbl.getModel();

        JasperModel model;

        if (obj instanceof JasperModel) {
            model = (JasperModel) obj;

        } else {
            List list;
            if (obj instanceof List) {
                list = (List) obj;
            } else if (obj instanceof Collection) {
                list = new ArrayList();
                list.addAll((Collection) obj);
            } else {
                list = new ArrayList(1);
                list.add(obj);
            }
            model = new JasperModel(list, new HashMap<>());
        }

        model.getParameters().putAll(this.createWithDefaultProperties());

        return model;
    }

    Map<String, Object> createWithDefaultProperties() {
        Map<String, Object> map = new HashMap<>();

        map.put(STR_PATH_RESOURCES, getJasperFactory().getRootResources(servletContext));

        return map;
    }

//    private Map<String, Object> getQueryParams(String templateName) {
//        String strQueryParams;
//        int pos = templateName.indexOf("?");
//        if(pos>= 0){
//            strQueryParams = templateName.substring(pos);
//            Mapper mapper = new ObjectMapper();
//            
//            mapper.
//        }else{
//            return Collections.EMPTY_MAP;
//        }
//    }
}
