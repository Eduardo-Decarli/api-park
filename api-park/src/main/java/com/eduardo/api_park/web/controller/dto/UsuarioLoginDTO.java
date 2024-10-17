package com.eduardo.api_park.web.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class UsuarioLoginDTO {
    @NotBlank // Não permite o email estar vazio para validar
    @Email(message = "formato de e-mail está inválido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$") // só aceita email valido
    private String username;
    @NotBlank // Não permite Senhas vazias
    @Size(min = 6, max = 6) // Não permite senhas menores ou maiores que 6
    private String password;
}
