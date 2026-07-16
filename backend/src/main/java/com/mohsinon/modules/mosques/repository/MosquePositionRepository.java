package com.mohsinon.modules.mosques.repository;

import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.entity.MosquePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MosquePositionRepository extends JpaRepository<MosquePosition, Long>, JpaSpecificationExecutor<Mosque>  {

    Optional<MosquePosition> findByCode(String code);

    boolean existsByCode(String code);

}