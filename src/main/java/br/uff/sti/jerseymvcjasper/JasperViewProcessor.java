/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.sti.jerseymvcjasper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import javax.servlet.ServletContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import net.sf.jasperreports.engine.JasperReport;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.mvc.Viewable;
import org.glassfish.jersey.server.mvc.spi.AbstractTemplateProcessor;
import org.jvnet.hk2.annotations.Optional;

/**
 * {@link org.glassfish.jersey.server.mvc.spi.TemplateProcessor Template processor}
 * providing support for Freemarker templates.
 *
 * @author Marcos Côrtes (marcoscortes at id.uff.br)
 */
class JasperViewProcessor extends AbstractTemplateProcessor<JasperReport> {
    
    private final JasperFactory factory;

       
    public JasperViewProcessor(
            final javax.ws.rs.core.Configuration config, 
            final ServiceLocator serviceLocator,
            @Optional final ServletContext servletContext) {
        super(config, servletContext, "jasper", "jrxml");

        this.factory = new JasperFactory(servletContext);
//        this.factory = getTemplateObjectFactory(serviceLocator, Configuration.class, new Value<Configuration>() {
//            @Override
//            public Configuration get() {
//                // Create different loaders.
//                final List<TemplateLoader> loaders = Lists.newArrayList();
//                if (servletContext != null) {
//                    loaders.add(new WebappTemplateLoader(servletContext));
//                }
//                loaders.add(new ClassTemplateLoader(FreemarkerViewProcessor.class, "/"));
//                try {
//                    loaders.add(new FileTemplateLoader(new File("/")));
//                } catch (IOException e) {
//                    // NOOP
//                }
//
//                // Create Factory.
//                final Configuration configuration = new Configuration();
//                configuration.setTemplateLoader(new MultiTemplateLoader(loaders.toArray(new TemplateLoader[loaders.size()])));
//                return configuration;
//            }
//        });
    }

    
    @Override
    protected JasperReport resolve(String string, Reader reader) throws Exception {
        return factory.compile(reader);
    }

    @Override
    public void writeTo(JasperReport t, Viewable vwbl, MediaType mt, MultivaluedMap<String, Object> mm, OutputStream out) throws IOException {
        
    }

}
