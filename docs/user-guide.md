# User Guide

This guide describes how to use SecureBank. It is organized by task and by user role. For installation, see [Getting Started](getting-started.md).

## Contents

1. [Roles](#1-roles)
2. [Registering a Customer Account](#2-registering-a-customer-account)
3. [Signing In and Out](#3-signing-in-and-out)
4. [The Dashboard](#4-the-dashboard)
5. [Customer Tasks](#5-customer-tasks)
6. [Administrator Tasks](#6-administrator-tasks)
7. [Statements and Reports](#7-statements-and-reports)
8. [Messages and What They Mean](#8-messages-and-what-they-mean)
9. [Frequently Asked Questions](#9-frequently-asked-questions)

---

## 1. Roles

| Role              | How obtained                                    | Scope                                                        |
| ----------------- | ----------------------------------------------- | ------------------------------------------------------------ |
| **Customer**      | Self-registration on the Register screen        | Own accounts only                                            |
| **Administrator** | Created automatically on first launch (`admin`) | System-wide: all accounts, interest posting, system reports  |

Only one administrator is provisioned by the application. There is no in-app screen for creating additional administrators.

## 2. Registering a Customer Account

1. On the sign-in screen, select **Register**.
2. Complete the form. Fields marked with an asterisk (*) are required.

   | Field            | Requirement                                                     |
   | ---------------- | --------------------------------------------------------------- |
   | Full Name *      | Not empty                                                       |
   | Email Address *  | Valid email format; must not already be registered              |
   | Phone Number     | Optional                                                        |
   | Username *       | At least 3 characters; letters, numbers, and underscores only; must be unique |
   | Password *       | At least 8 characters and at least one digit                    |
   | Confirm Password *| Must match the password                                        |
   | Terms and Conditions * | The checkbox must be selected                             |

3. Select **Register**.

On success, the system creates your user profile and **automatically opens one Checking account with a balance of ₱0.00**. You can then return to the sign-in screen and log in.

## 3. Signing In and Out

**Sign in:** Enter your username and password and select **Sign In**. An error dialog is shown if the credentials are not recognized; the password field is cleared so you can retry.

**Sign out:** Select **Logout** in the dashboard header and confirm. Signing out ends the session; the sign-in window is shown again.

## 4. The Dashboard

After signing in, the dashboard shows a header with a welcome message and your role, three summary cards, and a grid of actions.

| Card (Customer)   | Card (Administrator)  | Meaning                                                   |
| ----------------- | --------------------- | --------------------------------------------------------- |
| My Accounts       | Total Accounts        | Number of accounts you own / exist in the system          |
| Total Balance     | Total System Balance  | Sum of balances across your accounts / all accounts       |
| Transactions      | All Transactions      | Number of recorded transactions across those accounts     |

The dashboard refreshes automatically after actions that change balances or accounts.

## 5. Customer Tasks

### 5.1 Open an additional account

1. Select **Open Account**.
2. Enter the **Account Holder Name**, choose **Savings** or **Checking**, and enter the **Initial Balance** (₱).
3. Select **Create Account**. The new account number is displayed.

Rules: the initial balance must be greater than zero, and a Savings account requires a minimum opening balance of ₱100. The new account is linked to your signed-in profile. Enter your own registered full name as the account holder so that statements and reports show the correct name.

### 5.2 View my accounts

Select **My Accounts** to see a table of your accounts with account number, holder, type, balance, and interest rate.

### 5.3 Deposit money

1. Select **Deposit**.
2. Enter the **account number** (for example, `ACC1234`) and the **amount**.
3. Confirm. A success dialog summarizes the transaction.

The amount must be greater than zero and may have at most two decimal places. You can deposit only into your own accounts.

### 5.4 Withdraw money

1. Select **Withdraw**.
2. Enter the **account number** and the **amount**, then confirm.

The amount must be greater than zero, with at most two decimal places, and must not exceed the current balance. A Savings account must also retain at least ₱100 after the withdrawal. See [Business Rules](business-rules.md#4-withdrawals).

### 5.5 View account details and history

1. Select **Account Details**.
2. Enter the account number when prompted.

The details window shows the account's information and its complete transaction history. From here you can [export or print a statement](#71-account-statement). Requests for an account you do not own are refused with an *Access Denied* message.

## 6. Administrator Tasks

Administrators see the customer dashboard actions **Deposit**, **Withdraw**, and **Account Details** (usable on any account), plus the following.

### 6.1 Create an account for a customer

1. Select **Create Account**.
2. Enter the customer's full name as the **Account Holder Name**, choose the type, and enter the initial balance.

If a registered customer's full name matches the holder name (case-insensitive), the account is linked to that customer's profile automatically.

### 6.2 View all accounts

Select **All Accounts** to list every account in the system.

### 6.3 Apply interest

1. Select **Apply Interest** and confirm the prompt.
2. Interest is posted to **every** account and recorded as an `INTEREST` transaction.

Each invocation applies the account type's full annual rate once (Savings 3%, Checking 1%). Checking accounts with a zero or negative balance receive no interest. Because the operation is not date-aware, running it repeatedly compounds interest each time. See [Business Rules](business-rules.md#5-interest).

### 6.4 Generate a system report

Select **Generate Report** to open the Banking System Report, which includes an executive summary (account counts by type, total and average balance) and a detailed listing of all accounts. See [Statements and Reports](#7-statements-and-reports).

## 7. Statements and Reports

### 7.1 Account statement

In **Account Details**:

- **Export to File** opens a save dialog with a suggested name of the form `Account_Statement_<AccountNumber>_<yyyyMMdd_HHmmss>.txt` and writes a plain-text statement containing account information and the full transaction history.
- **Print** sends the same statement to a system printer using the standard print dialog.

### 7.2 System report (administrators)

In **Generate Report**:

- **Export to File** writes `BankReport_<timestamp>.txt` to the application's working directory.
- **Print Report** sends the report to a system printer.

## 8. Messages and What They Mean

| Message | Meaning and action |
| ------- | ------------------ |
| *Invalid credentials! Please try again.* | Username or password is incorrect. |
| *Username already exists. Please choose a different one.* / *Email address is already registered.* | Choose a different username, or use a different email address. |
| *Password must be at least 8 characters!* / *…contain at least one number!* | Strengthen the password. |
| *Account not found!* | The account number does not exist. Check for typos; numbers have the form `ACC` followed by four digits. |
| *You can only deposit to / withdraw from your own accounts!* | Customers may transact only on accounts they own. |
| *Insufficient balance! Available balance: ₱…* | The withdrawal exceeds the balance. |
| *Amount can only have up to 2 decimal places!* | Round the amount to centavos. |
| *Access denied! You can only view your own accounts.* | The requested account belongs to another customer. |
| *Withdrawal failed! Please try again.* | The withdrawal violated an account rule, for example the Savings minimum balance. |

## 9. Frequently Asked Questions

**Can I transfer money between accounts?**
Not in the current version. Only deposits and withdrawals are supported.

**Where is my data stored?**
In `bank_data.ser`, in the directory from which the application was launched. See [Getting Started](getting-started.md#5-data-file).

**I forgot my password. How do I recover it?**
There is no self-service recovery. An administrator cannot reset passwords from within the application either; resetting requires editing or deleting the data file, which affects all users.

**Why does my Checking account not allow an overdraft?**
The account type defines an overdraft floor of −₱500, but the withdrawal workflow rejects any amount greater than the current balance before that rule is evaluated. See [Business Rules](business-rules.md#4-withdrawals).
