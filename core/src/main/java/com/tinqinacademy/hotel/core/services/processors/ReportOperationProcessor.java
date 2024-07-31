package com.tinqinacademy.hotel.core.services.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinqinacademy.hotel.api.interfaces.ErrorHandlerService;
import com.tinqinacademy.hotel.api.model.ErrorWrapper;
import com.tinqinacademy.hotel.api.operations.report.ReportInput;
import com.tinqinacademy.hotel.api.operations.report.ReportOperation;
import com.tinqinacademy.hotel.api.operations.report.ReportOutput;
import com.tinqinacademy.hotel.core.services.base.BaseOperationProcessor;
import com.tinqinacademy.hotel.persistence.models.Booking;
import com.tinqinacademy.hotel.persistence.models.Guest;
import com.tinqinacademy.hotel.persistence.models.Room;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportOperationProcessor extends BaseOperationProcessor<ReportInput, ReportOutput> implements ReportOperation {

    @PersistenceContext
    private EntityManager entityManager;


    public ReportOperationProcessor(ConversionService conversionService, ObjectMapper mapper, ErrorHandlerService errorHandlerService, Validator validator) {
        super(conversionService, mapper, errorHandlerService, validator);
    }

    @Override
    public Either<ErrorWrapper, ReportOutput> process(ReportInput input) {
        return Try.of(() -> reportByCriteria(input))
                .toEither()
                .mapLeft(errorHandlerService::handle);
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

    private List<Predicate> createPredicates(CriteriaBuilder cb, Root<Booking> booking, Join<Booking, Guest> guest, Join<Booking, Room> room, ReportInput input) {
        List<Predicate> predicates = new ArrayList<>();

        addPredicateIfPresent(predicates, Optional.ofNullable(input.getStartDate()), date -> cb.greaterThanOrEqualTo(booking.get("startDate"), date));
        addPredicateIfPresent(predicates, Optional.ofNullable(input.getEndDate()), date -> cb.lessThanOrEqualTo(booking.get("endDate"), date));
        addPredicateIfPresent(predicates, input.getFirstName(), name -> cb.equal(guest.get("firstName"), name));
        addPredicateIfPresent(predicates, input.getLastName(), name -> cb.equal(guest.get("lastName"), name));
        addPredicateIfPresent(predicates, input.getPhoneNo(), phone -> cb.equal(guest.get("phoneNumber"), phone));
        addPredicateIfPresent(predicates, input.getIdCardNo(), cardNumber -> cb.equal(guest.get("idCardNumber"), cardNumber));
        addPredicateIfPresent(predicates, input.getIdCardValidity(), validity -> cb.equal(guest.get("idCardValidity"), validity));
        addPredicateIfPresent(predicates, input.getIdCardIssueAthority(), authority -> cb.equal(guest.get("idCardIssueAuthority"), authority));
        addPredicateIfPresent(predicates, input.getIdCardIssueDate(), issueDate -> cb.equal(guest.get("idCardIssueDate"), issueDate));
        addPredicateIfPresent(predicates, input.getRoomNo(), number -> cb.equal(room.get("roomNumber"), number));

        return predicates;
    }

    private List<Booking> buildCriteriaQuery(ReportInput input) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Booking> query = cb.createQuery(Booking.class);
        Root<Booking> booking = query.from(Booking.class);
        Join<Booking, Guest> guest = booking.join("guests", JoinType.LEFT);
        Join<Booking, Room> room = booking.join("room", JoinType.LEFT);

        List<Predicate> predicates = createPredicates(cb, booking, guest, room, input);

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }

    private List<Booking> getFilteredBookings(ReportInput input,List<Booking> bookings) {
        for (Booking filteredBooking : bookings) {
            Set<Guest> filteredGuests = filteredBooking.getGuests()
                    .stream()
                    .filter(g -> matchesCriteria(g, input))
                    .collect(Collectors.toSet());
            filteredBooking.setGuests(filteredGuests);
        }

        return bookings;
    }



    public ReportOutput reportByCriteria(ReportInput input) {
        logStart(input);

        List<Booking> bookings = buildCriteriaQuery(input);

        List<Booking> filteredBookings = getFilteredBookings(input, bookings);


        ReportOutput output = conversionService.convert(filteredBookings, ReportOutput.class);
        logEnd(output);
        return output;
    }
}
