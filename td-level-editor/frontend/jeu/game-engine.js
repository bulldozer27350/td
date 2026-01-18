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
        
        this.setupEventListeners();
    }
    
    // ========================================================================
    // INITIALISATION
    // ========================================================================
    
    async startGame(levelId, gameConfig) {
        try {
            this.gameConfig = gameConfig;
            this.currentTick = 0;
            this.isPaused = false;
            
            // Initialiser le moteur de jeu
            await apiClient.initializeGame(gameConfig);
            
            // Récupérer l'état initial
            this.gameState = await apiClient.getGameState();
            
            // Initialiser le renderer
            this.renderer.init(
                gameConfig.levelConfig.startingMoney ? 20 : gameConfig.pathsConfig.paths[0]?.points[gameConfig.pathsConfig.paths[0].points.length - 1]?.x + 5 || 20,
                gameConfig.levelConfig.startingLives ? 15 : gameConfig.pathsConfig.paths[0]?.points[gameConfig.pathsConfig.paths[0].points.length - 1]?.y + 5 || 15,
                gameConfig.pathsConfig.paths
            );
            
            // Afficher les tours disponibles
            this.displayAvailableTowers();
            
            // Mettre à jour l'affichage
            this.updateUI();
            this.renderer.render(this.gameState);
            
            // Démarrer la boucle de jeu
            this.isRunning = true;
            this.startGameLoop();
            
            console.log('Game started successfully');
        } catch (error) {
            console.error('Failed to start game:', error);
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
    }
    
    // ========================================================================
    // BOUCLE DE JEU
    // ========================================================================
    
    startGameLoop() {
        if (this.tickInterval) {
            clearInterval(this.tickInterval);
        }
        
        this.tickInterval = setInterval(async () => {
            if (!this.isPaused && this.isRunning) {
                await this.gameTick();
            }
        }, this.tickSpeed);
    }
    
    async gameTick() {
        try {
            // Appeler le tick du moteur
            await apiClient.tick();
            this.currentTick++;
            
            // Récupérer le nouvel état
            this.gameState = await apiClient.getGameState();
            
            // Mettre à jour l'affichage
            this.updateUI();
            this.renderer.render(this.gameState);
            
            // Vérifier si la partie est terminée
            const status = await apiClient.getGameStatus();
            if (status.gameOver) {
                this.onGameOver();
            }
        } catch (error) {
            console.error('Tick error:', error);
            // Si le jeu est terminé, gérer la fin
            if (error.message.includes('game is over')) {
                this.onGameOver();
            }
        }
    }
    
    togglePause() {
        this.isPaused = !this.isPaused;
        const btn = document.getElementById('pause-btn');
        btn.textContent = this.isPaused ? '▶️ Reprendre' : '⏸️ Pause';
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
            const level1 = tower.levels[0];
            return `
                <div class="tower-option ${this.selectedTowerType === tower.id ? 'selected' : ''}" 
                     onclick="gameEngine.selectTowerType('${tower.id}')">
                    <div class="tower-name">🗼 ${tower.name}</div>
                    <div class="tower-stats">
                        💰 ${level1.upgradeCost} | ⚔️ ${level1.damage} | 🎯 ${level1.range.toFixed(1)}
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
            const level1 = tower.levels[0];
            const canAfford = this.gameState.player.currentGold >= level1.upgradeCost;
            
            return `
                <div class="tower-option ${!canAfford ? 'disabled' : ''}" 
                     onclick="gameEngine.buildTower('${tower.id}', ${gridPos.x}, ${gridPos.y})"
                     style="margin-bottom: 10px;">
                    <div class="tower-name">🗼 ${tower.name}</div>
                    <div class="tower-stats">
                        💰 ${level1.upgradeCost} | ⚔️ ${level1.damage} | 🎯 ${level1.range.toFixed(1)}
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
                <p>État: ${tower.state === 'UNDER_CONSTRUCTION' ? '🏗️ En construction' : '✅ Opérationnelle'}</p>
            </div>
        `;
        
        buttonsContainer.innerHTML = '';
        
        // Bouton d'amélioration
        if (tower.state !== 'UNDER_CONSTRUCTION') {
            const upgradeBtn = document.createElement('button');
            upgradeBtn.className = 'btn btn-primary';
            upgradeBtn.style.marginRight = '10px';
            upgradeBtn.textContent = '⬆️ Améliorer';
            upgradeBtn.onclick = () => this.upgradeTower(gridPos.x, gridPos.y);
            buttonsContainer.appendChild(upgradeBtn);
        }
        
        // Bouton de vente
        if (tower.state !== 'UNDER_CONSTRUCTION') {
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