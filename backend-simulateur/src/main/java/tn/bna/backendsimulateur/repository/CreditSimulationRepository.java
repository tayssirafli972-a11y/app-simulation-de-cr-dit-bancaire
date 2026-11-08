package tn.bna.backendsimulateur.repository;

import tn.bna.backendsimulateur.entity.CreditSimulation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditSimulationRepository extends JpaRepository<CreditSimulation, Long> {
}