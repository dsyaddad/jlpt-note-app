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
    MainNoteDto toDto(MainNote entity);

    // helper
    default Level levelFromId(Long id, @Context GlobalCachedVariable cache) {
        if (id == null) return null;
        LevelDto dto = cache.getLevelMapByLevel().get(id);
        return dto != null ? toEntity(dto) : null;
    }

    default Long levelToId(Level level) {
        return level != null ? level.getId() : null;
    }

    // Level
    LevelDto toDto(Level entity);
    Level toEntity(LevelDto dto);

    // Formula
    FormulaDto toDto(Formula entity);
    Formula toEntity(FormulaDto dto);

    // Example
    ExampleDto toDto(Example entity);
    Example toEntity(ExampleDto dto);
}