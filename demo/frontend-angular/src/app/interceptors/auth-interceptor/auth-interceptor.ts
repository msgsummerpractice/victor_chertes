import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { catchError, throwError } from "rxjs";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('jwt_token');
  const cloned = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`, 
    },
  });
  return next(cloned).pipe(
    catchError((error: HttpErrorResponse) => {
        console.error('Intercepted an HTTP error globally:', error.message);

        return throwError(() => error);
    })
  );
};