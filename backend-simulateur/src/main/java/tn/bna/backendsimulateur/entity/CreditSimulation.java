package tn.bna.backendsimulateur.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Table(name = "simulations")
public class CreditSimulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private Double montant;

    @NotNull(message = "La durée est obligatoire")
    @Positive(message = "La durée doit être positive")
    private Integer dureeMois;

    @NotNull(message = "Le taux est obligatoire")
    @Positive(message = "Le taux doit être positif")
    private Double tauxAnnuel;

    private Double mensualite;
    private Double coutTotal;
    private Double interetsTotaux;
    private LocalDateTime dateSimulation;

    public CreditSimulation() {}

    public CreditSimulation(Double montant, Integer dureeMois, Double tauxAnnuel,
                            Double mensualite, Double coutTotal, Double interetsTotaux) {
        this.montant = montant;
        this.dureeMois = dureeMois;
        this.tauxAnnuel = tauxAnnuel;
        this.mensualite = mensualite;
        this.coutTotal = coutTotal;
        this.interetsTotaux = interetsTotaux;
        this.dateSimulation = LocalDateTime.now();
    }

    
    public Long getId() { return id; }
    public Double getMontant() { return montant; }
    public Integer getDureeMois() { return dureeMois; }
    public Double getTauxAnnuel() { return tauxAnnuel; }
    public Double getMensualite() { return mensualite; }
    public Double getCoutTotal() { return coutTotal; }
    public Double getInteretsTotaux() { return interetsTotaux; }
    public LocalDateTime getDateSimulation() { return dateSimulation; }

    public void setId(Long id) { this.id = id; }
    public void setMontant(Double montant) { this.montant = montant; }
    public void setDureeMois(Integer dureeMois) { this.dureeMois = dureeMois; }
    public void setTauxAnnuel(Double tauxAnnuel) { this.tauxAnnuel = tauxAnnuel; }
    public void setMensualite(Double mensualite) { this.mensualite = mensualite; }
    public void setCoutTotal(Double coutTotal) { this.coutTotal = coutTotal; }
    public void setInteretsTotaux(Double interetsTotaux) { this.interetsTotaux = interetsTotaux; }
    public void setDateSimulation(LocalDateTime dateSimulation) { this.dateSimulation = dateSimulation; }
}