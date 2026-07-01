package com.example.torneos.mapper;

import com.example.torneos.dto.torneo.TorneoRequest;
import com.example.torneos.dto.torneo.TorneoResponse;
import com.example.torneos.entity.Torneo;
import com.example.torneos.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TorneoMapper {
    TorneoMapper INSTANCE = Mappers.getMapper(TorneoMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    @Mapping(target = "participantes", ignore = true)
    Torneo toEntity(TorneoRequest request);

    @Mapping(target = "participantesIds", expression = "java(mapParticipantesToIds(torneo.getParticipantes()))")
    TorneoResponse toResponse(Torneo torneo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    @Mapping(target = "participantes", ignore = true)
    void updateTorneoFromRequest(TorneoRequest request, @MappingTarget Torneo torneo);

    default List<Long> mapParticipantesToIds(List<Usuario> participantes) {
        if (participantes == null) {
            return null;
        }
        return participantes.stream()
                .map(Usuario::getId)
                .collect(Collectors.toList());
    }
}
