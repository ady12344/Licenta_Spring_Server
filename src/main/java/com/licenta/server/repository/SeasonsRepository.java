package com.licenta.server.repository;

import com.licenta.server.models.Seasons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonsRepository extends JpaRepository<Seasons , Long> {

}
