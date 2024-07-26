package com.tinqinacademy.hotel.persistence.repository;

import com.tinqinacademy.hotel.persistence.models.Bed;
import com.tinqinacademy.hotel.persistence.models.enums.BedSize;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BedRepository extends JpaRepository<Bed, UUID> {
    @Query("""
            SELECT b FROM Bed b
            WHERE b.bedSize=?1
            """)
    Optional<Bed> findBySize(BedSize bedSize);




    @Transactional
    @Modifying
    @Query("DELETE FROM Bed b WHERE b.bedSize NOT IN :sizes")
    void deleteBySizeNotIn(List<BedSize> sizes);

}
