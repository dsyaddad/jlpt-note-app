package com.ds.jlptnoteapp.util;

import com.ds.jlptnoteapp.model.dto.LevelDto;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
public class GlobalCachedVariable {
    private Map<Long, String> levelMapById;
    private Map<Long, LevelDto> levelMapByLevel;
}