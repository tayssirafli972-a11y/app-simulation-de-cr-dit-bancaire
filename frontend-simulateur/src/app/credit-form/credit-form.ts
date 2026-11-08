import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CreditService, SimulationResult } from '../credit.service';

@Component({
  selector: 'app-credit-form',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './credit-form.html',
  styleUrls: ['./credit-form.css']
})
export class CreditFormComponent {
  montant: number | null = null;
  dureeMois: number | null = null;
  tauxAnnuel: number | null = null;
  resultat: SimulationResult | null = null;
  historique: any[] = [];
  loading: boolean = false;
  error: string | null = null;
  afficherHistorique: boolean = false;

  constructor(private creditService: CreditService) {
  }

  onSubmit(): void {
    if (this.montant === null || this.dureeMois === null || this.tauxAnnuel === null) {
      this.error = 'Veuillez remplir tous les champs';
      return;
    }

    this.loading = true;
    this.error = null;
    this.resultat = null;

    this.creditService.simulerCredit({
      montant: this.montant,
      dureeMois: this.dureeMois,
      tauxAnnuel: this.tauxAnnuel
    }).subscribe({
      next: (data) => {
        this.resultat = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur: ' + (err.message || 'Impossible de contacter le serveur');
        this.loading = false;
        console.error(err);
      }
    });
  }

  reinitialiser(): void {
    this.montant = null;
    this.dureeMois = null;
    this.tauxAnnuel = null;
    this.resultat = null;
    this.error = null;
  }

  chargerHistorique(): void {
    this.afficherHistorique = true;
    this.creditService.getHistorique().subscribe({
      next: (data) => this.historique = data,
      error: (err) => console.error('Erreur historique:', err)
    });
  }

  supprimerHistorique(): void {
    if (confirm('Voulez-vous vraiment vider TOUT l\'historique ?')) {
      this.creditService.supprimerHistorique().subscribe({
        next: () => {
          this.historique = [];
          this.resultat = null;
          alert('Historique vidé avec succès !');
        },
        error: (err) => {
          console.error('Erreur suppression:', err);
          alert('Erreur lors de la suppression de l\'historique');
        }
      });
    }
  }

  get dureeEnAnnees(): number {
    return this.dureeMois ? this.dureeMois / 12 : 0;
  }

  get pourcentageInterets(): number {
    if (!this.resultat || !this.montant) return 0;
    return (this.resultat.interetsTotaux / this.montant) * 100;
  }

  get capitalEmprunte(): number {
    return this.montant || 0;
  }

  get dureeAffichee(): number {
    return this.dureeMois || 0;
  }

  get tauxAffiche(): number {
    return this.tauxAnnuel || 0;
  }

  exporterCSV(): void {
    if (!this.resultat) {
      alert('Aucune simulation à exporter');
      return;
    }

    const resultat: any = this.resultat;

    let csv = '\uFEFF';
    csv += 'Mois;Part Capital (DT);Part Interet (DT);Capital Restant (DT)\n';

    resultat.lignes.forEach((ligne: any) => {
      csv += `${ligne.mois};${ligne.partCapital};${ligne.partInteret};${ligne.capitalRestant}\n`;
    });

    csv += '\n';
    csv += `Mensualite;${resultat.mensualite}\n`;
    csv += `Cout total;${resultat.coutTotal}\n`;
    csv += `Total interets;${resultat.interetsTotaux}\n`;

    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = window.URL.createObjectURL(blob);

    const lien = document.createElement('a');
    lien.href = url;
    lien.download = `simulation_credit_${new Date().toISOString().slice(0, 10)}.csv`;
    lien.style.display = 'none';

    document.body.appendChild(lien);
    lien.click();
    document.body.removeChild(lien);

    window.URL.revokeObjectURL(url);
  }

