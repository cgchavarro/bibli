/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.quantum.biblioteca.resources;

import co.edu.uniandes.quantum.biblioteca.dtos.VideoDTO;
import co.edu.uniandes.quantum.biblioteca.dtos.VideoDetailDTO;
import co.edu.uniandes.quantum.biblioteca.ejb.VideoLogic;
import co.edu.uniandes.quantum.biblioteca.entities.VideoEntity;
import co.edu.uniandes.quantum.biblioteca.exceptions.BusinessLogicException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author jf.garcia
 */
@Path("videos")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class VideoResource {

    @Inject
    VideoLogic VideoLogic;

    private static final String MENSAJE_ERROR = "El recurso /Videos/";
    private static final String NO_EXISTE = "no existe.";

    @GET
    public List<VideoDTO> getVideos() throws WebApplicationException {

        if (listEntity2DTO(VideoLogic.getVideos()).isEmpty()) {
            throw new WebApplicationException("No hay Videos en el sistema.");
        } else {
            return listEntity2DTO(VideoLogic.getVideos());
        }
    }

    @GET
    @Path("bib")
    public List<VideoDTO> getVideosBiblioteca(@PathParam("idBiblioteca") Long idBiblioteca) throws BusinessLogicException {
        if (listEntity2DTO(VideoLogic.getVideos(idBiblioteca)).isEmpty()) {
            throw new WebApplicationException("No hay Videos en el sistema.");
        } else {
            return listEntity2DTO(VideoLogic.getVideos(idBiblioteca));
        }
    }

    @GET
    @Path("{id: \\d+}/bib")
    public VideoDTO getVideo(@PathParam("idBiblioteca") Long idBiblioteca, @PathParam("id") Long idVideo) throws BusinessLogicException {
        VideoEntity entity = VideoLogic.getVideo(idBiblioteca, idVideo);
        if (entity == null) {
            throw new WebApplicationException(MENSAJE_ERROR + idVideo + NO_EXISTE, 404);
        }
        return new VideoDTO(entity);
    }
    
    @GET
    @Path("{id: \\d+}")
    public VideoDTO getVideo(@PathParam("id") Long idVideo) throws BusinessLogicException {
        VideoEntity entity = VideoLogic.getVideo(idVideo);
        if (entity == null) {
            throw new WebApplicationException(MENSAJE_ERROR + idVideo + NO_EXISTE, 404);
        }
        return new VideoDTO(entity);
    }

   @POST
   @Path("bib")
   public VideoDTO createVideo(@PathParam("idBiblioteca") Long idBiblioteca, VideoDTO video) throws BusinessLogicException {
   return new VideoDTO(VideoLogic.createVideo(idBiblioteca,  video.toEntity()));
}


    @PUT
    @Path("{id: \\d+}")
    public VideoDTO updateVideo(@PathParam("idBiblioteca") Long idBiblioteca, @PathParam("id") Long idVideo, VideoDTO video) throws BusinessLogicException {
        video.setId(idVideo);
        VideoEntity entity = VideoLogic.getVideo(idBiblioteca, idVideo);
        if (entity == null) {
            throw new WebApplicationException("El recurso /bibliotecas/" + idBiblioteca + "/videos/" + idVideo + " no existe.", 404);
        }
        return new VideoDTO(VideoLogic.updateVideo(idBiblioteca, video.toEntity()));

    }

    @DELETE
    @Path("{id: \\d+}")
    public void deleteVideo(@PathParam("idBiblioteca") Long idBiblioteca, @PathParam("id") Long idVideo) throws BusinessLogicException {
        VideoEntity entity = VideoLogic.getVideo(idBiblioteca, idVideo);
        if (entity == null) {
            throw new WebApplicationException("El recurso /bibliotecas/" + idBiblioteca + "/videos/" + idVideo + " no existe.", 404);
        }
        VideoLogic.deleteVideo(idBiblioteca, idVideo);
    }

    /**
     *
     * lista de entidades a DTO.
     *
     * Este método convierte una lista de objetos VideoEntity a una lista de
     * objetos VideoDTO (json)
     *
     * @param entityList corresponde a la lista de Videos de tipo Entity que
     * vamos a convertir a DTO.
     * @return la lista de Videos en forma DTO (json)
     */
    private List<VideoDTO> listEntity2DTO(List<VideoEntity> entityList) {
        List<VideoDTO> list = new ArrayList<>();
        for (VideoEntity entity : entityList) {
            list.add(new VideoDTO(entity));
        }
        return list;
    }

    private List<VideoDetailDTO> listEntity2DetailDTO(List<VideoEntity> entityList) {
        List<VideoDetailDTO> list = new ArrayList<>();
        for (VideoEntity entity : entityList) {
            list.add(new VideoDetailDTO(entity));
        }
        return list;
    }
}