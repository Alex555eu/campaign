package com.example.campaign.repository;

import com.example.campaign.model.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, UUID> {

    List<Keyword> findAllByKeywordStartingWithIgnoreCase(String query);

    List<Keyword> findAllByIdIsIn(List<UUID> ids);

}
