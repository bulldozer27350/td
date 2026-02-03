// ============================================================================
// GAME RENDERER - Affichage du jeu sur le canvas
// ============================================================================

class GameRenderer {
    constructor(canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getContext('2d');
        this.cellSize = 40;
        this.gridWidth = 0;
        this.gridHeight = 0;
        this.paths = [];
        this.hoveredCell = null;
        this.shotLines = [];
        // Gestion du zoom et du pan
        this.zoom = 1.0;
        this.minZoom = 1.0;
        this.maxZoom = 4.0;
        this.panX = 0;
        this.panY = 0;
        this.isDragging = false;
        this.dragStartX = 0;
        this.dragStartY = 0;
        this.lastMouseX = 0;
        this.lastMouseY = 0;
        
        // Icônes pour les entités
        this.icons = {
            tower: {
                default: '🗼',
                archer: '🏹',
                cannon: '💣',
                laser: '⚡',
                construction: '🏗️'
            },
            enemy: {
                default: '👾',
                goblin: '👺',
                orc: '👹',
                dragon: '🐉'
            },
            projectile: '💥'
        };
        
        // Configuration des couleurs
        this.colors = {
            grid: '#2a2a3e',
            path: '#4a4a6a',
            hover: 'rgba(255, 255, 255, 0.2)',
            pathHighlight: 'rgba(255, 200, 0, 0.3)',
            towerRange: 'rgba(100, 150, 255, 0.2)',
            selected: 'rgba(255, 100, 100, 0.3)'
        };
    }
    
    // ========================================================================
    // INITIALISATION
    // ========================================================================
    
    init(mapWidth, mapHeight, paths) {
        this.gridWidth = mapWidth;
        this.gridHeight = mapHeight;
        this.paths = paths;
        
        // Ajuster la taille du canvas
        const maxWidth = Math.min(window.innerWidth - 400, 1200);
        const maxHeight = window.innerHeight - 200;
        
        const cellSizeByWidth = maxWidth / mapWidth;
        const cellSizeByHeight = maxHeight / mapHeight;
        this.cellSize = Math.min(cellSizeByWidth, cellSizeByHeight, 50);

        // Sauvegarder les dimensions initiales pour le zoom
        this.initialCellSize = this.cellSize;
        this.canvasWidth = this.gridWidth * this.cellSize;
        this.canvasHeight = this.gridHeight * this.cellSize;

        this.canvas.width = this.canvasWidth;
        this.canvas.height = this.canvasHeight;
        
        this.canvas.width = this.gridWidth * this.cellSize;
        this.canvas.height = this.gridHeight * this.cellSize;
    }

    applyTransform() {
        this.ctx.setTransform(1, 0, 0, 1, 0, 0); // Reset
        this.ctx.translate(this.panX, this.panY);
        this.ctx.scale(this.zoom, this.zoom);
    }

    resetTransform() {
        this.ctx.setTransform(1, 0, 0, 1, 0, 0);
    }
    
    // ========================================================================
    // RENDU PRINCIPAL
    // ========================================================================
    
    render(gameState) {
        console.log(`🎨 Render called - Enemies alive: ${gameState.enemies.filter(e => e.isAlive).length}`);

        // Effacer le canvas AVANT la transformation
        this.resetTransform();
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        
        // Appliquer le zoom et le pan
        this.applyTransform();
        
        // Dessiner la grille
        this.drawGrid();
        
        // Dessiner la grille
        this.drawGrid();
        
        // Dessiner les chemins
        this.drawPaths();
        
        // Dessiner les tours
        this.drawTowers(gameState.towers);
        
        // Dessiner les ennemis
        this.drawEnemies(gameState.enemies);
        
        // Dessiner les traits de tir actifs
        this.drawShotLines();

        // Dessiner la cellule survolée
        if (this.hoveredCell) {
            this.highlightCell(this.hoveredCell.x, this.hoveredCell.y, this.colors.hover);
        }
    }
    
