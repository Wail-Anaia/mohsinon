package com.mohsinon.modules.mosques.repository;

import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.entity.MosqueMembership;
import com.mohsinon.modules.mosques.entity.MosquePosition;
import com.mohsinon.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MosqueMembershipRepository extends JpaRepository<MosqueMembership, UUID> {

    List<MosqueMembership> findByMosque(Mosque mosque);

    List<MosqueMembership> findByMosqueAndActiveTrue(Mosque mosque);

    List<MosqueMembership> findByUser(User user);
    
    List<MosqueMembership> findAllByMosqueAndActiveTrue(Mosque mosque);

    List<MosqueMembership> findByMosqueAndPosition_CodeOrderByStartDateDesc(
            Mosque mosque,
            String positionCode
    );

    List<MosqueMembership> findByUserOrderByStartDateDesc(
            User user
    );
    
    List<MosqueMembership> findAllByMosqueAndUserAndActiveTrue(
            Mosque mosque,
            User user
    );
    
    Optional<MosqueMembership> findByMosqueAndPosition_CodeAndActiveTrue(
            Mosque mosque,
            String positionCode
    );
    
    Optional<MosqueMembership> findByMosqueAndUserAndActiveTrue(
            Mosque mosque,
            User user
    );
    
    Optional<MosqueMembership> findByIdAndActiveTrue(UUID id);

    boolean existsByMosqueAndUserAndActiveTrue(
            Mosque mosque,
            User user
    );
    
    boolean existsByMosqueAndPositionAndActiveTrue(
            Mosque mosque,
            MosquePosition position
    );

}