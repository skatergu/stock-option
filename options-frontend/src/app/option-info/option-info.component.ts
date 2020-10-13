import { Component, OnInit } from '@angular/core';
import { OptionsService } from '../option-service/options.service';
import { Option } from '../models/option.model';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-options',
  templateUrl: './option-info.component.html',
  styleUrls: ['./option-info.component.css']
})
export class OptionInfoComponent implements OnInit {
  expiries: number[];
  optionTypes: string[];
  strikes: number[];
  currentTime: number;

  optionRes: Option;

  optionReq = new FormGroup({
    ulSymbol: new FormControl(),
    expiry: new FormControl(),
    strike: new FormControl(),
    optionType: new FormControl(),
  })

  constructor(private optionsService: OptionsService) { }

  ngOnInit(): void {
  }
  
  onUlChange() {
    this.optionsService.getExpiries(this.optionReq.get('ulSymbol').value).subscribe(date => {
       this.expiries = date;
       this.optionReq.get('expiry').setValue(date[0]);
       this.optionTypes= ['call', 'put'];
       this.optionReq.get('optionType').setValue(this.optionTypes[0]);
       this.onExpiryChange();
    });
  }

  onExpiryChange() {
     this.optionsService.getStrikes(this.optionReq.controls['ulSymbol'].value, this.optionReq.controls['expiry'].value).subscribe (strikes => {
          this.strikes = strikes;
          this.optionReq.controls['strike'].setValue(strikes[0]);
     })
  }

  getControlValue(type: string) {
    return this.optionReq.controls[type].value;
  }

  onSubmit() {
    this.getOption();
  }

  getOption() {
    this.optionsService.getOption(this.optionReq).subscribe(option=> {
      this.optionRes = option;
      this.currentTime = Date.now();
    })
  }

  clearResult() {
    this.optionRes = null;
  }

}
