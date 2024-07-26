package com.tinqinacademy.hotel.persistence.repository;

import com.tinqinacademy.hotel.persistence.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {



    @Query(value = """
                        SELECT r.*
                        FROM rooms r
                        LEFT JOIN bookings b ON r.id = b.room_id
                            AND b.start_date <= :endDate
                            AND b.end_date >= :startDate
                        WHERE b.room_id IS NULL;
            """, nativeQuery = true)
    List<Room> findAvailableRooms(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    Optional<Room> findRoomByRoomNumber(String roomNumber);

    boolean existsByRoomNumber(String roomNumber);
}
