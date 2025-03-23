package com.example.campaign.controller;


import com.example.campaign.model.Town;
import com.example.campaign.service.TownService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/town")
public class TownController {

    private final TownService townService;

    @GetMapping("/all")
    public ResponseEntity<List<Town>> getAllTowns() {
        List<Town> towns = townService.getAllTowns();
        return ResponseEntity.ok(towns);
    }

}
