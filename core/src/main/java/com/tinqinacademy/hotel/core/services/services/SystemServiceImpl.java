package com.tinqinacademy.hotel.core.services.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.tinqinacademy.hotel.api.model.BathroomType;
import com.tinqinacademy.hotel.api.model.input.VisitorRegisterInput;
import com.tinqinacademy.hotel.api.model.output.VisitorReportOutput;
import com.tinqinacademy.hotel.api.operations.addroom.AddRoomInput;
import com.tinqinacademy.hotel.api.operations.addroom.AddRoomOutput;
import com.tinqinacademy.hotel.api.operations.deleteroom.DeleteRoomInput;
import com.tinqinacademy.hotel.api.operations.deleteroom.DeleteRoomOutput;
import com.tinqinacademy.hotel.api.operations.partiallyupdate.PartiallyUpdateInput;
import com.tinqinacademy.hotel.api.operations.partiallyupdate.PartiallyUpdateOutput;
import com.tinqinacademy.hotel.api.operations.registervisitors.RegisterVisitorsInput;
import com.tinqinacademy.hotel.api.operations.registervisitors.RegisterVisitorsOutput;
import com.tinqinacademy.hotel.api.operations.report.ReportInput;
import com.tinqinacademy.hotel.api.operations.report.ReportOutput;
import com.tinqinacademy.hotel.api.operations.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.core.services.exceptions.HotelApiException;

import com.tinqinacademy.hotel.api.interfaces.SystemService;

