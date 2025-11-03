---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# TutorPal Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).

**Code reuse and AI-assisted development:**

* **Widespread use of AI tools (Yeo Yong Sheng)**: GitHub Copilot was used as an auto-complete and code-assistance tool throughout development, including logic, model, and test code.
* **GPT for test cases (Yeo Yong Sheng)**: ChatGPT generated some test cases to improve coverage, with adjustments for the codebase.

* **Widespread use of AI tools (Lee Chong Rui)**: GitHub Copilot was used as an auto-complete and code-assistance tool throughout development, including logic, model, and test code.
* **GPT for test cases (Lee Chong Rui)**: ChatGPT generated some test cases to improve coverage, with adjustments for the codebase.

* **GPT for test cases (Low Voon Bin Robin)**: ChatGPT generated test cases to improve coverage, with adjustments for the codebase.
* **GPT for Javadocs (Low Voon Bin Robin)**: ChatGPT improved Javadocs comments for better readability and clarity.

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md). TutorPal follows the same setup as AddressBook.

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S1-CS2103T-F11-2/tp/tree/master/src/main/java/seedu/tutorpal/Main.java) and [`MainApp`](https://github.com/AY2526S1-CS2103T-F11-2/tp/tree/master/src/main/java/seedu/tutorpal/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S1-CS2103T-F11-2/tp/tree/master/src/main/java/seedu/tutorpal/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S1-CS2103T-F11-2/tp/tree/master/src/main/java/seedu/tutorpal/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S1-CS2103T-F11-2/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S1-CS2103T-F11-2/tp/tree/master/src/main/java/seedu/tutorpal/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 2")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2526S1-CS2103T-F11-2/tp/tree/master/src/main/java/seedu/tutorpal/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="650" />

The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S1-CS2103T-F11-2/tp/tree/master/src/main/java/seedu/tutorpal/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.tutorpal.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.
<br>


### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* tuition centre is of small scale (around 50 students and 3 tutors)
* prefer desktop apps over other types
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: TutorPal helps small, single-subject tuition centre owners
manage students and tutors effortlessly by centralizing contact info, 
attendance and monthly payment tracking (student fees and tutor salaries)
in one easy-to-use command-line system. Designed for owners, tutors, and admins who are familiar with CLI workflows,
it helps save time, reduce errors, and  lets them focus on teaching instead of paperwork.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​               | I want to …​                  | So that I can…​                                                        |
|----------|----------------------|------------------------------|------------------------------------------------------------------------|
| `* * *`  | new user             | see usage instructions       | refer to instructions when I forget how to use the App                 |
| `* * *`  | user                 | add a student's contact      | collate all students' details in one place                             |
| `* * *`  | admin                | add a tutor's contact        | onboard tutors and assign them to classes                              |
| `* * *`  | user                 | delete a student's contact   | remove entries that I no longer need, maintaining a clean record       |
| `* * *`  | user                 | find a person by name        | locate details of persons without having to go through the entire list |
| `* * *`  | tuition centre owner | record payment status        | collect my fees on time                                                |
| `* * *`  | tuition centre owner | list all my student details  | get a overview of the students in my tuition centre                    |
| `* * *`  | tuition centre owner | unpay a month's payment      | correct payment mistakes quickly                                       |
| `* * *`  | tuition centre owner | view monthly payment summary | see paid/unpaid/overdue at a glance                                    |
| `* *`    | tutor                | record attendance            | track any students who may be missing classes                          |
| `* *`    | admin                | filter students by `class`, `tutor`, or `payment status` | find the right group of people easily                                  |
| `*`      | tuition centre owner | Set reminders for payments   | do not forget to ask for pending payments                              |

### Use cases
(For all use cases below, the **System** is the `TutorPal` and the **Actor** is the `Tuition Centre Admin`, unless specified otherwise)
<br><br>

**Use case: Add new student/tutor**

**MSS**

1.  Admin enters the add command with all required details in the correct format (e.g., `add r/student n/Kevin p/98761234 e/kevin@gmail.com a/Kent Ridge c/s4mon1600`).
2.  TutorPal adds the new student/tutor and displays a success message.

    Use case ends.

**Extensions**

- 1a. Missing required parameter: TutorPal detects an error in the entered details (e.g. missing or invalid parameter).

  - 1a1. TutorPal displays an error message and requests the correct input.
  - 1a2. Admin enters new details.
  - Steps 1a1-1a2 are repeated until the details are correct.
  - Use case resumes from step 2.

- 1b. TutorPal detects a duplicate (same name and phone number).
  - 1b1. TutorPal displays a duplicate error message.
  - Use case ends.
<br><br>

**Use case: Delete student/tutor**

**MSS**

1.  Admin enters the delete command with a valid index (e.g., `delete 5`).
2.  TutorPal deletes the contact at the specified index and displays a success message.

    Use case ends.

**Extensions**

- 1a. TutorPal detects an invalid index (non-numeric or out of range).
  - 1a1. TutorPal displays an error message and requests a valid index.
  - 1a2. Admin enters a new index.
  - Steps 1a1-1a2 are repeated until a valid index is entered.
  - Use case resumes from step 2.
<br><br>

**Use case: List students/tutors (with optional filter)**

**MSS**

1.  Admin enters the list command, optionally with a filter (e.g., `list`, `list c/s4mon1600`, `list t/Yeo Yong Sheng`).
2.  TutorPal displays the list of matching contacts with all attributes in a table format.

    Use case ends.

**Extensions**

- 1a. TutorPal detects an invalid filter (e.g., invalid format or filter).

  - 1a1. TutorPal displays an error message and requests a valid filter.
  - 1a2. Admin enters a new filter.
  - Steps 1a1-1a2 are repeated until a valid filter or no filter is entered.
  - Use case resumes from step 2.

- 1b. Admin provides multiple filters of the same type (e.g., `list c/s4 c/s2`, `list t/Alex t/John`).
    - 1b1. TutorPal applies OR logic: filters of the same type are combined with OR.
    - Use case resumes from step 2.

- 1c. Admin provides multiple filters of different types (e.g., `list c/s4 ps/unpaid`, `list t/Alex ps/paid`).
    - 1c1. TutorPal applies AND logic: filters of different types are combined with AND.
    - Use case resumes from step 2.

- 1d. Admin provides both multiple same-type filters and multiple different-type filters (e.g., `list c/s4 c/s2 ps/unpaid`).
    - 1d1. TutorPal applies combined logic: same-type filters are OR-ed, then different-type filters are AND-ed.
    - Use case resumes from step 2.

- 2a. No matching records found.
  - 2a1. TutorPal displays a `0 persons listed!` message.
  - Use case ends.
<br><br>

**Use case: Find student/tutor by name**

**MSS**

1.  Admin enters the find command with a name parameter (e.g., `find chong`).
2.  TutorPal displays matching contacts with their information and index.

    Use case ends.

**Extensions**

- 1a. TutorPal detects a missing or invalid name parameter i.e. empty string.

  - 1a1. TutorPal displays an error message and requests a valid name.
  - 1a2. Admin enters a new name.
  - Steps 1a1-1a2 are repeated until a valid name is entered.
  - Use case resumes from step 2.

- 2a. No matching records found.
  - 2a1. TutorPal displays a `0 persons listed!` message.
  - Use case ends.
<br><br>

**Use case: Mark monthly payment status (student or tutor)**

**MSS**

1.  Admin enters the pay command with a valid index and month (e.g., `pay 3 m/09-2025`).
2.  TutorPal marks the specified month as paid for the selected person (student: tuition fee; tutor: salary) and displays a success message.

    Use case ends.

**Extensions**

- 1a. TutorPal detects an invalid index or month format.
  - 1a1. TutorPal displays an error message and requests correct input.
  - 1a2. Admin enters new input.
  - Steps 1a1-1a2 are repeated until valid input is entered.
  - Use case resumes from step 2.

- 1b. TutorPal detects that the month entered is earlier than the tutor’s join month or later than the current month.
    - 1b1. TutorPal rejects the command and displays an error message informing the user that payment cannot be marked for a future month.
    - Use case ends.

- 1c. TutorPal detects that the month has already been marked as paid.
    - 1c1. TutorPal displays an error message stating that payment for the specified month has already been recorded.
    - Use case ends.
<br><br>

<br>
**Use case: Unmark monthly payment status (student or tutor)**

**MSS**

1. Admin enters the unpay command with a valid index and month (e.g., `unpay 3 m/09-2025`).
2. TutorPal marks the specified month as unpaid for the selected person and displays a success message.

Use case ends.

**Extensions**

- 1a. TutorPal detects an invalid index or month format.
    - 1a1. TutorPal displays an error message and requests correct input.
    - 1a2. Admin enters new input.
    - Steps 1a1-1a2 are repeated until valid input is entered.
    - Use case resumes from step 2.

- 1b. TutorPal detects that the month entered is earlier than the person's join month or later than the current month.
    - 1b1. TutorPal rejects the command and displays an error message.
    - Use case ends.

- 1c. TutorPal detects that the month is already marked as unpaid.
    - 1c1. TutorPal displays an error message stating that payment for the specified month is already unpaid.
    - Use case ends.

<br>
**Use case: Delete payment record**

**MSS**

1. Admin enters the delpay command with a valid index and month (e.g., `delpay 2 m/08-2025`).
2. TutorPal deletes the payment record for the specified month and displays a success message.

Use case ends.

**Extensions**

- 1a. TutorPal detects an invalid index or month format.
    - 1a1. TutorPal displays an error message and requests correct input.
    - 1a2. Admin enters new input.
    - Steps 1a1-1a2 are repeated until valid input is entered.
    - Use case resumes from step 2.

- 1b. TutorPal detects that the month is outside the valid range (before join month or after current month).
    - 1b1. TutorPal displays an error message indicating the valid range.
    - Use case ends.

- 1c. TutorPal detects that no payment record exists for the specified month.
    - 1c1. TutorPal displays an error message stating that the payment record was not found.
    - Use case ends.

<br>
**Use case: Mark student attendance**

**MSS**

1. Admin enters the mark command with a valid index and attendance week (e.g., mark 3 w/W10-2025).
2. TutorPal checks that the person at the index is a student and validates the attendance week.
3. TutorPal marks the attendance for the specified week and displays a success message.

Use case ends.

**Extensions**

- 1a. TutorPal detects an invalid index (non-numeric or out of range).
    - 1a1. TutorPal displays an error message and requests a valid index.
    - 1a2. Admin enters a new index.
    - Steps 1a1-1a2 are repeated until a valid index is entered.
    - Use case resumes from step 2.

- 2a. TutorPal detects that the person at the index is a tutor.
    - 2a1. TutorPal displays an error message indicating that attendance can only be marked for students.
    - Use case ends.

- 2b. TutorPal detects an invalid or missing attendance week parameter.
    - 2b1. TutorPal displays an error message and requests the correct format.
    - 2b2. Admin enters a new attendance week.
    - Steps 2b1-2b2 are repeated until a valid attendance week is entered.
    - Use case resumes from step 3.

- 2c. TutorPal detects that the attendance week is outside the valid range (before join week or after current week).
    - 2c1. TutorPal displays an error message indicating the valid range.
    - Use case ends.

- 2d. TutorPal detects that the attendance week has already been marked.
    - 2d1. TutorPal displays an error message indicating that attendance for this week is already marked.
    - Use case ends.
<br><br>

**Use case: Unmark student attendance**

**MSS**

1. Admin enters the unmark command with a valid index and attendance week (e.g., unmark 3 w/W10-2025).
2. TutorPal checks that the person at the index is a student and validates the attendance week.
3. TutorPal unmarks the attendance for the specified week and displays a success message.

Use case ends.

**Extensions**

- 1a. TutorPal detects an invalid index (non-numeric or out of range).
    - 1a1. TutorPal displays an error message and requests a valid index.
    - 1a2. Admin enters a new index.
    - Steps 1a1-1a2 are repeated until a valid index is entered.
    - Use case resumes from step 2.

- 2a. TutorPal detects that the person at the index is a tutor.
    - 2a1. TutorPal displays an error message indicating that attendance can only be unmarked for students.
    - Use case ends.

- 2b. TutorPal detects an invalid or missing attendance week parameter.
    - 2b1. TutorPal displays an error message and requests the correct format.
    - 2b2. Admin enters a new attendance week.
    - Steps 2b1-2b2 are repeated until a valid attendance week is entered.
    - Use case resumes from step 3.

- 2c. TutorPal detects that the attendance week is outside the valid range (before join week or after current week).
    - 2c1. TutorPal displays an error message indicating the valid range.
    - Use case ends.

- 2d. TutorPal detects that the attendance week was never marked.
    - 2d1. TutorPal displays an error message indicating that attendance for this week was not previously marked.
    - Use case ends.
<br><br>

**Use case: Edit person details**

**MSS**

1. Admin enters the edit command with a valid index and at least one field to edit (e.g., edit 2 p/91234567 e/johndoe@example.com).
2. TutorPal validates the edit parameters.
3. TutorPal updates the person's details and displays a success message.

Use case ends.

**Extensions**

- 1a. TutorPal detects an invalid index (non-numeric or out of range).
    - 1a1. TutorPal displays an error message and requests a valid index.
    - 1a2. Admin enters a new index.
    - Steps 1a1-1a2 are repeated until a valid index is entered.
    - Use case resumes from step 2.

- 1b. Admin provides no fields to edit.
    - 1b1. TutorPal displays an error message indicating that at least one field must be provided.
    - Use case ends.

- 1c. Admin attempts to edit the role field.
    - 1c1. TutorPal displays an error message indicating that role cannot be edited.
    - Use case ends.

- 2a. TutorPal detects invalid field values (e.g., invalid phone, email, or class format).
    - 2a1. TutorPal displays an error message specific to the invalid field.
    - 2a2. Admin enters corrected values.
    - Steps 2a1-2a2 are repeated until all values are valid.
    - Use case resumes from step 3.

- 2b. Admin edits a student's classes and provides more than one class.
    - 2b1. TutorPal displays an error message indicating that students can only have one class.
    - Use case ends.

- 2c. Admin edits classes and provides no classes (empty class list).
    - 2c1. TutorPal displays an error message indicating that at least one class is required.
    - Use case ends.

- 2d. Admin changes a student's join date, and the new join date would invalidate existing marked attendance.
    - 2d1. TutorPal displays an error message indicating that attendance records would become invalid.
    - Use case ends.

- 2e. TutorPal detects that the edited person would create a duplicate (same name and phone number as another person).
    - 2e1. TutorPal displays an error message indicating a duplicate person would be created.
    - Use case ends.
<br><br>

**Use case: Clear all entries**

**MSS**

1. Admin enters the clear command.
2. TutorPal clears all entries from the address book and displays a success message.

Use case ends.

**Extensions**

None.
<br><br>

**Use case: Exit application**

**MSS**

1. Admin enters the exit command.
2. TutorPal displays an exit acknowledgment message and closes the application.

    Use case ends.

**Extensions**

None.

### Non-Functional Requirements
1.  Should run on Windows 10+, macOS 12+, Ubuntu 22.04+ with Java 17+.
2.  Should have relatively fast startup on boot i.e. < 3000ms.
3.  Should have relatively smooth usage up to 1,000 persons; no UI freezes >1000 ms.
4.  The application interface (window, scrolling, and input fields) should remain responsive,
    with no unresponsiveness or noticeable lag lasting longer than 1 second (1000 ms)
    during normal operations such as typing, scrolling, or executing commands.
5.  Should be able to execute commands e.g. add/edit/delete/find/list in <1000 ms on a dataset of 1,000 persons.
6.  A user with above average typing speed of at least 50 words per minute (wpm) for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
7.  Should function offline without internet access.
8.  Data should be saved locally. Local data loaded on startup.

### Glossary

* **Mainstream OS**: Windows, macOS, and major Unix-like systems (e.g., Linux).
* **CLI (Command Line Interface)**: A text-based interface where users interact with the application by typing commands instead of using graphical buttons and menus
* **GUI (Graphical User Interface)**: The visual interface built with JavaFX, comprising `MainWindow`, `CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter`, etc. Layouts are defined in FXML under `src/main/resources/view`.
* **Student**: A person enrolled in the tuition centre who receives educational instruction. Their information includes contact details, class assignments, payment status, and academic records
* **Tutor**: An educator employed by the tuition centre to teach students. Can be assigned to multiple classes
* **Class Code**: A standardized identifier for classes in the format `sXdddHHMM` where:
  - `sX` represents the secondary level (s1-s5)
  - `ddd` represents the day of the week (mon, tue, wed, thu, fri)
  - `HHMM` represents the time in 24-hour format (e.g., s4mon1600 means Secondary 4, Monday, 4:00 PM)
* **Payment Status**: The current state of monthly payments. For students, this refers to tuition fees; for tutors, this refers to salaries. Can be `paid` (months up to current are paid), `unpaid` (current month not yet paid, earlier months paid), or `overdue` (there exists any unpaid month before the current month).
* **Index**: A positive integer used to identify a specific entry in the currently displayed contact list. Used in commands like `delete` and `pay`
* **Contact**: A record in TutorPal containing information about a student or tutor, including name, phone number, email, and address
* **Parameter**: A value provided by the user as part of a command, prefixed with identifiers like `n/` (name), `p/` (phone), `e/` (email), `c/` (class)
* **Role**: The classification of a contact as either a `student` or `tutor` in the system
* **Command**: An instruction typed by the user to perform an action in TutorPal (e.g., `add`, `delete`, `list`, `find`, `pay`)
* **ISO Week (ISO-8601)**: A week defined by the ISO-8601 standard. Used in attendance. Format: `WXX-yyyy` where `XX` is 01–53 and `yyyy` ≥ 2000. Note: not all years have week 53.
* **Attendance Week**: The ISO week specified in `mark`/`unmark` commands (e.g., `w/W10-2024`).
* **Join Date**: Date the person (student/tutor) joined. Format: `dd-MM-yyyy`, year ≥ 2000. Defaults to the current date of the local system if omitted on add.
* **Join Week**: The ISO-8601 week derived from the Join Date. Attendance can only be marked/unmarked from the Join Week up to the current week (inclusive).
* **Join Month**: The first billing month for payments. Format: `MM-yyyy`. Payment tracking starts from this month (inclusive).
* **Current Month**: Determined by the local system date/time and timezone of the device running TutorPal; used for payment validation.
* **Payment Record**: A stored entry representing the payment status for a specific month (`MM-yyyy`) associated with a student or tutor.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.


### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

### Paying a month (validation cases)
1. Duplicate month (m/) provided more than once

    1. Prerequisites: List all persons using the list command. Multiple persons in the list.

    1. Test case: `pay 1 m/09-2025 m/09-2025`<br>
      Expected: Command rejected with a “duplicate month” error. No changes applied.


### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Planned Enhancements**

**Team size:** 5

1. **Support guardian contact number in Add command**: Currently, the `add` command only allows storing a single phone number per contact. This is limiting for parents who need to provide both a primary contact and an emergency/guardian contact number. We plan to add an optional guardian contact field (prefix `g/`) to the `add` and `edit` commands. For example: `add r/student n/John Doe p/98765432 g/91234567 e/john@example.com a/123 Street c/s4mon1600`. The UI will display both the primary phone and guardian contact (if provided) in the person card, with the guardian contact labeled as "Guardian: 91234567".

2. **Display full attendance and payment history**: Currently, the person list view only shows recent or summary information for attendance and payment records, making it difficult for users to review complete historical data. We plan to introduce a `display INDEX` command that opens a dedicated window or panel showing the full attendance history (all marked weeks) and complete payment history (all months with their paid/unpaid status) for the person at the specified index. The display will be formatted in a user-friendly, scrollable table with columns for dates and status.

3. **Bulk delete payment and attendance records with adapted delpay command**: Currently, the `delpay` command only deletes a single payment record for a specified month (e.g., `delpay 2 m/08-2025`). If a user needs to adjust a person's join date to an earlier date, they must manually delete each subsequent payment and attendance record one by one, which is tedious and error-prone. We plan to introduce a new `clearhistory` command that deletes all payment and attendance records from the join date up to and including the specified date. For example: `clearhistory 2 upto/08-2025` would delete all payment records from the person's join month up to August 2025, as well as all attendance records from the join week up to the last week of August 2025. A confirmation message will be displayed: "Deleted all payment and attendance records for John Doe up to 08-2025 (5 payment records, 20 attendance records deleted)."

4. **Standardize date format for paymentHistory's join date**: Currently, the join date displayed in the UI and the join date stored in the data file's `paymentHistory` use inconsistent date formats, which makes manual editing of the data file difficult and error-prone. For instance, the data file's paymentHistory stores the join date as `2024-08-15` while the UI displays it as `15-08-2024`. We plan to standardize both representations to use the same format: `dd-MM-yyyy` (e.g., `15-08-2024`). This will ensure consistency across the application and make it easier for advanced users to manually edit the data file when necessary.
