import { Component, OnInit } from '@angular/core';
import { OptionsService } from '../option-service/options.service';
import { Option } from '../models/option.model';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-options',
  templateUrl: './option-model.component.html',
  styleUrls: ['./option-model.component.css']
})
export class OptionModelComponent implements OnInit {
  expiries: number[];
  optionTypes: string[];
  models: string[];
  priceTypes =  ["mid", "bid", "ask" ];

  optionRes: Option[];
  data : any[];
  title: string;

  optionReq = new FormGroup({
    ulSymbol: new FormControl(),
    expiry: new FormControl(),
    optionType: new FormControl(),
    model: new FormControl(),
    priceType: new FormControl(this.priceTypes[0]),
    isInTheMoney: new FormControl(true),
  })

  constructor(private optionsService: OptionsService) { }

  ngOnInit(): void {
    this.data = [];
    this.getModels();
  }
  
  getModels() {
     this.optionsService.getModels().subscribe(models => {
       this.models = models;
       this.optionReq.get('model').setValue(this.models[0]);
     })
  }

  onFormSubmit() {
    this.refreshData()
  }
   
  onChange() {
    this.getQuote();
  }

  getQuote() {
    this.optionsService.getOptions(this.optionReq).subscribe(options=> {
      this.optionRes = options;
      this.drawOptions();
    })
  }

  refreshData() {
    this.optionsService.getExpiries(this.optionReq.get('ulSymbol').value).subscribe(date => {
       this.expiries = date;
       this.optionReq.get('expiry').setValue(date[0]);
       this.optionTypes= ['call', 'put'];
       this.optionReq.get('optionType').setValue(this.optionTypes[0]);
       this.getQuote();
    })
  }

  drawOptions() {
    let opt = this.optionRes[0];
    this.title = this.optionReq.get('ulSymbol').value + " Price=" + opt.ulPrice;
    this.data = []; 
    this.optionRes.forEach(option => {
      let optionData =  [option.strike, option.price, option.modelValue];
      this.data.push(optionData);
    });
    console.table(this.data);
  }  

}
