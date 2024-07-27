package com.matheusvsdev.crud_clientes.service;

import com.matheusvsdev.crud_clientes.dto.ClientDTO;
import com.matheusvsdev.crud_clientes.entities.Client;
import com.matheusvsdev.crud_clientes.repository.ClientRepository;
import com.matheusvsdev.crud_clientes.service.exceptions.DatabaseException;
import com.matheusvsdev.crud_clientes.service.exceptions.NoSuchElementException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll (Pageable pageable) {
        Page<Client> clientPage = clientRepository.findAll(pageable);
        return clientPage.map(x -> new ClientDTO(x));
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Id não encontrado"));
        ClientDTO clientDTO = new ClientDTO(client);
        return clientDTO;
    }

    @Transactional
    public ClientDTO insert(ClientDTO clientDTO) {
        Client client = new Client();
        client.setName(clientDTO.getName());
        client.setCpf(clientDTO.getCpf());
        client.setIncome(clientDTO.getIncome());
        client.setBirthDate(clientDTO.getBirthDate());
        client.setChildren(clientDTO.getChildren());
        client = clientRepository.save(client);
        return new ClientDTO(client);
    }

    @Transactional
    public ClientDTO updateClients(Long id, ClientDTO clientDTO) {

        try {
            Client client = clientRepository.getReferenceById(id);
            client.setName(clientDTO.getName());
            client.setCpf(clientDTO.getCpf());
            client.setIncome(clientDTO.getIncome());
            client.setBirthDate(clientDTO.getBirthDate());
            client.setChildren(clientDTO.getChildren());
            client = clientRepository.save(client);
            return new ClientDTO(client);
        } catch (EntityNotFoundException e) {
            throw new NoSuchElementException("Id não encontrado");
        }

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new NoSuchElementException("Id não encontrado");
        } try {
            clientRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }

    }

}
