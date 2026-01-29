package com.towerdefense.domain.dynamik.level;

/**
 * Represents the progress of a player in a tower defense game rank.
 *
 * @param levelIndex  the index of the current rank
 * @param attackIndex the index of the current attack within the rank
 */
public record LevelProgress(String levelIndex, int attackIndex) {
}
