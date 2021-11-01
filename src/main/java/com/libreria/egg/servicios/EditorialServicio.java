package com.libreria.egg.servicios;

import com.libreria.egg.entidades.Editorial;
import com.libreria.egg.errores.ErrorServicio;
import com.libreria.egg.repositorios.RepositorioEditorial;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

public class EditorialServicio {

    @Autowired
    private RepositorioEditorial repositorioEditorial;

    @Transactional
    public void ingresarEditorial(String nombre, Boolean alta) throws ErrorServicio {

        validarDatos(nombre, alta);

        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorial.setAlta(alta);
        repositorioEditorial.save(editorial);
    }

    @Transactional
    public void modificarEditorial(String id, String nombre, Boolean alta) throws ErrorServicio {

        validarDatos(nombre, alta);

        Optional<Editorial> respuesta = repositorioEditorial.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
            editorial.setAlta(alta);

            repositorioEditorial.save(editorial);
        } else {
            throw new ErrorServicio("El id ingresado no corresponde a una editorial registrada");
        }

    }

    @Transactional
    public void eliminarEditorial(String id) throws ErrorServicio {

        Optional<Editorial> respuesta = repositorioEditorial.findById(id);

        if (respuesta.isPresent()) {
            repositorioEditorial.delete(respuesta.get());
        } else {
            throw new ErrorServicio("El id ingresado no corresponde a una editorial registrada");
        }
    }

    @Transactional
    public void deshabilitarEditorial(String id) throws ErrorServicio {

        Optional<Editorial> respuesta = repositorioEditorial.findById(id);

        if (respuesta.isPresent() || respuesta.get().getAlta() == Boolean.TRUE) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(Boolean.FALSE);
            repositorioEditorial.save(editorial);
        } else {
            throw new ErrorServicio("El id ingresado no corresponde a una editorial registrada o la editorial ya se encuentra dehabilitada");
        }
    }

    @Transactional
    public void habilitarAutor(String id) throws ErrorServicio {

        Optional<Editorial> respuesta = repositorioEditorial.findById(id);

        if (respuesta.isPresent() || respuesta.get().getAlta() == Boolean.FALSE) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(Boolean.TRUE);
            repositorioEditorial.save(editorial);
        } else {
            throw new ErrorServicio("El id ingresado no corresponde a una editorial registrada o la editorial ya se encuentra habilitada");
        }
    }

    public void validarDatos(String nombre, Boolean alta) throws ErrorServicio {

        if (nombre.trim().isEmpty() || nombre == null) {
            throw new ErrorServicio("El nombre no puede ser nulo");
        }

        if (alta == null) {
            throw new ErrorServicio("El valor de alta no puede ser nulo");
        }
    }

    public Editorial buscarEditorialPorNombre(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo o estar vacio");
        }

        Editorial editorial = repositorioEditorial.buscarEditorialPorNombre(nombre);

        if (editorial == null) {
            throw new ErrorServicio("El nombre ingresado no corresponde a una editorial registrada");
        } else {
            return editorial;
        }
    }

    public Editorial buscarEditoriaPorId(String id) throws ErrorServicio {

        if (id == null || id.trim().isEmpty()) {
            throw new ErrorServicio("El Id ingresado no puede ser nulo o estar vacio");
        }

        Optional<Editorial> editorial = repositorioEditorial.findById(id);

        if (editorial.isPresent()) {
            return editorial.get();
        } else {
            throw new ErrorServicio("El Id ingresado no corresponde a una editorial registrada");
        }
    }

    public List<Editorial> listarTodasLasEditoriales() throws ErrorServicio {

        List<Editorial> editoriales = repositorioEditorial.listarEditoriales();

        if (editoriales.isEmpty()) {
            throw new ErrorServicio("Aun no hay editoriales registradas");
        } else {
            return editoriales;
        }
    }

}
