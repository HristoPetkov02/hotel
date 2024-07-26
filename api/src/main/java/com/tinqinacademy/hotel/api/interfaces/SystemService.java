package com.tinqinacademy.hotel.api.interfaces;


import com.fasterxml.jackson.core.JsonProcessingException;
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

public interface SystemService {
    AddRoomOutput addRoom(AddRoomInput input);

    RegisterVisitorsOutput registerVisitors(RegisterVisitorsInput input);

    ReportOutput reportByCriteria(ReportInput input);

    DeleteRoomOutput deleteRoom(DeleteRoomInput input);

    UpdateRoomOutput updateRoom(UpdateRoomInput input);

    PartiallyUpdateOutput partiallyUpdate(PartiallyUpdateInput input);
}
