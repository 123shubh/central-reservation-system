package com.crs.services;

import java.time.LocalDate;
import javax.persistence.EntityExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.dto.ReservationDTO;
import com.common.entity.Hotel;
import com.common.entity.Reservation;
import com.common.entity.Response;
import com.common.entity.Room;
import com.common.exception.InvalidDate;
import com.common.exception.InventoryNotAvailableException;
import com.common.exception.RoomNotConfiguredException;
import com.common.repository.HotelRepository;
import com.common.repository.ReservationRepository;
import com.common.repository.RoomRepository;
import com.crs.mappingdtos.MappingReservationObjects;

import javassist.NotFoundException;

/**
 * 
 * @author deshm
 *
 */
@Service
public class ReservationServicesImpl implements ReservationServices {

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private ICommonServices commonServices;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	/**
	 * saving reservation with @input ReservationDTO response with response message
	 * success or exception message
	 */
	public Response postReservation(ReservationDTO reservationDTO) {
		try {

			if (checkMandatoryFields(reservationDTO)
					&& ifDateIsValid(reservationDTO.getArrivalDate(), reservationDTO.getDepartureDate())
					&& isHotelConfigured(reservationDTO.getHotelId())
					&& checkIfRoomConfiguredToHotel(reservationDTO.getRoomId(), reservationDTO.getHotelId())) {

				String reservationId = reservationDTO.getReservationId();
				Hotel hotel;
				if (hotelRepository.findById(reservationDTO.getHotelId()).isPresent()) {
					hotel = hotelRepository.findById(reservationDTO.getHotelId()).get();
				} else {
					throw new NotFoundException(
							"Hotel with provided hotel id '" + reservationDTO.getHotelId() + "' not found");
				}

				int availableInventoryCountInHotel = hotel.getNumberOfRoomsAvailable();

				if (reservationDTO.getReservationStatus().equalsIgnoreCase("commit")
						&& reservationRepository.findById(reservationId).isPresent()) {
					throw new EntityExistsException("Reservation with reservation id : "
							+ reservationDTO.getReservationId() + " already exist");
				} else if (reservationDTO.getReservationStatus().equalsIgnoreCase("modify")
						&& reservationRepository.findById(reservationId).isEmpty()) {
					throw new NotFoundException("Reservation with reservation id : " + reservationDTO.getReservationId()
							+ "does not exist for modification");
				} else if (reservationDTO.getReservationStatus().equalsIgnoreCase("cancel")
						&& reservationRepository.findById(reservationId).isEmpty()) {
					throw new NotFoundException("Reservation with reservation id : " + reservationDTO.getReservationId()
							+ "does not exist for cancellation");
				} else {
					if (!reservationDTO.getReservationStatus().equalsIgnoreCase("cancel")) {
						MappingReservationObjects mappingReservationObjects = new MappingReservationObjects();
						Reservation reservation = mappingReservationObjects.mapResDTOToResEnity(reservationDTO);
						if (reservationDTO.getReservationStatus().equalsIgnoreCase("commit")) {
							if (availableInventoryCountInHotel > 0) {
								reservationRepository.save(reservation);
								hotel.setNumberOfRoomsAvailable(availableInventoryCountInHotel - 1);
								hotelRepository.save(hotel);
								logger.info("New Reservation saved for reservation Id : {}, \n reservation {}",
										reservationId, reservation);
								
							} else {
								throw new InventoryNotAvailableException("Room is not available for hotel Id " + hotel);
							}

						} else {
							reservationRepository.save(reservation);
							logger.info("Reservation modified for reservation Id : {}, \n reservation {}",
									reservationId, reservation);
						}
					} else {
						reservationRepository.deleteById(reservationId);
						hotel.setNumberOfRoomsAvailable(availableInventoryCountInHotel + 1);
						logger.info("Reservation Cancelled with reservation Id: {}", reservationId);
						hotelRepository.save(hotel);
					}

				}
			}
		} catch (Exception e) {
			if (e.getMessage().toLowerCase().contains("not found mandatory field")) {
				return new Response(e.getMessage(), 404, e.getLocalizedMessage(), reservationDTO.getReservationId(),
						reservationDTO.getUniqueId(), reservationDTO.getHotelId(), true, false);
			} else if (e.getMessage().toLowerCase().contains("already exist")) {
				return new Response(e.getMessage(), 409, e.getLocalizedMessage(), reservationDTO.getReservationId(),
						reservationDTO.getUniqueId(), reservationDTO.getHotelId(), true, false);
			} else if (e.getMessage().toLowerCase().contains("invalid date")) {
				return new Response(e.getMessage(), 406, e.getLocalizedMessage(), reservationDTO.getReservationId(),
						reservationDTO.getUniqueId(), reservationDTO.getHotelId(), true, false);
			} else {
				return new Response(e.getMessage(), 503, "Unexpected error while posting reservation",
						reservationDTO.getReservationId(), reservationDTO.getUniqueId(), reservationDTO.getHotelId(),
						true, false);
			}
		}
		return new Response("Reservation Successfully submitted", 200, "Successfull reservation",
				reservationDTO.getReservationId(), reservationDTO.getUniqueId(), reservationDTO.getHotelId(), false,
				true);
	}

