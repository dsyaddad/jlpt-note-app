package com.ds.jlptnoteapp.model.transformer;

import com.ds.jlptnoteapp.model.dto.ConjugationOverrideDto;
import com.ds.jlptnoteapp.model.entity.ConjugationOverride;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ConjugationOverrideMapper {

    @Mappings({
            @Mapping(source = "lemma.id", target = "lemmaId"),
            @Mapping(source = "lemma.headwordKana", target = "lemmaHeadword")
    })
    ConjugationOverrideDto toDto(ConjugationOverride entity);

    @Mappings({
            // saat create/update dari DTO, kita hanya set FK-nya
            @Mapping(source = "lemmaId", target = "lemma.id"),
            @Mapping(target = "lemma.headwordKana", ignore = true) // biar JPA load saja saat needed
    })
    ConjugationOverride toEntity(ConjugationOverrideDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ConjugationOverrideDto dto,
                             @MappingTarget ConjugationOverride entity);
}