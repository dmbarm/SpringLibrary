package org.springlibrary.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springlibrary.dtos.users.LoginUserDTO;
import org.springlibrary.dtos.users.RegisterUserDTO;
import org.springlibrary.services.UsersService;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterUserDTO dto) {
        String jwtToken = usersService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(jwtToken);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginUserDTO dto) {
        String jwtToken = usersService.login(dto);
        return ResponseEntity.ok(jwtToken);
    }
}
