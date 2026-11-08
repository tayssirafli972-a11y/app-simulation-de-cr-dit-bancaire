package tn.bna.backendsimulateur.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreditSimulationRequest {

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    @Min(value = 100, message = "Le montant minimum est de 100 DT")
    private Double montant;

    @NotNull(message = "La durée est obligatoire")
    @Positive(message = "La durée doit être positive")
    @Min(value = 1, message = "La durée minimum est de 1 mois")
    private Integer dureeMois;

    @NotNull(message = "Le taux est obligatoire")
    @Positive(message = "Le taux doit être positif")
    @Min(value = 0, message = "Le taux ne peut pas être négatif")
    private Double tauxAnnuel;

    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }

    public Integer getDureeMois() { return dureeMois; }
    public void setDureeMois(Integer dureeMois) { this.dureeMois = dureeMois; }

    public Double getTauxAnnuel() { return tauxAnnuel; }
    public void setTauxAnnuel(Double tauxAnnuel) { this.tauxAnnuel = tauxAnnuel; }
}