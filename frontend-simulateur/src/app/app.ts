import { Component, signal } from '@angular/core';
import { CreditFormComponent } from './credit-form/credit-form';

@Component({
  selector: 'app-root',
  imports: [CreditFormComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('BNA BANK - Simulateur');
}