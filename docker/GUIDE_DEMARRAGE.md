# ============================================================================
# Guide de démarrage — Déploiement Docker Tower Defense
# ============================================================================
#
# Ce guide te permet de mettre en place le CD GitHub Actions et de déployer
# localement avec Docker. Lis-le attentivement une fois, ensuite tu n'en
# auras plus besoin pour les déploiements du quotidien.
# ============================================================================

## Résumé de l'architecture générée

```
d:\Depots\tower_defense\
│
├── docker/
│   ├── Dockerfile.engine      ← Image pour td-engine-http (port 8080)
│   ├── Dockerfile.progression ← Image pour td-progression-http (port 8082)
│   ├── Dockerfile.editor      ← Image pour td-level-editor-http (port 8081)
│   ├── Dockerfile.nginx       ← Image Nginx (reverse proxy + frontend)
│   └── nginx.conf             ← Configuration du reverse proxy
│
├── .github/workflows/
│   ├── ci.yml                 ← CI existant (compile et teste)
│   └── cd.yml                 ← CD nouveau (construit et publie les images Docker)
│
├── docker-compose.yml         ← Orchestre tous les services (développement)
├── docker-compose.prod.yml    ← Surcharge production (utilise les images GHCR)
├── .env.example               ← Modèle de configuration (à copier en .env)
└── .gitignore                 ← Mis à jour (.env exclu)
```

---

## ÉTAPE 1 — Prérequis : Activer les permissions GitHub Actions

> ⚠️ Cette étape est obligatoire, sinon le pipeline échouera avec une erreur 
> "permission denied to write packages".

1. Va sur **GitHub → ton dépôt → Settings**
2. Dans le menu gauche : **Actions → General**
3. Descends jusqu'à **"Workflow permissions"**
4. Sélectionne **"Read and write permissions"**
5. Clique **Save**

---

## ÉTAPE 2 — Rendre les packages publics (optionnel mais recommandé)

