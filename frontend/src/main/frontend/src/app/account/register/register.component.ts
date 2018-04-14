import {Component, OnInit} from "@angular/core";
import {HttpErrorResponse} from "@angular/common/http";
import {Account} from "../account";
import {AccountService} from "../account.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html'
})

export class RegisterComponent implements OnInit {
  confirmPassword: string;
  doNotMatch: string;
  success: boolean;
  account: Account;

  constructor(private accountService: AccountService) {
  }

  ngOnInit() {
    this.success = false;
    this.account = new Account();
  }

  register() {
    if (this.account.password !== this.confirmPassword) {
      this.doNotMatch = 'ERROR';
    } else {
      this.doNotMatch = null;
      this.accountService.save(this.account).subscribe(
        () => {
          this.success = true;
        },
        response => this.processError(response)
      );
    }
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
  }
}
