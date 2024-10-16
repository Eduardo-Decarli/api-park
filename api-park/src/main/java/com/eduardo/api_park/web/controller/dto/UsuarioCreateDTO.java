package com.eduardo.api_park.web.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioCreateDTO {

    // Anotações do Jakarta Validation
    @NotBlank // Não permite o email estar vazio para validar
    @Email(message = "formato de e-mail está inválido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$") // só aceita email valido
    private String username;
    @NotBlank // Não permite Senhas vazias
    @Size(min = 6, max = 6) // Não permite senhas menores ou maiores que 6
    private String password;


    public UsuarioCreateDTO() {
    }

    public UsuarioCreateDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UsuarioCreateDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
