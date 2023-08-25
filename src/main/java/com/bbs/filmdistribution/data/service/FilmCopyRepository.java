package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.FilmCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FilmCopyRepository extends JpaRepository<FilmCopy, Long>, JpaSpecificationExecutor<FilmCopy>
{

}
