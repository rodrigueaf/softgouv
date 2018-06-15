package com.gt.softgouv.repository;

import com.gt.softgouv.domain.Cheferie;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cheferie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheferieRepository extends JpaRepository<Cheferie, Long> {

}
