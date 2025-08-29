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

    // MainNote
    // Mapping DTO -> Entity
    @Mapping(target = "level", expression = "java(levelFromString(dto.getLevel(), globalCachedVariable))")
    MainNote toEntity(MainNoteDto dto, @Context GlobalCachedVariable globalCachedVariable);

    // Mapping Entity -> DTO
    @Mapping(target = "level", expression = "java(levelToString(entity.getLevel()))")
    MainNoteDto toDto(MainNote entity);

    // helper methods
    default Level levelFromString(String key, @Context GlobalCachedVariable cache) {
        if (key == null) return null;
        return toEntity(cache.getLevelMapByLevel().get(key));
    }

    default String levelToString(Level level) {
        return level != null ? level.getId().toString() : null;
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