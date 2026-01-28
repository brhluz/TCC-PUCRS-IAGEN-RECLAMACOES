import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, delay } from 'rxjs';
import { map } from 'rxjs/operators';
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
      protocol: '652dbad3-67ee-4165-84fb-07c0c61be205',
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

  private mockComplaintsV2: Complaint[] = [
    {
      id: '1',
      protocol: 'abdadda7-213f-403c-bbdf-1ce2c1e54cf6',
      customerName: 'Maria da Silva',
      customerEmail: 'maria@email.com',
      description: 'O tênis chegou com a cor errada e ninguém me responde no chat há dois dias.',
      status: ComplaintStatus.IN_PROGRESS,
      aiCategory: 'Produto errado / Demora no atendimento ou no retorno',
      forwardedDepartments: [
        {
          department: 'Logística',
          extractedReason: 'Tênis entregue com a cor errada.'
        },
        {
          department: 'Atendimento ao cliente',
          extractedReason: 'Ninguém responde no chat há dois dias.'
        }
      ],
      createdAt: new Date('2025-01-04T09:00:00'),
      updatedAt: new Date('2025-01-04T14:30:00')
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

  getComplaintsV2ByProtocol(protocol: string): Observable<Complaint | undefined> {
    const complaint = this.mockComplaintsV2.find(c => c.protocol === protocol);
    return of(complaint).pipe(delay(300));
  }

  createComplaint(complaint: Partial<Complaint>): Observable<Complaint> {
    const requestBody = {
      nome: complaint.customerName,
      email: complaint.customerEmail,
      descricao: complaint.description
    };

    return this.http.post<any>(`${this.apiUrl}/reclamacoes`, requestBody).pipe(
      map(response => ({
        id: response.protocolo,
        protocol: response.protocolo,
        customerName: response.nome,
        customerEmail: response.email,
        description: response.descricao,
        status: this.mapStatus(response.status),
        createdAt: new Date(response.criadoEm),
        updatedAt: new Date(response.atualizadoEm)
      }))
    );
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

  private mapStatus(status: string): ComplaintStatus {
    switch (status) {
      case 'RECEBIDA':
        return ComplaintStatus.NEW;
      case 'EM_ATENDIMENTO':
        return ComplaintStatus.IN_PROGRESS;
      case 'FINALIZADO':
        return ComplaintStatus.RESOLVED;
      default:
        return ComplaintStatus.NEW;
    }
  }

}
