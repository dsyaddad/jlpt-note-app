package com.ds.jlptnoteapp.model.transformer;

import com.ds.jlptnoteapp.model.dto.LemmaDto;
import com.ds.jlptnoteapp.model.entity.Lemma;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LemmaMapper {

    LemmaDto toDto(Lemma entity);

    Lemma toEntity(LemmaDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(LemmaDto dto, @MappingTarget Lemma entity);
}