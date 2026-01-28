import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-complaint-success',
  templateUrl: './complaint-success.component.html',
  styleUrls: ['./complaint-success.component.scss']
})
export class ComplaintSuccessComponent implements OnInit {
  protocol = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.protocol = this.route.snapshot.paramMap.get('protocol') || '';
  }

  viewStatus(): void {
    this.router.navigate(['/client/statusv2', this.protocol]);
  }

  goHome(): void {
    this.router.navigate(['/client/complaint-form']);
  }
}