package com.tinqinacademy.hotel.persistence.repository;

import com.tinqinacademy.hotel.persistence.models.Booking;
import com.tinqinacademy.hotel.persistence.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {


    @Query("""
            SELECT b FROM Booking b
            WHERE b.room.id = :roomId
            """)
    Optional<List<Booking>> findAllBookingsByRoomId(UUID roomId);

    boolean existsBookingByRoomId(UUID roomId);

    Optional<Booking> findBookingByRoomAndStartDateAndEndDate(Room room, LocalDate startDate, LocalDate endDate);


    @Transactional
    @Modifying
    @Query("""
            DELETE FROM Guest g
            WHERE g NOT IN (SELECT b.guests FROM Booking b)
            """)
    void deleteGuestsNotInBooking();
}
