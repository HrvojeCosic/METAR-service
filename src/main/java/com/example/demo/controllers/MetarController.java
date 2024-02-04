package com.example.demo.controllers;

import com.example.demo.domain.dto.request.AddMetarRequestDto;
import com.example.demo.domain.dto.response.GetMetarResponseDto;
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
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<GetMetarResponseDto> getMetar(@PathVariable String icaoCode) {
        GetMetarResponseDto metarDto = metarMapper.map(metarService.getMetar(icaoCode), GetMetarResponseDto.class);

        return new ResponseEntity<>(metarDto, HttpStatus.OK);
    }
}
