package com.crs.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.entity.Hotel;
import com.common.exception.AlreadyExistException;
import com.common.repository.HotelRepository;

@Service
public class HotelServiceImpl implements HotelService {

	@Autowired
	private HotelRepository hotelRepository;

	@Override
	public boolean addHotel(Hotel hotel) throws AlreadyExistException {

		if (hotelRepository.findById(hotel.getHotelId()).isEmpty()) {
			hotelRepository.save(hotel);
			return true;
		} else {
			throw new AlreadyExistException(
					"Hotel with provided hotelid '"  + hotel.getHotelId() + "' already exist");

		}

	}

	@Override
	public List<Hotel> getAllHotels() {
		return hotelRepository.findAll();
	}

}