import com.tinqinacademy.hotel.persistence.models.Bed;
import com.tinqinacademy.hotel.persistence.models.Booking;
import com.tinqinacademy.hotel.persistence.models.Guest;
import com.tinqinacademy.hotel.persistence.models.Room;
import com.tinqinacademy.hotel.persistence.models.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.GuestRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemServiceImpl implements SystemService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final GuestRepository guestRepository;
    private final ConversionService conversionService;
    private final BedRepository bedRepository;
    private final ObjectMapper mapper;

    @Override
    public AddRoomOutput addRoom(AddRoomInput input) {
        log.info("Start addRoom input = {}", input);
        UUID uuid = UUID.randomUUID();
        List<Bed> beds = new ArrayList<>();

        if (roomRepository.existsByRoomNumber(input.getRoomNo()))
            throw new HotelApiException("Room number already exists");

        if (input.getBedSizes().size() != input.getBedCount())
            throw new HotelApiException("Wrong bed count");

        for (String code : input.getBedSizes()) {
            if (BedSize.getByCode(code).getCode().isEmpty())
                continue;
            beds.add(bedRepository.findBySize(BedSize.getByCode(code))
                    .orElseThrow(() -> new HotelApiException("Wrong bed")));
        }

        Room room = Objects.requireNonNull(conversionService.convert(input, Room.RoomBuilder.class))
                .beds(beds)
                .build();

        roomRepository.save(room);

        AddRoomOutput output = conversionService.convert(room, AddRoomOutput.class);

        log.info("End addRoom output = {}", output);
        return output;
    }

    @Override
    public RegisterVisitorsOutput registerVisitors(RegisterVisitorsInput input) {
        log.info("Start registerVisitors input = {}", input);

        for (VisitorRegisterInput visitorRegister : input.getVisitorRegisterInputs()) {
            Room room = roomRepository.findRoomByRoomNumber(visitorRegister.getRoomNo())
                    .orElseThrow(() -> new HotelApiException("Wrong room id"));
            Booking booking = bookingRepository.findBookingByRoomAndStartDateAndEndDate(room, visitorRegister.getStartDate(), visitorRegister.getEndDate())
                    .orElseThrow(() -> new HotelApiException("Missing booking"));

            Guest guest = conversionService.convert(visitorRegister, Guest.class);
            if (guest == null)
                throw new HotelApiException("Guest is missing");
            Guest newGuest = guestRepository.save(guest);

            booking.getGuests().add(newGuest);

            bookingRepository.save(booking);
        }


        RegisterVisitorsOutput output = RegisterVisitorsOutput
                .builder()
                .build();
        log.info("End registerVisitors output = {}", output);
        return output;
    }

    @Override
    public ReportOutput reportByCriteria(ReportInput input) {
        log.info("Start reportByCriteria input = {}", input);
        List<VisitorReportOutput> visitors = Arrays.asList(
                VisitorReportOutput
                        .builder()
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(5))
                        .firstName("Пепи")
                        .lastName("Пупи")
                        .phoneNo("+35988888888888")
                        .idCardNo("399b09")
                        .idCardValidity(LocalDate.now().plusYears(7))
                        .idCardIssueAthority("МВР Tinqin")
                        .idCardIssueDate(LocalDate.now().minusYears(3))
                        .build(),
                VisitorReportOutput
                        .builder()
                        .startDate(LocalDate.now().plusDays(15))
                        .endDate(LocalDate.now().plusDays(9))
                        .firstName("Измислен")
                        .lastName("Драганов")
                        .phoneNo("+35988888888888")
                        .idCardNo("399b09")
                        .idCardValidity(LocalDate.now().plusYears(5))
                        .idCardIssueAthority("МВР Tinqin")
                        .idCardIssueDate(LocalDate.now().minusYears(5))
                        .build(),
                VisitorReportOutput
                        .builder()
                        .startDate(LocalDate.now().plusDays(15))
                        .endDate(LocalDate.now().plusDays(9))
                        .firstName("Измислен")
                        .lastName("Драганов")
                        .phoneNo("+35988888888888")
                        .idCardNo("399b09")
                        .idCardValidity(LocalDate.now().plusYears(5))
                        .idCardIssueAthority("МВР Tinqin")
                        .idCardIssueDate(LocalDate.now().minusYears(5))
                        .build()
        );
        ReportOutput output = ReportOutput
                .builder()
                .visitors(visitors)
                .build();
        log.info("End reportByCriteria output = {}", output);
        return output;
    }

    @Override
    public DeleteRoomOutput deleteRoom(DeleteRoomInput input) {
        log.info("Start deleteRoom input = {}", input);
        if (bookingRepository.existsBookingByRoomId(UUID.fromString(input.getId()))) {
            throw new HotelApiException("Room is booked");
        }
        roomRepository.deleteById(UUID.fromString(input.getId()));
        DeleteRoomOutput output = DeleteRoomOutput.builder().build();
        log.info("End deleteRoom output = {}", output);
        return output;
    }

    @Override
    public UpdateRoomOutput updateRoom(UpdateRoomInput input) {
        log.info("Start updateRoom input = {}", input);

        Room currentRoom = roomRepository.findById(UUID.fromString(input.getRoomId()))
                .orElseThrow(() -> new HotelApiException("Room with id "+ input.getRoomId() + " not found"));

        if (BathroomType.getByCode(input.getBathroomType()).equals(BathroomType.UNKNOWN)
                && input.getBathroomType() != null) {
            throw new HotelApiException("Bathroom type " + input.getBathroomType() + " not found");
        }

        List<Bed> beds = new ArrayList<>();
        for (String code : input.getBedSizes()) {
            if (BedSize.getByCode(code).getCode().isEmpty())
                continue;
            beds.add(bedRepository.findBySize(BedSize.getByCode(code))
                    .orElseThrow(() -> new HotelApiException("Wrong bed")));
        }

        Room updatedRoom = Objects.requireNonNull(conversionService.convert(input, Room.RoomBuilder.class))
                .beds(beds)
                .floorNumber(currentRoom.getFloorNumber())
                .createdOn(currentRoom.getCreatedOn())
                .build();

        if (!Objects.equals(currentRoom.getRoomNumber(), updatedRoom.getRoomNumber()) && roomRepository.existsByRoomNumber(updatedRoom.getRoomNumber())) {
                throw new HotelApiException("Room number must be unique");
        }
        roomRepository.save(updatedRoom);

        UpdateRoomOutput output = conversionService.convert(updatedRoom, UpdateRoomOutput.class);
        log.info("End updateRoom output = {}", output);
        return output;
    }

    @SneakyThrows
    @Override
    public PartiallyUpdateOutput partiallyUpdate(PartiallyUpdateInput input) {
        log.info("Start partiallyUpdate input = {}", input);

        if (input.getBedSizes() != null && input.getBedSizes().size() != input.getBedCount()) {
            throw new HotelApiException("Wrong bed count");
        }

        Room currentRoom = roomRepository.findById(UUID.fromString(input.getRoomId()))
                .orElseThrow(() -> new HotelApiException("Room with id "+ input.getRoomId() + " not found"));


        if (BathroomType.getByCode(input.getBathroomType()).equals(BathroomType.UNKNOWN)
                && input.getBathroomType() != null) {
            throw new HotelApiException("Bathroom type " + input.getBathroomType() + " not found");
        }

        if (input.getBedSizes() != null) {
            input.getBedSizes().forEach(bedSize ->
            {
                if (BedSize.getByCode(bedSize).equals(BedSize.UNKNOWN)) {
                    throw new HotelApiException("Bed size " + bedSize + " not found");
                }
            });
        }

        Room inputRoom = Objects.requireNonNull(conversionService.convert(input, Room.RoomBuilder.class))
                .beds(input.getBedSizes() != null ?
                        input.getBedSizes().stream().map(bed ->
                                bedRepository.findBySize(BedSize.getByCode(bed)).orElseThrow()
                        ).toList() : null)
                .build();

        JsonNode roomNode = mapper.valueToTree(currentRoom);
        JsonNode inputNode = mapper.valueToTree(inputRoom);

        JsonMergePatch patch = JsonMergePatch.fromJson(inputNode);
        Room updatedRoom = mapper.treeToValue(patch.apply(roomNode), Room.class);


        if (!Objects.equals(currentRoom.getRoomNumber(), updatedRoom.getRoomNumber()) && roomRepository.existsByRoomNumber(updatedRoom.getRoomNumber())) {
            throw new HotelApiException("Room number must be unique");
        }

        roomRepository.save(updatedRoom);

        PartiallyUpdateOutput output = conversionService.convert(updatedRoom, PartiallyUpdateOutput.class);
        log.info("End partiallyUpdate output = {}", output);
        return output;
    }
}
