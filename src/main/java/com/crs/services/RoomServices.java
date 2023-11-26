package com.crs.services;

import java.util.List;

import com.common.entity.Room;
import com.common.exception.AlreadyExistException;

import javassist.NotFoundException;

public interface RoomServices {

	public boolean addRoom(Room room) throws AlreadyExistException;
	
	public List<Room> getAllRooms();
	
	public Room getRoomById(String roomId)  throws NotFoundException;

	boolean checkIfConfiguredWithHotel(String roomId, String hotelId);
}
