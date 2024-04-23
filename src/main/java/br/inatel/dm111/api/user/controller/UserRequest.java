package br.inatel.dm111.api.user.controller;

public record UserRequest(String name,
                          String email,
                          String password,
                          String role) {
}
