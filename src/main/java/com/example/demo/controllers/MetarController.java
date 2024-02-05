package com.example.demo.controllers;

import com.example.demo.domain.dto.request.AddMetarRequestDto;
import com.example.demo.services.MetarService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/airport")
@RequiredArgsConstructor
public class MetarController {

    private final ModelMapper metarMapper;
    private final MetarService metarService;

    @PostMapping("/{icaoCode}/METAR")
    public ResponseEntity<Long> addMetar(@PathVariable String icaoCode,
                                         @RequestBody AddMetarRequestDto addMetarRequestDto) {
        Long id = metarService.addMetar(icaoCode, addMetarRequestDto);

        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping("/{icaoCode}/METAR")
    public ResponseEntity<Map<String, Object>> getMetar(
            @PathVariable String icaoCode,
            @RequestParam(name = "projections", required = false) String projections
    ) {
        List<String> projectionFields = projections != null ?
                Arrays.stream(projections.split(",")).toList() :
                List.of();

        return new ResponseEntity<>(metarService.getMetar(icaoCode, projectionFields), HttpStatus.OK);
    }
}
