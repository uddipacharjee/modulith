package com.incognito.modulith.users.controllers;

import com.incognito.modulith.users.dto.MerchantDto;
import com.incognito.modulith.users.service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/merchants")
@RequiredArgsConstructor
@Slf4j
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping
    public List<MerchantDto> getAllMerchants() {
        log.info("Fetching all merchants");
        return merchantService.findAll();
    }

    @GetMapping("/{id}")
    public MerchantDto getMerchantById(@PathVariable Long id) {
        log.info("Fetching merchant by id: {}", id);
        return merchantService.findById(id);
    }

    @GetMapping("/business-name/{businessName}")
    public List<MerchantDto> getMerchantsByBusinessName(@PathVariable String businessName) {
        log.info("Fetching merchants by business name: {}", businessName);
        return merchantService.findByBusinessName(businessName);
    }

    @GetMapping("/verification-status/{status}")
    public List<MerchantDto> getMerchantsByVerificationStatus(@PathVariable String status) {
        log.info("Fetching merchants by verification status: {}", status);
        return merchantService.findByVerificationStatus(status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MerchantDto createMerchant(@Valid @RequestBody MerchantDto merchantDto) {
        log.info("Creating new merchant");
        return merchantService.createMerchant(merchantDto);
    }

    @PutMapping("/{id}")
    public MerchantDto updateMerchant(@PathVariable Long id, @Valid @RequestBody MerchantDto merchantDto) {
        log.info("Updating merchant with id: {}", id);
        return merchantService.updateMerchant(id, merchantDto);
    }
}