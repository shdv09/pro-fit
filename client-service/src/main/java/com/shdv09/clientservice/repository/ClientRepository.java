package com.shdv09.clientservice.repository;

import com.shdv09.clientservice.model.Client;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long>, JpaSpecificationExecutor<Client> {}
