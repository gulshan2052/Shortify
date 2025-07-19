package com.robotlab.shortify.service;

import com.robotlab.shortify.entity.LengthConfig;
import com.robotlab.shortify.repository.LengthConfigRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Getter
public class LengthConfigService {

    private static final String ID = "ALIAS_LENGTH";
    private static final Integer DEFAULT_LENGTH = 5;

    private int length;

    @Autowired
    private LengthConfigRepository lengthConfigRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        Optional<LengthConfig> lengthConfigOptional = lengthConfigRepository.findById(ID);
        if(lengthConfigOptional.isPresent()) {
            this.length = lengthConfigOptional.get().getLength();
        }
        else {
            LengthConfig lengthConfig = new LengthConfig(ID, DEFAULT_LENGTH);
            lengthConfigRepository.save(lengthConfig);
            this.length = DEFAULT_LENGTH;
        }
    }

    public void incrementAliasLength() {
        lengthConfigRepository.incrementAliasLength(ID);
        Optional<LengthConfig> lengthConfigOptional = lengthConfigRepository.findById(ID);
        lengthConfigOptional.ifPresent(lengthConfig -> this.length = lengthConfig.getLength());
    }

}
