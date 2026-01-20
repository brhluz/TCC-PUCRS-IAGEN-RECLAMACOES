import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ComplaintService } from 'src/app/core/services/complaint.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { Complaint, ComplaintStatus } from 'src/app/core/models/complaint.model';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  complaints: Complaint[] = [];
  filteredComplaints: Complaint[] = [];
  loading = false;
  
  filters = {
    category: 'all',
    status: 'all'
  };

  categories = [
    { value: 'all', label: 'Todas as Categorias' },
    { value: 'Logística', label: 'Logística' },
    { value: 'Financeiro', label: 'Financeiro' },
    { value: 'Produto', label: 'Produto' }
  ];

  statuses = [
    { value: 'all', label: 'Todos os Status' },
    { value: ComplaintStatus.NEW, label: 'Recebida' },
    { value: ComplaintStatus.IN_PROGRESS, label: 'Em Atendimento' },
    { value: ComplaintStatus.RESOLVED, label: 'Finalizado' }
  ];

  userName = '';

  constructor(
    private complaintService: ComplaintService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userName = this.authService.currentUserValue?.name || 'Atendente';
    this.loadComplaints();
  }

  loadComplaints(): void {
    this.loading = true;
    this.complaintService.getComplaints(this.filters).subscribe({
      next: (data) => {
        this.complaints = data;
        this.filteredComplaints = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  applyFilters(): void {
    this.loadComplaints();
  }

  getStatusClass(status: ComplaintStatus): string {
    switch (status) {
      case ComplaintStatus.NEW:
        return 'status-new';
      case ComplaintStatus.IN_PROGRESS:
        return 'status-wip';
      case ComplaintStatus.RESOLVED:
        return 'status-done';
      default:
        return '';
    }
  }

  viewDetails(protocol: string): void {
    this.router.navigate(['/admin/complaint', protocol]);
  }

  logout(): void {
    this.authService.logout();
  }

  formatDate(date: Date | undefined): string {
    if (!date) return '-';
    const d = new Date(date);
    return `${d.getDate().toString().padStart(2, '0')}/${(d.getMonth() + 1).toString().padStart(2, '0')} ${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`;
  }
}