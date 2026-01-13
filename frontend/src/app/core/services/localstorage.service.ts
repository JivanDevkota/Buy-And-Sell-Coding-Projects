import {Injectable} from "@angular/core";

export interface User {
  userId: number
  username: string
  email: string
  roles: string[]
}

@Injectable({
  providedIn: 'root',
})

export class LocalstorageService {
  private static TOKEN_KEY = 'token';
  private static REFRESH_TOKEN_KEY = 'refresh_token';
  private static USER_KEY = 'user';

  static saveToken(token: string) {
    if (token) {
      localStorage.setItem(this.TOKEN_KEY, token);
    }
  }

  static getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  static saveRefreshToken(token: string) {
    if (token) {
      localStorage.setItem(this.REFRESH_TOKEN_KEY, token);
    }
  }

  static getRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  static saveUser(user: User): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  static getUser(): User | null {
    const user = localStorage.getItem(this.USER_KEY);
    return user ? JSON.parse(user) : null;
  }

  static getUserId(): number | null {
    return this.getUser()?.userId ?? null;
  }

  static getUserName(): string | null {
    return this.getUser()?.username ?? null;
  }

  static getRoles(): string[] {
    return this.getUser()?.roles ?? [];
  }

  //role check
  static hasRole(role: string): boolean {
    return this.getRoles().includes(role);
  }

  static isSellerLoggedIn(): Boolean {
    return !! this.getToken() && this.hasRole("ROLE_SELLER")
  }

  static isBuyerLoggedIn(): Boolean {
    return !! this.getToken() && this.hasRole("ROLE_BUYER")
  }

  static signOut() {
    localStorage.removeItem(this.USER_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
  }

}
