package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.FilmCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface FilmCopyRepository extends JpaRepository<FilmCopy, Long>, JpaSpecificationExecutor<FilmCopy>
{
    @Query( value = "select count(*) from FILM_DISTRIBUTION_ITEMS where FILM_COPY_ID = :filmCopyId", nativeQuery = true )
    int getDistributionsByFilmCopyId( long filmCopyId );
}
