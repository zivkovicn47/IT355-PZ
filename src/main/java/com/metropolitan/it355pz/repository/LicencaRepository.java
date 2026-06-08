package com.metropolitan.it355pz.repository;

import com.metropolitan.it355pz.model.Licenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicencaRepository extends JpaRepository<Licenca, Long> {
}
