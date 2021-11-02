package com.libreria.egg.servicios;

import com.libreria.egg.entidades.Cliente;
import com.libreria.egg.errores.ErrorServicio;
import com.libreria.egg.repositorios.RepositorioCliente;
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
}
