package com.ds.jlptnoteapp.lifecycle;

import com.ds.jlptnoteapp.model.dto.LevelDto;
import com.ds.jlptnoteapp.model.entity.Level;
import com.ds.jlptnoteapp.model.repository.LevelRepository;
import com.ds.jlptnoteapp.model.transformer.AppMapper;
import com.ds.jlptnoteapp.util.GlobalCachedVariable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class StartupListener implements ApplicationRunner {
    private final GlobalCachedVariable globalCachedVariable;
    private final LevelRepository levelRepository;
    private final AppMapper appMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        refreshGlobalVariable();
    }

    private void refreshGlobalVariable() {
        log.info("Refreshing global variable");
        globalCachedVariable.setLevelMapById(findAllAsMapById());
        globalCachedVariable.setLevelMapByLevel(findAllAsMapByLevel());
        globalCachedVariable.setLevelMapByStringLevel(findAllByStringLevel());
        log.info("Refreshing global variable done.");
    }

    private Map<String, LevelDto> findAllByStringLevel() {
        return levelRepository.findAll().stream()
                .collect(Collectors.toMap(Level::getLevel, appMapper::toDto));
    }

    private Map<Long, String> findAllAsMapById() {
        return levelRepository.findAll().stream()
                .collect(Collectors.toMap(Level::getId, Level::getLevel));
    }
    private Map<Long, LevelDto> findAllAsMapByLevel() {
        return levelRepository.findAll().stream()
                .collect(Collectors.toMap(Level::getId, appMapper::toDto));
    }
}
