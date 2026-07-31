import { Component, inject } from '@angular/core';
import { DogCard } from '../dog-card/dog-card';
import { DogService } from '../../services/dog/dog-service';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-dogs-container',
  imports: [DogCard, MatButtonModule],
  providers: [DogService],
  templateUrl: './dogs-container.html',
  styleUrl: './dogs-container.scss',
})
export class DogsContainer {
  dogService = inject(DogService);
}
