package com.matheusvsdev.crud_clientes.repository;

import com.matheusvsdev.crud_clientes.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
