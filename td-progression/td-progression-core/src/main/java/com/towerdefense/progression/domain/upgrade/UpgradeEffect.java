package com.towerdefense.progression.domain.upgrade;

//Effet d'upgrade sur une propriété spécifique d'un niveau de tour
public record UpgradeEffect(
 String stat,           // "damage", "range", "reloadSeconds"...
 double modifier,       // Valeur du modificateur
 ModifierType type      // ADD ou MULTIPLY
) {
	public double apply(double originalValue) {
        return switch (type) {
            case ADD -> originalValue + modifier;
            case MULTIPLY -> originalValue * (1 + modifier);
        };
    }
}
