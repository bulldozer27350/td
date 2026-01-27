package com.towerdefense.progression.http.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.towerdefense.progression.domain.upgrade.TowerUpgrade;
import com.towerdefense.progression.http.model.UpgradeEffect;
import com.towerdefense.progression.http.api.UpgradesApi;
import com.towerdefense.progression.http.model.PurchaseUpgradeResponse;
import com.towerdefense.progression.http.model.UpgradeInfo;
import com.towerdefense.progression.service.MetaGameService;

@RestController
public class UpgradesController implements UpgradesApi {

	private final MetaGameService metaGameService;

	public UpgradesController(MetaGameService metaGameService) {
		this.metaGameService = metaGameService;
	}

	@Override
	public ResponseEntity<List<UpgradeInfo>> getAllUpgrades() {
		List<TowerUpgrade> upgrades = metaGameService.getAllUpgrades();
		Set<String> unlockedIds = metaGameService.getPlayerProgress().getUnlockedUpgrades();

		List<UpgradeInfo> dtos = upgrades.stream()
				.map(up -> toUpgradeInfo(up, unlockedIds.contains(up.getId())))
				.toList();
		return ResponseEntity.ok(dtos);
	}
	
	UpgradeInfo toUpgradeInfo(TowerUpgrade towerUpgrade, boolean unlocked) {
		UpgradeInfo upgradeInfo = new UpgradeInfo();
		upgradeInfo.setCost(towerUpgrade.getCost());
		upgradeInfo.setId(towerUpgrade.getId());
		upgradeInfo.setName(towerUpgrade.getName());
		upgradeInfo.setTowerLevel(towerUpgrade.getTowerLevel());
		upgradeInfo.setTowerTypeId(towerUpgrade.getTowerTypeId());
		upgradeInfo.setUnlocked(unlocked);
		upgradeInfo.setEffect(toUpgradeEffect(towerUpgrade.getEffect()));
		return upgradeInfo;
	}

	private UpgradeEffect toUpgradeEffect(com.towerdefense.progression.domain.upgrade.UpgradeEffect effect) {
	    UpgradeEffect upEffect = new UpgradeEffect();
	    upEffect.setStat(effect.stat());
	    upEffect.setModifier(effect.modifier());
	    switch (effect.type()) {
            case ADD -> upEffect.setType(UpgradeEffect.TypeEnum.ADD);
            case MULTIPLY -> upEffect.setType(UpgradeEffect.TypeEnum.MULTIPLY);
            default -> upEffect.setType(null);
            }
	    return upEffect;
    }

    @Override
	public ResponseEntity<List<UpgradeInfo>> getUpgradesForTower(@PathVariable("towerTypeId") String towerTypeId) {
		var unlockedIds = metaGameService.getPlayerProgress().getUnlockedUpgrades();
		
		var dtos = metaGameService.getAllUpgrades().stream().filter(u -> u.getTowerTypeId().equals(towerTypeId))
				.map(u->toUpgradeInfo(u, unlockedIds.contains(u.getId())))
				.toList();


		return ResponseEntity.ok(dtos);
	}

	@Override
	public ResponseEntity<PurchaseUpgradeResponse> purchaseUpgrade(@PathVariable("upgradeId") String upgradeId) {
		boolean success = metaGameService.purchaseUpgrade(upgradeId);
        int remaining = metaGameService.getPlayerProgress().getUpgradePoints();
        
        if (!success) {
            PurchaseUpgradeResponse purchaseUpgradeResponse = new PurchaseUpgradeResponse();
            purchaseUpgradeResponse.setUpgradeId(upgradeId); 
            purchaseUpgradeResponse.setSuccess(false); 
            purchaseUpgradeResponse.setRemainingPoints(remaining);
			return ResponseEntity.badRequest()
                .body(purchaseUpgradeResponse);
        }
        PurchaseUpgradeResponse purchaseUpgradeResponse = new PurchaseUpgradeResponse();
        purchaseUpgradeResponse.setUpgradeId(upgradeId); 
        purchaseUpgradeResponse.setSuccess(true); 
        purchaseUpgradeResponse.setRemainingPoints(remaining);
        
        return ResponseEntity.ok(purchaseUpgradeResponse);
	}

}
