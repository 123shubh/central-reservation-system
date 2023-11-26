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

import com.common.entity.Hotel;
import com.common.exception.AlreadyExistException;
import com.crs.services.HotelService;

@RestController
@RequestMapping("/hotel")
public class HotelController {

	@Autowired
	private HotelService hotelService;

	@PostMapping("/add-hotel")
	public ResponseEntity<String> postHotel(@RequestBody Hotel hotel) throws AlreadyExistException {
		hotelService.addHotel(hotel);
		return ResponseEntity.status(HttpStatus.OK).body("hotel added");
	}

	@GetMapping("/get-all-hotels")
	public ResponseEntity<List<Hotel>> getAllHotels() {
		return ResponseEntity.status(HttpStatus.OK).body(hotelService.getAllHotels());
	}
}
