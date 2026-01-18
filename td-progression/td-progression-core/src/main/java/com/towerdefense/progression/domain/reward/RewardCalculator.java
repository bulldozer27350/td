package com.towerdefense.progression.domain.reward;

//Calculateur de récompenses basé sur les étoiles
public class RewardCalculator {

	public int calculateUpgradePoints(int stars) {
		return switch (stars) {
		case 1 -> 1;
		case 2 -> 3;
		case 3 -> 5;
		default -> 0;
		};
	}

	// Pour l'avenir : calculer d'autres types de récompenses
	public Reward calculateRewards(int stars) {
		return new Reward(calculateUpgradePoints(stars));
	}

}