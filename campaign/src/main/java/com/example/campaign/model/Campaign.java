package com.example.campaign.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String campaignName;

    @ManyToOne
    private Seller seller;

    @ManyToMany
    private List<Keyword> keywords;

    private BigDecimal bidAmount;

    private BigDecimal campaignFund;

    private Boolean status;

    @ManyToOne
    private Town town;

    private Integer radius;

}
