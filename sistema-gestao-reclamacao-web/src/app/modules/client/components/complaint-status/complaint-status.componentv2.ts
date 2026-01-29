import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { ComplaintService } from 'src/app/core/services/complaint.service';
import { Complaint } from 'src/app/core/models/complaint.model';

interface DepartmentForwarding {
  department: string;
  extractedReason: string;
}

@Component({
  selector: 'app-complaint-statusv2',
  templateUrl: './complaint-status.componentv2.html',
  styleUrls: ['./complaint-status.componentv2.scss']
})
export class ComplaintStatusComponentV2 implements OnInit {
  complaint: Complaint | null = null;
  loading = false;
  forwardedDepartments: DepartmentForwarding[] = [];

  constructor(
    private route: ActivatedRoute,
    private complaintService: ComplaintService,
    private router: Router
  ) {}
    
  ngOnInit(): void {
    const protocol = this.route.snapshot.paramMap.get('protocol');
    if (protocol) {
      this.loadComplaintStatus(protocol);
    }
  }

  loadComplaintStatus(protocol: string): void {
    this.loading = true;
    this.complaintService.getComplaintsV2ByProtocol(protocol).subscribe({
      next: (data) => {
        console.table(data);
        this.complaint = data || null;
        if (this.complaint) {
          this.buildForwardedDepartments();
        }
        this.loading = false;
      },
      error: (error) => {
        this.loading = false;
        const details =  error?.error?.message ?? error?.message ??  (typeof error?.error === 'string' ? error.error : JSON.stringify(error?.error ?? error));

        alert(`Erro ao enviar reclamação. Tente novamente mais tarde. Detalhes: ${details}`);

        console.error('Create complaint failed:', error);
      }
    });
  }

  buildForwardedDepartments(): void {
    if (!this.complaint) return;
    this.forwardedDepartments = this.complaint.forwardedDepartments || [];
  }

  formatShortDate(date: Date | undefined): string {
    if (!date) return '-';
    const d = new Date(date);
    const now = new Date();
    const isToday = d.getDate() === now.getDate() && 
                    d.getMonth() === now.getMonth() && 
                    d.getFullYear() === now.getFullYear();
    
    if (isToday) {
      return `Hoje, ${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`;
    }
    
    return `${d.getDate().toString().padStart(2, '0')}/${(d.getMonth() + 1).toString().padStart(2, '0')}/${d.getFullYear()} às ${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`;
  }

  goToBegin(): void {
  this.router.navigate(['/']);
}
}
