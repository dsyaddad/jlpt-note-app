package com.ds.jlptnoteapp.model.transformer;

import com.ds.jlptnoteapp.model.dto.ExampleDto;
import com.ds.jlptnoteapp.model.dto.FormulaDto;
import com.ds.jlptnoteapp.model.dto.LevelDto;
import com.ds.jlptnoteapp.model.dto.MainNoteDto;
import com.ds.jlptnoteapp.model.entity.Example;
import com.ds.jlptnoteapp.model.entity.Formula;
import com.ds.jlptnoteapp.model.entity.Level;
import com.ds.jlptnoteapp.model.entity.MainNote;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AppMapper {

    AppMapper INSTANCE = Mappers.getMapper(AppMapper.class);

    // MainNote
    MainNoteDto toDto(MainNote entity);
    MainNote toEntity(MainNoteDto dto);

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