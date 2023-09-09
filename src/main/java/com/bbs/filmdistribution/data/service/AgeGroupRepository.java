package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.AgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The repository for the {@link AgeGroup} object.
 */
public interface AgeGroupRepository extends JpaRepository<AgeGroup, Long>, JpaSpecificationExecutor<AgeGroup>
{

}
