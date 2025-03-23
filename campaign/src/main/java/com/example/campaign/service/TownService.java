package com.example.campaign.service;

import com.example.campaign.model.Town;
import com.example.campaign.repository.TownRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TownService {

    private final TownRepository townRepository;

    public Optional<Town> getTownById(UUID id) {
        return this.townRepository.findById(id);
    }

    public List<Town> getAllTowns() {
        return this.townRepository.findAll();
    }

}
