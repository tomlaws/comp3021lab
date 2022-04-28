package base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountTest {
    public static void usage1() {
        new Account();
    }

    public static void usage2(Account acc, int amount) {
        acc.setBalance(acc.getBalance()+amount);
    }

    public static synchronized void usage3 (Account acc) {
        int id = acc.count;
        System.out.println(id+" = "+acc.count);
    }

    public static void main(String s[]) {
        ExecutorService executor = Executors.newCachedThreadPool();

        Account account = new Account();
        // Create and launch 100 threads
        for (int i = 0; i < 100; i++) {
            executor.execute(()->{usage2(account,1);});
            executor.execute(AccountTest::usage1);
            executor.execute(()->{usage3(account);});
        }
        executor.shutdown();
        // Wait until all tasks are finished
        while (!executor.isTerminated()) {
        }

        System.out.println("What is balance ? " + account.getBalance());
        System.out.println("How many ? " + account.count);
    }
}


