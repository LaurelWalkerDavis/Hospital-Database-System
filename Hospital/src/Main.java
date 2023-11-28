import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static final int[][] NEEDINPUT = {
            {2, 5}, {2, 6}, {3, 5}, {4, 3}, {4, 4}
    };
    private static final int[][] NEEDDATES = {
            {2, 3}, {2, 4}
    };
    static Connection connection = null;
    static int catNum;
    static int queNum;
    static int categoryNum;
    static int queryNum;
    static String query;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        try {
            final String[][] DESCRIPTIONS = {
                    {
                            // Room Utilization
                            "1. List the rooms that are occupied, along with the associated patient names and the date the patient was admitted.",
                            "2. List the rooms that are currently unoccupied.",
                            "3. List all rooms in the hospital along with patient names and admission dates for those that are occupied."},
                    {
                            // Patient Information
                            "1. List all patients in the database, with full personal information.",
                            "2. List all patients currently admitted to the hospital. List only patient identification number and name.",
                            "3. List all patients who were discharged in a given date range. List only patient identification number and name.",
                            "4. List all patients who were admitted within a given date range. List only patient identification number and name.",
                            "5. For a given patient (patient identification number), list all admissions to the hospital along with the diagnosis for each admission.",
                            "6. For a given patient (patient identification number), list all treatments that were administered. Group treatments by admissions. List admissions in descending chronological order, and list treatments in ascending chronological order within each admission.",
                            "7. List patients who were admitted to the hospital within 30 days of their last discharge date. For each patient list their patient identification number, name, diagnosis, and admitting doctor.",
                            "8. For each patient that has ever been admitted to the hospital, list their total number of admissions, average duration of each admission, longest span between admissions, shortest span between admissions, and average span between admissions."
                    },
                    {
                            // Diagnosis and Treatment Information
                            "1. List the diagnoses given to patients, in descending order of occurrences. List diagnosis identification number, name, and total occurrences of each diagnosis.",
                            "2. List the diagnoses given to hospital patients, in descending order of occurrences. List diagnosis identification number, name, and total occurrences of each diagnosis.",
                            "3. List the treatments performed on admitted patients, in descending order of occurrences. List treatment identification number, name, and total number of occurrences of each treatment.",
                            "4. List the diagnoses associated with patients who have the highest occurrences of admissions to the hospital, in ascending order or correlation.",
                            "5. For a given treatment occurrence, list the patient name and the doctor who ordered the treatment."
                    },
                    {
                            // Employee Information
                            "1. List all workers at the hospital, in ascending last name, first name order. For each worker, list their, name, and job category.",
                            "2. List the primary doctors of patients with a high admission rate (at least 4 admissions within a one-year time frame).",
                            "3. For a given doctor (employee ID), list all associated diagnoses in descending order of occurrence. For each diagnosis, list the total number of occurrences for the given doctor.",
                            "4. For a given doctor (employee ID, list all treatments that they ordered in descending order of occurrence. For each treatment, list the total number of occurrences for the given doctor.",
                            "5. List employees who have been involved in the treatment of every admitted patient."
                    }
            };

            final String[][] QUERIES = {
                    {
                            // 1.1. List the rooms that are occupied, along with the associated patient names and the date the patient was admitted.
                            """
                            SELECT Admission.room_num, Patient.last_name, Patient.first_name, Admission.admission_date
                            \tFROM Admission
                            \tJOIN Patient ON Admission.patient_ID = Patient.patient_ID
                            \tWHERE Admission.discharge_date IS NULL
                            \tORDER BY Admission.room_num ASC;""",

                            // 1.2. List the rooms that are currently unoccupied.
                            """
                            SELECT Room.room_num
                            \tFROM Room
                            \tLEFT JOIN Admission ON Admission.room_num = Room.room_num
                            \tAND Admission.discharge_date IS NULL
                            \tWHERE Admission.admission_ID IS NULL
                            \tORDER BY Room.room_num ASC;""",

                            // 1.3. List all rooms in the hospital along with patient names and admission dates for those that are occupied.
                            """
                            SELECT Room.room_num, Patient.last_name, Patient.first_name, Admission.admission_date
                            \tFROM Room
                            \tLEFT JOIN Admission ON Admission.room_num = Room.room_num
                            \tAND Admission.discharge_date IS NULL
                            \tLEFT JOIN Patient ON Admission.patient_ID = Patient.patient_ID
                            \tORDER BY Room.room_num ASC;""",
                    },

                    {
                            // 2.1. List all patients in the database, with full personal information.
                            """
                            SELECT
                                Patient.patient_ID,
                                CONCAT(Patient.first_name, ' ', Patient.middle_init, '. ', Patient.last_name) AS patient_name,
                                Patient.dob,
                                CONCAT(Emerg_Contact.first_name, ' ', Emerg_Contact.middle_init, '. ', Emerg_Contact.last_name) AS emerg_contact_name,
                                Emerg_Contact.relation_patient,
                                Emerg_Contact.phone AS Contact_phone,
                                Policy.*
                            FROM Patient
                            JOIN Emerg_Contact ON Patient.emerg_contact = Emerg_Contact.contact_ID
                            JOIN Policy ON Patient.policy_ID = Policy.policy_ID
                            ORDER BY Patient.patient_ID ASC;""",

                            // 2.2. List all patients currently admitted to the hospital. List only patient identification number and name.
                            """
                            SELECT
                                Patient.patient_ID,
                                CONCAT(Patient.first_name, ' ', Patient.middle_init, '. ', Patient.last_name) AS patient_name
                            FROM Patient
                            JOIN Admission ON Admission.patient_ID = Patient.patient_ID
                            WHERE Admission.discharge_date IS NULL
                            ORDER BY Patient.patient_ID ASC;""",

                            // 2.3. List all patients who were discharged in a given date range. List only patient identification number and name.
                            "",

                            // 2.4. List all patients who were admitted within a given date range. List only patient identification number and name.
                            "",

                            // 2.5. For a given patient (either patient identification number or name), list all admissions to the hospital along with the diagnosis for each admission.
                            "",

                            // 2.6. For a given patient (either patient identification number or name), list all treatments that were administered. Group treatments by admissions. List admissions in descending chronological order, and list treatments in ascending chronological order within each admission.
                            "",

                            // 2.7. List patients who were admitted to the hospital within 30 days of their last discharge date. For each patient list their patient identification number, name, diagnosis, and admitting doctor.
                            """
                            SELECT
                                Patient.patient_ID,
                                CONCAT(Patient.first_name, ' ', Patient.middle_init, '. ', Patient.last_name) AS patient_name,
                                Diagnosis.diagnosis_name,
                                CONCAT(Employee.first_name, ' ', Employee.middle_init, '. ', Employee.last_name) AS admitting_doctor
                            FROM Admission
                            INNER JOIN (
                                SELECT a.patient_ID, MAX(a.discharge_date) AS last_discharge
                                FROM Admission a
                                WHERE a.discharge_date IS NOT NULL
                                GROUP BY a.patient_ID
                            ) DD ON Admission.patient_ID = DD.patient_ID
                            JOIN Patient ON Patient.patient_ID = Admission.patient_ID
                            JOIN Diagnosis ON Admission.diagnosis_ID = Diagnosis.diagnosis_ID
                            JOIN Employee ON Admission.primary_doctor = Employee.employee_ID
                            WHERE Admission.admission_date BETWEEN DD.last_discharge AND DATE_ADD(DD.last_discharge, INTERVAL 30 DAY);""",

                            // 2.8. For each patient that has ever been admitted to the hospital, list their total number of admissions, average duration of each admission, longest span between admissions, shortest span between admissions, and average span between admissions.
                            """
                            WITH cte_next_admit AS (
                                SELECT A.admission_ID, MIN(AD.admission_date) AS next_admin_date
                                FROM Admission A
                                LEFT JOIN Admission AD ON A.patient_ID = AD.patient_ID AND AD.admission_date > A.admission_date
                                GROUP BY A.admission_ID, A.patient_ID, A.admission_date
                            )
                            SELECT
                                CONCAT(p.first_name, ' ', p.middle_init, '. ', p.last_name) AS patient_name,
                                COUNT(a.admission_ID) AS total_admissions,
                                ROUND(AVG(IFNULL(TIMESTAMPDIFF(DAY, a.admission_date, a.discharge_date), 0)), 2) AS avg_duration,
                                MAX(TIMESTAMPDIFF(DAY, a.admission_date, na.next_admin_date)) AS longest_span_btw_admin,
                                MIN(TIMESTAMPDIFF(DAY, a.admission_date, na.next_admin_date)) AS shortest_span_btw_admin,
                                ROUND(AVG(TIMESTAMPDIFF(DAY, a.admission_date, na.next_admin_date)), 2) AS avg_span_btw_admin
                            FROM Patient p
                            JOIN Admission a ON p.patient_ID = a.patient_ID
                            JOIN cte_next_admit na ON a.admission_ID = na.admission_ID
                            GROUP BY patient_name
                            ORDER BY patient_name ASC;"""
                    },
                    {
                            // 3.1. List the diagnoses given to patients, in descending order of occurrences. List diagnosis identification number, name, and total occurrences of each diagnosis.
                            """
                            SELECT
                                d.diagnosis_ID,
                                d.diagnosis_name,
                                COUNT(a.diagnosis_ID) AS total_occurances
                            FROM Diagnosis d
                            JOIN Admission a ON a.diagnosis_ID = d.diagnosis_ID
                            GROUP BY d.diagnosis_ID, d.diagnosis_name
                            ORDER BY total_occurances DESC;""",

                            // 3.2. List the diagnoses given to hospital patients, in descending order of occurrences. List diagnosis identification number, name, and total occurrences of each diagnosis.
                            """
                            SELECT
                                d.diagnosis_ID,
                                d.diagnosis_name,
                                COUNT(a.diagnosis_ID) AS total_occurances
                            FROM Diagnosis d
                            JOIN Admission a ON a.diagnosis_ID = d.diagnosis_ID
                            GROUP BY d.diagnosis_ID, d.diagnosis_name
                            ORDER BY total_occurances DESC;""",

                            // 3.3. List the treatments performed on admitted patients, in descending order of occurrences. List treatment identification number, name, and total number of occurrences of each treatment.
                            """
                            SELECT
                                t.treatment_type_ID,
                                ttype.treatment_name,
                                ttype.treatment_type_name,
                                COUNT(t.treatment_type_ID) AS occurrences
                            FROM Treatment t
                            JOIN Treatment_Type ttype ON t.treatment_type_ID = ttype.treatment_type_ID
                            JOIN Admission ON Admission.admission_ID = t.admission_ID
                            WHERE Admission.discharge_date IS NULL
                            GROUP BY t.treatment_type_ID, ttype.treatment_name, ttype.treatment_type_name
                            ORDER BY occurrences DESC;""",

                            // 3.4. List the diagnoses associated with patients who have the highest occurrences of admissions to the hospital, in ascending order or correlation.
                            """
                            SELECT
                                p.patient_ID,
                                COUNT(a.admission_ID) AS total_admissions,
                                d.diagnosis_name
                            FROM Patient p
                            JOIN Admission a ON p.patient_ID = a.patient_ID
                            JOIN Diagnosis d ON a.diagnosis_ID = d.diagnosis_ID
                            GROUP BY p.patient_ID, d.diagnosis_name
                            ORDER BY total_admissions ASC, patient_ID;""",

                            // 3.5. For a given treatment occurrence, list the patient name and the doctor who ordered the treatment.
                            ""
                    },
                    {
                            // 4.1. List all workers at the hospital, in ascending last name, first name order. For each worker, list their name and job category.
                            """
                            SELECT
                                CONCAT(Employee.first_name, ' ', Employee.middle_init, '. ', Employee.last_name) AS employee_name,
                                Jobs.job_name
                            FROM Employee
                            JOIN Jobs ON Employee.job_ID = Jobs.job_ID
                            ORDER BY Employee.last_name ASC, Employee.first_name ASC;""",

                            // 4.2. List the primary doctors of patients with a high admission rate (at least 4 admissions within a one-year time frame).
                            """
                            SELECT DISTINCT
                                Admission.primary_doctor,
                                CONCAT(Employee.first_name, ' ', Employee.middle_init, '. ', Employee.last_name) AS doctor_name
                            FROM Admission
                            JOIN Employee ON Admission.primary_doctor = Employee.employee_ID
                            WHERE Admission.patient_ID IN (
                                SELECT Admission.patient_ID
                                FROM Admission
                                JOIN Patient ON Admission.patient_ID = Patient.patient_ID
                                WHERE Admission.admission_date >= DATE_SUB(NOW(), INTERVAL 1 YEAR)
                                GROUP BY Admission.patient_ID
                                HAVING COUNT(*) > 3
                            )
                            ORDER BY doctor_name ASC;""",

                            // 4.3. For a given doctor, list all associated diagnoses in descending order of occurrence. For each diagnosis, list the total number of occurrences for the given doctor.
                            "",

                            // 4.4. For a given doctor, list all treatments that they ordered in descending order of occurrence. For each treatment, list the total number of occurrences for the given doctor.
                            "",

                            // 4.5. List employees who have been involved in the treatment of every admitted patient.
                            """
                            SELECT
                                a.patient_ID,
                                t.treatment_ID,
                                CONCAT(d.first_name, ' ', d.last_name) AS order_doctor,
                                CONCAT(e.first_name, ' ', e.last_name) AS nurse_tech
                            FROM Treatment t
                            JOIN Employee d ON t.order_doctor = d.employee_ID
                            JOIN Employee e ON t.admin_employee = e.employee_ID
                            JOIN Admission a ON a.patient_ID = t.patient_ID
                            WHERE a.patient_ID IN (
                                SELECT DISTINCT x.patient_ID
                                FROM Admission x
                                WHERE x.discharge_date IS NULL
                            )
                            GROUP BY a.patient_ID, t.treatment_ID, d.employee_ID, e.employee_ID
                            ORDER BY a.patient_ID, t.treatment_ID;"""
                    }

            };

//////////// Set up the connection to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/hospital"; // connection string
            String username = "root"; // mysql username
            String password = "QuebecMaple102"; // mysql password

            connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);

            menuLoop:
            while (true) {

                // Category Menu
                System.out.println("Welcome to our database! We provide data from the following categories at our hospital:");
                System.out.println(
                        """
                                1. Room Utilization
                                2. Patient Information
                                3. Diagnosis and Treatment Information
                                4. Employee Information
                                0. Exit"""
                );

///////////////// User interaction to get category
                while (true) {
                    System.out.print("\n Enter the number beside the type of information you want: ");

                    // Check if the next input is an integer
                    if (scanner.hasNextInt()) {
                        catNum = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character after reading the integer

                        // Validate input
                        if (catNum > 0 && catNum <= 4) {
                            break;
                        } else if (catNum == 0) {
                            System.out.println("Thank you for querying with us!");
                            System.exit(0);
                        } else {
                            System.out.println("You entered an invalid number.");
                            System.out.println("Enter a number <= 4");
                        }
                    } else {
                        // Handle non-integer input
                        System.out.println("Invalid input. Please enter a valid integer.");
                        scanner.nextLine(); // Consume the invalid input
                    }
                }

///////////////// print the queries from that category
                categoryNum = catNum - 1;      // account for indexing

                for (String desc : DESCRIPTIONS[categoryNum]) {
                    System.out.println(desc);
                }

///////////////// User interaction to get second part of query
                while (true) {
                    System.out.print("\n Enter the number beside the query you want to execute or 0 to exit: ");

                    // Check if the next input is an integer
                    if (scanner.hasNextInt()) {
                        queNum = scanner.nextInt();
                        scanner.nextLine();
                        int len = DESCRIPTIONS[categoryNum].length;

                        // validate input
                        if (queNum > 0 && queNum <= len) {
                            break;
                        } else if (queNum == 0) {
                            System.out.println("Thank you for querying with us!");
                            System.exit(0);
                        } else {
                            System.out.println("You entered an invalid number.");
                            System.out.println("Enter a number <= " + len);
                        }
                    } else {
                        // handle non-integer input
                        System.out.println("Invalid input. Please enter a valid integer.");
                        scanner.nextLine(); // consume invalid input
                    }
                }

                queryNum = queNum - 1;  // account for indexing
                int[] queryArray = {catNum, queNum};

                // initialize variables
                String beginDate;
                String endDate;
                int extraInput;


///////////// if query needs two dates
                if (needsDates(queryArray)) {

                    // get two dates
                    while (true) {
                        System.out.print("\n Enter the beginning date range in this format: 'yyyy-MM-dd': ");
                        beginDate = scanner.nextLine();
                        if (validateDate(beginDate)) {
                            break;
                        } else {
                            System.out.println("Invalid date format.");
                        }
                    }
                    while (true) {
                        System.out.print("\n Enter the ending date range in this format: 'yyyy-MM-dd': ");
                        endDate = scanner.nextLine();
                        if (validateDate(endDate)) {
                            break;
                        } else {
                            System.out.println("Invalid date format.");
                        }
                    }
                    query = queryBuilder(beginDate, endDate);
                }

///////////// if query needs extra input
                else if (needsInput(queryArray)) {
                    // get extra input
                    extraLoop:
                    while (true) {
                        System.out.print("""
                                 Enter the number associated with the data you want to obtain.
                                (Treatment ID, Doctor ID, or Patient ID). Enter 9 to go back or 0 to exit:\s""");

                        // Check if the next input is an integer
                        if (scanner.hasNextInt()) {
                            extraInput = scanner.nextInt();
                            scanner.nextLine(); // Consume the newline character after reading the integer

                            // Validate input
                            if (extraInput == 0) {
                                System.out.println("Thank you for querying with us!");
                                System.exit(0);
                            } else if (extraInput == 9) {
                                break extraLoop;
                            } else {
                                System.out.println("You entered " + extraInput);
                                break;
                            }
                        } else {
                            // Handle non-integer input
                            System.out.println("Invalid input. Please enter a valid integer.");
                            scanner.nextLine(); // Consume the invalid input
                        }
                    }
                    query = queryBuilder(String.valueOf(extraInput), "");
                } else {

/////////////// regular query
                    query = QUERIES[categoryNum][queryNum];
                }


///////////////// database reading
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnsNumber = rsmd.getColumnCount();

//////////////// Print Tables

                // Print column titles
                System.out.println("\n" + DESCRIPTIONS[categoryNum][queryNum]);
                for (
                        int i = 1;
                        i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(" | ");
                    System.out.printf("%-30s", rsmd.getColumnName(i));
                }
                System.out.println();

                // Print a line to separate column names and data
                for (
                        int i = 1;
                        i <= columnsNumber; i++) {
                    if (i > 1) System.out.print("-+-");
                    for (int j = 0; j < 30; j++) {
                        System.out.print("-");
                    }
                }
                System.out.println();

                // print results of query
                if (!resultSet.isBeforeFirst()) {
                    // result set is empty, so the ID isn't in the database
                    System.out.println("ID not found in database.\n");
                } else {
                    // result set is NOT empty, so the ID is in the database
                    while (resultSet.next()) {
                        for (int i = 1; i <= columnsNumber; i++) {
                            if (i > 1) System.out.print(" | ");
                            String columnValue = resultSet.getString(i);
                            System.out.printf("%-30s", columnValue); // Adjust the width as needed
                        }
                        System.out.println();
                    }
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean needsInput(int[] selectedQuery) {
        for (int[] row : NEEDINPUT) {
            if (Arrays.equals(row, selectedQuery)) {
                return true;
            }
        }
        return false;
    }

    public static boolean needsDates(int[] selectedQuery) {
        for (int[] row : NEEDDATES) {
            if (Arrays.equals(row, selectedQuery)) {
                return true;
            }
        }
        return false;
    }


    // Validate the input date format
    public static boolean validateDate(String date) {
        String format = "\\d{4}-\\d{2}-\\d{2}";
        return date.matches(format);
    }


    public static String queryBuilder(String extraInput, String endDate) {
        String specialQuery = "";
        switch (catNum) {
            case 2:
                // do something
                switch (queNum) {
                    case 3:
                        // 2.3. List all patients who were discharged in a given date range. List only patient identification number and name.
                        return "SELECT\n" +
                                "    Patient.patient_ID,\n" +
                                "    CONCAT(Patient.first_name, ' ', Patient.middle_init, '. ', Patient.last_name) AS patient_name\n" +
                                "FROM Patient\n" +
                                "JOIN Admission ON Admission.patient_ID = Patient.patient_ID\n" +
                                "WHERE Admission.discharge_date BETWEEN '" + extraInput + "' AND '" + endDate + "';";
                    case 4:
                        // 2.4. List all patients who were admitted within a given date range. List only patient identification number and name.
                        return "SELECT\n" +
                                "    Patient.patient_ID,\n" +
                                "    CONCAT(Patient.first_name, ' ', Patient.middle_init, '. ', Patient.last_name) AS patient_name\n" +
                                "FROM Patient\n" +
                                "JOIN Admission ON Admission.patient_ID = Patient.patient_ID\n" +
                                "WHERE Admission.admission_date BETWEEN '" + extraInput + "' AND '" + endDate + "';";

                    case 5:
                        // 2.5. For a given patient (either patient identification number or name), list all admissions to the hospital along with the diagnosis for each admission.
                        return "SELECT\n" +
                                "    CONCAT(Patient.first_name, ' ', Patient.middle_init, '. ', Patient.last_name) AS patient_name,\n" +
                                "    Admission.admission_date,\n" +
                                "    Diagnosis.diagnosis_name\n" +
                                "FROM Patient\n" +
                                "JOIN Admission ON Admission.patient_ID = Patient.patient_ID\n" +
                                "LEFT JOIN Diagnosis ON Admission.diagnosis_ID = Diagnosis.diagnosis_ID\n" +
                                "WHERE Patient.patient_ID = '" + extraInput + "'\n" +
                                "ORDER BY Admission.admission_date DESC;";
                    case 6:
                        // 2.6. For a given patient (either patient identification number or name), list all treatments that were administered. Group treatments by admissions. List admissions in descending chronological order, and list treatments in ascending chronological order within each admission.
                        return "SELECT\n" +
                                "    CONCAT(Patient.first_name, ' ', Patient.middle_init, '. ', Patient.last_name) AS patient_name,\n" +
                                "    Admission.admission_date,\n" +
                                "    Treatment.treatment_ID,\n" +
                                "    Treatment.admin_timestamp\n" +
                                "FROM Patient\n" +
                                "JOIN Admission ON Patient.patient_ID = Admission.patient_ID\n" +
                                "LEFT JOIN Treatment ON Treatment.patient_ID = Admission.patient_ID\n" +
                                "WHERE Patient.patient_ID = '" + extraInput + "'\n" +
                                "GROUP BY Admission.admission_date, Treatment.treatment_ID, Treatment.admin_timestamp\n" +
                                "ORDER BY Admission.admission_date DESC, Treatment.admin_timestamp ASC;";
                }
            case 3:
                // 3.5. For a given treatment occurrence, list the patient name and the doctor who ordered the treatment.
                return "SELECT\n" +
                        "    CONCAT(p.first_name, ' ', p.middle_init, '. ', p.last_name) AS patient_name,\n" +
                        "    CONCAT(e.first_name, ' ', e.middle_init, '. ', e.last_name) AS ordering_dr\n" +
                        "FROM Patient p\n" +
                        "JOIN Treatment t ON t.patient_ID = p.patient_ID\n" +
                        "JOIN Employee e ON t.order_doctor = e.employee_ID\n" +
                        "WHERE t.treatment_ID = '" + extraInput + "';  -- treatment occurrence placed here";

            case 4:
                if (queNum == 3) {
                    // 4.3. For a given doctor, list all associated diagnoses in descending order of occurrence. For each diagnosis, list the total number of occurrences for the given doctor.
                    return "SELECT\n" +
                            "    d.diagnosis_name,\n" +
                            "    COUNT(d.diagnosis_ID) AS occurrences\n" +
                            "FROM Diagnosis d\n" +
                            "JOIN Admission a ON d.diagnosis_ID = a.diagnosis_ID\n" +
                            "JOIN Patient p ON a.patient_ID = p.patient_ID\n" +
                            "WHERE a.primary_doctor = '" + extraInput + "'\n" +
                            "GROUP BY d.diagnosis_name\n" +
                            "ORDER BY occurrences DESC;";

                } else {
                    // 4.4. For a given doctor, list all treatments that they ordered in descending order of occurrence. For each treatment, list the total number of occurrences for the given doctor.
                    return "SELECT\n" +
                            "    Treatment.treatment_ID,\n" +
                            "    Treatment_Type.treatment_name,\n" +
                            "    Treatment.order_timestamp,\n" +
                            "    COUNT(Treatment.treatment_ID) AS occurrences\n" +
                            "FROM Treatment\n" +
                            "JOIN Treatment_Type ON Treatment_Type.treatment_type_ID = Treatment.treatment_type_ID\n" +
                            "WHERE Treatment.order_doctor = '" + extraInput + "'\n" +
                            "GROUP BY Treatment.treatment_ID\n" +
                            "ORDER BY occurrences DESC;";
                }
        }
        return specialQuery;
    }
}
