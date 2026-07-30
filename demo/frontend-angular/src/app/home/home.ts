import { HttpClient } from '@angular/common/http';
import { Component, signal, inject, DestroyRef } from '@angular/core';
import { forkJoin } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

interface DogApiResponse {
  message: string;
  status: string;
}

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  private http = inject(HttpClient);
  private destroyRef = inject(DestroyRef);

  dogImages = signal<string[]>([]);
  isLoading = signal<boolean>(false);
  isError = signal<boolean>(false);

  fetchDogs() {
    this.isLoading.set(true);
    this.isError.set(false);
    this.dogImages.set([]);

    forkJoin([
      this.http.get<DogApiResponse>('https://dog.ceo/api/breed/hound/basset/images/random'),
      this.http.get<DogApiResponse>('https://dog.ceo/api/breed/hound/walker/images/random'),
      this.http.get<DogApiResponse>('https://dog.ceo/api/breed/hound/afghan/images/random')
    ])
    .pipe(
      takeUntilDestroyed(this.destroyRef)
    )
    .subscribe({
      next: ([goldenRes, frenchRes, germanRes]) => {
        this.dogImages.set([goldenRes.message,frenchRes.message,germanRes.message]);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error while fetching: ',err);
        this.isError.set(true);
        this.isLoading.set(false);
      }
    });
  }
}
