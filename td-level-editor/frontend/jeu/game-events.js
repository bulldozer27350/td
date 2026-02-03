class GameEventsHandler {
    constructor() {
        this.eventSource = null;
        this.listeners = {};
        this.reconnectAttempts = 0;
        this.maxReconnectAttempts = 10;
        this.reconnectDelay = 1000;
        this.isConnected = false; 
    }
    
    connect() {
        // Si déjà connecté, ne rien faire
        if (this.isConnected && this.eventSource) {
            console.log('⚠️ SSE already connected, skipping');
            return;
        }
        
        this._createConnection();
    }
    
    _createConnection() {
        try {
            // Fermer l'ancienne connexion si elle existe
            if (this.eventSource) {
                console.log('🔌 Closing old SSE connection');
                this.eventSource.close();
                this.eventSource = null;
            }
            
            console.log('🔌 Creating new SSE connection');
            const clientId = crypto.randomUUID(); // UUID, playerId, etc.
            this.eventSource = new EventSource(`http://localhost:8080/game/events?clientId=${clientId}`);
            
            // Événements du jeu

            // Gestion des tours
            this.eventSource.addEventListener('tower-placed', (event) => {
                const data = JSON.parse(event.data);
                this.emit('tower-placed', data);
            });
            
            this.eventSource.addEventListener('tower-shot', (event) => {
                const data = JSON.parse(event.data);
                this.emit('tower-shot', data);
            });
            
            this.eventSource.addEventListener('tower-upgraded', (event) => {
                const data = JSON.parse(event.data);
                this.emit('tower-upgraded', data);
            });
            
            this.eventSource.addEventListener('tower-sold', (event) => {
                const data = JSON.parse(event.data);
                this.emit('tower-sold', data);
            });

            // Gestion des ennemis
            
            this.eventSource.addEventListener('enemy-moved', (event) => {
                const data = JSON.parse(event.data);
                this.emit('enemy-moved', data);
            });

            this.eventSource.addEventListener('enemy-hit', (event) => {
                const data = JSON.parse(event.data);
                this.emit('enemy-hit', data);
            });
            
            this.eventSource.addEventListener('enemy-killed', (event) => {
                const data = JSON.parse(event.data);
                this.emit('enemy-killed', data);
            });

            // Gestion des évènements de jeu
            
            this.eventSource.addEventListener('state-update', (event) => {
                const data = JSON.parse(event.data);
                this.emit('state-update', data);
            });
            
            this.eventSource.addEventListener('game-won', (event) => {
                const data = JSON.parse(event.data);
                this.emit('game-won', data);
            });
            
            this.eventSource.addEventListener('game-lost', (event) => {
                const data = JSON.parse(event.data);
                this.emit('game-lost', data);
            });
            
            // Gestion de la connexion réussie
            this.eventSource.onopen = () => {
                console.log('✅ SSE connected');
                this.isConnected = true;
                this.reconnectAttempts = 0;
                this.reconnectDelay = 1000;
            };
            
            // Gestion des erreurs avec reconnexion automatique
            this.eventSource.onerror = (error) => {
                console.error('❌ SSE error:', error);
                this.isConnected = false;
                
                // Fermer la connexion actuelle
                if (this.eventSource) {
                    this.eventSource.close();
                    this.eventSource = null;
                }
                
                // Tenter une reconnexion
                this._attemptReconnect();
            };

            this.eventSource.addEventListener('ping', () => {
                // rien à faire, juste maintenir la connexion
            });
            
        } catch (error) {
            console.error('Failed to create SSE connection:', error);
            this._attemptReconnect();
        }
    }
    
    _attemptReconnect() {
        if (this.reconnectAttempts >= this.maxReconnectAttempts) {
            console.error('🚫 Max reconnection attempts reached');
            this.emit('connection-failed');
            return;
        }
        
        this.reconnectAttempts++;
        const delay = Math.min(this.reconnectDelay * Math.pow(2, this.reconnectAttempts - 1), 30000);
        
        console.log(`🔄 Reconnecting in ${delay}ms (attempt ${this.reconnectAttempts}/${this.maxReconnectAttempts})`);
        
        setTimeout(() => {
            this._createConnection();
        }, delay);
    }
    
    on(eventType, callback) {
        if (!this.listeners[eventType]) {
            this.listeners[eventType] = [];
        }
        this.listeners[eventType].push(callback);
    }
    
    emit(eventType, data) {
        if (this.listeners[eventType]) {
            this.listeners[eventType].forEach(callback => callback(data));
        }
    }
    
    removeAllListeners() {
        console.log('🧹 Clearing all listeners');
        this.listeners = {};
    }
    
    disconnect() {
        // NE PAS fermer la connexion SSE ! Juste vider les listeners
        console.log('⚠️ disconnect() called - clearing listeners only (keeping SSE open)');
        this.forceDisconnect();
    }
    
    forceDisconnect() {
        console.log('🔌 Force disconnect - closing SSE connection');
        this.isConnected = false;
        this.reconnectAttempts = 0;
        
        if (this.eventSource) {
            this.eventSource.close();
            this.eventSource = null;
        }
        
        this.removeAllListeners();
    }
}

const gameEvents = new GameEventsHandler();