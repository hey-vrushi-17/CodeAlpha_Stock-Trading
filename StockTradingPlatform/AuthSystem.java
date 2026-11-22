// ============================================================
//  AuthSystem.java  –  UPDATED Phase 7
//  • register() saves credentials to file via DataManager
//  • loadFromFile() restores all users on startup
// ============================================================

import java.util.ArrayList;
import java.util.HashMap;

public class AuthSystem {

    private HashMap<String, UserCredentials> credentialsMap;
    private HashMap<String, User>            userMap;
    private ArrayList<Stock>                 marketStocks;

    public AuthSystem(ArrayList<Stock> marketStocks) {
        this.credentialsMap = new HashMap<>();
        this.userMap        = new HashMap<>();
        this.marketStocks   = marketStocks;
    }

    // ── Validation ────────────────────────────────────────────
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }
    private boolean isValidUsername(String username) {
        return username.matches("[a-zA-Z0-9_]{3,20}");
    }

    // ── Register (saves to file) ──────────────────────────────
    public void register(String username, String email, String password,
                          java.util.Scanner scanner)
            throws DuplicateUserException {

        if (!isValidUsername(username))
            throw new IllegalArgumentException(
                "Username must be 3–20 characters (letters, digits, underscore).");
        if (credentialsMap.containsKey(username.toLowerCase()))
            throw new DuplicateUserException(username);
        if (!isValidEmail(email))
            throw new IllegalArgumentException("Invalid email. Must contain '@' and '.'");
        if (!isValidPassword(password))
            throw new IllegalArgumentException("Password too short. Minimum 6 characters.");

        String key = username.toLowerCase();
        UserCredentials creds = new UserCredentials(username, email, password);
        User user = new User(username, email, marketStocks, scanner);

        credentialsMap.put(key, creds);
        userMap.put(key, user);

        // ── Phase 7: Save to file ────────────────────────────
        DataManager.saveUser(username, email, password);
        DataManager.saveBalance(username, user.getBalance());

        System.out.println("\n  ✔ Registration Successful!");
        System.out.println("  Welcome aboard, " + username + "!");
        System.out.printf("  Starting balance: Rs. %.2f%n", user.getBalance());
        System.out.println("  [SAVED] Account saved to disk.");
    }

    // ── Login ─────────────────────────────────────────────────
    public User login(String username, String password)
            throws InvalidCredentialsException {

        String key = username.toLowerCase();
        if (!credentialsMap.containsKey(key))
            throw new InvalidCredentialsException();

        UserCredentials creds = credentialsMap.get(key);
        if (!creds.checkPassword(password))
            throw new InvalidCredentialsException();

        return userMap.get(key);
    }

    // ── Phase 7: Load all saved users from file on startup ────
    public int loadFromFile(java.util.Scanner scanner) {
        ArrayList<String[]> savedUsers = DataManager.loadAllUsers();
        int count = 0;

        for (String[] u : savedUsers) {
            String uname = u[0], email = u[1], pass = u[2];
            String key = uname.toLowerCase();

            if (!credentialsMap.containsKey(key)) {
                UserCredentials creds = new UserCredentials(uname, email, pass);
                User user = new User(uname, email, marketStocks, scanner);

                // Load saved balance, portfolio, and history
                user.loadSavedData();

                credentialsMap.put(key, creds);
                userMap.put(key, user);
                count++;
            }
        }
        return count;
    }

    public int getTotalUsers() { return credentialsMap.size(); }
}
