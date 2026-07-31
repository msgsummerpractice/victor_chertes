import { Injectable, signal, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin } from 'rxjs';

interface DogApiResponse {
  message: string;
  status: string;
}

@Injectable()
export class DogService {
    private http = inject(HttpClient);

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
    ]).subscribe({
      next: ([bassetRes, walkerRes, afghanRes]) => {
        this.dogImages.set([bassetRes.message, walkerRes.message, afghanRes.message]);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error while fetching: ', err);
        this.isError.set(true);
        this.isLoading.set(false);
      }
    });
  }
}
