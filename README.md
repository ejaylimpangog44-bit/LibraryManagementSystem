# LibraryManagementSystem

I. Introduction
Good day, everyone! Today, I will present my project called the Library Management System.
This is a computer program that helps a librarian manage books and library members.
Instead of writing everything on paper, we can use this program to add books, search for books, register members, and record borrowed and returned books.
I created this project using Java and Java Swing. Java is the programming language, while Java Swing helps me create the buttons, tables, and windows that users can see and click.

My project has several important features.
First, Add Books allows the librarian to put new books into the system.
Second, Manage Members allows the librarian to register and update member information.
Third, Search Books helps users find a book by its ID, title, or author.
Fourth, Borrow Books records when a member borrows a book.
Fifth, Return Books records when a borrowed book is returned.
The system can also show which books are available, which books are borrowed, and the due dates of borrowed items.
These features make library work easier, faster, and more organized.

Encapsulation means keeping information safe inside a class.
For example, a book has an ID, a title, and an author. In my program, these details are private, so they cannot be changed directly from outside the class.
Instead, the program uses special methods to access or update the information.
It is like a box that keeps important things inside and lets us access them in a controlled way.

Inheritance means that one class can get features from another class.
In my project, I have a parent class called `LibraryItem`. It contains common information about library items.
The `Book` and `Magazine` classes inherit from `LibraryItem`.
It is like a child learning from a parent. The child gets some features from the parent but can also have its own special features.

Polymorphism means that different objects can behave differently even when they share the same parent class.
In my project, a book and a magazine are both library items, but they have different types and different creator information.
A book shows its author, while a magazine shows its publisher.
The program can handle both using the same parent class, but each object gives the correct information.
It is like asking two people the same question, but each person gives a different answer.

Abstraction means showing the important parts while hiding complicated details.
In my project, `LibraryItem` is an abstract class. It defines the common features that library items should have.
The `Book` and `Magazine` classes then provide their own implementations of those features.
It is like using a television remote. We press a button to change the channel without needing to understand all the complicated things happening inside the television.

My project has eight Java classes, and each class has its own job.
`LibraryItem` is the parent class for library items.
`Book` manages book information, while `Magazine` manages magazine information.
`LibraryMember` stores member details.
`Loan` records borrowing information, including the borrowing date and due date.
`LibraryService` manages the main operations, such as adding items, registering members, and borrowing and returning items.
`LibraryManagementGUI` handles the graphical interface, including buttons, tables, and dialog boxes.
Finally, `Main` starts the program and opens the main window.
I separated the code into different classes to make it cleaner, easier to understand, and easier to maintain.

My program also checks the information entered by the user.
For example, if someone tries to add a book without a title, the program shows an error message.
It also prevents duplicate IDs and stops users from borrowing a book that is already borrowed.
These checks help prevent mistakes and keep the library records organized.

In conclusion, my Library Management System helps organize books, members, and borrowing records using a simple graphical interface.
Through this project, I learned how to use Java, create classes and objects, and apply the four pillars of Object-Oriented Programming.
I also learned that dividing a program into smaller classes makes the code cleaner and easier to manage.
This project helped me understand how OOP concepts can be used to solve real-world problems.
Thank you for listening to my presentation!
