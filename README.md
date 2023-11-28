# Hospital Database System

The Hospital Database has been developed to facilitate fast and accurate information storage for a new hospital that services the needs of a rapidly growing retirement community. The  system manages the primary care operation of the hospital, but not the financial operation. It has an easy to use interface that supports all the data entry and information gathering needs of the hospital.

## Demonstration
[Hospital Database Demonstration](https://tigermailauburn-my.sharepoint.com/:v:/g/personal/llw0008_auburn_edu/EZhRLr7yEMxJo779AUGuAQAB3hqrvFWAcz6OtZwirzwaPA?e=t2brzb)

## Run Program

Save the Hospital.jar file to your location of choice.

### Mac 
On a Mac, open a terminal and navigate to the location of the Hospital.jar file. Execute the following command:
```bash
java -jar Hospital.jar
```
### Windows 
On a Windows machine, open the command prompt and navigate to the location of the Hospital.jar file. Execute the following command:
```bash
java -jar Hospital.jar
```

## Usage
The program allows you to request a lot of different data. Here are some examples of requests you can make.
### Main Menu
```terminal
Welcome to our database! We provide data from the following categories at our hospital:
1. Room Utilization
2. Patient Information
3. Diagnosis and Treatment Information
4. Employee Information
0. Exit

 Enter the number beside the type of information you want: 
```

### Room Utilization

1. List the rooms that are occupied, along with the associated patient names and the date the patient was admitted.
2. List the rooms that are currently unoccupied.
3. List all rooms in the hospital along with patient names and admission dates for those that are occupied.

```terminal
 Enter the number beside the query you want to execute or 0 to exit: 1

room_num                       | last_name                      | first_name                     | admission_date                
-------------------------------+--------------------------------+--------------------------------+-------------------------------
1                              | Vargas                         | Malikah                        | 2023-12-15 08:28:05           
2                              | Ruiz                           | Terrell                        | 2023-12-07 10:47:29           
3                              | Soto                           | Tamika                         | 2023-12-03 10:02:42           
4                              | Reyes                          | Andre                          | 2023-11-30 10:27:17           
5                              | Guzman                         | LaKeisha                       | 2023-11-30 13:45:09           
6                              | Jimenez                        | Daquan                         | 2023-11-29 12:44:39           
7                              | Rios                           | Tyra                           | 2023-12-12 05:07:17           
8                              | Pacheco                        | Darius                         | 2023-11-22 03:11:58           
9                              | Navarro                        | Aisha                          | 2023-11-30 04:42:06           
10                             | Zamora                         | Trevon                         | 2023-12-08 04:58:52           
11                             | Garcia                         | Malik                          | 2023-12-14 08:28:05           
12                             | Rodriguez                      | Jasmine                        | 2023-12-07 10:47:29           
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

```
### Patient Information
1. List all patients in the database, with full personal information.
2. List all patients currently admitted to the hospital. List only patient identification number and name.
3. List all patients who were discharged in a given date range. List only patient identification number and name.
4. List all patients who were admitted within a given date range. List only patient identification number and name.
5. For a given patient (patient identification number), list all admissions to the hospital along with the diagnosis for each admission.
6. For a given patient (patient identification number), list all treatments that were administered. Group treatments by admissions. List admissions in descending chronological order, and list treatments in ascending chronological order within each admission.
7. List patients who were admitted to the hospital within 30 days of their last discharge date. For each patient list their patient identification number, name, diagnosis, and admitting doctor.
8. For each patient that has ever been admitted to the hospital, list their total number of admissions, average duration of each admission, longest span between admissions, shortest span between admissions, and average span between admissions.
```
 Enter the number beside the query you want to execute or 0 to exit: 3

 Enter the beginning date range in this format: 'yyyy-MM-dd': 2023-11-01

 Enter the ending date range in this format: 'yyyy-MM-dd': 2023-12-01

3. List all patients who were discharged in a given date range. List only patient identification number and name.
patient_ID                     | patient_name                  
-------------------------------+-------------------------------
3503                           | Aaliyah D. Lopez              
3149                           | Tyrone E. Hernandez           
2163                           | Ebony F. Gonzalez             
3599                           | LaToya H. Ramirez             
2245                           | Xavier I. Torres              
3102                           | Jamar K. Fernandez            
3493                           | Keisha L. Diaz                
3344                           | Nia N. Castro                 
2523                           | Marquis O. Flores             
3852                           | Tanesha P. Silva              
2452                           | Ashanti R. Santos             
2757                           | Kenya T. Mendoza              
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


```
### Diagnosis and Treatment Information
1. List the diagnoses given to patients, in descending order of occurrences. List diagnosis identification number, name, and total occurrences of each diagnosis.
2. List the diagnoses given to hospital patients, in descending order of occurrences. List diagnosis identification number, name, and total occurrences of each diagnosis.
3. List the treatments performed on admitted patients, in descending order of occurrences. List treatment identification number, name, and total number of occurrences of each treatment.
4. List the diagnoses associated with patients who have the highest occurrences of admissions to the hospital, in ascending order or correlation.
5. For a given treatment occurrence, list the patient name and the doctor who ordered the treatment.
```terminal
 Enter the number beside the query you want to execute or 0 to exit: 5

 Enter the number associated with the data you want to obtain.
(Treatment ID, Doctor ID, or Patient ID). Enter 9 to go back or 0 to exit: 4132
You entered 4132

5. For a given treatment occurrence, list the patient name and the doctor who ordered the treatment.
patient_name                   | ordering_dr                   
-------------------------------+-------------------------------
Malik A. Garcia                | Ji-hoon I. Runningwolf        
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


 Enter the number beside the query you want to execute or 0 to exit: 
```
### Employee Information
1. List all workers at the hospital, in ascending last name, first name order. For each worker, list their, name, and job category.
2. List the primary doctors of patients with a high admission rate (at least 4 admissions within a one-year time frame).
3. For a given doctor (employee ID), list all associated diagnoses in descending order of occurrence. For each diagnosis, list the total number of occurrences for the given doctor.
4. For a given doctor (employee ID, list all treatments that they ordered in descending order of occurrence. For each treatment, list the total number of occurrences for the given doctor.
5. List employees who have been involved in the treatment of every admitted patient.

```terminal
 Enter the number beside the query you want to execute or 0 to exit: 4
 Enter the number associated with the data you want to obtain.
(Treatment ID, Doctor ID, or Patient ID). Enter 9 to go back or 0 to exit: 1021
You entered 1021

4. For a given doctor (employee ID, list all treatments that they ordered in descending order of occurrence. For each treatment, list the total number of occurrences for the given doctor.
treatment_ID                   | treatment_name                 | order_timestamp                | occurrences                   
-------------------------------+--------------------------------+--------------------------------+-------------------------------
4105                           | Dental_extraction              | 2023-11-05 00:00:00            | 1                             
4113                           | Tonsillectomy                  | 2023-12-02 00:00:00            | 1                             
4121                           | Anticoagulants                 | 2023-12-07 00:00:00            | 1                             
4129                           | Insulin                        | 2023-12-03 00:00:00            | 1                             
4135                           | Colonoscopy                    | 2023-11-22 00:00:00            | 1                             
4143                           | Prostatectomy                  | 2023-12-01 00:00:00            | 1                             
4151                           | Antihypertensives              | 2023-11-08 00:00:00            | 1                             
4159                           | Diuretics                      | 2023-11-22 00:00:00            | 1                             
4160                           | Insulin                        | 2023-11-27 00:00:00            | 1                             
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


```
### Exit
```terminal
Welcome to our database! We provide data from the following categories at our hospital:
1. Room Utilization
2. Patient Information
3. Diagnosis and Treatment Information
4. Employee Information
0. Exit

 Enter the number beside the type of information you want: 0
Thank you for querying with us!
```

## Authorship

All work completed by Laurel Walker Davis © 2023.

## Purpose

This project was created as part of the Database 2 course at Auburn University.
