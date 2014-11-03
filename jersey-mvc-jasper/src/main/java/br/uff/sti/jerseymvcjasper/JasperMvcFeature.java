/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.sti.jerseymvcjasper;

import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import org.glassfish.jersey.server.mvc.MvcFeature;

/**
 * {@link Feature} used to add support for {@link MvcFeature MVC} and Jasper
 * templates.
 * <p/>
 * Note: This feature also registers {@link MvcFeature}.
 *
 * @author Marcos Côrtes (marcoscortes at id.uff.br
 */
@ConstrainedTo(RuntimeType.SERVER)
public class JasperMvcFeature implements Feature {

    private final static String SUFFIX = ".jrxml";

    /**
     * {@link String} property defining the base path to Jasper templates. If
     * set, the value of the property is added in front of the template name
     * defined in:
     * <ul>
     * <li>{@link org.glassfish.jersey.server.mvc.Viewable Viewable}</li>
     * <li>{@link org.glassfish.jersey.server.mvc.Template Template}, or</li>
     * <li>{@link org.glassfish.jersey.server.mvc.ErrorTemplate ErrorTemplate}</li>
     * </ul>
     * <p/>
     * Value can be absolute providing a full path to a system directory with
     * templates or relative to current
     * {@link javax.servlet.ServletContext servlet context}.
     * <p/>
     * There is no default value.
     * <p/>
     * The name of the configuration property is <tt>{@value}</tt>.
     */
    public final static String TEMPLATES_BASE_PATH = MvcFeature.TEMPLATE_BASE_PATH + SUFFIX;

    /**
     * If {@code true} then enable caching of Freemarker templates to avoid
     * multiple compilation.
     * <p/>
     * The default value is {@code false}.
     * <p/>
     * The name of the configuration property is <tt>{@value}</tt>.
     */
    public static final String CACHE_TEMPLATES = MvcFeature.CACHE_TEMPLATES + SUFFIX;

    /**
     * Property used to pass user-configured
     * {@link freemarker.template.Configuration configuration} able to create
     * {@link freemarker.template.Template Freemarker templates}.
     * <p/>
     * The default value is not set.
     * <p/>
     * The name of the configuration property is <tt>{@value}</tt>.
     * <p/>
     * If you want to set custom
     * {@link freemarker.template.Configuration configuration} then set
     * {@link freemarker.cache.TemplateLoader template loader} to multi loader
     * of: {@link freemarker.cache.WebappTemplateLoader} (if applicable),
     * {@link freemarker.cache.ClassTemplateLoader} and
     * {@link freemarker.cache.FileTemplateLoader} keep functionality of
     * resolving templates.
     *
     */
//    public static final String TEMPLATE_OBJECT_FACTORY = MvcFeature.TEMPLATE_OBJECT_FACTORY + SUFFIX;
    /**
     * Property defines output encoding produced by
     * {@link org.glassfish.jersey.server.mvc.spi.TemplateProcessor}. The value
     * must be a valid encoding defined that can be passed to the
     * {@link java.nio.charset.Charset#forName(String)} method.
     *
     * <p/>
     * The default value is {@code UTF-8}.
     * <p/>
     * The name of the configuration property is <tt>{@value}</tt>.
     * <p/>
     *
     * @param context
     * @return 
     */
//    public static final String ENCODING = MvcFeature.ENCODING + SUFFIX;
    @Override
    public boolean configure(final FeatureContext context) {
        final Configuration config = context.getConfiguration();

        if (!config.isRegistered(JasperViewProcessor.class)) {
            // Template Processor.
            context.register(JasperViewProcessor.class);

            // MvcFeature.
            if (!config.isRegistered(MvcFeature.class)) {
                context.register(MvcFeature.class);
            }

            return true;
        }
        return false;
    }
}
