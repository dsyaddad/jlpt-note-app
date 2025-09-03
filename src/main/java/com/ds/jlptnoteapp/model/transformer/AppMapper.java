// src/main/java/com/ds/jlptnoteapp/model/transformer/AppMapper.java
package com.ds.jlptnoteapp.model.transformer;

import com.ds.jlptnoteapp.model.dto.*;
import com.ds.jlptnoteapp.model.entity.*;
import com.ds.jlptnoteapp.util.GlobalCachedVariable;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AppMapper {

    AppMapper INSTANCE = Mappers.getMapper(AppMapper.class);

    @Mapping(target = "level", expression = "java(levelFromId(dto.getLevelId(), globalCachedVariable))")
    MainNote toEntity(MainNoteDto dto, @Context GlobalCachedVariable globalCachedVariable);

    @Mapping(target = "levelId", expression = "java(levelToId(entity.getLevel()))")
    @Mapping(target = "level", expression = "java(toDto(entity.getLevel()))")
    MainNoteDto toDto(MainNote entity);

    @Mapping(target = "mainNoteId", source = "mainNote.id")
    @Mapping(target = "subFunction", source = "subFunction")
    FormulaDto toDto(Formula entity);

    @Mapping(target = "mainNote.id", source = "mainNoteId")
    @Mapping(target = "subFunction", source = "subFunction")
    Formula toEntity(FormulaDto dto);

    @Mapping(target = "formulaId", source = "formula.id")
    ExampleDto toDto(Example entity);

    @Mapping(target = "formula.id", source = "formulaId")
    Example toEntity(ExampleDto dto);

    LevelDto toDto(Level entity);
    Level toEntity(LevelDto dto);

    // --- JlptWord ---
    @Mapping(target = "level", expression = "java(levelFromId(dto.getLevelId(), globalCachedVariable))")
    JlptWord toEntity(JlptWordDto dto, @Context GlobalCachedVariable globalCachedVariable);

    @Mapping(target = "levelId", expression = "java(levelToId(entity.getLevel()))")
    @Mapping(target = "level", expression = "java(toDto(entity.getLevel()))")
    JlptWordDto toDto(JlptWord entity);

    // --- JlptExample ---
    @Mapping(target = "wordId", source = "word.id")
    JlptExampleDto toDto(JlptExample entity);

    @Mapping(target = "word.id", source = "wordId")
    JlptExample toEntity(JlptExampleDto dto);


    default Level levelFromId(Long id, @Context GlobalCachedVariable cache) {
        if (id == null) return null;
        LevelDto dto = cache.getLevelMapByLevel().get(id);
        return dto != null ? toEntity(dto) : null;
    }

    default Long levelToId(Level level) {
        return level != null ? level.getId() : null;
    }
}

