package com.crs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.common.dto.ReservationDTO;
import com.common.entity.Response;
import com.crs.services.ICommonServices;
import com.crs.services.ReservationServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "This is reservation Controller")
@RestController
@RequestMapping("/reservation/api/v1")
@CrossOrigin("*")
public class ReservationController {

	@Autowired
	private ReservationServices reservationServices;

	@Autowired
	ICommonServices commonServices;

	private Logger logger = LoggerFactory.getLogger(ReservationController.class);

	@ResponseBody
	@PostMapping("/post-reservation")
	@ApiOperation(value = "this is for postiong reservation request")
	public ResponseEntity<Response> postReservation(@RequestBody ReservationDTO reservationDTO) {
		logger.info("Input Request received : Reservation Id : {} Reservation : {}", reservationDTO.getReservationId(),
				reservationDTO);
		Response response = reservationServices.postReservation(reservationDTO);
		// Create local Kafka topic and and run zookeeper and kafka locally to run smoothly
		//commonServices.sendDataToKafkaBroker(reservationDTO.toString());
		logger.info("Sending response to adapter : {}", response);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
