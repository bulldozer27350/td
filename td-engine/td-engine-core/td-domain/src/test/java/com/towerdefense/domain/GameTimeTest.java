package com.towerdefense.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GameTimeTest {

    @Test
    void secondsToTicks_converts_correctly() {
        assertEquals(10, GameTime.secondsToTicks(1.0));
        assertEquals(20, GameTime.secondsToTicks(2.0));
        assertEquals(5, GameTime.secondsToTicks(0.5));
        assertEquals(1, GameTime.secondsToTicks(0.05)); // Arrondi supérieur
    }

    @Test
    void secondsToTicks_always_returns_at_least_one() {
        assertEquals(1, GameTime.secondsToTicks(0.01));
        assertEquals(1, GameTime.secondsToTicks(0.001));
    }

    @Test
    void secondsToTicks_throws_on_negative() {
        assertThrows(IllegalArgumentException.class, () -> 
            GameTime.secondsToTicks(-1.0)
        );
    }

    @Test
    void ticksToSeconds_converts_correctly() {
        assertEquals(1.0, GameTime.ticksToSeconds(10), 0.001);
        assertEquals(0.5, GameTime.ticksToSeconds(5), 0.001);
        assertEquals(0.0, GameTime.ticksToSeconds(0), 0.001);
    }

    @Test
    void casesPerSecondToCasesPerTick_converts_correctly() {
        // Vitesse de 1 case/seconde = 0.1 case/tick (avec 10 ticks/sec)
        assertEquals(0.1, GameTime.casesPerSecondToCasesPerTick(1.0), 0.001);
        
        // Vitesse de 5 cases/seconde = 0.5 case/tick
        assertEquals(0.5, GameTime.casesPerSecondToCasesPerTick(5.0), 0.001);
    }

    @Test
    void roundtrip_conversion_is_consistent() {
        double originalSeconds = 2.5;
        int ticks = GameTime.secondsToTicks(originalSeconds);
        double convertedBack = GameTime.ticksToSeconds(ticks);
        
        // La conversion doit être cohérente (avec marge d'arrondi)
        assertEquals(originalSeconds, convertedBack, 0.2);
    }
}