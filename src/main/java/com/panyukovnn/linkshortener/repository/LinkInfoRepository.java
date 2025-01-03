package com.panyukovnn.linkshortener.repository;

import com.panyukovnn.linkshortener.model.LinkInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LinkInfoRepository extends JpaRepository<LinkInfo, UUID> {

    @Query("""
        FROM LinkInfo
        WHERE shortLink = :shortLink
        AND active = true
        AND (endTime is null or endTime > :now)
        """)
    Optional<LinkInfo> findActiveShortLink(String shortLink, LocalDateTime now);

    @Query("""
        UPDATE LinkInfo
                set openingCount = openingCount + 1
                where shortLink = :shortLink
        """)
    @Modifying
    @Transactional
    void incrementOpeningCountByShortLink(String shortLink);

    @Query(value = """
        SELECT *
        FROM link_info
        WHERE (:linkPart is null or lower(link) like lower(concat('%', :linkPart, '%')))
                AND (cast(:endTimeFrom as date) is null or end_time >= :endTimeFrom)
                AND (cast(:endTimeTo as date) is null or end_time <= :endTimeTo)
                AND (:descriptionPart is null or lower(description) like lower(concat('%', :descriptionPart, '%')))
                AND (:active is NULL or active = :active)
        """, nativeQuery = true)
    List<LinkInfo> findByFilter(
        String linkPart,
        LocalDateTime endTimeFrom,
        LocalDateTime endTimeTo,
        String descriptionPart,
        Boolean active
    );


}
