package com.crs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.entity.Room;
import com.common.exception.AlreadyExistException;
import com.common.repository.RoomRepository;

import javassist.NotFoundException;

@Service
public class RoomServicesImpl implements RoomServices {

	@Autowired
	private RoomRepository roomRepository;

	@Override
	public boolean addRoom(Room room) throws AlreadyExistException {
		if(roomRepository.findById(room.getRoomId()).isEmpty()) {roomRepository.save(room);
		return true;}else {
			throw new AlreadyExistException("Room with provided room id '"+room.getRoomId() +"' already exist");
		}
	}

	@Override
	public List<Room> getAllRooms() {
		return roomRepository.findAll();
	}

	@Override
	public boolean checkIfConfiguredWithHotel(String roomId, String hotelId) {
		Room room = roomRepository.checkIfConfiguredWithHotel(roomId, hotelId);
		return room != null;
	}

	@Override
	public Room getRoomById(String roomId) throws NotFoundException {
		Optional<Room> room = roomRepository.findById(roomId);
		if (room.isPresent()) {
			return room.get();
		} else {
			throw new NotFoundException("Room with room id not found roomId : " + roomId);
		}
	}

}
