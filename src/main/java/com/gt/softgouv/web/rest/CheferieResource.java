package com.gt.softgouv.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.gt.softgouv.domain.Cheferie;

import com.gt.softgouv.repository.CheferieRepository;
import com.gt.softgouv.web.rest.errors.BadRequestAlertException;
import com.gt.softgouv.web.rest.util.HeaderUtil;
import com.gt.softgouv.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Cheferie.
 */
@RestController
@RequestMapping("/api")
public class CheferieResource {

    private final Logger log = LoggerFactory.getLogger(CheferieResource.class);

    private static final String ENTITY_NAME = "cheferie";

    private final CheferieRepository cheferieRepository;

    public CheferieResource(CheferieRepository cheferieRepository) {
        this.cheferieRepository = cheferieRepository;
    }

    /**
     * POST  /cheferies : Create a new cheferie.
     *
     * @param cheferie the cheferie to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cheferie, or with status 400 (Bad Request) if the cheferie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cheferies")
    @Timed
    public ResponseEntity<Cheferie> createCheferie(@RequestBody Cheferie cheferie) throws URISyntaxException {
        log.debug("REST request to save Cheferie : {}", cheferie);
        if (cheferie.getId() != null) {
            throw new BadRequestAlertException("A new cheferie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cheferie result = cheferieRepository.save(cheferie);
        return ResponseEntity.created(new URI("/api/cheferies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cheferies : Updates an existing cheferie.
     *
     * @param cheferie the cheferie to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cheferie,
     * or with status 400 (Bad Request) if the cheferie is not valid,
     * or with status 500 (Internal Server Error) if the cheferie couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cheferies")
    @Timed
    public ResponseEntity<Cheferie> updateCheferie(@RequestBody Cheferie cheferie) throws URISyntaxException {
        log.debug("REST request to update Cheferie : {}", cheferie);
        if (cheferie.getId() == null) {
            return createCheferie(cheferie);
        }
        Cheferie result = cheferieRepository.save(cheferie);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cheferie.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cheferies : get all the cheferies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cheferies in body
     */
    @GetMapping("/cheferies")
    @Timed
    public ResponseEntity<List<Cheferie>> getAllCheferies(Pageable pageable) {
        log.debug("REST request to get a page of Cheferies");
        Page<Cheferie> page = cheferieRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cheferies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cheferies/:id : get the "id" cheferie.
     *
     * @param id the id of the cheferie to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cheferie, or with status 404 (Not Found)
     */
    @GetMapping("/cheferies/{id}")
    @Timed
    public ResponseEntity<Cheferie> getCheferie(@PathVariable Long id) {
        log.debug("REST request to get Cheferie : {}", id);
        Cheferie cheferie = cheferieRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cheferie));
    }

    /**
     * DELETE  /cheferies/:id : delete the "id" cheferie.
     *
     * @param id the id of the cheferie to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cheferies/{id}")
    @Timed
    public ResponseEntity<Void> deleteCheferie(@PathVariable Long id) {
        log.debug("REST request to delete Cheferie : {}", id);
        cheferieRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
