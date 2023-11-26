/*
 * package com.crs.controller;
 * 
 * import java.util.List;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.http.HttpStatus; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.crs.entity.Rate; import com.crs.entity.Response; import
 * com.crs.services.RateServices;
 * 
 * import io.swagger.annotations.Api;
 * 
 * @RestController
 * 
 * @RequestMapping("/rate")
 * 
 * @Api(value = "This is for rate controlling") public class RateController {
 * 
 * @Autowired private RateServices rateServices;
 * 
 * @PostMapping("/post-rate") public ResponseEntity<String>
 * postRate(@RequestBody Rate rate) { rateServices.addRate(rate); return
 * ResponseEntity.status(HttpStatus.OK).body("rate added");
 * 
 * }
 * 
 * @GetMapping("/get-all-rates") public ResponseEntity<List<Rate>> getAllRates()
 * {
 * 
 * return ResponseEntity.status(HttpStatus.OK).body(rateServices.getAllRates());
 * }
 * 
 * }
 */