package com.libreria.egg.servicios;

import com.libreria.egg.entidades.Cliente;
import com.libreria.egg.entidades.Libro;
import com.libreria.egg.entidades.Prestamo;
import com.libreria.egg.errores.ErrorServicio;
import com.libreria.egg.repositorios.RepositorioCliente;
import com.libreria.egg.repositorios.RepositorioLibro;
import com.libreria.egg.repositorios.RepositorioPrestamo;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrestamoServicio {

    @Autowired
    private RepositorioPrestamo repositorioPrestamo;
    @Autowired
    private RepositorioLibro repositorioLibro;
    @Autowired
    private RepositorioCliente repositorioCliente;

    @Transactional
    public void crearPrestamo(Date fechaPrestamo, Date fechaDevolucion, Boolean alta, Libro libro, Cliente cliente) throws ErrorServicio {

        validarDatos(fechaPrestamo, fechaDevolucion, alta, libro, cliente);

        Prestamo prestamo = new Prestamo();
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setAlta(alta);
        prestamo.setLibro(libro);
        prestamo.setCliente(cliente);

        repositorioPrestamo.save(prestamo);
    }

    @Transactional
    public void modificarPrestamo(String id, Date fechaPrestamo, Date fechaDevolucion, Boolean alta, Libro libro, Cliente cliente) throws ErrorServicio {

        validarDatos(fechaPrestamo, fechaDevolucion, alta, libro, cliente);

        Optional<Prestamo> respuesta = repositorioPrestamo.findById(id);

        if (respuesta.isPresent()) {
            Prestamo prestamo = respuesta.get();
            prestamo.setFechaPrestamo(fechaPrestamo);
            prestamo.setFechaDevolucion(fechaDevolucion);
            prestamo.setAlta(alta);
            prestamo.setLibro(libro);
            prestamo.setCliente(cliente);

            repositorioPrestamo.save(prestamo);
        } else {
            throw new ErrorServicio("El Id ingresado no pertenece a un prestamo existente");
        }
    }

    @Transactional
    public void darDeBajaPrestamo(String id) throws ErrorServicio {

        Optional<Prestamo> respuesta = repositorioPrestamo.findById(id);

        if (respuesta.isPresent() && respuesta.get().getAlta() == Boolean.TRUE) {
            Prestamo prestamo = respuesta.get();
            prestamo.setAlta(Boolean.FALSE);
            repositorioPrestamo.save(prestamo);
        } else {
            throw new ErrorServicio("El Id ingresado no pertenece a un prestamo registrado");
        }
    }

    @Transactional
    public void eliminarPrestamo(String id) throws ErrorServicio {

        Optional<Prestamo> respuesta = repositorioPrestamo.findById(id);

        if (respuesta.isPresent()) {
            repositorioPrestamo.delete(respuesta.get());
        } else {
            throw new ErrorServicio("El Id ingresado no pertenece a un prestamo registrado");
        }
    }

    private void validarDatos(Date fechaPrestamo, Date fechaDevolucion, Boolean alta, Libro libro, Cliente cliente) throws ErrorServicio {

        if (fechaPrestamo.before(new Date()) || fechaPrestamo == null) {
            throw new ErrorServicio("La fecha de prestamo no puede ser nula o anterior a la de creacion");
        }

        if (fechaDevolucion.before(fechaPrestamo) || fechaDevolucion == null) {
            throw new ErrorServicio("La fecha de devolucion no puede ser nula o anterior a la fecha de prestamo ");
        }

        if (alta == null) {
            throw new ErrorServicio("El estado de alta no puede ser nulo");
        }

        Optional<Libro> respuestaLibro = repositorioLibro.findById(libro.getId());
        if (!respuestaLibro.isPresent()) {
            throw new ErrorServicio("El libro ingresado no se encuentra registrado");
        }

        Optional<Cliente> respuestaCliente = repositorioCliente.findById(cliente.getId());
        if (!respuestaCliente.isPresent()) {
            throw new ErrorServicio("El cliente ingresado no se encuentra registrado");
        }
    }

    public List<Prestamo> listarPrestamosCliente(Cliente cliente) throws ErrorServicio {

        if (cliente == null) {
            throw new ErrorServicio("El cliente ingresado no puede ser nulo");
        }

        List<Prestamo> prestamosClientes = repositorioPrestamo.listarPrestamosCliente(cliente);

        if (prestamosClientes.isEmpty() || prestamosClientes == null) {
            throw new ErrorServicio("El cliente ingresado no posee prestamos activos");
        } else {
            return prestamosClientes;
        }
    }

    public List<Prestamo> listarPrestamosLibro(Libro libro) throws ErrorServicio {

        if (libro == null) {
            throw new ErrorServicio("El libro ingresado no puede ser nulo");
        }

        List<Prestamo> prestamosLibro = repositorioPrestamo.listarPrestamoLibros(libro);

        if (prestamosLibro.isEmpty() || prestamosLibro == null) {
            throw new ErrorServicio("El libro ingresado no posee prestamos activos");
        } else {
            return prestamosLibro;
        }
    }

    public List<Prestamo> listarTodosLosPrestamosActivos() throws ErrorServicio {

        List<Prestamo> prestamosActivos = repositorioPrestamo.listarTodosLosPrestamosActivos();

        if (prestamosActivos.isEmpty() || prestamosActivos == null) {
            throw new ErrorServicio("Aun no hay prestamos activos");
        } else {
            return prestamosActivos;
        }
    }
}
