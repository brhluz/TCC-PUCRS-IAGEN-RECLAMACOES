export interface Complaint {
  id?: string;
  protocol?: string;
  customerName: string;
  customerEmail: string;
  description: string;
  status: ComplaintStatus;
  aiCategory?: string;
  createdAt?: Date;
  updatedAt?: Date;
  resolution?: string;
  forwardedDepartments?: {
    department: string;
    extractedReason: string;
  }[];
}

export enum ComplaintStatus {
  NEW = 'Recebida',
  IN_PROGRESS = 'Em Atendimento',
  RESOLVED = 'Finalizado'
}

export interface ComplaintListItem {
  protocol: string;
  date: string;
  customerName: string;
  aiCategory: string;
  status: ComplaintStatus;
}