package com.mohsinon.modules.mosques.repository;

import com.mohsinon.modules.mosques.entity.Mosque;
import com.mohsinon.modules.mosques.entity.MosqueMembership;
import com.mohsinon.modules.mosques.entity.MosquePosition;
import com.mohsinon.modules.mosques.membership.model.MembershipStatus;
import com.mohsinon.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MosqueMembershipRepository extends JpaRepository<MosqueMembership, UUID> {

    /**
     * جميع عضويات المسجد.
     */
    List<MosqueMembership> findByMosque(Mosque mosque);

    /**
     * جميع عضويات المستخدم.
     */
    List<MosqueMembership> findByUser(User user);

    /**
     * جميع عضويات المسجد حسب الحالة.
     */
    List<MosqueMembership> findByMosqueAndStatus(
            Mosque mosque,
            MembershipStatus status
    );

    /**
     * جميع عضويات المسجد حسب الحالة.
     */
    List<MosqueMembership> findAllByMosqueAndStatus(
            Mosque mosque,
            MembershipStatus status
    );

    /**
     * جميع عضويات المستخدم داخل مسجد حسب الحالة.
     */
    List<MosqueMembership> findAllByMosqueAndUserAndStatus(
            Mosque mosque,
            User user,
            MembershipStatus status
    );

    /**
     * العضوية الحالية لمنصب معين.
     */
    Optional<MosqueMembership> findByMosqueAndPosition_CodeAndStatus(
            Mosque mosque,
            String positionCode,
            MembershipStatus status
    );

    /**
     * العضوية الحالية لمستخدم داخل مسجد.
     */
    Optional<MosqueMembership> findByMosqueAndUserAndStatus(
            Mosque mosque,
            User user,
            MembershipStatus status
    );

    /**
     * البحث عن عضوية حسب المعرف والحالة.
     */
    Optional<MosqueMembership> findByIdAndStatus(
            UUID id,
            MembershipStatus status
    );

    /**
     * التحقق من وجود عضوية فعالة.
     */
    boolean existsByMosqueAndUserAndStatus(
            Mosque mosque,
            User user,
            MembershipStatus status
    );

    /**
     * التحقق من وجود شاغل حالي لمنصب فريد.
     */
    boolean existsByMosqueAndPositionAndStatus(
            Mosque mosque,
            MosquePosition position,
            MembershipStatus status
    );

    /**
     * تاريخ شاغلي منصب معين.
     */
    List<MosqueMembership> findByMosqueAndPosition_CodeOrderByStartDateDesc(
            Mosque mosque,
            String positionCode
    );

    /**
     * تاريخ عضويات المستخدم.
     */
    List<MosqueMembership> findByUserOrderByStartDateDesc(
            User user
    );

    /**
     * عدد العضويات حسب الحالة.
     */
    long countByMosqueAndStatus(
            Mosque mosque,
            MembershipStatus status
    );
    
    long countByMosque(Mosque mosque);

}