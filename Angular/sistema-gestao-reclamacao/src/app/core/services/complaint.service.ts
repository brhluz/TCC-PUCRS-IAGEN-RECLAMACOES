import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, delay } from 'rxjs';
import { Complaint, ComplaintStatus } from '../models/complaint.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ComplaintService {
  private apiUrl = environment.apiUrl;

  private mockComplaints: Complaint[] = [
    {
      id: '1',
      protocol: '#2025-8392',
      customerName: 'Maria da Silva',
      customerEmail: 'maria@email.com',
      description: 'Fiz a compra do produto X na semana passada e até agora não recebi o código de rastreio. Tentei contato pelo telefone mas ninguém atende. Preciso de uma posição urgente pois é um presente.',
      status: ComplaintStatus.NEW,
      aiCategory: 'Logística / Atraso',
      createdAt: new Date('2025-01-04T09:00:00'),
      updatedAt: new Date('2025-01-04T14:30:00')
    },
    {
      id: '2',
      protocol: '#2025-8390',
      customerName: 'João Souza',
      customerEmail: 'joao@email.com',
      description: 'Cobrado valor incorreto na fatura',
      status: ComplaintStatus.IN_PROGRESS,
      aiCategory: 'Financeiro / Cobrança',
      createdAt: new Date('2025-01-03T14:20:00'),
      updatedAt: new Date('2025-01-03T14:20:00')
    },
    {
      id: '3',
      protocol: '#2025-8388',
      customerName: 'Ana Clara',
      customerEmail: 'ana@email.com',
      description: 'Produto com defeito de fabricação',
      status: ComplaintStatus.RESOLVED,
      aiCategory: 'Produto / Defeito',
      createdAt: new Date('2025-01-02T10:15:00'),
      updatedAt: new Date('2025-01-02T10:15:00'),
      resolution: 'Produto substituído'
    }
  ];

  constructor(private http: HttpClient) {}

  getComplaints(filters?: any): Observable<Complaint[]> {
    let filtered = [...this.mockComplaints];
    
    if (filters?.status && filters.status !== 'all') {
      filtered = filtered.filter(c => c.status === filters.status);
    }
    
    if (filters?.category && filters.category !== 'all') {
      filtered = filtered.filter(c => c.aiCategory?.includes(filters.category));
    }
    
    return of(filtered).pipe(delay(300));
  }

  getComplaintByProtocol(protocol: string): Observable<Complaint | undefined> {
    const complaint = this.mockComplaints.find(c => c.protocol === protocol);
    return of(complaint).pipe(delay(300));
  }

  createComplaint(complaint: Partial<Complaint>): Observable<Complaint> {
    const newComplaint: Complaint = {
      id: String(this.mockComplaints.length + 1),
      protocol: `#2025-${8400 + this.mockComplaints.length}`,
      customerName: complaint.customerName!,
      customerEmail: complaint.customerEmail!,
      description: complaint.description!,
      status: ComplaintStatus.NEW,
      aiCategory: this.classifyWithAI(complaint.description!),
      createdAt: new Date(),
      updatedAt: new Date()
    };
    
    this.mockComplaints.unshift(newComplaint);
    return of(newComplaint).pipe(delay(1000));
  }

  updateComplaintStatus(protocol: string, status: ComplaintStatus, resolution?: string): Observable<Complaint> {
    const complaint = this.mockComplaints.find(c => c.protocol === protocol);
    if (complaint) {
      complaint.status = status;
      complaint.updatedAt = new Date();
      if (resolution) {
        complaint.resolution = resolution;
      }
    }
    return of(complaint!).pipe(delay(500));
  }

  private classifyWithAI(description: string): string {
    const lowerDesc = description.toLowerCase();
    
    if (lowerDesc.includes('entrega') || lowerDesc.includes('rastreio') || lowerDesc.includes('atraso')) {
      return 'Logística / Atraso';
    } else if (lowerDesc.includes('cobrança') || lowerDesc.includes('valor') || lowerDesc.includes('fatura')) {
      return 'Financeiro / Cobrança';
    } else if (lowerDesc.includes('defeito') || lowerDesc.includes('quebrado') || lowerDesc.includes('danificado')) {
      return 'Produto / Defeito';
    } else {
      return 'Geral / Outros';
    }
  }
}