package com.crs.services;

import com.common.dto.ReservationDTO;
import com.common.entity.Response;
import com.common.exception.RoomNotConfiguredException;

import javassist.NotFoundException;

public interface ReservationServices {

	public Response postReservation(ReservationDTO reservationDTO);
	public boolean checkIfRoomConfiguredToHotel(String roomId, String hotelId) throws NotFoundException, RoomNotConfiguredException;
}
