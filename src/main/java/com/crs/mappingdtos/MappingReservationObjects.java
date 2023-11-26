package com.crs.mappingdtos;

import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.dto.ReservationDTO;
import com.common.entity.Reservation;
import javassist.NotFoundException;

public class MappingReservationObjects {

	public MappingReservationObjects() {
		super();
	}

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public Reservation mapResDTOToResEnity(ReservationDTO reservationDTO) throws NotFoundException {
		Reservation reservation = new Reservation();
		ArrayList<String> arrayListOfPrimaryGuestMobileNumber = new ArrayList<>();
		ArrayList<String> arrayListOfgetSecondaryGuestNames = new ArrayList<>();
		ArrayList<String> arrayListOfSecondaryGuestMobileNumbers = new ArrayList<>();

		try {
			for (String primaryGuestMobileNumber : reservationDTO.getPrimaryGuestMobileNumber()) {
				arrayListOfPrimaryGuestMobileNumber.add(primaryGuestMobileNumber);
			}

			for (String secondaryGuestNames : reservationDTO.getSecondaryGuestNames()) {
				arrayListOfgetSecondaryGuestNames.add(secondaryGuestNames);
			}

			for (String secondaryGuestMobileNumbers : reservationDTO.getSecondaryGuestMobileNumbers()) {
				arrayListOfSecondaryGuestMobileNumbers.add(secondaryGuestMobileNumbers);
			}
			reservation = new Reservation(reservationDTO.getReservationId(), reservationDTO.getHotelId(),
					reservationDTO.getHotelName(), reservationDTO.getGuestId(),
					reservationDTO.getPrimaryGuestFirstName(), reservationDTO.getPrimaryGuestLastName(),
					arrayListOfPrimaryGuestMobileNumber, arrayListOfgetSecondaryGuestNames,
					arrayListOfSecondaryGuestMobileNumbers, reservationDTO.getUniqueId(), reservationDTO.getRoomId(),
					reservationDTO.getRoomName(), reservationDTO.getRateId(), reservationDTO.getRateName(),
					reservationDTO.getHotelCityName(), reservationDTO.getHotelCityPostalCode(),
					reservationDTO.getBookingCreatedTime(), reservationDTO.getArrivalDate(),
					reservationDTO.getDepartureDate(), reservationDTO.getNumberOfStayDays(),
					reservationDTO.getTotalAmount(), reservationDTO.getChargesPerDay(),
					reservationDTO.getChargesPerPerson(), reservationDTO.getNumberOfChildren(),
					reservationDTO.getExtraCharges(), reservationDTO.getCurrencyCode(),
					reservationDTO.getChannelPartnerOTA(), reservationDTO.getReservationStatus());
		} catch (Exception e) {
			throw new NotFoundException(e.getLocalizedMessage());
		}

		return reservation;
	}

	public ReservationDTO mapResEnityToResDTO(Reservation reservation){

		ModelMapper modelMapper = new ModelMapper();
		ReservationDTO reservationDTO = new ReservationDTO();
		try {
			reservationDTO = modelMapper.map(reservation, ReservationDTO.class);
			return reservationDTO;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reservationDTO;
	}

}
