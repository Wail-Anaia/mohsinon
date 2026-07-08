package com.mohsinon.modules.mosques.repository;

import com.mohsinon.modules.mosques.entity.Mosque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MosqueRepository extends JpaRepository<Mosque, UUID> {

}