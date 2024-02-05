package sa.alrakeen.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sa.alrakeen.exceptions.AuthorizationFailedException;
import sa.alrakeen.service.AirportService;

@Path("/airport")
public class AirportResource {

    @Inject
    private AirportService service;

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@HeaderParam("Authorization") String authHeader,
                           @HeaderParam("email") String email,
                           String airportJson) {
        try {
            return Response.ok().entity(service.save(authHeader, email, airportJson)).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (AuthorizationFailedException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @PATCH
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@HeaderParam("Authorization") String authHeader,
                           @HeaderParam("email") String email,
                           String airportJson) {
        try {
            return Response.ok().entity(service.update(authHeader, email, airportJson)).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (AuthorizationFailedException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@HeaderParam("Authorization") String authHeader,
                           @HeaderParam("email") String email,
                           String airportJson) {
        try {
            service.delete(authHeader, email, airportJson);
//            not sure what to return here...
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (AuthorizationFailedException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }


    @GET
    @Path("/findbyname")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByName(@QueryParam("name") String name) {
        try {
            return Response.ok().entity(service.findByName(name)).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/findbycode")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByCode(@QueryParam("code") String code) {
        try {
            return Response.ok().entity(service.findByCode(code)).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@QueryParam("skip") @DefaultValue("0") int skip) {
        try {
            return Response.ok().entity(service.list(skip)).build();
        } catch (JsonProcessingException|NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

}