package com.libreria.egg.servicios;

import com.libreria.egg.entidades.Cliente;
import com.libreria.egg.errores.ErrorServicio;
import com.libreria.egg.repositorios.RepositorioCliente;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

@Service
public class ClienteServicio {

    @Autowired
    private RepositorioCliente repositorioCliente;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class) //Estos atributos con sus valores son los que vienen por defecto con la anotacion
    public void registrarCliente(Long documento, String nombre, String apellido, String telefono) throws ErrorServicio {

        validarDatos(documento, nombre, apellido, telefono);

        Cliente cliente = new Cliente();
        cliente.setDocumento(documento);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);;
        cliente.setTelefono(telefono);
        cliente.setAlta(true);

        repositorioCliente.save(cliente);
    }

    @Transactional
    public void modificarCliente(String id, Long documento, String nombre, String apellido, String telefono) throws ErrorServicio {

        validarDatos(documento, nombre, apellido, telefono);

        Optional<Cliente> respuesta = repositorioCliente.findById(id);

        if (respuesta.isPresent()) {

            Cliente cliente = respuesta.get();
            cliente.setDocumento(documento);
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);;
            cliente.setTelefono(telefono);
            cliente.setAlta(true);

            repositorioCliente.save(cliente);
        } else {
            throw new ErrorServicio("El Id ingresado no corresponde a un cliente registrado");
        }
    }

    @Transactional
    public void deshabilitarCliente(String id) throws ErrorServicio {

        Optional<Cliente> respuesta = repositorioCliente.findById(id);

        if (respuesta.isPresent() && respuesta.get().getAlta() == true) {
            Cliente cliente = respuesta.get();
            cliente.setAlta(false);
            repositorioCliente.save(respuesta.get());
        } else {
            throw new ErrorServicio("El Id ingresado no corresponde a un cliente registrado o el cliente ya se encuentra deshabilidato");
        }
    }

    @Transactional
    public void habilitarCliente(String id) throws ErrorServicio {

        Optional<Cliente> respuesta = repositorioCliente.findById(id);

        if (respuesta.isPresent() && respuesta.get().getAlta() == false) {
            Cliente cliente = respuesta.get();
            cliente.setAlta(true);
            repositorioCliente.save(respuesta.get());
        } else {
            throw new ErrorServicio("El Id ingresado no corresponde a un cliente registrado o el cliente ya se encuentra habilidato");
        }
    }

    private void validarDatos(Long documento, String nombre, String apellido, String telefono) throws ErrorServicio {

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
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public Cliente buscarClientePorId(String id) throws ErrorServicio {

        if (id == null || id.trim().isEmpty()) {
            throw new ErrorServicio("El Id ingresado no puede ser nulo o vacio");
        }

        Optional<Cliente> cliente = repositorioCliente.findById(id);

        if (cliente.isPresent()) {
            return cliente.get();
        } else {
            throw new ErrorServicio("El Id no corresponde a un cliente registrado");
        }
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodosLosClientesActivos() throws ErrorServicio {

        List<Cliente> clientes = repositorioCliente.listarTodosLosClientesActivos();

        if (clientes == null || clientes.isEmpty()) {
            throw new ErrorServicio("No hay clientes activos registrados");
        } else {
            return clientes;
        }
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodosLosClientes() throws ErrorServicio {

        List<Cliente> clientes = repositorioCliente.listarTodosLosClientes();

        if (clientes == null || clientes.isEmpty()) {
            throw new ErrorServicio("No hay clientes activos registrados");
        } else {
            return clientes;
        }
    }
}
