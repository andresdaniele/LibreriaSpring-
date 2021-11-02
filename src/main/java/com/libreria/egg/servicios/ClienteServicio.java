package com.libreria.egg.servicios;

import com.libreria.egg.entidades.Cliente;
import com.libreria.egg.errores.ErrorServicio;
import com.libreria.egg.repositorios.RepositorioCliente;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServicio {

    @Autowired
    private RepositorioCliente repositorioCliente;

    @Transactional
    public void registrarCliente(Long documento, String nombre, String apellido, String telefono, Boolean alta) throws ErrorServicio {

        validarDatos(documento, nombre, apellido, telefono, alta);

        Cliente cliente = new Cliente();
        cliente.setDocumento(documento);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);;
        cliente.setTelefono(telefono);
        cliente.setAlta(alta);

        repositorioCliente.save(cliente);
    }

    @Transactional
    public void modificarCliente(String id, Long documento, String nombre, String apellido, String telefono, Boolean alta) throws ErrorServicio {

        validarDatos(documento, nombre, apellido, telefono, alta);

        Optional<Cliente> respuesta = repositorioCliente.findById(id);

        if (respuesta.isPresent()) {

            Cliente cliente = respuesta.get();
            cliente.setDocumento(documento);
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);;
            cliente.setTelefono(telefono);
            cliente.setAlta(alta);

            repositorioCliente.save(cliente);
        } else {
            throw new ErrorServicio("El Id ingresado no corresponde a un cliente registrado");
        }
    }

    @Transactional
    public void eliminarCliente(String id) throws ErrorServicio {

        Optional<Cliente> respuesta = repositorioCliente.findById(id);

        if (respuesta.isPresent()) {

            repositorioCliente.save(respuesta.get());
        } else {
            throw new ErrorServicio("El Id ingresado no corresponde a un cliente registrado");
        }
    }

    @Transactional
    public void deshabilitarCliente(String id) throws ErrorServicio {

        Optional<Cliente> respuesta = repositorioCliente.findById(id);

        if (respuesta.isPresent() && respuesta.get().getAlta() == Boolean.TRUE) {
            Cliente cliente = respuesta.get();
            cliente.setAlta(Boolean.FALSE);
            repositorioCliente.save(respuesta.get());
        } else {
            throw new ErrorServicio("El Id ingresado no corresponde a un cliente registrado o el cliente ya se encuentra deshabilidato");
        }
    }

    @Transactional
    public void habilitarCliente(String id) throws ErrorServicio {

        Optional<Cliente> respuesta = repositorioCliente.findById(id);

        if (respuesta.isPresent() && respuesta.get().getAlta() == Boolean.FALSE) {
            Cliente cliente = respuesta.get();
            cliente.setAlta(Boolean.TRUE);
            repositorioCliente.save(respuesta.get());
        } else {
            throw new ErrorServicio("El Id ingresado no corresponde a un cliente registrado o el cliente ya se encuentra habilidato");
        }
    }

    private void validarDatos(Long documento, String nombre, String apellido, String telefono, Boolean alta) throws ErrorServicio {

        if (documento == null || documento < 0) {
            throw new ErrorServicio("El documento ingresado no puede ser nulo o un numero negativo");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre ingresado no puede ser nulo o vacio");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new ErrorServicio("El apellido ingresado no puede ser nulo o vacio");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new ErrorServicio("El telefono ingresado no puede ser nulo o vacio");
        }
        if (alta == null) {
            throw new ErrorServicio("El estado de alta no puede ser nulo");
        }
    }

    public List<Cliente> buscarClientePorNombre(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre ingresado no puede ser nulo o estar vacio");
        }

        List<Cliente> clientes = repositorioCliente.buscarClientePorNombre(nombre);

        if (clientes == null || clientes.isEmpty()) {
            throw new ErrorServicio("El nombre ingresado no corresponde a un cliente registrado");
        } else {
            return clientes;
        }
    }

    public List<Cliente> buscarClientePorApellido(String apellido) throws ErrorServicio {

        if (apellido == null || apellido.trim().isEmpty()) {
            throw new ErrorServicio("El nombre ingresado no puede ser nulo o estar vacio");
        }

        List<Cliente> clientes = repositorioCliente.buscarClientePorApellido(apellido);

        if (clientes == null || clientes.isEmpty()) {
            throw new ErrorServicio("El apellido ingresado no corresponde a un cliente registrado");
        } else {
            return clientes;
        }
    }

    public List<Cliente> buscarClientePorTelefono(String telefono) throws ErrorServicio {

        if (telefono == null || telefono.trim().isEmpty()) {
            throw new ErrorServicio("El telefono ingresado no puede ser nulo o estar vacio");
        }

        List<Cliente> clientes = repositorioCliente.buscarClientePorTelefono(telefono);

        if (clientes == null || clientes.isEmpty()) {
            throw new ErrorServicio("El telefono ingresado no corresponde a un cliente registrado");
        } else {
            return clientes;
        }
    }

    public Cliente buscarClientePorDocumento(Long documento) throws ErrorServicio {

        if (documento == null || documento < 0) {
            throw new ErrorServicio("El documento ingresado no puede ser nulo o ser un numero negativo");
        }

        Cliente cliente = repositorioCliente.buscarClientePorDocumento(documento);

        if (cliente == null) {
            throw new ErrorServicio("El documento ingresado no corresponde a un cliente registrado");
        } else {
            return cliente;
        }
    }

    public List<Cliente> listarTodosLosClientesActivos() throws ErrorServicio {

        List<Cliente> clientes = repositorioCliente.listarTodosLosClientesActivos();

        if (clientes == null || clientes.isEmpty()) {
            throw new ErrorServicio("No hay clientes activos registrados");
        } else {
            return clientes;
        }
    }
}