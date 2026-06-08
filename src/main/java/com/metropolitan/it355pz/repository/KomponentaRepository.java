package com.metropolitan.it355pz.repository;

import com.metropolitan.it355pz.model.Komponenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KomponentaRepository extends JpaRepository<Komponenta, Long> {
}
