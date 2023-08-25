package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FilmRepository extends JpaRepository<Film, Long>, JpaSpecificationExecutor<Film>
{

}
