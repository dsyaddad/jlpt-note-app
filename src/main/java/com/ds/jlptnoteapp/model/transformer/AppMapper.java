// src/main/java/com/ds/jlptnoteapp/model/transformer/AppMapper.java
package com.ds.jlptnoteapp.model.transformer;

import com.ds.jlptnoteapp.model.dto.*;
import com.ds.jlptnoteapp.model.entity.*;
import com.ds.jlptnoteapp.util.GlobalCachedVariable;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AppMapper {

    AppMapper INSTANCE = Mappers.getMapper(AppMapper.class);
    // ===== MainNote =====
    @Mapping(target = "level", ignore = true)      // set di service via repo reference
    @Mapping(target = "formulas", ignore = true)   // koleksi dikelola di service (sync)
    MainNote toEntity(MainNoteDto dto, @Context GlobalCachedVariable cache);

    @Mapping(target = "level", ignore = true)
    @Mapping(target = "formulas", ignore = true)
    void updateEntity(@MappingTarget MainNote target, MainNoteDto dto, @Context GlobalCachedVariable cache);

    @Mapping(target = "levelId", expression = "java(levelToId(entity.getLevel()))")
    @Mapping(target = "level", expression = "java(toDto(entity.getLevel()))")
    MainNoteDto toDto(MainNote entity);

    // ===== Formula =====
    @Mapping(target = "mainNote", ignore = true)   // ⬅️ jangan buat MainNote transien
    @Mapping(target = "examples", ignore = true)   // sync di service
    Formula toEntity(FormulaDto dto);

    @Mapping(target = "mainNote", ignore = true)
    @Mapping(target = "examples", ignore = true)
    void updateEntity(@MappingTarget Formula target, FormulaDto dto);

    FormulaDto toDto(Formula entity); // mapping simple fields saja

    // ===== Example =====
    @Mapping(target = "formula", ignore = true)    // ⬅️ jangan buat Formula transien
    Example toEntity(ExampleDto dto);

    @Mapping(target = "formula", ignore = true)
    void updateEntity(@MappingTarget Example target, ExampleDto dto);

    ExampleDto toDto(Example entity);

    // ===== Level =====
    LevelDto toDto(Level entity);
    Level toEntity(LevelDto dto); // (dipakai hanya untuk toDto MainNote)

    // pastikan mapper TIDAK menyentuh koleksi
    @Mapping(target = "examples", ignore = true)     // ⬅️ penting
    @Mapping(target = "level", ignore = true)        // (sesuai saran sebelumnya)
    void updateEntity(@MappingTarget JlptWord target, JlptWordDto dto, @Context GlobalCachedVariable cache);

    // untuk toEntity (NEW), boleh ignore juga dan isi manual di service
    @Mapping(target = "examples", ignore = true)     // ⬅️ penting
    @Mapping(target = "level", ignore = true)
    JlptWord toEntity(JlptWordDto dto, @Context GlobalCachedVariable cache);

    // child mapper tetap:
    @Mapping(target = "word", ignore = true)
    JlptExample toEntity(JlptExampleDto dto);
    @Mapping(target = "word", ignore = true)
    void updateEntity(@MappingTarget JlptExample target, JlptExampleDto dto);


    @Mapping(target = "levelId", expression = "java(levelToId(entity.getLevel()))")
    @Mapping(target = "level", expression = "java(toDto(entity.getLevel()))")
    JlptWordDto toDto(JlptWord entity);

    // --- JlptExample ---
    @Mapping(target = "wordId", source = "word.id")
    JlptExampleDto toDto(JlptExample entity);


    default Level levelFromId(Long id, @Context GlobalCachedVariable cache) {
        if (id == null) return null;
        LevelDto dto = cache.getLevelMapByLevel().get(id);
        return dto != null ? toEntity(dto) : null;
    }

    default Long levelToId(Level level) {
        return level != null ? level.getId() : null;
    }
}

