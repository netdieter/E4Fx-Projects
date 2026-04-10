package com.shoppinglist.backend.resource;

import com.shoppinglist.backend.model.ShoppingList;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/lists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShoppingListResource {

    @GET
    public List<ShoppingList> getAllLists() {
        return ShoppingList.listAll();
    }

    @GET
    @Path("/{id}")
    public Response getListById(@PathParam("id") Long id) {
        ShoppingList list = ShoppingList.findById(id);
        if (list == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Liste nicht gefunden\"}")
                    .build();
        }
        return Response.ok(list).build();
    }

    @POST
    public Response createList(ShoppingList list) {
        if (list.name == null || list.name.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Name ist erforderlich\"}")
                    .build();
        }
        list.persist();
        return Response.status(Response.Status.CREATED).entity(list).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateList(@PathParam("id") Long id, ShoppingList updatedList) {
        ShoppingList list = ShoppingList.findById(id);
        if (list == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Liste nicht gefunden\"}")
                    .build();
        }
        
        if (updatedList.name != null && !updatedList.name.trim().isEmpty()) {
            list.name = updatedList.name;
        }
        list.updatedAt = java.time.Instant.now();
        list.persist();
        
        return Response.ok(list).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteList(@PathParam("id") Long id) {
        ShoppingList list = ShoppingList.findById(id);
        if (list == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Liste nicht gefunden\"}")
                    .build();
        }
        list.delete();
        return Response.noContent().build();
    }
}
