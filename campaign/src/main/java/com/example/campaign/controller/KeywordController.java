package com.example.campaign.controller;


import com.example.campaign.model.Keyword;
import com.example.campaign.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/keyword")
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping("/matching")
    public ResponseEntity<List<Keyword>> getKeywordsMatchingQuery(@RequestParam String query) {
        List<Keyword> keywords = keywordService.getAllMatchingKeywords(query);
        return ResponseEntity.ok(keywords);
    }


}
