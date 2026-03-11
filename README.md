# MatrixOS File Sorter 📂

MatrixOS File Sorter is a desktop utility designed to automatically organize files into categorized folders, helping users keep their system clean and structured.

The application scans directories and sorts files based on their type, reducing manual effort and improving file management.

---

## 🚀 Features

• Automatic file organization
• Categorizes files by type (documents, images, videos, etc.)
• Simple login system using CSV storage
• Lightweight desktop application
• Fast and easy file sorting

---

## 🛠️ Technologies Used

* Java
* File I/O
* CSV data handling
* Desktop application architecture

---

## 📂 Project Structure

```
MatrixOS/
│
├── src/                # Source code
├── bin/                # Compiled files
├── data/               # Application data
│   └── login.csv       # User login information
│
├── MatrixOS.xml
├── manifest.mf
├── README.md
```

---

## ⚙️ Installation / Running the Application

1. Download the **application executable (.exe)** file.

2. Download the **data folder** along with the executable.

3. Ensure the folder structure looks like this:

```
MatrixOS/
│
├── MatrixOS.exe
└── data/
    └── login.csv
```

⚠️ The program **requires the `data/login.csv` file** to run properly because it stores user login information.

If the `data` folder is missing, the application may fail to start.

---

## ▶️ Running the Application

1. Double-click the **MatrixOS.exe** file.
2. Login using the credentials stored in `login.csv`.
3. Select the folder you want to organize.
4. The program will automatically sort the files into appropriate categories.

---

## 🎯 Use Cases

* Organizing messy download folders
* Sorting files in shared systems
* Managing large collections of documents
* Improving productivity through automated file management

---

## 👨‍💻 Author

Developed as a desktop utility project.

---

## 📜 License

This project is intended for educational and learning purposes.
