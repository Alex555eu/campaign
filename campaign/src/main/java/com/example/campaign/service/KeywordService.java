package com.example.campaign.service;


import com.example.campaign.model.Keyword;
import com.example.campaign.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public List<Keyword> getAllMatchingKeywords(String query) {
        return keywordRepository.findAllByKeywordStartingWithIgnoreCase(query);
    }

    public List<Keyword> getAllKeywordsByIds(List<UUID> ids) {
        return keywordRepository.findAllByIdIsIn(ids);
    }

}
