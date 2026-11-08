package tn.bna.backendsimulateur.dto;

public class LigneAmortissementDto {
    private Integer mois;
    private Double partCapital;
    private Double partInteret;
    private Double capitalRestant;

    public LigneAmortissementDto(Integer mois, Double partCapital, Double partInteret, Double capitalRestant) {
        this.mois = mois;
        this.partCapital = partCapital;
        this.partInteret = partInteret;
        this.capitalRestant = capitalRestant;
    }
    
    public Integer getMois() { return mois; }
    public void setMois(Integer mois) { this.mois = mois; }

    public Double getPartCapital() { return partCapital; }
    public void setPartCapital(Double partCapital) { this.partCapital = partCapital; }

    public Double getPartInteret() { return partInteret; }
    public void setPartInteret(Double partInteret) { this.partInteret = partInteret; }

    public Double getCapitalRestant() { return capitalRestant; }
    public void setCapitalRestant(Double capitalRestant) { this.capitalRestant = capitalRestant; }
}