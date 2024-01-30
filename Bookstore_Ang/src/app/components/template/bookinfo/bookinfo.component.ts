import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-bookinfo',
  templateUrl: './bookinfo.component.html',
  styleUrls: ['./bookinfo.component.css']
})
export class BookinfoComponent {
  @Input() bookName: string = '';
  @Input() status: boolean = false;
  @Input() imagePath: string = '';
  @Input() description: string = '';
  @Input() theme: string = '';
}