    // ========================================================================
    // GRILLE ET CHEMINS
    // ========================================================================
    
    drawGrid() {
        this.ctx.strokeStyle = this.colors.grid;
        this.ctx.lineWidth = 1;
        
        // Lignes verticales
        for (let x = 0; x <= this.gridWidth; x++) {
            this.ctx.beginPath();
            this.ctx.moveTo(x * this.cellSize, 0);
            this.ctx.lineTo(x * this.cellSize, this.canvas.height);
            this.ctx.stroke();
        }
        
        // Lignes horizontales
        for (let y = 0; y <= this.gridHeight; y++) {
            this.ctx.beginPath();
            this.ctx.moveTo(0, y * this.cellSize);
            this.ctx.lineTo(this.canvas.width, y * this.cellSize);
            this.ctx.stroke();
        }
    }
    
    drawPaths() {
        this.paths.forEach(path => {
            this.ctx.fillStyle = this.colors.path;
            
            for (let i = 0; i < path.points.length - 1; i++) {
                const start = path.points[i];
                const end = path.points[i + 1];

                const dx = Math.sign(end.x - start.x);
                const dy = Math.sign(end.y - start.y);

                let x = start.x;
                let y = start.y;

                // Inclure le point de départ
                this.ctx.fillRect(
                    x * this.cellSize,
                    y * this.cellSize,
                    this.cellSize,
                    this.cellSize
                );

                // Avancer jusqu'au point suivant
                while (x !== end.x || y !== end.y) {
                    x += dx;
                    y += dy;

                    this.ctx.fillRect(
                        x * this.cellSize,
                        y * this.cellSize,
                        this.cellSize,
                        this.cellSize
                    );
                }
            }
            
            // Marquer entrée et sortie
            if (path.points.length > 0) {
                const start = path.points[0];
                const end = path.points[path.points.length - 1];
                
                this.drawIcon('🟢', start.x, start.y, this.cellSize * 0.4);
                this.drawIcon('🔴', end.x, end.y, this.cellSize * 0.4);
            }
        });
    }
    
    // ========================================================================
    // ENTITÉS
    // ========================================================================
    
    drawTowers(towers) {
        towers.forEach(tower => {
            const gridX = Math.floor(tower.position.x);
            const gridY = Math.floor(tower.position.y);
            
            // Icône de la tour
            let icon = this.icons.tower.default;
            if (tower.state === 'BUILDING') {
                icon = this.icons.tower.construction;
            } else if (this.icons.tower[tower.towerType]) {
                icon = this.icons.tower[tower.towerType];
            }
            
            this.drawIcon(icon, gridX, gridY, this.cellSize * 0.6);
            
            // Indicateur de rang (si disponible)
            if (tower.rank) {
                this.ctx.fillStyle = 'white';
                this.ctx.font = `bold ${this.cellSize * 0.25}px Arial`;
                this.ctx.textAlign = 'right';
                this.ctx.textBaseline = 'top';
                this.ctx.fillText(
                    `L${tower.rank}`,
                    (gridX + 1) * this.cellSize - 2,
                    gridY * this.cellSize + 2
                );
            }
        });
    }
    
    drawEnemies(enemies) {
        enemies.forEach(enemy => {
            this.drawEnemy(enemy);
        });
    }

