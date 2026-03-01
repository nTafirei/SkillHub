package com.marotech.skillhub.repository;

import com.marotech.skillhub.model.BaseEntity;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn("entityManagerFactory")
public interface GenericRepository<T extends BaseEntity> extends CrudRepository<T, String> {
}

