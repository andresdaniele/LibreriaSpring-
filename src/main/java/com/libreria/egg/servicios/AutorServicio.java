package com.libreria.egg.servicios;

import com.libreria.egg.entidades.Autor;
import com.libreria.egg.errores.ErrorServicio;
import com.libreria.egg.repositorios.RepositorioAutor;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

public class AutorServicio {

    @Autowired
    private RepositorioAutor repositorioAutor;

    @Transactional
    public void ingresarAutor(String nombre, Boolean alta) throws ErrorServicio {

        validarDatos(nombre, alta);

        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setAlta(alta);
        repositorioAutor.save(autor);
    }

    @Transactional
    public void modificarAutor(String id, String nombre, Boolean alta) throws ErrorServicio {

        validarDatos(nombre, alta);

        Optional<Autor> respuesta = repositorioAutor.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
            autor.setAlta(alta);

            repositorioAutor.save(autor);
        } else {
            throw new ErrorServicio("El id ingresado no corresponde a un autor registrado");
        }
    }

    @Transactional
    public void eliminarautor(String id) throws ErrorServicio {

        Optional<Autor> respuesta = repositorioAutor.findById(id);

        if (respuesta.isPresent()) {
            repositorioAutor.delete(respuesta.get());
        } else {
            throw new ErrorServicio("El id ingresado no corresponde a un autor registrado");
        }
    }

    @Transactional
    public void deshabilitarAutor(String id) throws ErrorServicio {

        Optional<Autor> respuesta = repositorioAutor.findById(id);

        if (respuesta.isPresent() || respuesta.get().getAlta() == Boolean.TRUE) {
            Autor autor = respuesta.get();
            autor.setAlta(Boolean.FALSE);
            repositorioAutor.save(autor);
        } else {
            throw new ErrorServicio("El id ingresado no corresponde a un autor registrado o el autor ya se encuentra deshabilitado");
        }
    }

    @Transactional
    public void habilitarAutor(String id) throws ErrorServicio {

        Optional<Autor> respuesta = repositorioAutor.findById(id);

        if (respuesta.isPresent() || respuesta.get().getAlta() == Boolean.FALSE) {
            Autor autor = respuesta.get();
            autor.setAlta(Boolean.TRUE);
            repositorioAutor.save(autor);
        } else {
            throw new ErrorServicio("El id ingresado no corresponde a un autor registrado o el autor no se encuentra habilitado");
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

    public Autor buscarAutorPorNombre(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo o estar vacio");
        }

        Autor autor = repositorioAutor.buscarAutorPorNombre(nombre);

        if (autor == null) {
            throw new ErrorServicio("El nombre ingresado no corresponde a un autor registrado");
        } else {
            return autor;
        }
    }

    public Autor buscarAutorPorId(String id) throws ErrorServicio {

        if (id == null || id.trim().isEmpty()) {
            throw new ErrorServicio("El Id ingresado no puede ser nulo o estar vacio");
        }

        Optional<Autor> autor = repositorioAutor.findById(id);

        if (autor.isPresent()) {
            return autor.get();
        } else {
            throw new ErrorServicio("El Id ingresado no corresponde a un autor registrado");
        }
    }

    public List<Autor> listarTodosLosAutores() throws ErrorServicio {

        List<Autor> autores = repositorioAutor.listarAutores();

        if (autores.isEmpty()) {
            throw new ErrorServicio("Aun no hay autores registrados");
        } else {
            return autores;
        }
    }
}
