package com.towerdefense.progression.http.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.towerdefense.progression.domain.upgrade.TowerUpgrade;
import com.towerdefense.progression.http.api.UpgradesApi;
import com.towerdefense.progression.http.config.ReloadableBeansManager;
import com.towerdefense.progression.http.model.PurchaseUpgradeResponse;
import com.towerdefense.progression.http.model.UpgradeEffect;
import com.towerdefense.progression.http.model.UpgradeInfo;

@RestController
public class UpgradesController implements UpgradesApi {

    @Autowired
    private ReloadableBeansManager beansManager;
    
	@Override
	public ResponseEntity<List<UpgradeInfo>> getAllUpgrades() {
		List<TowerUpgrade> upgrades = this.beansManager.getMetaGameService().getAllUpgrades();
		Set<String> unlockedIds = this.beansManager.getMetaGameService().getPlayerProgress().getUnlockedUpgrades();

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
		upgradeInfo.setTowerRank(towerUpgrade.getTowerRank());
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
		var unlockedIds = this.beansManager.getMetaGameService().getPlayerProgress().getUnlockedUpgrades();
		
		var dtos = this.beansManager.getMetaGameService().getAllUpgrades().stream().filter(u -> u.getTowerTypeId().equals(towerTypeId))
				.map(u->toUpgradeInfo(u, unlockedIds.contains(u.getId())))
				.toList();


		return ResponseEntity.ok(dtos);
	}

	@Override
	public ResponseEntity<PurchaseUpgradeResponse> purchaseUpgrade(@PathVariable("upgradeId") String upgradeId) {
		boolean success = this.beansManager.getMetaGameService().purchaseUpgrade(upgradeId);
        int remaining = this.beansManager.getMetaGameService().getPlayerProgress().getUpgradePoints();
        
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
