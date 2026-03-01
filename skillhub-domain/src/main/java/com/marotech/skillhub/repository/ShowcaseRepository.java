package com.marotech.skillhub.repository;

import com.marotech.skillhub.model.BaseEntity;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
@DependsOn("entityManagerFactory")
public interface ShowcaseRepository<T extends BaseEntity>
        extends ListPagingAndSortingRepository<T, String> {


}

