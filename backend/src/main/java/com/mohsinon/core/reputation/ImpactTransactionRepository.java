package com.mohsinon.core.reputation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImpactTransactionRepository extends JpaRepository<ImpactTransaction, UUID> {

    Page<ImpactTransaction> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(t.points), 0) FROM ImpactTransaction t WHERE t.userId = :userId")
    int sumPointsByUserId(@Param("userId") UUID userId);

    boolean existsByUserIdAndReferenceTypeAndReferenceId(UUID userId, String referenceType, UUID referenceId);
}
