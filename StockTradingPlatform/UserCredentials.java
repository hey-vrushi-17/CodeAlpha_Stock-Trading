// ============================================================
//  UserCredentials.java  –  UPDATED Phase 7
//  Added getPassword() for DataManager to save credentials.
// ============================================================

public class UserCredentials {

    private String username;
    private String email;
    private String password;

    public UserCredentials(String username, String email, String password) {
        this.username = username;
        this.email    = email;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }  // Phase 7: for file save

    public boolean checkPassword(String input) {
        return this.password.equals(input);
    }

    public void display() {
        System.out.printf("  Username : %s%n", username);
        System.out.printf("  Email    : %s%n", email);
        System.out.printf("  Password : %s%n", "*".repeat(password.length()));
    }
}
