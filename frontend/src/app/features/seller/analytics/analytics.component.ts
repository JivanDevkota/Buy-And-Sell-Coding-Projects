import { Component } from '@angular/core';

@Component({
  selector: 'app-analytics',
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.css']
})
export class AnalyticsComponent {
  stats = {
    totalViews:     { value: '3,968', change: '+23% from last month', up: true },
    downloads:      { value: '847',   change: '+12% from last month', up: true },
    conversionRate: { value: '3.2%',  change: '-0.5% from last month', up: false },
    avgTime:        { value: '2:45',  change: '+15 seconds', up: true },
  };

  chartData = [
    { month: 'Jan', views: 1200 }, { month: 'Feb', views: 1800 },
    { month: 'Mar', views: 1400 }, { month: 'Apr', views: 2200 },
    { month: 'May', views: 1900 }, { month: 'Jun', views: 2800 },
    { month: 'Jul', views: 2400 }, { month: 'Aug', views: 3100 },
    { month: 'Sep', views: 2700 }, { month: 'Oct', views: 3500 },
    { month: 'Nov', views: 3200 }, { month: 'Dec', views: 3968 },
  ];

  topProjects = [
    { name: 'E-Commerce React App',  views: '1,247 views', sales: 42 },
    { name: 'Flight Booking System', views: '892 views',   sales: 28 },
    { name: 'Quiz Platform',         views: '456 views',   sales: 15 },
  ];

  traffic = [
    { source: 'Direct',   pct: 45 },
    { source: 'Search',   pct: 30 },
    { source: 'Social',   pct: 15 },
    { source: 'Referral', pct: 10 },
  ];

  maxViews = 0;

  ngOnInit(): void {
    this.maxViews = Math.max(...this.chartData.map(d => d.views));
  }

  barHeight(views: number): number {
    return Math.round((views / this.maxViews) * 100);
  }
}
