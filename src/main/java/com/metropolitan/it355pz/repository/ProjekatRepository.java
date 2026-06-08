package com.metropolitan.it355pz.repository;

import com.metropolitan.it355pz.model.Projekat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjekatRepository extends JpaRepository<Projekat, Long> {
}
