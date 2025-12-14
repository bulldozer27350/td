package com.towerdefense;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.towerdefense.orchestrator.AppConfig;
import com.towerdefense.orchestrator.Sequencer;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
class SequencerTest {

	@Autowired
	Sequencer sequencer;
	
    @Test
    void fullCycle() {

        
    }
}
