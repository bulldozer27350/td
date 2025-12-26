package com.towerdefense.domain.dynamik.level;

/**
 * Represents the progress of a player in a tower defense game level.
 *
 * @param levelIndex  the index of the current level
 * @param attackIndex the index of the current attack within the level
 */
public record LevelProgress(int levelIndex, int attackIndex) {
}
