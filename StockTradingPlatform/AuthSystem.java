// ============================================================
//  AuthSystem.java  –  Registration & Login Manager (Phase 2)
//  Maintains two linked maps:
//    credentialsMap : username → UserCredentials
//    userMap        : username → User (trading account)
//  Throws custom exceptions for invalid operations.
// ============================================================

import java.util.ArrayList;
import java.util.HashMap;

public class AuthSystem {

    // ── Private storage maps ──────────────────────────────────
    private HashMap<String, UserCredentials> credentialsMap;
    private HashMap<String, User>            userMap;
    private ArrayList<Stock>                 marketStocks;   // shared market

    // ── Constructor ──────────────────────────────────────────
    public AuthSystem(ArrayList<Stock> marketStocks) {
        this.credentialsMap = new HashMap<>();
        this.userMap        = new HashMap<>();
        this.marketStocks   = marketStocks;
    }

    // ── Validation helpers ────────────────────────────────────

    private boolean isValidEmail(String email) {
        // Simple check: must contain '@' and '.'
        return email.contains("@") && email.contains(".");
    }

    private boolean isValidPassword(String password) {
        // Minimum 6 characters
        return password.length() >= 6;
    }

    private boolean isValidUsername(String username) {
        // 3–20 characters, letters/digits/underscore only
        return username.matches("[a-zA-Z0-9_]{3,20}");
    }

    // ============================================================
    //  register()  –  Creates a new account
    //  Throws: DuplicateUserException, IllegalArgumentException
    // ============================================================
    public void register(String username, String email, String password,
                          java.util.Scanner scanner)
            throws DuplicateUserException {

        // ── Validate username ─────────────────────────────────
        if (!isValidUsername(username)) {
            throw new IllegalArgumentException(
                "Username must be 3–20 characters (letters, digits, underscore only).");
        }

        // ── Check for duplicate username ──────────────────────
        if (credentialsMap.containsKey(username.toLowerCase())) {
            throw new DuplicateUserException(username);
        }

        // ── Validate email ────────────────────────────────────
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException(
                "Invalid email address. Must contain '@' and '.'");
        }

        // ── Validate password ─────────────────────────────────
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException(
                "Password too short. Minimum 6 characters required.");
        }

        // ── Create credentials & user account ─────────────────
        String key = username.toLowerCase();
        UserCredentials creds = new UserCredentials(username, email, password);
        User user = new User(username, email, marketStocks, scanner);

        credentialsMap.put(key, creds);
        userMap.put(key, user);

        System.out.println("\n  ✔ Registration Successful!");
        System.out.println("  Welcome aboard, " + username + "!");
        System.out.printf("  Starting balance: Rs. %.2f%n", user.getBalance());
    }

    // ============================================================
    //  login()  –  Authenticates an existing user
    //  Returns the User object on success.
    //  Throws: InvalidCredentialsException
    // ============================================================
    public User login(String username, String password)
            throws InvalidCredentialsException {

        String key = username.toLowerCase();

        // ── Username must exist ───────────────────────────────
        if (!credentialsMap.containsKey(key)) {
            throw new InvalidCredentialsException();
        }

        // ── Password must match ───────────────────────────────
        UserCredentials creds = credentialsMap.get(key);
        if (!creds.checkPassword(password)) {
            throw new InvalidCredentialsException();
        }

        return userMap.get(key);
    }

    // ── Total registered users ────────────────────────────────
    public int getTotalUsers() {
        return credentialsMap.size();
    }
}
