package com.crs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.entity.Room;
import com.common.exception.AlreadyExistException;
import com.crs.services.RoomServices;

@RestController
@RequestMapping("/room")
public class RoomController {
	
	@Autowired
	private RoomServices roomServices;
	
	
	@PostMapping("/add-room")
	public ResponseEntity<String> postRoom(@RequestBody Room room) throws AlreadyExistException{
		roomServices.addRoom(room);
		return ResponseEntity.status(HttpStatus.OK).body("room added success");
	}

	@GetMapping("/get-all-rooms")
	public ResponseEntity<List<Room>> getAllRooms(){
		return ResponseEntity.status(HttpStatus.OK).body(roomServices.getAllRooms());
	}
}
