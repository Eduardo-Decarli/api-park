package com.eduardo.api_park.web.controller;

import com.eduardo.api_park.entity.Usuario;
import com.eduardo.api_park.service.UsuarioService;
import com.eduardo.api_park.web.controller.dto.UsuarioCreateDTO;
import com.eduardo.api_park.web.controller.dto.UsuarioResponseDTO;
import com.eduardo.api_park.web.controller.dto.UsuarioSenhaDTO;
import com.eduardo.api_park.web.controller.dto.mapper.UsuarioMepper;
import com.eduardo.api_park.web.controller.exceptions.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Usuarios", description = "Contem todas as operações relativas aos recursos para cadastro, edição e leitura de um usuário.")
@RestController // Indica para o Spring que a classe é um controlador REST e vai lidar com requisições HTTP
@RequestMapping("api/v1/usuarios") // Indica para o Spring a rota inicial que os métodos da classe devem começar
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @Operation(summary = "Criar um novo usuário", description = "Recurso para criar um novo usuario",
    responses = {
            @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Usuário e-mail já cadastrado no sistema",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PostMapping // Indica que esse método irá trabalhar com a requisição POST vinda da rota inicial
    public ResponseEntity<UsuarioResponseDTO> create(@Valid @RequestBody UsuarioCreateDTO createDto){ // Valid destaca que terá validação do jakarta Validation e @RequestBody que virá um corpo na requisição e terá que ser colocada na variavel a seguir
        Usuario user = usuarioService.salvar(UsuarioMepper.toUsuario(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMepper.toDTO(user));
    }


    @Operation(summary = "Recuperar um Usuário pelo ID", description = "Recuperar um Usuário pelo ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable Long id){ // @PathVariable marca que a variavel irá armazenar um valor vindo da url
        Usuario user = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMepper.toDTO(user));
    }


    @Operation(summary = "Atualizar a senha", description = "Atualizar senha",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "400", description = "Senha não confere",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UsuarioSenhaDTO dto){
        Usuario user = usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Listar Usuarios do Sistema", description = "Listar usuarios",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class)))),
                    @ApiResponse(responseCode = "400", description = "Senha não confere",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> getAll(){
        List<Usuario> users = usuarioService.buscarTodos();
        return ResponseEntity.ok(UsuarioMepper.toListDto(users));
    }
}
