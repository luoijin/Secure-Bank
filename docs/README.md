# SecureBank Documentation

This directory contains the technical and user documentation for SecureBank, a Java Swing desktop banking application. For a project overview, return to the [root README](../README.md).

## Contents

| Document                                   | Audience                     | Purpose                                                                  |
| ------------------------------------------ | ---------------------------- | ------------------------------------------------------------------------ |
| [Getting Started](getting-started.md)      | Everyone                     | Install prerequisites, run or build the application, resolve common issues |
| [User Guide](user-guide.md)                | Customers, administrators    | Step-by-step instructions for every feature                              |
| [Architecture](architecture.md)            | Developers, reviewers        | System design, components, data model, persistence, and key workflows    |
| [Business Rules](business-rules.md)        | Developers, analysts, testers | Authoritative description of account, validation, interest, and access rules |
| [Security Assessment](security.md)         | Developers, reviewers        | Current security posture, findings, and a prioritized remediation plan   |
| [Diagrams](diagrams.md)                    | Everyone                     | Catalogue of UML diagrams and the program flow chart                     |

## Suggested Reading Order

- **New users:** Getting Started, then User Guide.
- **New developers:** Getting Started, Architecture, Business Rules, then Security Assessment.
- **Reviewers and evaluators:** Architecture, Business Rules, Security Assessment, then Diagrams.

## Documentation Conventions

- **Currency** is the Philippine peso (₱), matching the application interface.
- **Source references** use paths relative to the repository root, for example `src/controller/BankController.java`.
- **Diagrams** embedded as Mermaid render natively on GitHub. The original PNG diagrams are stored in [`../diagrams/`](../diagrams/) and are described in [Diagrams](diagrams.md).
- **Statements of behavior** in these documents describe the source code as committed to this repository. Where the implementation differs from what a reader might reasonably expect, the difference is stated explicitly rather than omitted.

## Maintaining These Documents

When changing application behavior, update the affected documents in the same change:

| If you change…                                  | Update…                                          |
| ----------------------------------------------- | ------------------------------------------------ |
| Interest rates, limits, or validation           | [Business Rules](business-rules.md), [User Guide](user-guide.md) |
| Classes, packages, or persistence format        | [Architecture](architecture.md), [Diagrams](diagrams.md)         |
| Authentication, storage, or access control      | [Security Assessment](security.md), [Business Rules](business-rules.md) |
| Java version, build process, or dependencies    | [Getting Started](getting-started.md), [root README](../README.md) |
