package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.TimelineDTO;
import facades.TimelineFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("timeline")
public class TimelineResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final TimelineFacade FACADE = TimelineFacade.getTimelineFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    //ikke testet
    //id'et skal bruges til at oprette timelinen
    //det skal sættes i frontenden - laves med en metode
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("basic")
    @Path("/create")
    public String createTimeline(String timeline){
        TimelineDTO timelineDTO = GSON.fromJson(timeline, TimelineDTO.class);
        TimelineDTO createdTimeline = FACADE.createTimeline(timelineDTO);
        return GSON.toJson(createdTimeline);

    }
}
