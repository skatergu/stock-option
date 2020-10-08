import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { getURL } from '../utils/WebHelper';
import { Option } from '../models/option.model';
import { of } from 'rxjs';
import { FormGroup } from '@angular/forms';

@Injectable({
    providedIn: 'root',
})
export class OptionsService {
    constructor(private http: HttpClient) {}
    
    getExpiries(ulSymbol: string): Observable<number[]>{
      let url = '/option/expiry/' + ulSymbol;
      return this.http.get<number[]>(getURL(url));
    }

    getStrikes(ulSymbol: string, expiry: string): Observable<number[]> {
      let url = '/option/strike/' + ulSymbol + "/" + expiry;

      return this.http.get<number[]>(getURL(url));
    }

    getModels() : Observable<string[]> {
      let url = '/option/models';
      return this.http.get<string[]>(getURL(url));
    }

    getOptions(formGroup: FormGroup): Observable<Option[]> {
      let url = '/option/getOptions';
      let queryParams = new HttpParams().set('ulSymbol', formGroup.get('ulSymbol').value);
      queryParams = queryParams.append('expiry', formGroup.get('expiry').value);
      queryParams = queryParams.append('optionType', formGroup.get('optionType').value);
      queryParams = queryParams.append('model', formGroup.get('model').value);
      queryParams = queryParams.append('priceType', formGroup.get('priceType').value);

      console.log(queryParams);

      return this.http.get<Option[]>(getURL(url), {'params':queryParams});
    }

    getOption(formGroup: FormGroup): Observable<Option> {
      let url = '/option/getOption';
      let queryParams = new HttpParams().set('ulSymbol', formGroup.get('ulSymbol').value);
      queryParams = queryParams.append('expiry', formGroup.get('expiry').value);
      queryParams = queryParams.append('optionType', formGroup.get('optionType').value);
      queryParams = queryParams.append('strike', formGroup.get('strike').value);

      console.log(queryParams);

      return this.http.get<Option>(getURL(url), {'params':queryParams});
    }

  }