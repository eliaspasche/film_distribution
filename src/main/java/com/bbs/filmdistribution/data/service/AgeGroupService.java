package com.bbs.filmdistribution.data.service;

import com.bbs.filmdistribution.data.entity.AgeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgeGroupService {

    private final AgeGroupRepository repository;

    public Optional<AgeGroup> get(Long id) {
        return repository.findById(id);
    }

    public AgeGroup update(AgeGroup entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<AgeGroup> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<AgeGroup> list(Pageable pageable, Specification<AgeGroup> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
