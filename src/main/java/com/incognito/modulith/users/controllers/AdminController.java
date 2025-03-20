package com.incognito.modulith.users.controllers;

import com.incognito.modulith.users.dto.AdminDto;
import com.incognito.modulith.users.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public List<AdminDto> getAllAdmins() {
        log.info("Fetching all admins");
        return adminService.findAll();
    }

    @GetMapping("/{id}")
    public AdminDto getAdminById(@PathVariable Long id) {
        log.info("Fetching admin by id: {}", id);
        return adminService.findById(id);
    }

    @GetMapping("/department/{department}")
    public List<AdminDto> getAdminsByDepartment(@PathVariable String department) {
        log.info("Fetching admins by department: {}", department);
        return adminService.findByDepartment(department);
    }

    @GetMapping("/level/{level}")
    public List<AdminDto> getAdminsByLevel(@PathVariable Integer level) {
        log.info("Fetching admins by level: {}", level);
        return adminService.findByAdminLevel(level);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminDto createAdmin(@Valid @RequestBody AdminDto adminDto) {
        log.info("Creating new admin");
        return adminService.createAdmin(adminDto);
    }

    @PutMapping("/{id}")
    public AdminDto updateAdmin(@PathVariable Long id, @Valid @RequestBody AdminDto adminDto) {
        log.info("Updating admin with id: {}", id);
        return adminService.updateAdmin(id, adminDto);
    }
}