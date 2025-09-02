// src/main/java/com/ds/jlptnoteapp/model/transformer/AppMapper.java
package com.ds.jlptnoteapp.model.transformer;

import com.ds.jlptnoteapp.model.dto.ExampleDto;
import com.ds.jlptnoteapp.model.dto.FormulaDto;
import com.ds.jlptnoteapp.model.dto.LevelDto;
import com.ds.jlptnoteapp.model.dto.MainNoteDto;
import com.ds.jlptnoteapp.model.entity.Example;
import com.ds.jlptnoteapp.model.entity.Formula;
import com.ds.jlptnoteapp.model.entity.Level;
import com.ds.jlptnoteapp.model.entity.MainNote;
import com.ds.jlptnoteapp.util.GlobalCachedVariable;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AppMapper {

    AppMapper INSTANCE = Mappers.getMapper(AppMapper.class);

    // DTO -> Entity
    @Mapping(target = "level", expression = "java(levelFromId(dto.getLevelId(), globalCachedVariable))")
    MainNote toEntity(MainNoteDto dto, @Context GlobalCachedVariable globalCachedVariable);

    // Entity -> DTO
    @Mapping(target = "levelId", expression = "java(levelToId(entity.getLevel()))")
    @Mapping(target = "level", expression = "java(toDto(entity.getLevel()))")
    MainNoteDto toDto(MainNote entity);

    // Formula
    @Mapping(target = "mainNoteId", source = "mainNote.id")
    @Mapping(target = "subFunction", source = "subFunction")
    FormulaDto toDto(Formula entity);

    @Mapping(target = "mainNote.id", source = "mainNoteId")
    @Mapping(target = "subFunction", source = "subFunction")
    Formula toEntity(FormulaDto dto);

    // Example
    @Mapping(target = "formulaId", source = "formula.id")
    ExampleDto toDto(Example entity);

    @Mapping(target = "formula.id", source = "formulaId")
    Example toEntity(ExampleDto dto);

    // Level
    LevelDto toDto(Level entity);
    Level toEntity(LevelDto dto);

    // helper
    default Level levelFromId(Long id, @Context GlobalCachedVariable cache) {
        if (id == null) return null;
        LevelDto dto = cache.getLevelMapByLevel().get(id);
        return dto != null ? toEntity(dto) : null;
    }

    default Long levelToId(Level level) {
        return level != null ? level.getId() : null;
    }
}
