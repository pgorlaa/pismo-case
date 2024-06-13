package com.test.pismo.repository;

import com.test.pismo.model.OperationType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationTypeRepository extends CrudRepository<OperationType, Long> {
}
