import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SimulationRequest {
  montant: number;
  dureeMois: number;
  tauxAnnuel: number;
}

export interface LigneAmortissement {
  mois: number;
  partCapital: number;
  partInteret: number;
  capitalRestant: number;
}

export interface SimulationResult {
  mensualite: number;
  coutTotal: number;
  interetsTotaux: number;
  lignes: LigneAmortissement[];
}

export interface Historique {
  id: number;
  montant: number;
  dureeMois: number;
  tauxAnnuel: number;
  mensualite: number;
  coutTotal: number;
  interetsTotaux: number;
  dateSimulation: string;
}

@Injectable({
  providedIn: 'root'
})
export class CreditService {

  private apiUrl = 'http://localhost:8080/api/credit';

  constructor(private http: HttpClient) { }

  simulerCredit(data: SimulationRequest): Observable<SimulationResult> {
    return this.http.post<SimulationResult>(`${this.apiUrl}/simuler`, data);
  }

  getHistorique(): Observable<Historique[]> {
    return this.http.get<Historique[]>(`${this.apiUrl}/historique`);
  }
  supprimerHistorique(): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/historique`);
}
}