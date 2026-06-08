package com.metropolitan.it355pz.repository;

import com.metropolitan.it355pz.model.Inzenjer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InzenjerRepository extends JpaRepository<Inzenjer, Long> {
}
