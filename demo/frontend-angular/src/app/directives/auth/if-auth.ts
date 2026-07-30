import { Directive, TemplateRef, ViewContainerRef, effect, inject, input } from '@angular/core';

@Directive({
  selector: '[appIfAuth]',
})
export class IfAuth {
  
  private readonly _viewContainerRef = inject(ViewContainerRef);

  private readonly _templateRef = inject(TemplateRef);

  appIfAuth = input<boolean>(false);

  constructor() {
    effect(() => {
      if (this.appIfAuth()) {
        this._viewContainerRef.createEmbeddedView(this._templateRef);
      } else {
        this._viewContainerRef.clear();
      }
    })
  }
}
