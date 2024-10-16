package com.eduardo.api_park.web.controller;

import com.eduardo.api_park.entity.Usuario;
import com.eduardo.api_park.service.UsuarioService;
import com.eduardo.api_park.web.controller.dto.UsuarioCreateDTO;
import com.eduardo.api_park.web.controller.dto.UsuarioResponseDTO;
import com.eduardo.api_park.web.controller.dto.UsuarioSenhaDTO;
import com.eduardo.api_park.web.controller.dto.mapper.UsuarioMepper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> create(@RequestBody UsuarioCreateDTO createDto){
        Usuario user = usuarioService.salvar(UsuarioMepper.toUsuario(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMepper.toDTO(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable Long id){
        Usuario user = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMepper.toDTO(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UsuarioSenhaDTO dto){
        Usuario user = usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAll(){
        List<Usuario> users = usuarioService.buscarTodos();
        return ResponseEntity.ok().body(users);
    }
}
