import java.io.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

public class AccountGenerator {

    public static void main(String[] args) {
        List<String> firstNames = new ArrayList<>();
        List<String> lastNames = new ArrayList<>();
        Random random = new Random();
        HashSet<Integer> usedIds = new HashSet<>(); // To ensure uniqueness

        // Read first names from file
        try (BufferedReader reader = new BufferedReader(new FileReader("first_names.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                firstNames.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading first names file: " + e.getMessage());
            return;
        }

        // Read last names from file
        try (BufferedReader reader = new BufferedReader(new FileReader("last_names.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lastNames.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading last names file: " + e.getMessage());
            return;
        }

        // Ensure we have enough names
        if (firstNames.size() < 184 || lastNames.size() < 185) {
            System.out.println("Insufficient names in one or both files!");
            return;
        }

        List<CheckingAccount> accounts = new ArrayList<>();

        // Generate accounts
        for (int i = 0; i < 34040; i++) {
            String firstName = firstNames.get(random.nextInt(firstNames.size()));
            String lastName = lastNames.get(random.nextInt(lastNames.size()));
            double balance = Math.random() * 50000; // Random balance from 0 to 50000

            int accountId;
            do {
                accountId = random.nextInt(900000) + 100000; // Generate a random 6-digit ID
            } while (usedIds.contains(accountId)); // Ensure it's unique
            usedIds.add(accountId);

            CheckingAccount account = new CheckingAccount(0.05); // Create the account object here
            account.setFirstName(firstName);
            account.setLastName(lastName);
            account.deposit(balance);
            account.setId(accountId);

            accounts.add(account);
        }

        // Write accounts to file
        try (PrintWriter writer = new PrintWriter("generated_accounts.txt", "UTF-8")) {
            for (CheckingAccount account : accounts) {
                writer.printf("%d,%s,%s,%.2f%n",
                        account.getId(),
                        account.getFirstName(),
                        account.getLastName(),
                        account.getBalance());
            }
            System.out.println("Generated and saved " + accounts.size() + " accounts to generated_accounts.txt.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static class CheckingAccount {
        private String firstName;
        private String lastName;
        private double balance;
        private double interestRate;
        private int id; // The account number

        public CheckingAccount(double interestRate) {
            this.interestRate = interestRate;
            this.balance = 0.0;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void deposit(double amount) {
            this.balance += amount;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public double getBalance() {
            return balance;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }
    }
}