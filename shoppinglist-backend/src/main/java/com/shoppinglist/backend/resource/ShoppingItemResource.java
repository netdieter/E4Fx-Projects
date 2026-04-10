package com.shoppinglist.backend.resource;

import com.shoppinglist.backend.model.ShoppingItem;
import com.shoppinglist.backend.model.ShoppingList;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/lists/{listId}/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShoppingItemResource {

    @GET
    public List<ShoppingItem> getItemsByList(@PathParam("listId") Long listId) {
        ShoppingList list = ShoppingList.findById(listId);
        if (list == null) {
            throw new WebApplicationException("Liste nicht gefunden", Response.Status.NOT_FOUND);
        }
        return ShoppingItem.list("shoppingList", list);
    }

    @POST
    public Response createItem(@PathParam("listId") Long listId, ShoppingItem item) {
        ShoppingList list = ShoppingList.findById(listId);
        if (list == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Liste nicht gefunden\"}")
                    .build();
        }
        
        if (item.name == null || item.name.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Name ist erforderlich\"}")
                    .build();
        }
        
        item.shoppingList = list;
        item.persist();
        return Response.status(Response.Status.CREATED).entity(item).build();
    }

    @PUT
    @Path("/{itemId}")
    public Response updateItem(@PathParam("listId") Long listId, 
                               @PathParam("itemId") Long itemId, 
                               ShoppingItem updatedItem) {
        ShoppingItem item = ShoppingItem.findById(itemId);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Artikel nicht gefunden\"}")
                    .build();
        }
        
        // Überprüfen, ob der Artikel zur Liste gehört
        if (!item.shoppingList.id.equals(listId)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Artikel gehört nicht zu dieser Liste\"}")
                    .build();
        }
        
        if (updatedItem.name != null && !updatedItem.name.trim().isEmpty()) {
            item.name = updatedItem.name;
        }
        item.checked = updatedItem.checked;
        item.updatedAt = java.time.Instant.now();
        item.persist();
        
        return Response.ok(item).build();
    }

    @DELETE
    @Path("/{itemId}")
    public Response deleteItem(@PathParam("listId") Long listId, 
                               @PathParam("itemId") Long itemId) {
        ShoppingItem item = ShoppingItem.findById(itemId);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Artikel nicht gefunden\"}")
                    .build();
        }
        
        // Überprüfen, ob der Artikel zur Liste gehört
        if (!item.shoppingList.id.equals(listId)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Artikel gehört nicht zu dieser Liste\"}")
                    .build();
        }
        
        item.delete();
        return Response.noContent().build();
    }
}