    drawEnemy(enemy) {
        if (!enemy.isAlive) return;
        
        const x = enemy.position.x * this.cellSize + this.cellSize / 2;
        const y = enemy.position.y * this.cellSize + this.cellSize / 2;
        
        // Réinitialiser le style avant de dessiner l'ennemi
        this.ctx.globalAlpha = 1.0;
        this.ctx.fillStyle = '#ffffff';

        // Icône de l'ennemi
        let icon = this.icons.enemy.default;
        // Vous pouvez mapper les types d'ennemis ici
        
        this.ctx.font = `${this.cellSize * 0.5}px Arial`;
        this.ctx.textAlign = 'center';
        this.ctx.textBaseline = 'middle';
        this.ctx.fillText(icon, x, y);
        
        // Barre de vie
        const healthBarWidth = this.cellSize * 0.6;
        const healthBarHeight = 4;
        const healthPercent = enemy.currentHp / enemy.maxHp;
        
        this.ctx.fillStyle = 'rgba(0, 0, 0, 0.5)';
        this.ctx.fillRect(
            x - healthBarWidth / 2,
            y - this.cellSize * 0.4,
            healthBarWidth,
            healthBarHeight
        );
        
        this.ctx.fillStyle = healthPercent > 0.5 ? '#4ade80' : healthPercent > 0.25 ? '#fbbf24' : '#ef4444';
        this.ctx.fillRect(
            x - healthBarWidth / 2,
            y - this.cellSize * 0.4,
            healthBarWidth * healthPercent,
            healthBarHeight
        );
    }

    drawShotLines() {
        const now = Date.now();
        
        // Sauvegarder l'état du contexte
        this.ctx.save();
        
        // Dessiner les traits actifs et supprimer les expirés
        this.shotLines = this.shotLines.filter(shot => {
            const age = now - shot.timestamp;
            if (!shot.enemy) {
                return false;
            }
            if (age > shot.duration) {
                console.log(`💨 ${now} SHOT VISUAL REMOVED (age: ${age}ms)`);
                return false; // Supprimer ce trait
            }
            
            // Calculer l'opacité en fonction de l'âge
            const opacity = 1 - (age / shot.duration);
            
            // Trait ultra-fin pour simuler une trainée de balle
            this.ctx.strokeStyle = `rgba(255, 230, 100, ${opacity * 0.8})`;
            this.ctx.lineWidth = 0.5; // Ultra fin
            this.ctx.lineCap = 'round';

            this.ctx.beginPath();
            this.ctx.moveTo(shot.fromX, shot.fromY);
            this.ctx.lineTo(shot.toX, shot.toY);
            this.ctx.stroke();
            
            return true; // Garder ce trait
        });
        // Restaurer l'état du contexte
        this.ctx.restore();
    }
    
    addShotLine(shotData) {
        this.shotLines.push({
            enemy: shotData.enemy,
            fromX: shotData.towerPosition.x * this.cellSize + this.cellSize / 2,
            fromY: shotData.towerPosition.y * this.cellSize + this.cellSize / 2,
            toX: shotData.targetPosition.x * this.cellSize + this.cellSize / 2,
            toY: shotData.targetPosition.y * this.cellSize + this.cellSize / 2,
            timestamp: Date.now(),
            duration: 5
        });

        console.log(`✨ ${Date.now()} SHOT VISUAL ADDED (will disappear at ${Date.now() + 50})`);
        
        // Redessiner immédiatement
        this.render(gameEngine.gameState);
    }

    drawProjectiles(projectiles) {
        projectiles.forEach(projectile => {
            const x = projectile.position.x * this.cellSize;
            const y = projectile.position.y * this.cellSize;
            
            // Dessiner un petit cercle pour le projectile
            this.ctx.fillStyle = '#fbbf24';
            this.ctx.beginPath();
            this.ctx.arc(x, y, 4, 0, Math.PI * 2);
            this.ctx.fill();
        });
    }

    // ========================================================================
    // ZOOM ET PAN
    // ========================================================================

    handleWheel(event) {
        event.preventDefault();
        
        const rect = this.canvas.getBoundingClientRect();
        const mouseX = event.clientX - rect.left;
        const mouseY = event.clientY - rect.top;
        
        // Position de la souris dans le monde du jeu (avant zoom)
        const worldX = (mouseX - this.panX) / this.zoom;
        const worldY = (mouseY - this.panY) / this.zoom;
        
        // Calculer le nouveau zoom
        const zoomDelta = event.deltaY > 0 ? 0.9 : 1.1;
        const newZoom = Math.max(this.minZoom, Math.min(this.maxZoom, this.zoom * zoomDelta));
        
        // Ajuster le pan pour que le zoom soit centré sur la souris
        this.panX = mouseX - worldX * newZoom;
        this.panY = mouseY - worldY * newZoom;
        
        this.zoom = newZoom;
        this.cellSize = this.initialCellSize * this.zoom;
        
        // Redessiner
        if (gameEngine.gameState) {
            this.render(gameEngine.gameState);
        }
    }

