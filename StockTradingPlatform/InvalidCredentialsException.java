// ============================================================
//  InvalidCredentialsException.java  –  Custom Exception (Phase 2)
//  Thrown when username or password is wrong during login.
// ============================================================

public class InvalidCredentialsException extends Exception {

    public InvalidCredentialsException() {
        super("Login failed: Invalid username or password. Please try again.");
    }
}
