// ============================================================================
// GAME ENGINE - Gestion du jeu en cours
// ============================================================================

class GameEngine {
    constructor() {
        this.canvas = document.getElementById('game-canvas');
        this.renderer = new GameRenderer(this.canvas);
        this.gameState = null;
        this.gameConfig = null;
        this.isRunning = false;
        this.isPaused = false;
        this.tickInterval = null;
        this.tickSpeed = 200; // ms entre chaque tick
        this.currentTick = 0;
        this.playerId = 'player-1';
        this.selectedTowerType = null;
        this.selectedTowerPosition = null;
        this.lastTickTime = 0;
        this.updateDebounceTimer = null;
        
        this.setupEventListeners();
    }
    
    // ========================================================================
    // INITIALISATION
    // ========================================================================
    
    // ========================================================================
    // GESTION DES LISTENERS SSE
    // ========================================================================

    setupSSEListeners() {
        gameEvents.removeAllListeners();
    
        gameEvents.on('tower-shot', (data) => {
            console.log(`🔫 ${Date.now()} tower-shot`);
            //this.renderer.addShotLine(data);
            // addShotLine fait déjà son propre render
        });

        gameEvents.on('enemy-moved', (data) => {
            console.log(`🚶 ${Date.now()} enemy-moved - HP: ${data.enemy.currentHp}`);
            this.updateEnemyPosition(data.enemy);
            // AJOUT : Render pour afficher le mouvement
            this.renderer.render(this.gameState);
        });

        gameEvents.on('enemy-hit', (data) => {
            console.log(`🎯 ${Date.now()} enemy-hit - Remaining HP: ${data.remainingHp}`);
            let enemy = data.enemy;
            
            if (data.towerPosition && data.enemyPosition) {
                this.renderer.addShotLine({
                    enemy: enemy,
                    towerPosition: data.towerPosition,
                    targetPosition: data.enemyPosition
                });
            }

            this.updateEnemyHealth(data);
            // Render pour afficher la vie mise à jour
            this.renderer.render(this.gameState);
        });

        gameEvents.on('enemy-killed', (data) => {
            console.log(`💀 ${Date.now()} enemy-killed - ID: ${data.enemyId}`);
            this.markEnemyAsDead(data.enemyId);
            // Render pour faire disparaître l'ennemi
            this.renderer.render(this.gameState);
        });

        gameEvents.on('game-won', () => this.onGameOver());
        gameEvents.on('game-lost', () => this.onGameOver());
    }

    async startGame(levelId, gameConfig) {
        try {
            this.gameConfig = gameConfig;
            this.currentTick = 0;
            this.isPaused = false;

            // Configurer les listeners SSE (retire les doublons)
            this.setupSSEListeners();

            // Écouter les mises à jour d'état (moins fréquentes)
            gameEvents.on('state-update', (state) => {
                this.gameState = state;
                this.updateUI();
                this.renderer.render(this.gameState);
            });
            
            // Initialiser le moteur de jeu
            await apiClient.initializeGame(gameConfig);
            
            // Récupérer l'état initial
            this.gameState = await apiClient.getGameState();

            this.playerId = this.gameState.player.id;
            
            // Initialiser le renderer
            this.renderer.init(
                gameConfig.levelConfig.startingMoney ? 20 : gameConfig.levelConfig.paths[0]?.points[gameConfig.pathsConfig.paths[0].points.length - 1]?.x + 5 || 20,
                gameConfig.levelConfig.startingLives ? 15 : gameConfig.levelConfig.paths[0]?.points[gameConfig.pathsConfig.paths[0].points.length - 1]?.y + 5 || 15,
                gameConfig.levelConfig.paths
            );
            
            // Afficher les tours disponibles
            this.displayAvailableTowers();
            
            // Mettre à jour l'affichage
            this.updateUI();
            this.renderer.render(this.gameState);
            
            // Démarrer la boucle de jeu
            this.isRunning = true;
            this.startTickLoop();
            
            console.log('Game started successfully');
        } catch (error) {
            console.error('Failed to start game:', error);
            gameEvents.disconnect(); // AJOUT : Déconnecter en cas d'erreur
            alert('Erreur lors du démarrage du jeu: ' + error.message);
            gameManager.returnToMenu();
        }
    }
    
    stopGame() {
        this.isRunning = false;
        this.isPaused = false;

        if (this.tickInterval) {
            clearInterval(this.tickInterval);
            this.tickInterval = null;
        }
        
        gameEvents.removeAllListeners();
    }
       
    // ========================================================================
    // BOUCLE DE TICK
    // ========================================================================

    startTickLoop() {
        // S'assurer qu'il n'y a pas déjà une boucle en cours
        if (this.tickInterval) {
            clearInterval(this.tickInterval);
        }
        
        this.tickInterval = setInterval(() => {
            this.executeTick();
        }, this.tickSpeed);
    }

