package com.confinabox.cfp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.confinabox.cfp.domain.Speacker;

import com.confinabox.cfp.repository.SpeackerRepository;
import com.confinabox.cfp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Speacker.
 */
@RestController
@RequestMapping("/api")
public class SpeackerResource {

    private final Logger log = LoggerFactory.getLogger(SpeackerResource.class);

    private static final String ENTITY_NAME = "speacker";

    private final SpeackerRepository speackerRepository;

    public SpeackerResource(SpeackerRepository speackerRepository) {
        this.speackerRepository = speackerRepository;
    }

    /**
     * POST  /speackers : Create a new speacker.
     *
     * @param speacker the speacker to create
     * @return the ResponseEntity with status 201 (Created) and with body the new speacker, or with status 400 (Bad Request) if the speacker has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/speackers")
    @Timed
    public ResponseEntity<Speacker> createSpeacker(@RequestBody Speacker speacker) throws URISyntaxException {
        log.debug("REST request to save Speacker : {}", speacker);
        if (speacker.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new speacker cannot already have an ID")).body(null);
        }
        Speacker result = speackerRepository.save(speacker);
        return ResponseEntity.created(new URI("/api/speackers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /speackers : Updates an existing speacker.
     *
     * @param speacker the speacker to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated speacker,
     * or with status 400 (Bad Request) if the speacker is not valid,
     * or with status 500 (Internal Server Error) if the speacker couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/speackers")
    @Timed
    public ResponseEntity<Speacker> updateSpeacker(@RequestBody Speacker speacker) throws URISyntaxException {
        log.debug("REST request to update Speacker : {}", speacker);
        if (speacker.getId() == null) {
            return createSpeacker(speacker);
        }
        Speacker result = speackerRepository.save(speacker);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, speacker.getId().toString()))
            .body(result);
    }

    /**
     * GET  /speackers : get all the speackers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of speackers in body
     */
    @GetMapping("/speackers")
    @Timed
    public List<Speacker> getAllSpeackers() {
        log.debug("REST request to get all Speackers");
        return speackerRepository.findAll();
        }

    /**
     * GET  /speackers/:id : get the "id" speacker.
     *
     * @param id the id of the speacker to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the speacker, or with status 404 (Not Found)
     */
    @GetMapping("/speackers/{id}")
    @Timed
    public ResponseEntity<Speacker> getSpeacker(@PathVariable Long id) {
        log.debug("REST request to get Speacker : {}", id);
        Speacker speacker = speackerRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(speacker));
    }

    /**
     * DELETE  /speackers/:id : delete the "id" speacker.
     *
     * @param id the id of the speacker to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/speackers/{id}")
    @Timed
    public ResponseEntity<Void> deleteSpeacker(@PathVariable Long id) {
        log.debug("REST request to delete Speacker : {}", id);
        speackerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
