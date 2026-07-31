import { Pipe, PipeTransform } from "@angular/core";

@Pipe({name: 'capitalizeAndSmile'})
export class capitalizeAndSmilePipe implements PipeTransform {
    transform(value: string): string {
        return value.toUpperCase().concat(":)");
    }
}