import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { catchError, throwError } from "rxjs";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const cloned = req.clone({
    setHeaders: {
      Authorization: 'Bearer <insert-your-token-here>', 
    },
  });
  return next(cloned).pipe(
    catchError((error: HttpErrorResponse) => {
        console.error('Intercepted an HTTP error globally:', error.message);

        return throwError(() => error);
    })
  );
};