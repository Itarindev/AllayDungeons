package allayplugins.dev.itarin.dao.phase;

import allayplugins.dev.itarin.model.phase.Phase;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PhaseDAO {

    @Getter
    private static final List<Phase> phases = new ArrayList<>();

    public static Phase findPhaseById(String phaseId) {
        return phases.stream().filter(phase -> phase.getId().equals(phaseId)).findFirst().orElse(null);
    }

    public static Phase getPhaseByMob(UUID uuid) {
        return phases.stream()
                .filter(p -> p.getBossUuid() != null && p.getBossUuid().equals(uuid)
                        || p.getLivingMobs().contains(uuid))
                .findFirst()
                .orElse(null);
    }
}