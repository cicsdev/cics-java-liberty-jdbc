/**
 * Copyright IBM Corp. 2024
 */
/** The information element */
const information = document.getElementById("information");
const errorMessage = document.getElementById("error");

const employeeNumberLen = 6;
const employeeNumberStartOff = 0
const employeeNumberEndOff = employeeNumberStartOff + employeeNumberLen;

const firstNameLen = 12;
const firstNameStartOff = employeeNumberEndOff + 1;
const firstNameEndOff = firstNameStartOff + firstNameLen;

const lastNameLen = 15;
const lastNameStartOff = firstNameEndOff + 1;
const lastNameEndOff = lastNameStartOff + lastNameLen;

const salaryStartOff = lastNameEndOff + 1;

/**
 * Loads all employees from a Java servlet.
 */
async function loadEmployees() {
    try {
        const response = await fetch('employees');

        if (!response.ok) {
            throw new Error("Failed to connect to database.");
        }

        const employees = await response.text();
        employees.split("\n")
            .map(processEmployee)
            .forEach(addEmployee);
    } catch (error) {
        const message = document.createElement("p");
        message.innerText = error;
        errorMessage.appendChild(message);

        errorMessage.hidden = false;
    }
}

function processEmployee(text) {
    return {
        "employeeNumber": text.substring(employeeNumberStartOff, employeeNumberEndOff),
        "firstName": text.substring(firstNameStartOff, firstNameEndOff),
        "lastName": text.substring(lastNameStartOff, lastNameEndOff),
        "salary": text.substring(salaryStartOff)
    };
}

/**
 * Adds an employee to the information section of the page.
 * 
 * @param {object} employee The JSON data about the employee
 */
async function addEmployee(employee) {

    if (!employee.employeeNumber) {
        return;
    }

    const div = document.createElement("div");
    const header = document.createElement("h2");
    const salary = document.createElement("p");

    div.id = `employee-${employee.employeeNumber}`
    div.appendChild(header);
    div.appendChild(salary);
    information.appendChild(div);

    header.textContent = `${employee.firstName} ${employee.lastName}`
    salary.textContent = `Salary: ${employee.salary}`
}

// Start loading the employees
loadEmployees();
