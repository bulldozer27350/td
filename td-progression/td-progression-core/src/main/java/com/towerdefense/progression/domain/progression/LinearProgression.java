package com.towerdefense.progression.domain.progression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Progression linéaire simple : chaque niveau débloque le suivant
public class LinearProgression {
	private final Map<String, ProgressionNode> nodes = new LinkedHashMap<>();
	private final List<String> orderedLevelIds;

	public LinearProgression(List<String> levelIds) {
		this.orderedLevelIds = new ArrayList<>(levelIds);

		// Créer la chaîne de progression
		for (int i = 0; i < levelIds.size(); i++) {
			String levelId = levelIds.get(i);
			List<String> requirements = i > 0 ? List.of(levelIds.get(i - 1)) : List.of();
			nodes.put(levelId, new ProgressionNode(levelId, requirements));
		}

		// Débloquer le premier niveau
		if (!levelIds.isEmpty()) {
			nodes.get(levelIds.get(0)).setUnlocked(true);
		}
	}

	// Met à jour les niveaux débloqués en fonction des complétions
	public void updateUnlocks(Set<String> completedLevels) {
		nodes.values().forEach(node -> {
			if (node.canUnlock(completedLevels)) {
				node.setUnlocked(true);
			}
		});
	}

	public List<String> getUnlockedLevelIds() {
		return nodes.values().stream().filter(ProgressionNode::isUnlocked).map(ProgressionNode::getLevelId).toList();
	}

	public String getNextSuggestedLevel(Set<String> completedLevels) {
		// Retourner le premier niveau débloqué mais non complété
		return nodes.values().stream().filter(ProgressionNode::isUnlocked)
				.filter(node -> !completedLevels.contains(node.getLevelId())).map(ProgressionNode::getLevelId)
				.findFirst().orElse(null);
	}

	public boolean isUnlocked(String levelId) {
		ProgressionNode node = nodes.get(levelId);
		return node != null && node.isUnlocked();
	}
}
