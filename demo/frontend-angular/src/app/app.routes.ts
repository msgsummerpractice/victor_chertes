import { Routes } from '@angular/router';
import { Home } from './home/home';
import { NotFound } from './not-found/not-found';

export const routes: Routes = [
    {
        path: '',
        redirectTo: '/home',
        pathMatch: 'full'
    },

    {
        path: 'home',
        component: Home
    },

    {
        path: 'login',
        loadComponent: () => import('./login/login').then(m => m.Login)
    },

    {
        path: '**',
        component: NotFound
    }
];
