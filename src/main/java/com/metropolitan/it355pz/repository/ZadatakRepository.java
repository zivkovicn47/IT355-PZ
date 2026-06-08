package com.metropolitan.it355pz.repository;

import com.metropolitan.it355pz.model.Zadatak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZadatakRepository extends JpaRepository<Zadatak, Long> {
}
