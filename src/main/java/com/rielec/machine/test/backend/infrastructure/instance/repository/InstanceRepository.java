package com.rielec.machine.test.backend.infrastructure.instance.repository;

import com.rielec.machine.test.backend.domain.instance.Instance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InstanceRepository extends MongoRepository<Instance, String>, QuerydslPredicateExecutor<Instance> {
}
