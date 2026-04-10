package com.shoppinglist.backend.resource;

import com.shoppinglist.backend.model.Device;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceResource {

    @GET
    public List<Device> getAllDevices() {
        return Device.listAll();
    }

    @GET
    @Path("/{id}")
    public Response getDeviceById(@PathParam("id") Long id) {
        Device device = Device.findById(id);
        if (device == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Gerät nicht gefunden\"}")
                    .build();
        }
        return Response.ok(device).build();
    }

    @POST
    public Response registerDevice(Device device) {
        if (device.deviceId == null || device.deviceId.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Device-ID ist erforderlich\"}")
                    .build();
        }
        
        // Prüfen, ob Gerät bereits existiert
        Device existing = Device.find("deviceId", device.deviceId).firstResult();
        if (existing != null) {
            // Update lastSeenAt
            existing.lastSeenAt = java.time.Instant.now();
            existing.persist();
            return Response.ok(existing).build();
        }
        
        if (device.name == null || device.name.trim().isEmpty()) {
            device.name = "Unbekanntes Gerät";
        }
        
        device.persist();
        return Response.status(Response.Status.CREATED).entity(device).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateDevice(@PathParam("id") Long id, Device updatedDevice) {
        Device device = Device.findById(id);
        if (device == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Gerät nicht gefunden\"}")
                    .build();
        }
        
        if (updatedDevice.name != null && !updatedDevice.name.trim().isEmpty()) {
            device.name = updatedDevice.name;
        }
        if (updatedDevice.active != device.active) {
            device.active = updatedDevice.active;
        }
        device.lastSeenAt = java.time.Instant.now();
        device.persist();
        
        return Response.ok(device).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDevice(@PathParam("id") Long id) {
        Device device = Device.findById(id);
        if (device == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Gerät nicht gefunden\"}")
                    .build();
        }
        device.delete();
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/heartbeat")
    public Response heartbeat(@PathParam("id") Long id) {
        Device device = Device.findById(id);
        if (device == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Gerät nicht gefunden\"}")
                    .build();
        }
        device.lastSeenAt = java.time.Instant.now();
        device.persist();
        return Response.ok().build();
    }
}
