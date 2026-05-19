import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../servicios/auth';

export const rolesGuard: CanActivateFn = (route) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const expectedRoles = route.data['expectedRoles'] as string[] | undefined;

  if (!auth.isAuthenticated()) {
    return router.createUrlTree(['/login']);
  }

  if (!expectedRoles?.length || auth.hasAnyRole(expectedRoles)) {
    return true;
  }

  return router.createUrlTree(['/unauthorized']);
};
