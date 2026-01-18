import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ComplaintService } from 'src/app/core/services/complaint.service';
import { ComplaintStatus } from 'src/app/core/models/complaint.model';

@Component({
  selector: 'app-complaint-form',
  templateUrl: './complaint-form.component.html',
  styleUrls: ['./complaint-form.component.scss']
})
export class ComplaintFormComponent implements OnInit {
  complaintForm!: FormGroup;
  submitted = false;
  submitting = false;
  charCount = 0;
  maxChars = 500;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private complaintService: ComplaintService
  ) {}

  ngOnInit(): void {
    this.complaintForm = this.formBuilder.group({
      customerName: ['', Validators.required],
      customerEmail: ['', [Validators.required, Validators.email]],
      description: ['', [Validators.required, Validators.maxLength(this.maxChars)]]
    });

    this.complaintForm.get('description')?.valueChanges.subscribe(value => {
      this.charCount = value ? value.length : 0;
    });
  }

  get f() { return this.complaintForm.controls; }

  onSubmit(): void {
    this.submitted = true;

    if (this.complaintForm.invalid) {
      return;
    }

    this.submitting = true;

    const complaint = {
      ...this.complaintForm.value,
      status: ComplaintStatus.NEW
    };

    this.complaintService.createComplaint(complaint).subscribe({
      next: (response) => {
        this.submitting = false;
        this.router.navigate(['/client/success', response.protocol]);
      },
      error: () => {
        this.submitting = false;
        alert('Erro ao enviar reclamação. Tente novamente.');
      }
    });
  }

  onCancel(): void {
    this.complaintForm.reset();
    this.submitted = false;
  }
}