  exporterPDF(): void {
    if (!this.resultat) {
      alert('Aucune simulation à exporter');
      return;
    }

    const resultat: any = this.resultat;

    const doc = new jsPDF('p', 'mm', 'a4');

    const bnaGreen: [number, number, number] = [27, 158, 133];
    const bnaGray: [number, number, number] = [90, 90, 90];

    // ===== EN-TÊTE =====
    doc.setFillColor(bnaGreen[0], bnaGreen[1], bnaGreen[2]);
    doc.rect(0, 0, 210, 25, 'F');

    doc.setTextColor(255, 255, 255);
    doc.setFontSize(18);
    doc.setFont('helvetica', 'bold');
    doc.text('BNA BANK', 15, 12);

    doc.setFontSize(10);
    doc.setFont('helvetica', 'normal');
    doc.text('Simulateur de Credit - Banque Nationale Agricole', 15, 19);

    // ===== TITRE =====
    doc.setTextColor(bnaGreen[0], bnaGreen[1], bnaGreen[2]);
    doc.setFontSize(16);
    doc.setFont('helvetica', 'bold');
    doc.text('Recapitulatif de la simulation', 15, 38);

    // ===== INFORMATIONS =====
    doc.setTextColor(0, 0, 0);
    doc.setFontSize(11);
    doc.setFont('helvetica', 'normal');

    let y = 50;

    doc.text(`Capital emprunte : ${this.montant} DT`, 15, y); y += 7;
    doc.text(`Duree du credit : ${this.dureeMois} mois`, 15, y); y += 7;
    doc.text(`Taux annuel : ${this.tauxAnnuel} %`, 15, y); y += 7;
    doc.text(`Mensualite : ${resultat.mensualite.toFixed(2)} DT`, 15, y); y += 10;

    doc.setDrawColor(bnaGreen[0], bnaGreen[1], bnaGreen[2]);
    doc.setLineWidth(0.5);
    doc.line(15, y, 195, y);
    y += 8;

    // ===== RÉCAPITULATIF FINANCIER =====
    doc.setFontSize(12);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(bnaGreen[0], bnaGreen[1], bnaGreen[2]);
    doc.text('Cout total du credit :', 15, y);
    doc.setTextColor(bnaGray[0], bnaGray[1], bnaGray[2]);
    doc.text(`${resultat.coutTotal.toFixed(2)} DT`, 195, y, { align: 'right' });
    y += 7;

    doc.setTextColor(bnaGreen[0], bnaGreen[1], bnaGreen[2]);
    doc.text('Total des interets :', 15, y);
    doc.setTextColor(bnaGray[0], bnaGray[1], bnaGray[2]);
    doc.text(`${resultat.interetsTotaux.toFixed(2)} DT`, 195, y, { align: 'right' });
    y += 12;

    // ===== TABLEAU =====
    doc.setFontSize(13);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(bnaGreen[0], bnaGreen[1], bnaGreen[2]);
    doc.text("Tableau d'amortissement", 15, y);

    const tableData = resultat.lignes.map((ligne: any) => [
      ligne.mois.toString(),
      ligne.partCapital.toFixed(2) + ' DT',
      ligne.partInteret.toFixed(2) + ' DT',
      ligne.capitalRestant.toFixed(2) + ' DT'
    ]);

    autoTable(doc, {
      startY: y + 5,
      head: [['Mois', 'Part Capital', 'Part Interet', 'Capital Restant']],
      body: tableData,
      theme: 'striped',
      headStyles: {
        fillColor: bnaGreen,
        textColor: [255, 255, 255],
        fontStyle: 'bold',
        halign: 'center'
      },
      bodyStyles: { fontSize: 9 },
      alternateRowStyles: { fillColor: [232, 245, 242] },
      columnStyles: {
        0: { halign: 'center', cellWidth: 25 },
        1: { halign: 'right', cellWidth: 50 },
        2: { halign: 'right', cellWidth: 50 },
        3: { halign: 'right', cellWidth: 55 }
      },
      margin: { left: 15, right: 15 }
    });

    // ===== PIED DE PAGE =====
    const pageCount = (doc as any).internal.getNumberOfPages();
    for (let i = 1; i <= pageCount; i++) {
      doc.setPage(i);
      doc.setFontSize(8);
      doc.setTextColor(150, 150, 150);
      doc.text(
        `BNA BANK - Genere le ${new Date().toLocaleDateString('fr-FR')} - Page ${i}/${pageCount}`,
        105, 290, { align: 'center' }
      );
    }

    const nomFichier = `simulation_credit_${new Date().toISOString().slice(0, 10)}.pdf`;
    doc.save(nomFichier);
  }
}