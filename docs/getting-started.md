# Getting Started

This guide explains how to run, build, and troubleshoot SecureBank.

## 1. Requirements

| Requirement | Detail |
| ----------- | ------ |
| Java        | **Java 25** runtime (JRE) to run; Java 25 JDK to build. The bundled JAR and the `bin/` classes are compiled as class-file version 69, which corresponds to Java 25. |
| Display     | A graphical desktop environment. The application is a Swing GUI and cannot run headless. |
| Fonts       | The interface requests the *Segoe UI* font. On systems without it, Java substitutes another font and spacing may differ slightly. |
| Disk        | Write access to the directory from which the application is launched (see [Data File](#5-data-file)). |

Verify your Java version:

```bash
java -version
```

## 2. Running the Application

### Option A: Prebuilt JAR (recommended)

From the repository root:

```bash
java -jar SecureBank.jar
```

The JAR's manifest declares `main.BankingSystemApp` as the entry point.

### Option B: From compiled classes

The `bin/` directory contains the classes produced by Eclipse:

```bash
java -cp bin main.BankingSystemApp
```

## 3. Building from Source

The project has no external code dependencies. Although `lib/itextpdf-5.5.13.3.jar` is present and referenced by the Eclipse configuration, no source file currently imports it.

### Command line (macOS / Linux)

```bash
javac -d out $(find src -name "*.java")
java -cp out main.BankingSystemApp
```

### Command line (Windows PowerShell)

```powershell
Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName } | Set-Content sources.txt
javac -d out "@sources.txt"
java -cp out main.BankingSystemApp
```

### Repackaging the executable JAR

After compiling to `out/`:

```bash
jar --create --file SecureBank.jar --main-class main.BankingSystemApp -C out .
```

## 4. Eclipse Setup

The repository includes Eclipse project metadata (project name `BankingSystem`).

1. Choose **File → Import → General → Existing Projects into Workspace** and select the repository folder.
2. Ensure the project uses a **JavaSE-25** JRE (**Project → Properties → Java Build Path → Libraries**).
3. Fix the library entry. `.classpath` contains an absolute path from the original author's machine:

   ```text
   C:/java/BankingSystem/lib/itextpdf-5.5.13.3.jar
   ```

   Either re-add the JAR from the project's `lib/` folder or remove the entry; the code does not use it.
4. Run `src/main/BankingSystemApp.java` as a **Java Application**. Because the working directory is the project root, the data file is created there.

## 5. Data File

SecureBank persists all users, accounts, and transactions to a single file, `bank_data.ser`.

- The path is **relative to the working directory** of the process. Launching the application from different directories creates or loads different data files.
- The file is written after every state-changing operation and again on application shutdown.
- On first launch (no file, or no users), a default administrator is created. See [Default Credentials](../README.md#default-credentials).
- To reset the system to a clean state, close the application, back up if needed, and delete `bank_data.ser`. **This permanently removes all users, accounts, and transaction history.**

The committed `bank_data.ser` contains sample data created during development, including a test customer.

## 6. Generated Files

| File                                        | Created by                          | Location                              |
| ------------------------------------------- | ----------------------------------- | ------------------------------------- |
| `bank_data.ser`                             | Automatic persistence               | Working directory                     |
| `BankReport_<timestamp>.txt`                | Administrator report → *Export to File* | Working directory                     |
| `Account_Statement_<account>_<timestamp>.txt` | Account Details → *Export to File*  | Location chosen in a save dialog      |

## 7. Troubleshooting

| Symptom | Likely cause | Resolution |
| ------- | ------------ | ---------- |
| `UnsupportedClassVersionError ... class file version 69.0` | Java runtime older than 25 | Install Java 25 and re-run. |
| Application opens but previous accounts are missing | Launched from a different directory, so a new `bank_data.ser` was created | Launch from the directory that contains the intended `bank_data.ser`. |
| Console shows `Error loading data` and the system starts empty | The data file is unreadable, for example after a class change or file corruption | Restore a backup of `bank_data.ser`. Note that after such a failure the application re-creates the default administrator and may overwrite the unreadable file on its next save; back up the file before relaunching. |
| Eclipse reports a missing library or wrong JRE | Absolute `.classpath` entry or unsupported JRE level | Follow [Eclipse Setup](#4-eclipse-setup). |
| `HeadlessException` | No graphical display available | Run in a desktop session; remote sessions need display forwarding. |
| Cannot sign in as `admin` | The administrator exists only if the data file had no users at first launch | Use the credentials of an existing administrator, or reset the data file as described in [Data File](#5-data-file). |
