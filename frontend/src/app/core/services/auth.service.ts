import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {RegisterRequest} from "../model/RegisterRequest";
import {catchError, map, Observable, of} from "rxjs";
import {LoginRequest} from "../model/LoginRequest";
import {LocalstorageService} from "./localstorage.service";

const baseUrl = "http://localhost:8080/api";

@Injectable({
  providedIn: 'root',
})

export class AuthService {

  constructor(private http: HttpClient) {
  }

  registerUser(registerRequest: RegisterRequest): Observable<any> {
    return this.http.post(baseUrl + "/register", registerRequest);
  }

  loginUser(loginRequest: LoginRequest): Observable<boolean> {
    return this.http
      .post<any>(`${baseUrl}/login`, loginRequest)
      .pipe(
        map(response => {
          LocalstorageService.saveToken(response.accessToken);
          LocalstorageService.saveRefreshToken(response.refreshToken);

          // ✅ build User object from response
          LocalstorageService.saveUser({
            userId: response.userId,
            username: response.username,
            email: response.email,
            roles: response.roles
          });

          return true;
        }),
        catchError(() => of(false))
      );
  }

  refreshToken(): Observable<any> {
    return this.http.post(`${baseUrl}/refresh`, {
      refreshToken: LocalstorageService.getRefreshToken()
    });
  }

}
