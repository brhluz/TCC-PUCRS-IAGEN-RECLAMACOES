import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ComplaintService } from 'src/app/core/services/complaint.service';
import { Complaint, ComplaintStatus } from 'src/app/core/models/complaint.model';

@Component({
  selector: 'app-complaint-detail',
  templateUrl: './complaint-detail.component.html',
  styleUrls: ['./complaint-detail.component.scss']
})
export class ComplaintDetailComponent implements OnInit {
  complaint: Complaint | null = null;
  resolutionForm!: FormGroup;
  loading = false;
  saving = false;

  statuses = [
    { value: ComplaintStatus.NEW, label: 'Recebida' },
    { value: ComplaintStatus.IN_PROGRESS, label: 'Em Atendimento' },
    { value: ComplaintStatus.RESOLVED, label: 'Finalizado' }
  ];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private complaintService: ComplaintService
  ) {}

  ngOnInit(): void {
    this.resolutionForm = this.formBuilder.group({
      status: [''],
      resolution: ['']
    });

    const protocol = this.route.snapshot.paramMap.get('protocol');
    if (protocol) {
      this.loadComplaint(protocol);
    }
  }

  loadComplaint(protocol: string): void {
    this.loading = true;
    this.complaintService.getComplaintByProtocol(protocol).subscribe({
      next: (data) => {
        this.complaint = data || null;
        if (this.complaint) {
          this.resolutionForm.patchValue({
            status: this.complaint.status,
            resolution: this.complaint.resolution || ''
          });
        }
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  saveChanges(): void {
    if (!this.complaint) return;

    this.saving = true;
    const { status, resolution } = this.resolutionForm.value;

    this.complaintService.updateComplaintStatus(
      this.complaint.protocol!,
      status,
      resolution
    ).subscribe({
      next: () => {
        this.saving = false;
        alert('Alterações salvas com sucesso!');
        this.router.navigate(['/admin/dashboard']);
      },
      error: () => {
        this.saving = false;
        alert('Erro ao salvar alterações');
      } 
    });
  }

  goBack(): void {
    this.router.navigate(['/admin/dashboard']);
  }

  formatDate(date: Date | undefined): string {
    if (!date) return '-';
    const d = new Date(date);
    return `${d.getDate().toString().padStart(2, '0')}/${(d.getMonth() + 1).toString().padStart(2, '0')}/${d.getFullYear()} às ${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`;
  }
}