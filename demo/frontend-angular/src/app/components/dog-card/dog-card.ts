import { Component, input } from '@angular/core';
import { capitalizeAndSmilePipe } from '../../pipes/capitalize-and-smile-pipe/capitalize-and-smile-pipe';

@Component({
  selector: 'app-dog-card',
  imports: [capitalizeAndSmilePipe],
  templateUrl: './dog-card.html',
  styleUrl: './dog-card.css',
})
export class DogCard {
  imageUrl = input<string>('');
  name = input<string>('');
  description = input<string>('');
  isLoading = input<boolean>(false);
}
