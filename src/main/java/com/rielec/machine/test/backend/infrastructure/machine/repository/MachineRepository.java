package com.rielec.machine.test.backend.infrastructure.machine.repository;

import com.rielec.machine.test.backend.domain.machine.Machine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends MongoRepository<Machine, String>, QuerydslPredicateExecutor<Machine> {
}
