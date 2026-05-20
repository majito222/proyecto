import { Component } from '@angular/core';
import { Card } from 'primeng/card';
import { Tag } from 'primeng/tag';

@Component({
  selector: 'app-admin',
  imports: [Card, Tag],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class Admin {}