    async executeTick() {
        // Ne pas exécuter si le jeu n'est pas en cours ou est en pause
        if (!this.isRunning || this.isPaused) {
            return;
        }
        
        // Éviter les appels trop rapides (throttling)
        const now = Date.now();
        if (now - this.lastTickTime < 50) { // Minimum 50ms entre les ticks
            return;
        }
        this.lastTickTime = now;
        
        try {
            // Appeler le tick du moteur
            await apiClient.tick();
            this.currentTick++;
            
            // Mettre à jour le compteur de tick
            document.getElementById('current-tick').textContent = this.currentTick;
        } catch (error) {
            console.error('Tick error:', error);
            
            if (error.message.includes('game is over') || error.message.includes('GAME_OVER')) {
                this.onGameOver();
            }
        }
    }

    changeTickSpeed(newSpeed) {
        this.tickSpeed = newSpeed;
        
        // Redémarrer la boucle avec la nouvelle vitesse
        if (this.isRunning && this.tickInterval) {
            this.startTickLoop();
        }
    }
    
    togglePause() {
        this.isPaused = !this.isPaused;
        const btn = document.getElementById('pause-btn');
        btn.textContent = this.isPaused ? '▶️ Reprendre' : '⏸️ Pause';

        const speedSelector = document.getElementById('speed-selector');
        if (speedSelector) {
            speedSelector.disabled = this.isPaused;
        }
    }
    
    // ========================================================================
    // INTERFACE UTILISATEUR
    // ========================================================================
    
    updateUI() {
        if (!this.gameState) return;
        
        document.getElementById('player-gold').textContent = this.gameState.player.currentGold;
        document.getElementById('player-lives').textContent = this.gameState.player.currentLives;
        document.getElementById('current-tick').textContent = this.currentTick;
    }
    
    displayAvailableTowers() {
        const container = document.getElementById('available-towers');
        const towers = this.gameConfig.towersConfig.towers;
        
        container.innerHTML = towers.map(tower => {
            const rank1 = tower.ranks[0];
            return `
                <div class="tower-option ${this.selectedTowerType === tower.id ? 'selected' : ''}" 
                     onclick="gameEngine.selectTowerType('${tower.id}')">
                    <div class="tower-name">🗼 ${tower.name}</div>
                    <div class="tower-stats">
                        💰 ${rank1.upgradeCost} | ⚔️ ${rank1.damage} | 🎯 ${rank1.range.toFixed(1)}
                    </div>
                </div>
            `;
        }).join('');
    }
    
    selectTowerType(towerType) {
        this.selectedTowerType = towerType;
        this.displayAvailableTowers();
    }

    // ========================================================================
    // MISE À JOUR DES ENTITÉS VIA SSE
    // ========================================================================

    updateEnemyHealth(hitData) {
        if (!this.gameState || !this.gameState.enemies) return;
        
        const enemy = this.gameState.enemies.find(e => e.id === hitData.enemy.id);
        if (enemy) {
            const oldHp = enemy.currentHp;
            enemy.currentHp = hitData.remainingHp;
            
            console.log(`💔 HP UPDATE: ${oldHp} → ${hitData.remainingHp}`);

            if (enemy.currentHp <= 0) {
                enemy.isAlive = false;
            }
        }
    }

    markEnemyAsDead(enemyId) {
        if (!this.gameState || !this.gameState.enemies) return;
        
        const enemy = this.gameState.enemies.find(e => e.id === enemyId);
        if (enemy) {
            enemy.isAlive = false;
            enemy.currentHp = 0;
        }
    }

    updateEnemyPosition(enemyData) {
        if (!this.gameState || !this.gameState.enemies) return;
        
        const enemy = this.gameState.enemies.find(e => e.id === enemyData.id);
        if (enemy) {
            enemy.position.x = enemyData.position.x;
            enemy.position.y = enemyData.position.y;
        }
    }
    
    // ========================================================================
    // ÉVÉNEMENTS
    // ========================================================================
    
    setupEventListeners() {
        this.canvas.addEventListener('click', (e) => this.onCanvasClick(e));
        this.canvas.addEventListener('mousemove', (e) => this.onCanvasHover(e));
        this.canvas.addEventListener('mouseleave', () => this.onCanvasLeave());
    }
    
    onCanvasClick(event) {
        if (!this.isRunning || this.isPaused) return;
        
        const gridPos = this.renderer.screenToGrid(event.clientX, event.clientY);
        
        // Vérifier si on clique sur une tour existante
        const clickedTower = this.gameState.towers.find(tower => 
            Math.floor(tower.position.x) === gridPos.x && 
            Math.floor(tower.position.y) === gridPos.y
        );
        
        if (clickedTower) {
            this.showTowerActions(clickedTower, gridPos);
        } else if (!this.renderer.isOnPath(gridPos.x, gridPos.y)) {
            // Case vide, proposer de construire
            this.showBuildMenu(gridPos);
        }
    }
    
    onCanvasHover(event) {
        if (!this.isRunning) return;
        
        const gridPos = this.renderer.screenToGrid(event.clientX, event.clientY);
        this.renderer.setHoveredCell(gridPos.x, gridPos.y);
        this.renderer.render(this.gameState);
    }
    
