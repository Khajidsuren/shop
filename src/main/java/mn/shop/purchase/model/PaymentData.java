package mn.shop.purchase.model;

import java.util.List;

public class PaymentData {
    private int count;
    private double paidAmount;
    private List<Payment> rows;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public List<Payment> getRows() {
        return rows;
    }

    public void setRows(List<Payment> rows) {
        this.rows = rows;
    }
}
