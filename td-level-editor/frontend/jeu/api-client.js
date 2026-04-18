// ============================================================================
// API CLIENT - Gestion des appels aux APIs
// ============================================================================

// Détermination dynamique des URLs selon l'environnement
const isLocalhost = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1';
const host = window.location.hostname;
const protocol = window.location.protocol;

const API_CONFIG = {
    PROGRESSION: '/api/progression',
    GAME_ENGINE: '/api/engine'
};

class APIClient {
    constructor() {
        this.clientId = crypto.randomUUID();
    }

    getClientId() {
        return this.clientId;
    }

    // ========================================================================
    // PROGRESSION API
    // ========================================================================
    
    async getPlayerProgress() {
        const response = await fetch(`${API_CONFIG.PROGRESSION}/player/progress`);
        return await response.json();
    }
    
    async getAvailableLevels() {
        const response = await fetch(`${API_CONFIG.PROGRESSION}/player/levels`);
        return await response.json();
    }
    
    async prepareLevel(levelId) {
        const response = await fetch(`${API_CONFIG.PROGRESSION}/player/levels/${levelId}/prepare`, {
            method: 'POST'
        });
        return await response.json();
    }
    
    async completeLevel(levelId, stars) {
        const response = await fetch(`${API_CONFIG.PROGRESSION}/player/levels/${levelId}/complete`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stars })
        });
        return await response.json();
    }
    
    async getAllUpgrades() {
        const response = await fetch(`${API_CONFIG.PROGRESSION}/upgrades`);
        return await response.json();
    }
    
    async purchaseUpgrade(upgradeId) {
        const response = await fetch(`${API_CONFIG.PROGRESSION}/upgrades/${upgradeId}/purchase`, {
            method: 'POST'
        });
        return await response.json();
    }
    
    // ========================================================================
    // GAME ENGINE API
    // ========================================================================
    
    async initializeGame(gameConfig) {
        const response = await fetch(`${API_CONFIG.GAME_ENGINE}/game/initialize`, {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                'X-Client-Id': this.clientId
            },
            body: JSON.stringify(gameConfig)
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Initialization failed');
        }
    }
    
    async getGameState() {
        const response = await fetch(`${API_CONFIG.GAME_ENGINE}/game/state`, {
            headers: { 'X-Client-Id': this.clientId }
        });
        return await response.json();
    }
    

    
    async setGameSpeed(speedMultiplier) {
        const response = await fetch(`${API_CONFIG.GAME_ENGINE}/game/commands/speed`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Client-Id': this.clientId
            },
            body: JSON.stringify({ speedMultiplier })
        });
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to set speed');
        }
    }

    async pauseGame() {
        return this.setGameSpeed(0.0);
    }

    async resumeGame() {
        return this.setGameSpeed(1.0);
    }

    async placeTower(playerId, towerType, x, y) {
        const response = await fetch(`${API_CONFIG.GAME_ENGINE}/game/commands/place-tower`, {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                'X-Client-Id': this.clientId
            },
            body: JSON.stringify({ playerId, towerType, x, y })
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to place tower');
        }
    }
    
    async upgradeTower(playerId, x, y) {
        const response = await fetch(`${API_CONFIG.GAME_ENGINE}/game/commands/upgrade-tower`, {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                'X-Client-Id': this.clientId
            },
            body: JSON.stringify({ 
                playerId, 
                towerXPosition: x, 
                towerYPosition: y 
            })
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to upgrade tower');
        }
    }
    
    async sellTower(playerId, x, y) {
        const response = await fetch(`${API_CONFIG.GAME_ENGINE}/game/commands/sell-tower`, {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                'X-Client-Id': this.clientId
            },
            body: JSON.stringify({ 
                playerId, 
                towerXPosition: x, 
                towerYPosition: y 
            })
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Failed to sell tower');
        }
    }
    
    async getGameStatus() {
        const response = await fetch(`${API_CONFIG.GAME_ENGINE}/game/status`, {
            headers: { 'X-Client-Id': this.clientId }
        });
        return await response.json();
    }
}

// Instance globale
const apiClient = new APIClient();