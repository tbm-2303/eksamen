package facades;

import dtos.SpotDTO;
import dtos.TimelineDTO;
import entities.Location;
import entities.Spot;
import entities.Timeline;
import errorhandling.ExceptionDTO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityResult;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SpotFacade {
    private static SpotFacade instance;
    private static EntityManagerFactory emf;

    private SpotFacade(){

    }
    public static SpotFacade getSpotFacade(EntityManagerFactory _emf){
        if (instance == null){
            emf = _emf;
            instance = new SpotFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    //test er lavet og virker
    public SpotDTO createSpot(SpotDTO spotDTO, TimelineDTO timelineDTO){
        EntityManager em = getEntityManager();
        int timelineID = timelineDTO.getId();
        Timeline timeline = new Timeline(timelineDTO);
        timeline.setId(timelineID);
        spotDTO.setTimeline(timeline);
        //refactor spot entity and spotdto!
        Spot spot = new Spot(spotDTO.getName(), spotDTO.getDescription(), spotDTO.getTimestamp(), spotDTO.getLocation(), spotDTO.getTimeline());
        try{
            em.getTransaction().begin();;
            em.persist(spot);
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }
        return new SpotDTO(spot);
    }
    /*PSEUDO KODE til createSpot - gemmes
    * Create spot (Spot spot, Timeline timeline)
    *Spot spot = new spot...
    * Timeline timline = new Timeline
    * Find timeline, med entitymanager.
    * entity manager
    * try
    * begin
    * persist
    * commit
    * finally
    * close
    * return spot
    * */

    public List<SpotDTO> timeSortedSpots(TimelineDTO timelineDTO){
        EntityManager em = emf.createEntityManager();
        //Take in timeline as parameter
        //get timeline id
        int id = timelineDTO.getId();
        //search for spots where timeline id = the given id
        TypedQuery<Spot> query = em.createQuery("SELECT s FROM Spot s WHERE s.timeline.id = :id", Spot.class);
        query.setParameter("id", id);

        List <Spot> spots = query.getResultList();
        //sort the spots after date the oldest first - find the correct way for LocalDate
        List <SpotDTO> spotDTOS = SpotDTO.getDTOS(spots);
        spotDTOS.sort(Comparator.comparing(SpotDTO::getTimestamp));
        List<SpotDTO> sortedSpots = new ArrayList<>(spotDTOS);

        //sortedSpots.add();

        return sortedSpots;
    }

    //test virker
    public List<String> seeSpot(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            //find spot where id =
            TypedQuery <Spot> query = em.createQuery("SELECT s FROM Spot s WHERE s.id = :id", Spot.class);
            query.setParameter("id", id);
            Spot spot = query.getSingleResult();
            List<String> spotData = new ArrayList<>();
            String name = spot.getName();
            String description = spot.getDescription();
            String timeStamp = spot.getTimeStamp().toString();
            String location = spot.getLocation().toString();
            spotData.add(name);
            spotData.add(description);
            spotData.add(timeStamp);
            spotData.add(location);

            return spotData;
        }finally {
            em.close();
        }
    }


}
