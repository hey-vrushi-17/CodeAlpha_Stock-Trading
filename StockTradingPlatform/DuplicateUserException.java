// ============================================================
//  DuplicateUserException.java  –  Custom Exception (Phase 2)
//  Thrown when a username already exists during registration.
// ============================================================

public class DuplicateUserException extends Exception {

    public DuplicateUserException(String username) {
        super("Registration failed: Username '" + username + "' is already taken. Please choose another.");
    }
}