Par défaut, les images GHCR sont **privées**. Pour les rendre publiques (et 
pouvoir les télécharger sans authentification depuis n'importe où) :

1. Va sur **GitHub → ton profil → Packages**
2. Clique sur l'image (ex: `td-engine`) après le premier build
3. **Package settings → Change visibility → Public**

Sinon, pour rester en privé, il faudra se connecter avant chaque `docker pull` :
```bash
docker login ghcr.io -u bulldozer27350 --password-stdin
# → coller un Personal Access Token (PAT) avec scope "read:packages"
```

---

## ÉTAPE 3 — Créer le fichier .env local

```powershell
# Dans le dossier du projet
Copy-Item .env.example .env
```

Ouvre `.env` et ajuste si nécessaire. Pour débuter, les valeurs par défaut suffisent.

---

## ÉTAPE 4 — Premier push pour déclencher le CD

```powershell
cd d:\Depots\tower_defense

# Ajouter tous les nouveaux fichiers
git add docker/ .github/workflows/cd.yml docker-compose.yml docker-compose.prod.yml .env.example .gitignore

# Commiter
git commit -m "feat: ajout Docker + pipeline CD GitHub Actions"

# Pousser sur main
git push origin main
```

GitHub Actions va automatiquement :
1. Détecter le push sur `main`
2. Lancer le workflow `cd.yml`
3. Construire les 4 images Docker
4. Les publier sur `ghcr.io/TON_ORG/TON_REPO/td-engine:latest` etc.

Tu peux suivre l'avancement dans **GitHub → ton dépôt → Actions**.

**Durée estimée du premier build** : 8 à 15 minutes (Maven télécharge les dépendances).  
**Durée des builds suivants** : 2 à 4 minutes (cache Docker activé).

---

## ÉTAPE 5 — Démarrer localement avec Docker

```powershell
cd d:\Depots\tower_defense

# Démarrer tous les services (construit les images localement)
docker-compose up --build -d

# Vérifier que tout tourne
docker-compose ps

# Voir les logs en direct
docker-compose logs -f

# Accéder au jeu
start http://localhost/jeu/
```

---

## ÉTAPE 6 — Déployer sur un serveur (ex: chez toi derrière No-IP)

Sur le serveur (ou ta machine si tu utilises ton PC comme serveur) :

```bash
# 1. Installer Docker (si pas déjà fait)
# Sur Ubuntu/Debian :
curl -fsSL https://get.docker.com | sh

# 2. Cloner le projet
git clone https://github.com/bulldozer27350/td.git
cd td

# 3. Créer le fichier .env
cp .env.example .env
nano .env   # Modifier CORS_ALLOWED_ORIGINS avec ton domaine No-IP

# 4. Se connecter à GHCR (si les images sont privées)
docker login ghcr.io -u bulldozer27350

# 5. Démarrer avec les images GHCR (pas de build necesssaire !)
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d

# 6. Mettre à jour après un nouveau push
docker-compose pull           # Télécharge les nouvelles images
docker-compose up -d          # Redémarre avec les nouvelles images
```

---

## Cycle de développement au quotidien

```
Tu modifies du code
        ↓
git push origin main
        ↓
GitHub Actions lance le CD automatiquement
(tu peux continuer à coder pendant ce temps)
        ↓
Les images sont publiées sur GHCR (~3 min)
        ↓
Sur le serveur : docker-compose pull && docker-compose up -d
        ↓
Nouvelle version en ligne !
```

---

## Commandes utiles au quotidien

```powershell
# ── Démarrage / Arrêt ──────────────────────────────────────────────────────
docker-compose up -d                    # Démarrer en arrière-plan
docker-compose down                     # Arrêter et supprimer les conteneurs
docker-compose restart td-engine        # Redémarrer un seul service

# ── Logs ───────────────────────────────────────────────────────────────────
docker-compose logs -f                  # Tous les logs en direct
docker-compose logs -f td-engine        # Logs d'un service spécifique
docker-compose logs --tail=50 td-engine # 50 dernières lignes

# ── Statut ─────────────────────────────────────────────────────────────────
docker-compose ps                       # Liste des conteneurs et leur état
docker stats                            # Utilisation CPU/RAM en temps réel

# ── Mise à jour (production) ───────────────────────────────────────────────
docker-compose pull                     # Télécharge les nouvelles images
docker-compose up -d                    # Recrée les conteneurs avec les nouvelles images

# ── Nettoyage ──────────────────────────────────────────────────────────────
docker system prune -f                  # Supprime les images/conteneurs inutilisés
docker volume ls                        # Liste les volumes
```

---

## Comprendre les tags d'images GHCR

Après chaque push sur `main`, GitHub Actions crée automatiquement 2 tags :

| Tag | Exemple | Utilisation |
|-----|---------|-------------|
| `latest` | `td-engine:latest` | Toujours la dernière version |
| `sha-abc1234` | `td-engine:sha-a3f8d21` | Version précise (rollback possible) |

**Rollback vers une version précédente** (si un bug est introduit) :
```bash
# Voir les versions disponibles sur GHCR
# GitHub → ton profil → Packages → td-engine

# Déployer une version précédente
docker pull ghcr.io/TON_ORG/td-engine:sha-abc1234
# Modifier docker-compose.prod.yml pour pointer vers ce SHA, puis :
docker-compose up -d
```

---

## Structure des images publiées sur GHCR

```
ghcr.io/bulldozer27350/td/
├── td-engine:latest           ← Moteur de jeu
├── td-engine:sha-abc1234
├── td-progression:latest      ← Service de progression
├── td-progression:sha-abc1234
├── td-level-editor:latest     ← Éditeur de niveaux
├── td-level-editor:sha-abc1234
└── td-nginx:latest            ← Frontend + Reverse Proxy
    td-nginx:sha-abc1234
```

C'est exactement l'équivalent d'un dépôt Maven :
- `ghcr.io` = l'URL du "repository" (comme `repo.maven.apache.org`)
- `TON_ORG/TON_REPO` = le `groupId`
- `td-engine` = l'`artifactId`
- `latest` / `sha-abc1234` = la `version`
