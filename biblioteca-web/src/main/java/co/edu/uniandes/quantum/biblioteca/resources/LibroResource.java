/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.quantum.biblioteca.resources;

import co.edu.uniandes.quantum.biblioteca.dtos.LibroDTO;
import co.edu.uniandes.quantum.biblioteca.dtos.LibroDetailDTO;
import co.edu.uniandes.quantum.biblioteca.ejb.LibroLogic;
import co.edu.uniandes.quantum.biblioteca.entities.LibroEntity;
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
 * @author cg.chavarro
 */
@Path("libros")
@Produces("application/json")
@Consumes("application/json")
@RequestScoped
public class LibroResource {
//    GET /libros   ya
//GET /libros/{id}   ya
//POST /libros      ya
//PUT /libros/{id}   ya
//DELETE /libros/{id}  ya

    @Inject
    LibroLogic LibroLogic;

    private static final String MENSAJE_ERROR = "El recurso /libros/";
    private static final String NO_EXISTE = "no existe.";

    @GET
    public List<LibroDTO> getBooks() throws WebApplicationException {

        if (listEntity2DTO(LibroLogic.getBooks()).isEmpty()) {
            throw new WebApplicationException("No hay libros en el sistema.");
        } else {
            return listEntity2DTO(LibroLogic.getBooks());
        }
    }

    @GET
       public List<LibroDTO> getBooksBiblioteca(@PathParam("idBiblioteca") Long idBiblioteca) throws BusinessLogicException {
        if (listEntity2DTO(LibroLogic.getLibros(idBiblioteca)).isEmpty()) {
            throw new WebApplicationException("No hay libros en el sistema.");
        } else {
            return listEntity2DTO(LibroLogic.getLibros(idBiblioteca));
        }
    }
    
    @GET
    @Path("{idPrestamo: \\d+}/libros")
    public List<LibroDTO> getBooksPrestamo(@PathParam("idUsuario") Long idUsuario,@PathParam("idPrestamo") Long idPrestamo ) throws BusinessLogicException {
        if (listEntity2DTO(LibroLogic.getBooksPrestamo(idUsuario, idPrestamo)).isEmpty()) {
            throw new WebApplicationException("No hay libros en el prestamo.");
        } else {
            return listEntity2DTO(LibroLogic.getBooksPrestamo(idUsuario, idPrestamo));
        }
    }
   

    @GET
    @Path("{id: \\d+}")
    public LibroDTO getBook(@PathParam("id") Long id, @PathParam("idBiblioteca") Long idBiblioteca) throws BusinessLogicException {
        LibroEntity entity = LibroLogic.getLibro(idBiblioteca, id);
        if (entity == null) {
            throw new WebApplicationException(MENSAJE_ERROR + id + NO_EXISTE, 404);
        }
        return new LibroDTO(entity);
    }
    
    @GET
    @Path("{id: \\d+}")
    public LibroDTO getBook(@PathParam("id") Long idLibro) throws BusinessLogicException {
        LibroEntity entity = LibroLogic.getLibro(idLibro);
        if (entity == null) {
            throw new WebApplicationException(MENSAJE_ERROR + idLibro + NO_EXISTE, 404);
        }
        return new LibroDTO(entity);
    }

    /**
     *
     * @param book
     * @return
     * @throws BusinessLogicException
     */
    @POST
    @Path("bib")
    public LibroDTO createBook(LibroDTO book, @PathParam("idBiblioteca") Long idBiblioteca) throws BusinessLogicException {
        return new LibroDTO(LibroLogic.crearLibro(book.toEntity(), idBiblioteca));
    }
    
     @POST
    public LibroDTO ponerBookPrestamo(LibroDTO book, @PathParam("idUsuario") Long idUsuario, @PathParam("idPrestamo") Long idPrestamo) throws BusinessLogicException {
        LibroEntity lib=book.toEntity();
        return new LibroDTO(LibroLogic.colocarLibroPrestamo(lib, idUsuario, idPrestamo));
    }

    /**
     *
     * Ejemplo: { "description": "Las habilidades gerenciales en arquitectos de
     * software.", "editorial": { "id": 200, "name": "Oveja Negra 2" }, "image":
     * "https://images-na.ssl-images-amazon.com/images/I/516GyHY9p6L.jpg",
     * "isbn": "930330149-8", "name": "La comunicación en el software",
     * "publishingdate": "2017-08-20T00:00:00-05:00" }
     *
     * @param id
     * @param book
     * @return
     * @throws BusinessLogicException
     */
    @PUT
    @Path("{id: \\d+}")
    public LibroDTO updateBook(@PathParam("id") Long id, LibroDTO book, @PathParam("idBiblioteca") Long idBiblioteca) throws BusinessLogicException {
        book.setId(id);
        LibroEntity entity = LibroLogic.getLibro(idBiblioteca, id);
        if (entity == null) {
            throw new WebApplicationException(MENSAJE_ERROR + id + " no existe.", 404);
        }
        return new LibroDTO(LibroLogic.updateBook(id, book.toEntity(), idBiblioteca));
    }

    @DELETE
    @Path("{booksId: \\d+}")
    public void deleteBook(@PathParam("booksId") Long id, @PathParam("idBiblioteca") Long idBiblioteca) throws BusinessLogicException {
        LibroEntity entity = LibroLogic.getLibro(idBiblioteca, id);
        if (entity == null) {
            throw new WebApplicationException("El recurso /books/" + id + " no existe.", 404);
        }
        LibroLogic.deleteBook(id, idBiblioteca);
    }

    /**
     *
     * lista de entidades a DTO.
     *
     * Este método convierte una lista de objetos LibroEntity a una lista de
     * objetos LibroDTO (json)
     *
     * @param entityList corresponde a la lista de libros de tipo Entity que
     * vamos a convertir a DTO.
     * @return la lista de libros en forma DTO (json)
     */
    private List<LibroDTO> listEntity2DTO(List<LibroEntity> entityList) {
        List<LibroDTO> list = new ArrayList<>();
        for (LibroEntity entity : entityList) {
            list.add(new LibroDTO(entity));
        }
        return list;
    }

    private List<LibroDetailDTO> listEntity2DetailDTO(List<LibroEntity> entityList) {
        List<LibroDetailDTO> list = new ArrayList<>();
        for (LibroEntity entity : entityList) {
            list.add(new LibroDetailDTO(entity));
        }
        return list;
    }
}