	/**
	 * 
	 * @param reservationDTO
	 * @return
	 * @throws NotFoundException
	 */
	public boolean checkMandatoryFields(ReservationDTO reservationDTO) throws NotFoundException {
		if (reservationDTO.getReservationId() == null || reservationDTO.getReservationId().isEmpty()) {
			throw new NotFoundException("Did not found mandatory field reserevation Id in reservation");
		} else if (reservationDTO.getHotelId() == null || reservationDTO.getHotelId().isEmpty()) {
			throw new NotFoundException("Did not found mandatory field hotel id in reservation");
		} else if (reservationDTO.getPrimaryGuestFirstName() == null
				|| reservationDTO.getPrimaryGuestFirstName().isEmpty()) {
			throw new NotFoundException("Did not found mandatory field primary guest first name in reservation");
		} else if (reservationDTO.getPrimaryGuestLastName() == null
				|| reservationDTO.getPrimaryGuestLastName().isEmpty()) {
			throw new NotFoundException("Did not found mandatory field primary guest last Name in reservation");
		} else if (reservationDTO.getPrimaryGuestMobileNumber() == null
				|| reservationDTO.getPrimaryGuestMobileNumber().isEmpty()) {
			throw new NotFoundException("Did not found mandatory field primary guest mobilr number in reservation");
		} else if (reservationDTO.getUniqueId() == null || reservationDTO.getUniqueId().isEmpty()) {
			throw new NotFoundException("Did not found mandatory field unique id in reservation");
		} else if (reservationDTO.getRoomId() == null || reservationDTO.getRoomId().isEmpty()) {
			throw new NotFoundException("Did not found mandatory field room id in reservation");
		} else if (reservationDTO.getRateId() == null || reservationDTO.getRateId().isEmpty()) {
			throw new NotFoundException("Did not found mandatory field rate id in reservation");
		} else if (reservationDTO.getArrivalDate() == null) {
			throw new NotFoundException("Did not found mandatory field arrival date in reservation");
		} else if (reservationDTO.getDepartureDate() == null) {
			throw new NotFoundException("Did not found mandatory field departure date in reservation");
		} else if (reservationDTO.getNumberOfStayDays() == 0) {
			throw new NotFoundException("Did not found mandatory field number of stay days in reservation");
		} else if (reservationDTO.getTotalAmount() == 0) {
			throw new NotFoundException("Did not found mandatory field total amount in reservation");
		} else if (reservationDTO.getCurrencyCode() == null || reservationDTO.getCurrencyCode().isEmpty()) {
			throw new NotFoundException("Did not found mandatory field currency code in reservation");
		} else if (reservationDTO.getChannelPartnerOTA() == null || reservationDTO.getChannelPartnerOTA().isEmpty()) {
			throw new NotFoundException("Did not found mandatory field channel partner OTA in reservation");
		} else if (reservationDTO.getReservationStatus() == null || reservationDTO.getReservationStatus().isEmpty()) {
			throw new NotFoundException("Did not found mandatory field reservation status in reservation");
		} else {
			return true;
		}
	}

	public boolean ifDateIsValid(LocalDate arrivalDate, LocalDate departureDate) throws InvalidDate {
		if (arrivalDate.isAfter(departureDate)) {
			throw new InvalidDate("Invalid date, arrival date is after departure date");
		} else if (arrivalDate.isBefore(LocalDate.now())) {
			throw new InvalidDate("Invalid date, arrival date is in past dates");
		} else if (departureDate.isBefore(LocalDate.now())) {
			throw new InvalidDate("Invalid date, departure date is in past dates");
		}
		return true;
	}

	public boolean isHotelConfigured(String hotelId) throws NotFoundException {
		if (!hotelId.isEmpty()) {
			if (hotelRepository.findById(hotelId).isEmpty()) {
				throw new NotFoundException("Hotel is not configured");
			} else {
				return true;
			}
		} else {
			throw new NotFoundException("Invalid hotel id received in reservation");
		}

	}

	/**
	 * 
	 * @param roomId
	 * @param hotelId
	 * @return
	 * @throws NotFoundException
	 * @throws RoomNotConfiguredException
	 */
	@Override
	public boolean checkIfRoomConfiguredToHotel(String roomId, String hotelId)
			throws NotFoundException, RoomNotConfiguredException {
		if (roomId.isEmpty() || roomId.equals("") && hotelId.isEmpty() && hotelId.equals("")) {
			throw new NotFoundException("Error while getting hotel or room id from reservation");
		} else {
			Room rooms = roomRepository.checkIfConfiguredWithHotel(roomId.toLowerCase(), hotelId.toLowerCase());
			if (rooms != null) {
				return true;
			} else {
				throw new RoomNotConfiguredException(
						"Room with Room Id : " + roomId + ", not configured with Hotel with Hotel Id : " + hotelId);
			}
		}
	}

}
