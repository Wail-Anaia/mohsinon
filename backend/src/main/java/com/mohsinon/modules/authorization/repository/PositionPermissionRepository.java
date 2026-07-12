package com.mohsinon.modules.authorization.repository;

import com.mohsinon.modules.authorization.entity.Permission;
import com.mohsinon.modules.authorization.entity.PositionPermission;
import com.mohsinon.modules.mosques.entity.MosquePosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionPermissionRepository
        extends JpaRepository<PositionPermission, Long> {

    List<PositionPermission> findByPosition(MosquePosition position);
    
    List<PositionPermission> findByPositionId(Long positionId);
    
    Optional<PositionPermission> findByPositionAndPermission(
            MosquePosition position,
            Permission permission
    );

    void deleteByPositionAndPermission(
            MosquePosition position,
            Permission permission
    );

    boolean existsByPositionAndPermission(
            MosquePosition position,
            Permission permission
    );

}