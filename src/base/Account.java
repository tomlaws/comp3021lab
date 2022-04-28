package base;

class Account {
    static int count;
    private int balance = 0;

    {
        count++;
    }

    public synchronized int getBalance() {
        return balance;
    }

    public synchronized void setBalance(int amount) {
        balance = amount;
    }
}
