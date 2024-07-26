package com.tinqinacademy.hotel.persistence.init;

import com.tinqinacademy.hotel.persistence.models.Bed;
import com.tinqinacademy.hotel.persistence.models.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
public class BedDataInitializer implements ApplicationRunner {

    private final BedRepository bedRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Start DataInitializer for beds");


        List<BedSize> enumSizes = Arrays.stream(BedSize.values())
                .filter(bedSize -> !bedSize.getCode().isEmpty())
                .toList();

        // Delete entries that are no longer in the enum using a native query
        bedRepository.deleteBySizeNotIn(enumSizes);


        // Fetch the remaining entries from the database
        List<Bed> existingBeds = bedRepository.findAll();


        // this is for inserting or updating a bed
        for (BedSize bedSize : BedSize.values()) {

            //if its empty, we skip it
            if (bedSize.getCode().isEmpty()) {
                continue;
            }
            //I get the bed that matches the bed size
            //if it exists it will be in the optional
            //if not it will be added to the database
            Optional<Bed> matchingBed = existingBeds.stream()
                    .filter(b -> b.getBedSize() == bedSize)
                    .findFirst();

            if (matchingBed.isPresent()) {
                // Update capacity if it's different
                if (!matchingBed.get().getCapacity().equals(bedSize.getCapacity())) {
                    Bed oldBed = bedRepository.findBySize(bedSize).orElse(Bed.builder().build());
                    Bed newBed = Bed.builder()
                            .id(oldBed.getId())
                            .bedSize(bedSize)
                            .capacity(bedSize.getCapacity())
                            .createdOn(oldBed.getCreatedOn())
                            .build();
                    // Saving the updated bed
                    bedRepository.save(newBed);
                    log.info("Updated capacity for bedSize = {}", bedSize);
                }
            } else {
                // Insert new bed record
                Bed bed = Bed.builder()
                        .bedSize(bedSize)
                        .capacity(bedSize.getCapacity())
                        .build();
                bedRepository.save(bed);

                log.info("Adding bed = {}", bed);
            }
        }
        log.info("End DataInitializer for beds");
    }
}
