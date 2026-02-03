// ============================================================================
// GAME MANAGER - Gestion globale de l'application
// ============================================================================

class GameManager {
    constructor() {
        this.currentLevel = null;
        this.playerProgress = null;
        this.availableLevels = [];
        this.allUpgrades = [];
    }
    
    // ========================================================================
    // INITIALISATION
    // ========================================================================
    
    async init() {
        try {
            await this.loadPlayerData();
            this.showLevelSelection();
        } catch (error) {
            console.error('Initialization error:', error);
            alert('Erreur de connexion aux serveurs. Vérifiez que les APIs sont démarrées.');
        }
    }
    
    async loadPlayerData() {
        this.playerProgress = await apiClient.getPlayerProgress();
        this.availableLevels = await apiClient.getAvailableLevels();
        this.allUpgrades = await apiClient.getAllUpgrades();
        
        this.updateProgressDisplay();
    }
    
    // ========================================================================
    // NAVIGATION ENTRE ÉCRANS
    // ========================================================================
    
    showLevelSelection() {
        this.switchScreen('level-selection-screen');
        this.displayLevels();
    }
    
    showUpgrades() {
        this.switchScreen('upgrades-screen');
        this.displayUpgrades();
    }
    
    showGame() {
        this.switchScreen('game-screen');
    }
    
    switchScreen(screenId) {
        document.querySelectorAll('.screen').forEach(screen => {
            screen.classList.remove('active');
        });
        document.getElementById(screenId).classList.add('active');
    }
    
    // ========================================================================
    // AFFICHAGE DES NIVEAUX
    // ========================================================================
    
    displayLevels() {
        const container = document.getElementById('levels-grid');
        
        if (this.availableLevels.length === 0) {
            container.innerHTML = `
                <div class="loading">Aucun niveau disponible</div>
            `;
            return;
        }
        
        container.innerHTML = this.availableLevels.map(level => {
            const isLocked = !level.unlocked;
            const stars = level.stars || 0;
            
            return `
                <div class="level-card ${isLocked ? 'locked' : ''}" 
                     onclick="${isLocked ? '' : `gameManager.selectLevel('${level.id}')`}">
                    <div class="level-header">
                        <div class="level-name">📍 ${level.name}</div>
                        <div class="level-status">${isLocked ? '🔒' : level.completed ? '✅' : '⭕'}</div>
                    </div>
                    <div class="stars">
                        ${this.renderStars(stars)}
                    </div>
                    ${!isLocked ? '<p style="margin-top: 10px; color: #667eea; font-weight: bold;">Cliquez pour jouer</p>' : '<p style="margin-top: 10px; color: #999;">Niveau verrouillé</p>'}
                </div>
            `;
        }).join('');
    }
    
    renderStars(count) {
        return Array(3).fill(0).map((_, i) => 
            i < count ? '⭐' : '☆'
        ).join(' ');
    }
    
    updateProgressDisplay() {
        document.getElementById('upgrade-points').textContent = this.playerProgress.upgradePoints;
        document.getElementById('upgrade-points-2').textContent = this.playerProgress.upgradePoints;
        document.getElementById('completed-levels').textContent = this.playerProgress.completedLevels;
    }
    
    // ========================================================================
    // AFFICHAGE DES UPGRADES
    // ========================================================================
    