    onCanvasLeave() {
        if (!this.isRunning) return;
        
        this.renderer.clearHover();
        this.renderer.render(this.gameState);
    }
    
    // ========================================================================
    // ACTIONS SUR TOURS
    // ========================================================================
    
    showBuildMenu(gridPos) {
        const modal = document.getElementById('build-tower-modal');
        const container = document.getElementById('build-tower-options');
        
        const towers = this.gameConfig.towersConfig.towers;
        container.innerHTML = towers.map(tower => {
            const rank1 = tower.ranks[0];
            const canAfford = this.gameState.player.currentGold >= rank1.upgradeCost;
            
            return `
                <div class="tower-option ${!canAfford ? 'disabled' : ''}" 
                     onclick="gameEngine.buildTower('${tower.id}', ${gridPos.x}, ${gridPos.y})"
                     style="margin-bottom: 10px;">
                    <div class="tower-name">🗼 ${tower.name}</div>
                    <div class="tower-stats">
                        💰 ${rank1.upgradeCost} | ⚔️ ${rank1.damage} | 🎯 ${rank1.range.toFixed(1)}
                    </div>
                    ${!canAfford ? '<div style="color: red; font-size: 0.8em;">Fonds insuffisants</div>' : ''}
                </div>
            `;
        }).join('');
        
        modal.classList.add('active');
    }
    
    closeBuildModal() {
        document.getElementById('build-tower-modal').classList.remove('active');
    }
    
    async buildTower(towerType, x, y) {
        try {
            await apiClient.placeTower(this.playerId, towerType, x, y);
            this.closeBuildModal();
            
            // Rafraîchir l'état
            this.gameState = await apiClient.getGameState();
            this.updateUI();
            this.renderer.render(this.gameState);
        } catch (error) {
            alert('Erreur: ' + error.message);
        }
    }
    
    showTowerActions(tower, gridPos) {
        const modal = document.getElementById('tower-action-modal');
        const detailsContainer = document.getElementById('tower-action-details');
        const buttonsContainer = document.getElementById('tower-action-buttons');
        
        // Trouver les infos de la tour
        const towerConfig = this.gameConfig.towersConfig.towers.find(t => t.id === tower.towerType);
        if (!towerConfig) return;
        
        detailsContainer.innerHTML = `
            <div style="margin-bottom: 20px;">
                <h3>🗼 ${towerConfig.name}</h3>
                <p>État: ${tower.state === 'BUILDING' ? '🏗️ En construction' : '✅ Opérationnelle'}</p>
            </div>
        `;
        
        buttonsContainer.innerHTML = '';
        
        // Bouton d'amélioration
        if (tower.state !== 'BUILDING') {
            const upgradeBtn = document.createElement('button');
            upgradeBtn.className = 'btn btn-primary';
            upgradeBtn.style.marginRight = '10px';
            upgradeBtn.textContent = '⬆️ Améliorer';
            upgradeBtn.onclick = () => this.upgradeTower(gridPos.x, gridPos.y);
            buttonsContainer.appendChild(upgradeBtn);
        }
        
        // Bouton de vente
        if (tower.state !== 'BUILDING') {
            const sellBtn = document.createElement('button');
            sellBtn.className = 'btn btn-danger';
            sellBtn.textContent = '💰 Vendre';
            sellBtn.onclick = () => this.sellTower(gridPos.x, gridPos.y);
            buttonsContainer.appendChild(sellBtn);
        }
        
        modal.classList.add('active');
    }
    
    closeTowerActionModal() {
        document.getElementById('tower-action-modal').classList.remove('active');
    }
    
    async upgradeTower(x, y) {
        try {
            await apiClient.upgradeTower(this.playerId, x, y);
            this.closeTowerActionModal();
            
            // Rafraîchir l'état
            this.gameState = await apiClient.getGameState();
            this.updateUI();
            this.renderer.render(this.gameState);
        } catch (error) {
            alert('Erreur: ' + error.message);
        }
    }
    
    async sellTower(x, y) {
        if (!confirm('Vendre cette tour ?')) return;
        
        try {
            await apiClient.sellTower(this.playerId, x, y);
            this.closeTowerActionModal();
            
            // Rafraîchir l'état
            this.gameState = await apiClient.getGameState();
            this.updateUI();
            this.renderer.render(this.gameState);
        } catch (error) {
            alert('Erreur: ' + error.message);
        }
    }
    
    // ========================================================================
    // FIN DE PARTIE
    // ========================================================================
    
    onGameOver() {
        this.stopGame();
        
        const lives = this.gameState.player.currentLives;
        const won = lives > 0;
        
        // Calculer les étoiles en fonction des vies restantes
        let stars = 0;
        if (won) {
            const livesPercent = lives / this.gameConfig.levelConfig.startingLives;
            if (livesPercent > 0.75) stars = 3;
            else if (livesPercent > 0.5) stars = 2;
            else stars = 1;
        }
        
        gameManager.onGameEnd(won, stars);
    }
}

// Instance globale
const gameEngine = new GameEngine();