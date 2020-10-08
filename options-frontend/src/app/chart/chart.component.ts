import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit {
  @Input() title : string; 
  @Input() data: [];

  type = 'ComboChart';
  columnNames = ['Strike', 'Price', 'Model Price'];
  
  options = {
    hAxis: {
      title: 'Strikes'
    },
    vAxis: {
      title: 'Price'
    },
    interpolateNulls: true,
    seriesType: 'scatter',
    series: {0: {type: 'scatter', color: 'green'},
             1: {type: 'line', color: 'red', pointSize: 5, curveType: 'function'} }
  };
  width = 1800;
  height = 1300;

  constructor() { 
  }

  ngOnInit(): void {
  }
}