    displayUpgrades() {
        const container = document.getElementById('upgrades-list');
        
        if (this.allUpgrades.length === 0) {
            container.innerHTML = '<div class="loading">Aucun upgrade disponible</div>';
            return;
        }
        
        // Grouper par type de tour
        const grouped = this.allUpgrades.reduce((acc, upgrade) => {
            if (!acc[upgrade.towerTypeId]) {
                acc[upgrade.towerTypeId] = [];
            }
            acc[upgrade.towerTypeId].push(upgrade);
            return acc;
        }, {});
        
        container.innerHTML = Object.entries(grouped).map(([towerType, upgrades]) => `
            <div class="upgrade-group">
                <h3>🗼 ${towerType}</h3>
                ${upgrades.map(upgrade => {
                    const canAfford = this.playerProgress.upgradePoints >= upgrade.cost;
                    const isUnlocked = upgrade.unlocked;
                    
                    return `
                        <div class="upgrade-item ${isUnlocked ? 'unlocked' : ''}">
                            <div class="upgrade-info">
                                <div class="upgrade-name">
                                    ${isUnlocked ? '✅' : '⭐'} ${upgrade.name}
                                </div>
                                <div class="upgrade-desc">
                                    Rang ${upgrade.towerRank} - ${upgrade.effect.stat}: ${upgrade.effect.modifier > 0 ? '+' : ''}${upgrade.effect.modifier} 
                                </div>
                            </div>
                            ${!isUnlocked ? `
                                <span class="upgrade-cost">💰 ${upgrade.cost}</span>
                                <button class="btn btn-success btn-small" 
                                        ${!canAfford ? 'disabled' : ''}
                                        onclick="gameManager.purchaseUpgrade('${upgrade.id}')">
                                    Débloquer
                                </button>
                            ` : '<span style="color: #28a745; font-weight: bold;">Débloqué</span>'}
                        </div>
                    `;
                }).join('')}
            </div>
        `).join('');
    }
    
    async purchaseUpgrade(upgradeId) {
        try {
            const result = await apiClient.purchaseUpgrade(upgradeId);
            
            if (result.success) {
                // Recharger les données
                await this.loadPlayerData();
                this.displayUpgrades();
                alert('✅ Upgrade débloqué avec succès !');
            } else {
                alert('❌ Impossible de débloquer cet upgrade.');
            }
        } catch (error) {
            alert('Erreur: ' + error.message);
        }
    }
    
    // ========================================================================
    // GESTION DU JEU
    // ========================================================================
    
    async selectLevel(levelId) {
        try {
            this.currentLevel = this.availableLevels.find(l => l.id === levelId);
            
            // Préparer le niveau (avec les upgrades appliqués)
            const gameConfig = await apiClient.prepareLevel(levelId);
            
            // Afficher le nom du niveau
            document.getElementById('level-name').textContent = this.currentLevel.name;
            
            // Passer à l'écran de jeu
            this.showGame();
            
            // Démarrer le jeu
            await gameEngine.startGame(levelId, gameConfig);
        } catch (error) {
            console.error('Failed to start level:', error);
            alert('Erreur lors du chargement du niveau: ' + error.message);
        }
    }
    
    quitGame() {
        if (!confirm('Quitter la partie en cours ?')) return;
        
        gameEngine.stopGame();
        this.returnToMenu();
    }
    
    async onGameEnd(won, stars) {
        const modal = document.getElementById('game-over-modal');
        const title = document.getElementById('game-over-title');
        const message = document.getElementById('game-over-message');
        const rewards = document.getElementById('game-over-rewards');
        
        if (won) {
            title.textContent = '🎉 Victoire !';
            message.innerHTML = `
                <p style="font-size: 1.2em; margin-bottom: 20px;">
                    Vous avez terminé le niveau avec ${stars} étoile${stars > 1 ? 's' : ''} !
                </p>
            `;
            
            // Signaler la complétion au serveur
            try {
                const result = await apiClient.completeLevel(this.currentLevel.id, stars);
                
                rewards.innerHTML = `
                    <div class="reward-item">
                        <div class="reward-value">+${result.upgradePointsEarned}</div>
                        <div>Points d'Upgrade gagnés</div>
                    </div>
                    <div class="reward-item">
                        <div class="reward-value">${result.totalUpgradePoints}</div>
                        <div>Total de points</div>
                    </div>
                `;
                
                // Recharger les données du joueur
                await this.loadPlayerData();
            } catch (error) {
                console.error('Failed to complete level:', error);
            }
        } else {
            title.textContent = '💀 Défaite';
            message.innerHTML = `
                <p style="font-size: 1.2em; margin-bottom: 20px;">
                    Vous avez perdu toutes vos vies...
                </p>
            `;
            rewards.innerHTML = '';
        }
        
        modal.classList.add('active');
    }
    
    returnToMenu() {
        document.getElementById('game-over-modal').classList.remove('active');
        gameEngine.stopGame();
        this.showLevelSelection();
    }
}

// Instance globale
const gameManager = new GameManager();