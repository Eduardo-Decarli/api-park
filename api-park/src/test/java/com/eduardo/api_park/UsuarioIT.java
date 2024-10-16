package com.eduardo.api_park;

import com.eduardo.api_park.web.controller.dto.UsuarioCreateDTO;
import com.eduardo.api_park.web.controller.dto.UsuarioResponseDTO;
import com.eduardo.api_park.web.controller.dto.UsuarioSenhaDTO;
import com.eduardo.api_park.web.controller.exceptions.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createUsuario_ComUsernameEPasswordValidos_RetornarUsuarioCriadoComStatus201(){
        UsuarioResponseDTO responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("tody@gmail.com", "123456"))
                .exchange().expectStatus()
                .isCreated().expectBody(UsuarioResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("tody@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");

    }

    @Test
    public void createUsuario_ComUsernameInvalido_RetornarErrorMessageStatus422(){
        ErrorMessage responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("", "123456"))
                .exchange().expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("tody@", "123456"))
                .exchange().expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("tody@email.", "123456"))
                .exchange().expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUsuario_ComPasswordInvalido_RetornarErrorMessageStatus422(){
        ErrorMessage responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("tody@gmail.com", ""))
                .exchange().expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("tody@gmail.com", "1234"))
                .exchange().expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("tody@gmail.com", "12345678"))
                .exchange().expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUsuario_ComUsernameRepetido_RetornarErrorMessageComStatus409(){
        ErrorMessage responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("ana@email.com", "123456"))
                .exchange().expectStatus()
                .isEqualTo(409).expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
    }

    @Test
    public void createUsuario_ComIdExistente_RetornarUsuarioCriadoComStatus200(){
        UsuarioResponseDTO responseBody = testClient.post().uri("/api/v1/usuarios/100")
                .exchange().expectStatus()
                .isOk().expectBody(UsuarioResponseDTO.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(100);
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("ana@email.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");
    }

    @Test
    public void createUsuario_ComIdInexistente_RetornarUsuarioCriadoComStatus200(){
        ErrorMessage responseBody = testClient.post().uri("/api/v1/usuarios/0")
                .exchange().expectStatus()
                .isNotFound().expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void editarSenha_ComDadosValidos_RetornarUsuarioCriadoComStatus404(){
        testClient.patch().uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDTO("123456", "123456", "123456"))
                .exchange().expectStatus()
                .isNoContent();
    }

    public void editarSenha_ComIdInexistente_RetornarUsuarioCriadoComStatus404(){
        ErrorMessage responseBody = testClient.patch().uri("/api/v1/usuarios/0")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDTO("123456", "123456", "123456"))
                .exchange().expectStatus()
                .isNotFound().expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    public void editarSenha_ComCamposInvalidos_RetornarUsuarioCriadoComStatus404(){
        ErrorMessage responseBody = testClient.patch().uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDTO("", "", ""))
                .exchange().expectStatus()
                .isEqualTo(422).expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient.patch().uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDTO("12345", "12345", "12345"))
                .exchange().expectStatus()
                .isEqualTo(422).expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient.patch().uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDTO("12345678", "12345678", "12345678"))
                .exchange().expectStatus()
                .isEqualTo(422).expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    public void editarSenha_ComSenhasInvalidas_RetornarUsuarioCriadoComStatus404(){
        ErrorMessage responseBody = testClient.patch().uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDTO("123456", "123456", "000000"))
                .exchange().expectStatus()
                .isEqualTo(400).expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient.patch().uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDTO("000000", "12345", "12345"))
                .exchange().expectStatus()
                .isEqualTo(400).expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

    }
}
