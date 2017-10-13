package com.confinabox.cfp.repository;

import com.confinabox.cfp.domain.Speacker;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Speacker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpeackerRepository extends JpaRepository<Speacker, Long> {

}
