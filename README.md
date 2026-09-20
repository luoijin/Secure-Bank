<div align="center">

# SecureBank

**A role-based desktop banking application built with Java Swing.**

![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk&logoColor=white)
![UI](https://img.shields.io/badge/UI-Swing-blue)
![Architecture](https://img.shields.io/badge/Architecture-MVC-informational)
![Storage](https://img.shields.io/badge/Storage-Java%20Serialization-lightgrey)

</div>

---

## Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Technology Stack](#technology-stack)
4. [Quick Start](#quick-start)
5. [Default Credentials](#default-credentials)
6. [Project Structure](#project-structure)
7. [Documentation](#documentation)
8. [Known Limitations](#known-limitations)
9. [Repository and License](#repository-and-license)

---

## Overview

SecureBank is a standalone desktop application that simulates the core operations of a retail bank: customer registration, account management, deposits, withdrawals, interest posting, transaction history, and administrative reporting. Amounts are denominated in Philippine pesos (₱).

The application follows the Model–View–Controller (MVC) pattern. A single `BankController` mediates between the Swing views and the domain model, and persists all state to a local file, so no database server or network connection is required.

> **Important:** SecureBank is a demonstration and learning project. In its current form it is **not suitable for production use or for handling real financial data**. See [Known Limitations](#known-limitations) and the [Security Assessment](docs/security.md) for details.

## Features

### Customers

- Self-service registration with input validation (a checking account is opened automatically)
- Sign in and view a personal dashboard with account, balance, and transaction summaries
- Open additional Savings or Checking accounts
- Deposit into and withdraw from owned accounts
- View account details and full transaction history
- Export or print an account statement

### Administrators

- System-wide dashboard: total accounts, total system balance, and total transactions
- Create accounts on behalf of customers and browse all accounts
- Apply interest to every account in a single, confirmed operation
- Generate, export, and print a system report
- View details of, and transact on, any account

### Account Types

| Type     | Annual Interest | Withdrawal Rule                                         |
| -------- | --------------- | ------------------------------------------------------- |
| Savings  | 3%              | Balance must remain at or above ₱100 after withdrawal   |
| Checking | 1%              | Defined overdraft floor of −₱500 (see business rules)   |

Full details are in [Business Rules](docs/business-rules.md).

## Technology Stack

| Component          | Technology                                                         |
| ------------------ | ------------------------------------------------------------------ |
| Language / runtime | Java 25 (class files are compiled for Java 25)                     |
| User interface     | Java Swing (`java.desktop` module)                                 |
| Architecture       | MVC, with a singleton controller                                   |
| Persistence        | Java object serialization to `bank_data.ser`                       |
| IDE configuration  | Eclipse (`.project`, `.classpath`, `.settings/`)                   |
| Diagrams           | UML class, sequence, activity, and use-case diagrams (`diagrams/`) |

## Quick Start

### Prerequisites

- A Java runtime or JDK **version 25**. Older runtimes fail with `UnsupportedClassVersionError`.

### Run the prebuilt application

From the repository root:

```bash
java -jar SecureBank.jar
```

Run the command from the repository root so that the application finds and updates `bank_data.ser` in the working directory.

### Build from source

```bash
javac -d out $(find src -name "*.java")
java -cp out main.BankingSystemApp
```

Detailed instructions for Windows, Eclipse, and repackaging the JAR are in [Getting Started](docs/getting-started.md).

## Default Credentials

On first launch, when no user data exists, the application creates a default administrator:

| Username | Password   | Role          |
| -------- | ---------- | ------------- |
| `admin`  | `admin123` | Administrator |

These credentials are also displayed on the login screen. Treat them as demonstration credentials only.

## Project Structure

```text
Secure-Bank/
├── README.md                  Project overview (this file)
├── SecureBank.jar             Prebuilt executable JAR (Main-Class: main.BankingSystemApp)
├── bank_data.ser              Serialized application data (created and updated at runtime)
├── docs/                      Project documentation
├── diagrams/                  UML diagrams and program flow chart (PNG)
├── lib/                       iText PDF library (bundled; not currently referenced by the code)
├── src/
│   ├── main/                  Application entry point
│   │   └── BankingSystemApp.java
│   ├── controller/            Application logic and persistence
│   │   └── BankController.java
│   ├── model/                 Domain classes
│   │   ├── Account.java            (abstract)
│   │   ├── CheckingAccount.java
│   │   ├── SavingsAccount.java
│   │   ├── Transaction.java
│   │   └── User.java
│   ├── view/                  Swing user interface
│   │   ├── LoginView, RegistrationView, MainView
│   │   ├── CreateAccountView, AccountListView, CustomerAccountListView
│   │   ├── TransactionView, AccountDetailsView, ReportView
│   │   └── Style.java              (shared theme and component factory)
│   └── module-info.java       Java module descriptor
└── bin/                       Compiled classes (Eclipse output directory)
```

## Documentation

| Document                                          | Description                                                            |
| ------------------------------------------------- | ---------------------------------------------------------------------- |
| [Documentation Index](docs/README.md)             | Entry point to all project documentation                               |
| [Getting Started](docs/getting-started.md)        | Requirements, running, building, IDE setup, and troubleshooting        |
| [User Guide](docs/user-guide.md)                  | Task-oriented instructions for customers and administrators            |
| [Architecture](docs/architecture.md)              | MVC design, components, data model, persistence, and key flows         |
| [Business Rules](docs/business-rules.md)          | Account rules, validation, interest, and access control                |
| [Security Assessment](docs/security.md)           | Current security posture, findings, and a prioritized remediation plan |
| [Diagrams](docs/diagrams.md)                      | Catalogue of the UML diagrams and program flow chart                   |

## Known Limitations

The following are the most significant limitations of the current release. Each is analyzed, with recommended remediation, in the [Security Assessment](docs/security.md).

- Passwords are stored and compared in plain text.
- A default administrator account with a published password is created automatically.
- Persistence uses unencrypted Java serialization with non-atomic writes.
- Monetary values are stored as `double`, which is unsuitable for financial arithmetic.
- Account ownership is partly determined by matching the account holder's name.
- There is no automated test suite.

## Repository and License

- **Repository:** <https://github.com/luoijin/Secure-Bank>
- **License:** No license file is currently included in this repository. Please contact the repository owner regarding reuse or redistribution.
