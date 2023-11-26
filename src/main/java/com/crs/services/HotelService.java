package com.crs.services;

import java.util.List;

import com.common.entity.Hotel;
import com.common.exception.AlreadyExistException;


public interface HotelService {

	public boolean addHotel(Hotel hotel) throws AlreadyExistException;
	
	public List<Hotel> getAllHotels();
	
}
