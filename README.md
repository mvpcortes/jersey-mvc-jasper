jersey-mvc-jasper
=================
[![Build Status](https://travis-ci.org/marcosvpcortes/jersey-mvc-jasper.svg?branch=master)](https://travis-ci.org/marcosvpcortes/jersey-mvc-jasper)

Jersey plugin to integrate Jasper Report at Jersey Template system, generating PDF files.

Installing
----------
Install it has two steps:
* Include the depedency in pom.xml;
* Register the MvcFeature of the jersey-mvc-jasper;

### Include the dependency

        <dependency>
            <groupId>br.uff.sti</groupId>
            <artifactId>jersey-mvc-jasper</artifactId>
            <version>0.1.10</version>
            <exclusions>
                <exclusion>
                    <artifactId>jasperreports</artifactId>
                    <groupId>net.sf.jasperreports</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        

> Details:
> * You should exclude the Jasper Report dependency od this plugin to avoid conflict with the module insered by yout application or by others dependencies. **It is important because Jasper is very sensible to differences between the .jasper compiled version and the jasper module version**.
> * Currently, this plugin is not in a maven repository (I am learning do it, sorry...). You should download this plugin and clean/install with maven
>
>       ``mvn clean install``

### Register the MvcFeature

Register the MvcFeature provide by this module. More details [here](https://jersey.java.net/documentation/latest/mvc.html#mvc.registration).

    ...
    register(org.glassfish.jersey.jackson.JacksonFeature.class);
    register(MultiPartFeature.class);
    register(JasperMvcFeature.class);
    ...

Usage
-----
Jersey by itself has a lot of ways to define a web-service method (returning a entity, a Response, etc). You using the same syntax to render with jersey-mvc-jasper, but using the annotation @Template with your template name and defining the @Produces type:

    @GET
    @Path("{id}.pdf")
    @Template(name="/jasper/my_template")
    @Produces("application/pdf")
    public Person getPerson(@PathParam("id") id){
      Person person = this.getPerson(id);//using spring/CDI/etc...
      return person;
    }

    
The jersey-mv-jasper will search in "WEB-INF/jasper/" for a file like my_template.jasper. If it not found, will search by my_template.jrxml" and compile it.

You can return a List to represent multiples entities to be rendered by jasper using $F{...} (field).

    @GET
    @Path(".pdf")
    @Template(name="/jasper/my_template")
    @Produces("application/pdf")
    public List<Person> getPersons(){
      List<Person> persons = this.getPersons();//using spring/CDI/etc...
      return persons;
    }

Return too a instance of [JasperModel](src/main/java/br/uff/sti/jerseymvcjasper/JasperModel.java) that can receive the list of entities and a collections of key->value to be used with $P{...} (parameters) in jasper.
    
    @GET
    @Path(".pdf")
    @Template(name="/jasper/my_template")
    @Produces("application/pdf")
    public JasperModel getPersons(){
      List<Person> persons = this.getPersons();//using spring/CDI/etc...
      return JasperModel.create(Person.class).putParam("LOGO", "/logo.png").addModels(persons);
    }
  

See more
--------
* [Jersey Manual - MVC templates](https://jersey.java.net/documentation/latest/mvc.html)
* [Plugin to pre-compile jaspers from jrxml](https://github.com/alexnederlof/Jasper-report-maven-plugin)
* [FreeMarker template for Jersey](https://github.com/jersey/jersey/tree/master/ext/mvc-freemarker)
