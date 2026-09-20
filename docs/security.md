# Security Assessment

This document records the security posture of SecureBank as implemented in the current source code, lists the findings identified during code review, and proposes a prioritized remediation plan.

## Contents

1. [Scope and Summary](#1-scope-and-summary)
2. [Controls Currently in Place](#2-controls-currently-in-place)
3. [Findings Overview](#3-findings-overview)
4. [Finding Details](#4-finding-details)
5. [Prioritized Remediation Plan](#5-prioritized-remediation-plan)
6. [Guidance for Using the Current Version](#6-guidance-for-using-the-current-version)

---

## 1. Scope and Summary

**Scope.** This assessment is a static review of the Java source in `src/`, the committed data file `bank_data.ser`, and the repository contents. The application was not penetration tested, and no dynamic analysis was performed.

**Summary.** SecureBank implements the *structure* of a secure system (roles, ownership checks, input validation, confirmation prompts), but its storage and authentication mechanisms are not designed for confidential data. The most important gaps are plain-text passwords, published default administrator credentials, and an unprotected serialized data file.

**Severity scale.** Ratings assume the application would hold real customer data. For an isolated classroom demonstration with fictitious data, the practical risk is lower, as described in [Section 6](#6-guidance-for-using-the-current-version).

| Rating | Meaning |
| ------ | ------- |
| High   | Directly exposes credentials or allows tampering with financial data. |
| Medium | Weakens confidentiality, integrity, or accountability under realistic conditions. |
| Low    | Hardening or maintainability issue with limited direct exposure. |

## 2. Controls Currently in Place

| Control | Where | Effect |
| ------- | ----- | ------ |
| Role-based feature exposure | `MainView` | Customers and administrators see different actions. |
| Ownership checks on transactions and details | `BankController`, `TransactionView`, `AccountDetailsView` | Customers are refused access to accounts they do not own (subject to SEC-05). |
| Password field masking | `LoginView`, `RegistrationView` (`JPasswordField`) | Passwords are not shown on screen. |
| Password composition rules | `RegistrationView` | Minimum 8 characters with at least one digit. |
| Username and email validation and uniqueness | `RegistrationView`, `BankController` | Restricts the username character set; prevents duplicate usernames and emails. |
| Defensive copy of transaction list | `Account.getTransactions()` | Callers cannot alter the ledger through the returned list. |
| Append-only transaction records | `Transaction`, `Account` | `Transaction` fields are `final` and no operation removes or edits an entry. |
| Confirmation prompts | `MainView` | Logout and interest posting require confirmation. |
| Amount validation | `TransactionView`, `Account` | Positive amounts only; the model rejects non-positive deposits. |

## 3. Findings Overview

| ID | Title | Rating | Category |
| -- | ----- | ------ | -------- |
| SEC-01 | Passwords stored and compared in plain text | High | Authentication |
| SEC-02 | Default administrator credentials are hard-coded and displayed | High | Authentication |
| SEC-03 | Unencrypted, unsigned Java-serialized data file | High | Data protection |
| SEC-04 | No brute-force protection or session timeout | Medium | Authentication |
| SEC-05 | Account ownership partly determined by holder-name match | Medium | Authorization |
| SEC-06 | Monetary values use `double`; input parsing is permissive | Medium | Data integrity |
| SEC-07 | Non-atomic writes and destructive load-failure recovery | Medium | Data integrity |
| SEC-08 | No audit trail | Medium | Accountability |
| SEC-09 | Sensitive data and build artifacts committed to version control | Medium | Configuration |
| SEC-10 | Short, random, enumerable account numbers | Low | Authorization |
| SEC-11 | Unsynchronized shared state | Low | Reliability |
| SEC-12 | Unhandled exceptions and stack-trace output | Low | Error handling |
| SEC-13 | Unused third-party library bundled | Low | Supply chain |

## 4. Finding Details

### SEC-01: Passwords stored and compared in plain text (High)

`User` holds the password as a `String`, `BankController.authenticate` compares it with `equals`, and the value is written unchanged to `bank_data.ser`. Anyone who can read the data file, a backup, or the repository can recover every password, including the administrator's.

**Recommendation.** Store only a salted, slow, adaptive hash. `PBKDF2WithHmacSHA256` is available in the JDK (`SecretKeyFactory`); bcrypt or Argon2 are preferable if a library is acceptable. Use a unique random salt per user, a constant-time comparison, and migrate existing plain-text entries by hashing them on the next successful sign-in.

### SEC-02: Default administrator credentials are hard-coded and displayed (High)

`BankController.initializeDefaultUsers` creates `admin` / `admin123`, and `LoginView` prints these credentials in a card on the sign-in screen. Any person with access to the application can sign in with full administrative rights.

**Recommendation.** Remove the credentials card. On first run, prompt the operator to create the administrator account with a strong password, or generate a one-time random password and force a change at first sign-in.

### SEC-03: Unencrypted, unsigned Java-serialized data file (High)

All users, balances, and transactions are stored in `bank_data.ser` using Java serialization, without encryption, integrity protection, or access control beyond file-system permissions.

- **Confidentiality:** the file contents are readable with common tools.
- **Integrity:** the file can be replaced or modified, for example to change balances or roles, and the application will load it without verification.
- **Deserialization risk:** `ObjectInputStream` is used without an `ObjectInputFilter`. Deserializing an attacker-supplied file is a recognized vulnerability class in Java.
- **Evolvability:** the format is bound to the class definitions.

**Recommendation.** Replace serialization with a transactional store. The module descriptor already requires `java.sql`, so an embedded database such as SQLite or H2 accessed through JDBC is a natural fit. Until then, at minimum install a restrictive `ObjectInputFilter` and restrict file permissions on `bank_data.ser`.

### SEC-04: No brute-force protection or session timeout (Medium)

Sign-in attempts are unlimited and unthrottled. Once signed in, a session persists until logout; there is no idle timeout. The session lives in the `BankController` singleton (`currentUser`).

**Recommendation.** Add progressive delay or temporary lockout after repeated failures, log failed attempts, and sign users out after a period of inactivity.

### SEC-05: Account ownership partly determined by holder-name match (Medium)

A customer is treated as the owner of any account whose holder name equals their full name, ignoring case (`BankController.hasAccessToAccount`, mirrored in `TransactionView` and `AccountDetailsView`). Full names are not unique and are user-supplied. Consequences:

- Two customers who share a full name can view and transact on each other's accounts.
- A customer can open an account with another person's name as holder; the controller then links it to any user with that full name.
- Fallback logic in the dashboard and *My Accounts* silently attaches name-matched accounts to a user's account list.

**Recommendation.** Use account-number links (or a numeric user identifier on the account) as the single source of truth for ownership. Remove name-based matching, and centralize the authorization check in the controller so views cannot diverge from it.

### SEC-06: Monetary values use `double`; input parsing is permissive (Medium)

Balances and amounts are `double`, which cannot represent most decimal fractions exactly. Interest calculations amplify rounding drift. Input is parsed with `Double.parseDouble`, which accepts forms such as exponent notation and, by code inspection, `Infinity`; the two-decimal check applies only to text containing a decimal point. There is no upper bound on amounts.

**Recommendation.** Represent money as `BigDecimal` with an explicit scale and rounding mode (or as integer centavos). Parse with `new BigDecimal(text)`, enforce scale and a maximum, and round interest deliberately.

### SEC-07: Non-atomic writes and destructive load-failure recovery (Medium)

`saveData` opens `FileOutputStream` directly on `bank_data.ser`, which truncates the file before writing. A crash or power loss during a save can leave a partial file. On a failed load, the controller resets to empty collections, re-creates the default administrator, and saves, overwriting the unreadable file.

**Recommendation.** Write to a temporary file and atomically move it into place, keep a rolling backup, and on a load failure stop with an error instead of overwriting.

### SEC-08: No audit trail (Medium)

Transactions record type, amount, resulting balance, and time, but not the acting user, and administrative actions (interest posting, account creation, report generation, sign-ins) are not logged. Console output is limited to a few status messages.

**Recommendation.** Add an append-only audit log with timestamp, actor, action, target, and outcome. Extend `Transaction` with an initiator and a unique reference.

### SEC-09: Sensitive data and build artifacts committed to version control (Medium)

`bank_data.ser` is tracked in Git and contains sample users with plain-text passwords. The compiled `bin/` directory, `SecureBank.jar`, and three copies of the diagrams are also tracked. Committed data cannot be reliably removed from history after publication.

**Recommendation.** Add a `.gitignore` covering `bank_data.ser`, `bin/`, `out/`, `*.jar`, and generated reports (`BankReport_*.txt`); publish releases through GitHub Releases rather than the source tree; rotate any password that has been committed.

### SEC-10: Short, random, enumerable account numbers (Low)

Account numbers are `ACC` plus four random digits produced by `Math.random()`. Only 10,000 values exist, they are easy to enumerate, and creation would loop indefinitely once the space is exhausted.

**Recommendation.** Use a longer identifier, generate it with `SecureRandom` or a sequence, and include a check digit. Do not rely on secrecy of the number for access control.

### SEC-11: Unsynchronized shared state (Low)

The controller's maps are plain `HashMap` instances accessed from the Event Dispatch Thread, while the shutdown hook saves from another thread. Concurrent modification during shutdown could produce an inconsistent snapshot or an exception.

**Recommendation.** Confine state access to a single thread or synchronize access, and consider explicit save-on-exit rather than a hook that races with the UI.

### SEC-12: Unhandled exceptions and stack-trace output (Low)

`BankController.createAccount` throws `IllegalArgumentException` for a Savings opening balance below ₱100, but `CreateAccountView` catches only `NumberFormatException` (see [Business Rules, §8](business-rules.md#8-implementation-notes-and-inconsistencies)). Persistence failures are printed with `printStackTrace()` and not reported to the user, so a failed save can go unnoticed.

**Recommendation.** Handle expected exceptions in the view, surface persistence failures to the user, and use a logging framework instead of `printStackTrace()`.

### SEC-13: Unused third-party library bundled (Low)

`lib/itextpdf-5.5.13.3.jar` is present and referenced by the Eclipse classpath, but no source file imports it. Unused dependencies increase the supply-chain surface without providing value.

**Recommendation.** Remove it. If PDF export is planned, review the library's current licensing and maintenance status first.

## 5. Prioritized Remediation Plan

This plan is a recommendation for future work; none of it is implemented in the current release.

### Priority 1: Before any use with real data

| Action | Addresses |
| ------ | --------- |
| Hash passwords with a salted, adaptive algorithm and migrate existing entries | SEC-01 |
| Remove published default credentials; require first-run administrator setup | SEC-02 |
| Replace serialized file storage with a transactional database | SEC-03, SEC-07 |
| Move monetary values to `BigDecimal` and tighten amount validation | SEC-06 |
| Base ownership solely on account links; centralize authorization in the controller | SEC-05 |

### Priority 2: Hardening

| Action | Addresses |
| ------ | --------- |
| Failed-login throttling and idle-session timeout | SEC-04 |
| Audit log and transaction initiator | SEC-08 |
| Add `.gitignore`; remove data and binaries from version control | SEC-09 |
| Longer, securely generated account numbers | SEC-10 |
| Handle expected exceptions and report persistence failures | SEC-12 |

### Priority 3: Engineering quality

| Action | Addresses |
| ------ | --------- |
| Introduce a build tool (Maven or Gradle) with dependency management | SEC-13 |
| Add unit tests for the model and controller rules in [Business Rules](business-rules.md) | All |
| Separate persistence from the controller; remove singleton access from views | SEC-11, [Architecture §8](architecture.md#8-design-observations) |
| Remove unused code and the unused library | SEC-13 |

## 6. Guidance for Using the Current Version

Until the Priority 1 items are complete:

- Use the application only with **fictitious data**. Do not enter real names, contact details, or financial information.
- Do not reuse a password from any other system when registering.
- Keep `bank_data.ser` out of shared folders and public repositories, and back it up before each session that matters.
- Treat the `admin` / `admin123` credentials as public knowledge.
- Do not deploy the application to a shared or multi-user environment.
