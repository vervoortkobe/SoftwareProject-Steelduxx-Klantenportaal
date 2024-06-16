import { Pipe, PipeTransform } from '@angular/core';
import GeneralUtils from '../utils/general-utils';

@Pipe({
  name: 'fileformatter'
})
export class FileformatterPipe implements PipeTransform {

  transform(value: number): string {
    return GeneralUtils.humanFileSize(value);
  }

}