    handleMouseDown(event) {
        // Clic molette ou clic droit pour drag
        if (event.button === 1 || event.button === 2) {
            event.preventDefault();
            this.isDragging = true;
            this.dragStartX = event.clientX;
            this.dragStartY = event.clientY;
            this.canvas.style.cursor = 'grabbing';
        }
    }

    handleMouseMove(event) {
        this.lastMouseX = event.clientX;
        this.lastMouseY = event.clientY;
        
        if (this.isDragging) {
            const dx = event.clientX - this.dragStartX;
            const dy = event.clientY - this.dragStartY;
            
            this.panX += dx;
            this.panY += dy;
            
            this.dragStartX = event.clientX;
            this.dragStartY = event.clientY;
            
            // Redessiner
            if (gameEngine.gameState) {
                this.render(gameEngine.gameState);
            }
        }
    }

    handleMouseUp(event) {
        if (event.button === 1 || event.button === 2) {
            this.isDragging = false;
            this.canvas.style.cursor = 'default';
        }
    }

    resetView() {
        this.zoom = 1.0;
        this.panX = 0;
        this.panY = 0;
        this.cellSize = this.initialCellSize;
        if (gameEngine.gameState) {
            this.render(gameEngine.gameState);
        }
    }
    
    // ========================================================================
    // UTILITAIRES
    // ========================================================================
    
    drawIcon(icon, gridX, gridY, size) {
        this.ctx.font = `${size}px Arial`;
        this.ctx.textAlign = 'center';
        this.ctx.textBaseline = 'middle';
        this.ctx.fillText(
            icon,
            gridX * this.cellSize + this.cellSize / 2,
            gridY * this.cellSize + this.cellSize / 2
        );
    }
    
    highlightCell(x, y, color) {
        this.ctx.fillStyle = color;
        this.ctx.fillRect(
            x * this.cellSize,
            y * this.cellSize,
            this.cellSize,
            this.cellSize
        );
    }
    
    drawTowerRange(gridX, gridY, range) {
        const centerX = (gridX + 0.5) * this.cellSize;
        const centerY = (gridY + 0.5) * this.cellSize;
        const radius = range * this.cellSize;
        
        this.ctx.fillStyle = this.colors.towerRange;
        this.ctx.beginPath();
        this.ctx.arc(centerX, centerY, radius, 0, Math.PI * 2);
        this.ctx.fill();
        
        this.ctx.strokeStyle = 'rgba(100, 150, 255, 0.5)';
        this.ctx.lineWidth = 2;
        this.ctx.stroke();
    }
    
    // ========================================================================
    // CONVERSION COORDONNÉES
    // ========================================================================
    
    screenToGrid(screenX, screenY) {
        const rect = this.canvas.getBoundingClientRect();
        const canvasX = screenX - rect.left;
        const canvasY = screenY - rect.top;
        
        // Transformer les coordonnées écran en coordonnées monde
        const worldX = (canvasX - this.panX) / this.zoom;
        const worldY = (canvasY - this.panY) / this.zoom;
        
        return {
            x: Math.floor(worldX / this.initialCellSize),
            y: Math.floor(worldY / this.initialCellSize)
        };
    }
    
    isOnPath(x, y) {
        return this.paths.some(path => 
            path.points.some(point => point.x === x && point.y === y)
        );
    }
    
    setHoveredCell(x, y) {
        if (x >= 0 && x < this.gridWidth && y >= 0 && y < this.gridHeight) {
            this.hoveredCell = { x, y };
        } else {
            this.hoveredCell = null;
        }
    }
    
    clearHover() {
        this.hoveredCell = null;
    }
}