import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ComplaintService } from 'src/app/core/services/complaint.service';
import { Complaint } from 'src/app/core/models/complaint.model';

interface TimelineItem {
  title: string;
  date: string;
  completed: boolean;
}

@Component({
  selector: 'app-complaint-status',
  templateUrl: './complaint-status.component.html',
  styleUrls: ['./complaint-status.component.scss']
})
export class ComplaintStatusComponent implements OnInit {
  complaint: Complaint | null = null;
  loading = false;
  timeline: TimelineItem[] = [];

  constructor(
    private route: ActivatedRoute,
    private complaintService: ComplaintService
  ) {}

  ngOnInit(): void {
    const protocol = this.route.snapshot.paramMap.get('protocol');
    if (protocol) {
      this.loadComplaintStatus(protocol);
    }
  }

  loadComplaintStatus(protocol: string): void {
    this.loading = true;
    this.complaintService.getComplaintByProtocol(protocol).subscribe({
      next: (data) => {
        this.complaint = data || null;
        if (this.complaint) {
          this.buildTimeline();
        }
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  buildTimeline(): void {
    if (!this.complaint) return;

    this.timeline = [
      {
        title: 'Reclamação Recebida',
        date: this.formatDate(this.complaint.createdAt),
        completed: true
      },
      {
        title: 'Classificado Automaticamente',
        date: this.formatDate(this.complaint.createdAt, 5),
        completed: true
      },
      {
        title: this.complaint.status === 'Finalizado' ? 'Finalizado' : 'Em análise pelo Atendente',
        date: this.formatDate(this.complaint.updatedAt),
        completed: this.complaint.status !== 'Recebida'
      }
    ];
  }

  formatDate(date: Date | undefined, addMinutes = 0): string {
    if (!date) return '-';
    const d = new Date(date);
    d.setMinutes(d.getMinutes() + addMinutes);
    return `${d.getDate().toString().padStart(2, '0')}/${(d.getMonth() + 1).toString().padStart(2, '0')}/${d.getFullYear()} às ${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`;
  }

  formatShortDate(date: Date | undefined): string {
    if (!date) return '-';
    const d = new Date(date);
    return `Hoje, ${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`;
  }
}