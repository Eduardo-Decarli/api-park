package com.eduardo.api_park.web.controller.dto.mapper;

import com.eduardo.api_park.entity.Usuario;
import com.eduardo.api_park.web.controller.dto.UsuarioCreateDTO;
import com.eduardo.api_park.web.controller.dto.UsuarioResponseDTO;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMepper {

    public static Usuario toUsuario(UsuarioCreateDTO createDto){
        return new ModelMapper().map(createDto, Usuario.class);
    }

    public static UsuarioResponseDTO toDTO(Usuario usuario){

        String role = usuario.getRole().name().substring("ROLE_".length());
        ModelMapper mapperMain = new ModelMapper();
        TypeMap<Usuario, UsuarioResponseDTO> propertyMapper = mapperMain.createTypeMap(Usuario.class, UsuarioResponseDTO.class);

        propertyMapper.addMappings(
                mapper -> mapper.map(src -> role, UsuarioResponseDTO::setRole)
        );
          return mapperMain.map(usuario, UsuarioResponseDTO.class);
    }

    public static List<UsuarioResponseDTO> toListDto(List<Usuario> usuario){
        return usuario.stream().map(user -> toDTO(user)).collect(Collectors.toList());
    }
}
