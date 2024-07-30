package com.tinqinacademy.hotel.core.services.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import com.tinqinacademy.hotel.persistence.models.*;
import com.tinqinacademy.hotel.persistence.models.enums.BedSize;
import com.tinqinacademy.hotel.persistence.repository.BedRepository;
import com.tinqinacademy.hotel.persistence.repository.BookingRepository;
import com.tinqinacademy.hotel.persistence.repository.GuestRepository;
import com.tinqinacademy.hotel.persistence.repository.RoomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @PersistenceContext
    private EntityManager entityManager;

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

    private <T> void addPredicateIfPresent(List<Predicate> predicates, Optional<T> value, Function<T, Predicate> predicateFunction) {
        value.ifPresent(v -> predicates.add(predicateFunction.apply(v)));
    }

    private boolean matchesCriteria(Guest guest, ReportInput input) {
        return (input.getFirstName().isEmpty() || input.getFirstName().get().equals(guest.getFirstName())) &&
                (input.getLastName().isEmpty() || input.getLastName().get().equals(guest.getLastName())) &&
                (input.getPhoneNo().isEmpty() || input.getPhoneNo().get().equals(guest.getPhoneNumber())) &&
                (input.getIdCardNo().isEmpty() || input.getIdCardNo().get().equals(guest.getIdCardNumber())) &&
                (input.getIdCardValidity().isEmpty() || input.getIdCardValidity().get().equals(guest.getIdCardValidity())) &&
                (input.getIdCardIssueAthority().isEmpty() || input.getIdCardIssueAthority().get().equals(guest.getIdCardIssueAuthority())) &&
                (input.getIdCardIssueDate().isEmpty() || input.getIdCardIssueDate().get().equals(guest.getIdCardIssueDate()));
    }



    @Override
    public ReportOutput reportByCriteria(ReportInput input) {
        log.info("Start reportByCriteria input = {}", input);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> query = cb.createQuery(Booking.class);
        Root<Booking> booking = query.from(Booking.class);
        Join<Booking, Guest> guest = booking.join("guests", JoinType.LEFT);
        Join<Booking, Room> room = booking.join("room",JoinType.LEFT);


        List<Predicate> predicates = new ArrayList<>();

        addPredicateIfPresent(predicates, Optional.ofNullable(input.getStartDate()), date -> cb.greaterThanOrEqualTo(booking.get("startDate"), date));
        addPredicateIfPresent(predicates, Optional.ofNullable(input.getEndDate()), date -> cb.lessThanOrEqualTo(booking.get("endDate"), date));
        addPredicateIfPresent(predicates, input.getFirstName(), name -> cb.equal(guest.get("firstName"),  name ));
        addPredicateIfPresent(predicates, input.getLastName(), name -> cb.equal(guest.get("lastName"),  name ));
        addPredicateIfPresent(predicates, input.getPhoneNo(), phone -> cb.equal(guest.get("phoneNumber"), phone));
        addPredicateIfPresent(predicates, input.getIdCardNo(), cardNumber -> cb.equal(guest.get("idCardNumber"), cardNumber));
        addPredicateIfPresent(predicates, input.getIdCardValidity(), validity -> cb.equal(guest.get("idCardValidity"), validity));
        addPredicateIfPresent(predicates, input.getIdCardIssueAthority(), authority -> cb.equal(guest.get("idCardIssueAuthority"),  authority ));
        addPredicateIfPresent(predicates, input.getIdCardIssueDate(), issueDate -> cb.equal(guest.get("idCardIssueDate"), issueDate));
        addPredicateIfPresent(predicates, input.getRoomNo(), number -> cb.equal(room.get("roomNumber"), number));


        query.where(predicates.toArray(new Predicate[0]));


        List<Booking> bookings = entityManager.createQuery(query).getResultList();

        for (Booking filteredBooking : bookings) {
            Set<Guest> filteredGuests = filteredBooking.getGuests()
                    .stream()
                    .filter(g -> matchesCriteria(g, input))
                    .collect(Collectors.toSet());
            filteredBooking.setGuests(filteredGuests);
        }



        ReportOutput output = conversionService.convert(bookings, ReportOutput.class);
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
