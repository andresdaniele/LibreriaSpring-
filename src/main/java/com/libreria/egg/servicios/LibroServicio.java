/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.libreria.egg.servicios;

import com.libreria.egg.entidades.Autor;
import com.libreria.egg.entidades.Editorial;
import com.libreria.egg.entidades.Libro;
import com.libreria.egg.errores.ErrorServicio;
import com.libreria.egg.repositorios.RepositorioAutor;
import com.libreria.egg.repositorios.RepositorioEditorial;
import com.libreria.egg.repositorios.RepositorioLibro;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LibroServicio {

    @Autowired
    private RepositorioLibro repositorioLibro;

    @Autowired
    private RepositorioAutor repositorioAutor;

    @Autowired
    private RepositorioEditorial repositorioEditorial;

    @Transactional
    public void ingresarLibro(Long isbn, String nombre, Integer anio, Integer ejemplares, Integer ejemplaresPrestados,
            Boolean alta, String idAutor, String idEditorial) throws ErrorServicio {

        validarDatos(isbn, nombre, anio, ejemplares, ejemplaresPrestados, alta, idAutor, idEditorial);

        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setNombre(nombre);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(ejemplares - ejemplaresPrestados);
        libro.setAlta(alta);
        libro.setAutor(repositorioAutor.findById(idAutor).get());
        libro.setEditorial(repositorioEditorial.findById(idEditorial).get());

        repositorioLibro.save(libro);

    }

    @Transactional
    public void modificarLibro(String id, Long isbn, String nombre, Integer anio, Integer ejemplares, Integer ejemplaresPrestados,
            Boolean alta, String idAutor, String idEditorial) throws ErrorServicio {

        validarDatos(isbn, nombre, anio, ejemplares, ejemplaresPrestados, alta, idAutor, idEditorial);

        Optional<Libro> respuesta = repositorioLibro.findById(id);

        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setIsbn(isbn);
            libro.setNombre(nombre);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(ejemplaresPrestados);
            libro.setEjemplaresRestantes(ejemplares - ejemplaresPrestados);
            libro.setAlta(alta);
            libro.setAutor(repositorioAutor.findById(idAutor).get());
            libro.setEditorial(repositorioEditorial.findById(idEditorial).get());
        } else {
            throw new ErrorServicio("El id ingresado no corresponde a un libro registrado");
        }
    }

    @Transactional
    public void eliminarLibro(String id) throws ErrorServicio {

        Optional<Libro> respuesta = repositorioLibro.findById(id);

        if (respuesta.isPresent()) {
            repositorioLibro.delete(respuesta.get());
        } else {
            throw new ErrorServicio("El id ingresado no corresponde a un libro registrado");
        }
    }

    @Transactional
    public void deshabilitarLibro(String id) throws ErrorServicio {

        Optional<Libro> respuesta = repositorioLibro.findById(id);

        if (respuesta.isPresent() && respuesta.get().getAlta() == Boolean.TRUE) {
            Libro libro = respuesta.get();
            libro.setAlta(Boolean.FALSE);
            repositorioLibro.save(libro);
        } else {
            throw new ErrorServicio("El id ingresado no corresponde a un libro registrado o el libro ya se encuentra deshabilitado");
        }
    }

    @Transactional
    public void habilitarLibro(String id) throws ErrorServicio {

        Optional<Libro> respuesta = repositorioLibro.findById(id);

        if (respuesta.isPresent() && respuesta.get().getAlta() == Boolean.FALSE) {
            Libro libro = respuesta.get();
            libro.setAlta(Boolean.TRUE);
            repositorioLibro.save(libro);
        } else {
            throw new ErrorServicio("El id ingresado no corresponde a un libro registrado o el libro no se encuentra deshabilitado");
        }
    }

    private void validarDatos(Long isbn, String nombre, Integer anio, Integer ejemplares, Integer ejemplaresPrestados,
            Boolean alta, String idAutor, String idEditorial) throws ErrorServicio {

        if (isbn == null || isbn.toString().length() != 13) {
            throw new ErrorServicio("El ISBN debe tener 13 caracteres");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre el libro no puede ser nulo");
        }

        if (anio > LocalDate.now().getYear() || anio == null) {
            throw new ErrorServicio("El año del libro no puede ser nulo ni mayor al actual");
        }

        if (ejemplares == null || ejemplares < 0) {
            throw new ErrorServicio("El numero de ejemplares no puede ser menor a cero");
        }

        if (ejemplaresPrestados == null || ejemplaresPrestados > ejemplares) {
            throw new ErrorServicio("El numero de ejemplares prestados no puede ser mayor al existente");
        }

        if (alta == null) {
            throw new ErrorServicio("El valor de alta no puede ser nulo");
        }

        Optional<Autor> respuestaAutor = repositorioAutor.findById(idAutor);
        if (!respuestaAutor.isPresent()) {
            throw new ErrorServicio("El autor no puede ser nulo");
        }

        Optional<Editorial> respuestaEditorial = repositorioEditorial.findById(idEditorial);
        if (!respuestaEditorial.isPresent()) {
            throw new ErrorServicio("La editorial no puede ser nula");
        }

    }

    public List<Libro> buscarLibroPorNombre(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre ingresado no puede ser nulo o estar vacio");
        }

        List<Libro> libro = repositorioLibro.buscarLibrosPorNombre(nombre);

        if (libro.isEmpty()) {
            throw new ErrorServicio("No se encontraron libros con el nombre ingresado");
        } else {
            return libro;
        }
    }

    public Libro buscarLibroPorIsbn(Long isbn) throws ErrorServicio {

        if (isbn.toString().length() < 13 || isbn == null) {
            throw new ErrorServicio("El isbn ingresado posee un formato incorrecto");
        }

        Libro libro = repositorioLibro.buscarLibroPorIsbn(isbn);

        if (libro == null) {
            throw new ErrorServicio("El ISNB ingresado no corresponde a un libro registrado");
        } else {
            return libro;
        }
    }

    public List<Libro> buscarLibroPorAutor(Autor autor) throws ErrorServicio {

        if (autor == null) {
            throw new ErrorServicio("El autor no puede ser nulo");
        }

        List<Libro> libros = repositorioLibro.buscarLibrosPorAutor(autor);

        if (libros.isEmpty()) {
            throw new ErrorServicio("El autor ingresado no posee libros registrados");
        } else {
            return libros;
        }
    }

    public List<Libro> buscarLibroPorEditorial(Editorial editorial) throws ErrorServicio {

        if (editorial == null) {
            throw new ErrorServicio("La editorial no puede ser nula");
        }

        List<Libro> libros = repositorioLibro.buscarLibrosPorEditorial(editorial);

        if (libros.isEmpty()) {
            throw new ErrorServicio("La editorial ingresada no posee libros registrados");
        } else {
            return libros;
        }
    }

    public Libro buscarLibrosPorId (String id) throws ErrorServicio {
        
        if(id == null || id.trim().isEmpty()) {
            throw new ErrorServicio("El Id ingresado no puede ser nulo o vacio");
        }
        
        Optional<Libro> libro = repositorioLibro.findById(id);
        
        if(libro.isPresent()){
            return libro.get();
        } else {
            throw new ErrorServicio("El Id no corresponde a un libro registrado");
        }
    }
    
    public List<Libro> listarTodosLosLibros() throws ErrorServicio {
        
        List<Libro> libros = repositorioLibro.listarLibros();
        
        if(libros.isEmpty()) {
            throw new ErrorServicio("Aun no hay libro registrados en el sistema");
        } else {
            return libros;
        }               
    }
}
