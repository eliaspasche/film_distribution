package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.FilmDistribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FilmDistributionRepository extends JpaRepository<FilmDistribution, Long>, JpaSpecificationExecutor<FilmDistribution>
{

}