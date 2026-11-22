// ============================================================
//  UserCredentials.java  –  Stores login details (Phase 2)
//  Encapsulation: all fields private, accessed via getters only.
// ============================================================

public class UserCredentials {

    // ── Private fields (Encapsulation) ───────────────────────
    private String username;
    private String email;
    private String password;   // stored as plain text (Phase 2 scope)

    // ── Constructor ──────────────────────────────────────────
    public UserCredentials(String username, String email, String password) {
        this.username = username;
        this.email    = email;
        this.password = password;
    }

    // ── Getters ──────────────────────────────────────────────
    public String getUsername() { return username; }
    public String getEmail()    { return email; }

    // ── Password validation (never expose raw password) ──────
    public boolean checkPassword(String input) {
        return this.password.equals(input);
    }

    // ── Display (masks password for security) ────────────────
    public void display() {
        System.out.printf("  Username : %s%n", username);
        System.out.printf("  Email    : %s%n", email);
        System.out.printf("  Password : %s%n", "*".repeat(password.length()));
    }
}
