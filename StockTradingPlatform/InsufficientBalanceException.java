// ============================================================
//  InsufficientBalanceException.java  –  NEW Phase 4
//  Thrown when user tries to buy but cannot afford the cost.
// ============================================================

public class InsufficientBalanceException extends Exception {

    private double required;
    private double available;

    public InsufficientBalanceException(double required, double available) {
        super(String.format(
            "Insufficient balance! Required: Rs. %.2f | Available: Rs. %.2f | Short by: Rs. %.2f",
            required, available, (required - available)));
        this.required  = required;
        this.available = available;
    }

    public double getRequired()  { return required; }
    public double getAvailable() { return available; }
    public double getShortfall() { return required - available; }
}
