package com.api.produtosfavoritos.security;

import com.api.produtosfavoritos.cliente.Cliente;
import com.api.produtosfavoritos.cliente.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConsultaClienteSecurity implements UserDetailsService {

    @Autowired
    ClienteRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Cliente> cliente = repository.getByEmail(email);
        return cliente.map(clienteConsultado -> new ClienteSecurity(clienteConsultado))
                .orElseThrow(() -> new UsernameNotFoundException("Email Inexistente"));
    }
}
