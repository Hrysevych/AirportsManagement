package sa.alrakeen.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sa.alrakeen.exceptions.EmailAlreadyExistsException;
import sa.alrakeen.exceptions.UserNotFoundException;
import sa.alrakeen.service.UserService;

@Path("/user")
public class UserResource {

    @Inject
    private UserService service;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(String json) {
        try {
            return Response.status(Response.Status.CREATED).entity(service.save(json)).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (EmailAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).entity("Email already exists in the database").build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String userJson) {
        try {
            String tokenJson = service.login(userJson);
            return Response.ok(tokenJson).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (UserNotFoundException e) {
            // Handle UserNotFoundException and return a 404 Not Found response
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logout(String userJson) {
        try {
            service.logout(userJson);
            return Response.noContent().build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (UserNotFoundException e) {
            // Handle UserNotFoundException and return a 404 Not Found response
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found: " + e.getMessage())
                    .build();
        }
    }
}