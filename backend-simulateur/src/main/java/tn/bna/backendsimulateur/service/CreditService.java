package tn.bna.backendsimulateur.service;

import tn.bna.backendsimulateur.dto.AmortissementDto;
import tn.bna.backendsimulateur.dto.CreditSimulationRequest;
import tn.bna.backendsimulateur.dto.LigneAmortissementDto;
import tn.bna.backendsimulateur.entity.CreditSimulation;
import tn.bna.backendsimulateur.repository.CreditSimulationRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreditService {

    private final CreditSimulationRepository repository;

    public CreditService(CreditSimulationRepository repository) {
        this.repository = repository;
    }

    public AmortissementDto calculerAmortissement(CreditSimulationRequest request) {

        Double montant = request.getMontant();
        Integer dureeMois = request.getDureeMois();
        Double tauxAnnuel = request.getTauxAnnuel();
        Double tauxMensuel = tauxAnnuel / 100 / 12;

        
        System.out.println("=== SIMULATION RECUE ===");
        System.out.println("Montant: " + montant);
        System.out.println("Duree: " + dureeMois);
        System.out.println("Taux annuel: " + tauxAnnuel);
        System.out.println("Taux mensuel: " + tauxMensuel);

        double mensualite;

        if (tauxMensuel == 0) {
            mensualite = montant / dureeMois;
        } else {
            double facteur = Math.pow(1 + tauxMensuel, dureeMois);
            mensualite = montant * (tauxMensuel * facteur) / (facteur - 1);
        }

        mensualite = Math.round(mensualite * 100.0) / 100.0;

        List<LigneAmortissementDto> lignes = genererTableauAmortissement(montant, tauxMensuel, dureeMois, mensualite);

        double coutTotal = mensualite * dureeMois;
        double interetsTotaux = coutTotal - montant;

        coutTotal = Math.round(coutTotal * 100.0) / 100.0;
        interetsTotaux = Math.round(interetsTotaux * 100.0) / 100.0;

        CreditSimulation simulation = new CreditSimulation(
                montant, dureeMois, tauxAnnuel,
                mensualite, coutTotal, interetsTotaux
        );
        repository.save(simulation);

        return new AmortissementDto(mensualite, coutTotal, interetsTotaux, lignes);
    }

    private List<LigneAmortissementDto> genererTableauAmortissement(Double montant, Double tauxMensuel,
                                                                    Integer dureeMois, Double mensualite) {
        List<LigneAmortissementDto> lignes = new ArrayList<>();
        double capitalRestant = montant;

        for (int mois = 1; mois <= dureeMois; mois++) {
            double partInteret = capitalRestant * tauxMensuel;
            double partCapital = mensualite - partInteret;

            if (mois == dureeMois) {
                partCapital = capitalRestant;
                partInteret = mensualite - partCapital;
            }

            capitalRestant = capitalRestant - partCapital;

            partCapital = Math.round(partCapital * 100.0) / 100.0;
            partInteret = Math.round(partInteret * 100.0) / 100.0;
            capitalRestant = Math.round(Math.max(0, capitalRestant * 100.0)) / 100.0;

            lignes.add(new LigneAmortissementDto(mois, partCapital, partInteret, capitalRestant));
        }

        return lignes;
    }

    public List<CreditSimulation> getHistorique() {
        return repository.findAll();
    }

    public void supprimerHistorique() {
        repository.deleteAll();
    }
}