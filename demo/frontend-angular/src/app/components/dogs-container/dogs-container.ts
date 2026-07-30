import { Component, inject } from '@angular/core';
import { DogCard } from '../dog-card/dog-card';
import { DogService } from '../../services/dog/dog-service';

@Component({
  selector: 'app-dogs-container',
  imports: [DogCard],
  providers: [DogService],
  templateUrl: './dogs-container.html',
  styleUrl: './dogs-container.css',
})
export class DogsContainer {
  dogService = inject(DogService);
}