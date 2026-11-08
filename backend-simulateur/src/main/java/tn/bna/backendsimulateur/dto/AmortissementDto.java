package tn.bna.backendsimulateur.dto;

import java.util.List;

public class AmortissementDto {
    private Double mensualite;
    private Double coutTotal;
    private Double interetsTotaux;
    private List<LigneAmortissementDto> lignes;

    public AmortissementDto(Double mensualite, Double coutTotal, Double interetsTotaux,
                            List<LigneAmortissementDto> lignes) {
        this.mensualite = mensualite;
        this.coutTotal = coutTotal;
        this.interetsTotaux = interetsTotaux;
        this.lignes = lignes;
    }

    // Getters et Setters
    public Double getMensualite() { return mensualite; }
    public void setMensualite(Double mensualite) { this.mensualite = mensualite; }

    public Double getCoutTotal() { return coutTotal; }
    public void setCoutTotal(Double coutTotal) { this.coutTotal = coutTotal; }

    public Double getInteretsTotaux() { return interetsTotaux; }
    public void setInteretsTotaux(Double interetsTotaux) { this.interetsTotaux = interetsTotaux; }

    public List<LigneAmortissementDto> getLignes() { return lignes; }
    public void setLignes(List<LigneAmortissementDto> lignes) { this.lignes = lignes; }
}