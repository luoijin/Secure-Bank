# Business Rules

This document is the reference for the rules the application enforces. Each rule has an identifier (`BR-nn`) so that it can be cited in tests, reviews, and change requests. Section 8 lists places where the implemented behavior differs from what the account definitions suggest.

Sources: `src/model/*`, `src/controller/BankController.java`, and `src/view/*`.

## Contents

1. [Users and Roles](#1-users-and-roles)
2. [Registration and Validation](#2-registration-and-validation)
3. [Accounts](#3-accounts)
4. [Withdrawals](#4-withdrawals)
5. [Interest](#5-interest)
6. [Access Control](#6-access-control)
7. [Transaction Records](#7-transaction-records)
8. [Implementation Notes and Inconsistencies](#8-implementation-notes-and-inconsistencies)

---

## 1. Users and Roles

| ID    | Rule |
| ----- | ---- |
| BR-01 | Every user has one role: `CUSTOMER` or `ADMIN`. A missing role defaults to `CUSTOMER`. |
| BR-02 | When the system starts with no users, one administrator is created: username `admin`, password `admin123`, email `admin@bank.com`. |
| BR-03 | Self-registration always creates a `CUSTOMER`. No in-app function creates or promotes administrators. |
| BR-04 | Usernames are unique and case-sensitive (`Alice` and `alice` are distinct users). Sign-in matches the username exactly. |

## 2. Registration and Validation

| ID    | Field / Step | Rule |
| ----- | ------------ | ---- |
| BR-10 | Full name    | Required. |
| BR-11 | Email        | Required; must match `^[A-Za-z0-9+_.-]+@(.+)$`; must be unique. Uniqueness is checked case-insensitively after trimming. |
| BR-12 | Phone        | Optional. |
| BR-13 | Username     | Required; at least 3 characters; only letters, digits, and underscores; must be unique. |
| BR-14 | Password     | Required; at least 8 characters; at least one digit; must equal the confirmation field. |
| BR-15 | Terms        | The Terms and Conditions checkbox must be selected. |
| BR-16 | On success   | A **Checking** account with a ₱0.00 balance is created automatically, with the customer's full name as holder, and linked to the customer. |

## 3. Accounts

### 3.1 Account types

| Attribute            | Savings                    | Checking                      |
| -------------------- | -------------------------- | ----------------------------- |
| Type code            | `SAVINGS`                  | `CHECKING`                    |
| Annual interest rate | 3% (0.03)                  | 1% (0.01)                     |
| Minimum balance      | ₱100 (after any withdrawal)| None                          |
| Overdraft floor      | Not applicable             | −₱500 (defined; see [§8](#8-implementation-notes-and-inconsistencies)) |
| Minimum opening balance | ₱100                    | None beyond "greater than zero" in the UI |

### 3.2 Account creation

| ID    | Rule |
| ----- | ---- |
| BR-20 | Account holder name is required (non-blank). |
| BR-21 | The controller rejects a negative initial balance. The account-creation form additionally requires the balance to be **greater than zero**. Only registration creates a zero-balance account. |
| BR-22 | A Savings account requires an initial balance of at least ₱100. |
| BR-23 | Account numbers have the form `ACC` followed by four digits (`ACC0000` to `ACC9999`), chosen at random and re-drawn until unique. |
| BR-24 | If the creator is not an administrator, the new account is linked to the creator. |
| BR-25 | Independently of BR-24, the account is linked to the first registered user whose full name equals the holder name, ignoring case. |
| BR-26 | Every new account is opened with an `INITIAL_DEPOSIT` transaction for the initial balance. |

## 4. Withdrawals

Deposits and withdrawals are entered on the same form and share input validation.

| ID    | Rule |
| ----- | ---- |
| BR-30 | The account number and amount are required. |
| BR-31 | The amount must be a number greater than zero. |
| BR-32 | The amount may have at most two decimal places. |
| BR-33 | The account must exist. |
| BR-34 | A customer may transact only on accounts they own (see [§6](#6-access-control)). Administrators may transact on any account. |
| BR-35 | **Withdrawals only:** the amount must not exceed the current balance. This check is applied in both the view and the controller, before account-type rules. |
| BR-36 | **Withdrawals only:** the account type's `canWithdraw` rule must hold. Savings: `balance − amount ≥ 100`. Checking: `balance − amount ≥ −500`. |
| BR-37 | A successful deposit or withdrawal is recorded as a transaction and persisted immediately. |

**Effective outcome.** Because BR-35 always applies first, the balance after a successful withdrawal is never below zero. Consequently, for Checking accounts the −₱500 overdraft floor in BR-36 is never the deciding rule. For Savings accounts, BR-36 is decisive: a Savings account with a balance of ₱100 or less cannot be debited at all.

## 5. Interest

| ID    | Rule |
| ----- | ---- |
| BR-40 | Interest is posted only when an administrator selects **Apply Interest** and confirms. There is no scheduled or automatic posting. |
| BR-41 | The operation applies to **all** accounts in one pass and saves once at the end. |
| BR-42 | Each account receives `balance × annual rate` once per invocation, added to the balance and recorded as an `INTEREST` transaction. |
| BR-43 | Savings accounts always receive interest. Checking accounts receive interest only when their balance is greater than zero. |
| BR-44 | The operation has no date or period awareness. Repeated invocations compound; nothing prevents interest from being applied more than once in a period. |

`Account.calculateMonthlyInterest()` (annual rate ÷ 12) is implemented for both types but is not called by the application.

## 6. Access Control

| ID    | Rule |
| ----- | ---- |
| BR-50 | Administrators can view all accounts, create accounts, apply interest, generate reports, and use Deposit, Withdraw, and Account Details on any account. |
| BR-51 | Customers can view **My Accounts** and use Deposit, Withdraw, and Account Details on their own accounts. |
| BR-52 | A customer *owns* an account if the account number is in the customer's account list **or** the account holder name equals the customer's full name, ignoring case. |
| BR-53 | *My Accounts* lists accounts from the customer's account list. Only if that list is empty does it fall back to matching by holder name, and any matches found are added to the list. |
| BR-54 | Dashboard aggregates (count, balance, transactions) follow the same list-then-name-fallback approach for customers and cover the whole system for administrators. |

## 7. Transaction Records

| Type              | Amount sign | Created by                                                |
| ----------------- | ----------- | --------------------------------------------------------- |
| `INITIAL_DEPOSIT` | Positive (or zero) | Account creation                                     |
| `DEPOSIT`         | Positive    | Successful deposit                                        |
| `WITHDRAWAL`      | Negative    | Successful withdrawal                                     |
| `INTEREST`        | Positive    | Interest posting                                          |

Each record stores the type, amount, resulting balance, and creation timestamp. Records have no setters and are never removed by the application. They do not store who initiated the transaction.

## 8. Implementation Notes and Inconsistencies

The behaviors below follow directly from the current code. They are recorded so that testers and maintainers can distinguish intended rules from incidental behavior.

| # | Observation | Effect |
| - | ----------- | ------ |
| 1 | The Checking overdraft floor (−₱500) is unreachable because withdrawals exceeding the balance are rejected first (BR-35). | Checking accounts cannot be overdrawn. If overdraft is intended, BR-35 must be removed or made type-aware. |
| 2 | The account-creation form warns that a Savings account under ₱100 requires a minimum and asks "Proceed anyway?", but the controller then rejects the request (BR-22). | The user can confirm and trigger an unhandled `IllegalArgumentException` instead of a friendly error. |
| 3 | Interest is applied at the full annual rate per invocation (BR-42, BR-44). | If the operation is meant to run monthly, accounts will accrue 12× the intended interest. `calculateMonthlyInterest()` suggests a monthly design intent. |
| 4 | Ownership can be established by matching the holder name to a customer's full name (BR-52). Full names are not unique. | Two customers with the same full name can access each other's accounts; a customer can also create an account under another person's name (BR-25 then links it to that person). |
| 5 | Account numbers are limited to 10,000 values (BR-23). | Account creation would loop indefinitely once all numbers are in use. Numbers are also guessable. |
| 6 | Amounts are stored as `double`. | Binary floating-point rounding can produce off-by-a-centavo errors over many operations. |
| 7 | Amounts are parsed with `Double.parseDouble`, and the two-decimal check (BR-32) only inspects text containing a decimal point. Exponent notation (for example `1e2`) therefore bypasses BR-32, and by code inspection a value such as `Infinity` passes the "greater than zero" test. | Minor input-validation gap; a deposit or opening balance of `Infinity` could corrupt a balance. Amounts should be parsed as `BigDecimal` and range-checked. |
| 8 | Passwords are stored and compared in plain text (BR-14 governs only length and digit content). | See the [Security Assessment](security.md). |
