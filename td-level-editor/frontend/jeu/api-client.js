// ============================================================================
// API CLIENT - Gestion des appels aux APIs
// ============================================================================

const API_CONFIG = {
    PROGRESSION: 'http://localhost:8082/api',
    GAME_ENGINE: 'http://localhost:8080'
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
    
    async tick() {
        const response = await fetch(`${API_CONFIG.GAME_ENGINE}/game/tick`, {
            method: 'POST',
            headers: { 'X-Client-Id': this.clientId }
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Tick failed');
        }
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