package tn.bna.backendsimulateur.controller;

import tn.bna.backendsimulateur.dto.AmortissementDto;
import tn.bna.backendsimulateur.dto.CreditSimulationRequest;
import tn.bna.backendsimulateur.entity.CreditSimulation;
import tn.bna.backendsimulateur.service.CreditService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/credit")
public class CreditController {

    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @PostMapping("/simuler")
    public ResponseEntity<AmortissementDto> simulerCredit(@Valid @RequestBody CreditSimulationRequest request) {
        AmortissementDto resultat = creditService.calculerAmortissement(request);
        return ResponseEntity.ok(resultat);
    }

    @GetMapping("/historique")
    public ResponseEntity<List<CreditSimulation>> getHistorique() {
        List<CreditSimulation> historique = creditService.getHistorique();
        return ResponseEntity.ok(historique);
    }

    @DeleteMapping("/historique")
    public ResponseEntity<Void> supprimerHistorique() {
        creditService.supprimerHistorique();
        return ResponseEntity.noContent().build();
    }
}