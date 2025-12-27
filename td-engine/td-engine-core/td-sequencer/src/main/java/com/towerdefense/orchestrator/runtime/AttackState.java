package com.towerdefense.orchestrator.runtime;

/**
 * Represents the state of an attack in the tower defense game.
 */
public enum AttackState {
	/** The attack is waiting to start. */
    WAITING,
    /** The attack is currently running. */
    RUNNING,
    /** The attack has finished. */
    FINISHED
}
