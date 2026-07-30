import { Component } from '@angular/core';
import { DogsContainer } from '../dogs-container/dogs-container';

@Component({
  selector: 'app-home',
  imports: [DogsContainer],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {}