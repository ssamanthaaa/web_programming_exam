package it.units;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


// http://localhost:8080/WPExamProject/rest/bonjour
@Path("/bonjour")
public class Hello {
//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    public String direBonjour() {
//        return "Bonjour, tout le monde!";
//    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String sayJsonHello() {
    	System.out.println("Hello");
        return "{\"name\":\"greeting\", \"message\":\"Bonjour tout le monde!\"}";
    }
}
