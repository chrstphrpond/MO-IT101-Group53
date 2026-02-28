package com.motorph;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * AttendanceProcessor.java - Handles reading attendance CSV and computing hours worked.
 * Implements FR-02: Read Attendance Logs and Compute Hours Worked.
 */
public class AttendanceProcessor {

    private static final LocalTime STANDARD_START = LocalTime.of(8, 0);  // 8:00 AM
    private static final double HOURS_PER_DAY     = 8.0;
    private static final double LATE_THRESHOLD_MIN = 10.0; // grace period in minutes
    private CSVReader csvReader = new CSVReader();

    /**
     * Computes total regular hours worked by an employee for a given pay period.
     * Reads daily time-in and time-out records from the attendance CSV.
     * Counts only weekday records (Mon-Fri), capped at 8 hours per day.
     *
     * @param employeeNumber The employee ID to filter records
     * @param startDate      Start of pay period (YYYY-MM-DD)
     * @param endDate        End of pay period (YYYY-MM-DD)
     * @return Total regular hours worked as a double
     */
    public double computeHoursWorked(String employeeNumber, String startDate, String endDate) {
        List<String[]> records = csvReader.readCSV("data/attendance.csv");
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("H:mm");

        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate end   = LocalDate.parse(endDate,   DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        double totalHours = 0.0;

        for (String[] row : records) {
            // Expected CSV columns: [0]=EmpNo, [1]=LastName, [2]=FirstName,
            //                       [3]=Date, [4]=TimeIn, [5]=TimeOut
            if (row.length < 6) continue;
            if (!row[0].trim().equals(employeeNumber)) continue;

            try {
                LocalDate date   = LocalDate.parse(row[3].trim(), dateFmt);
                // Only count records within the pay period
                if (date.isBefore(start) || date.isAfter(end)) continue;
                // Skip weekends
                int dow = date.getDayOfWeek().getValue(); // 1=Mon ... 7=Sun
                if (dow >= 6) continue;

                LocalTime timeIn  = LocalTime.parse(row[4].trim(), timeFmt);
                LocalTime timeOut = LocalTime.parse(row[5].trim(), timeFmt);

                // Compute raw hours worked that day
                double dailyHours = ChronoUnit.MINUTES.between(timeIn, timeOut) / 60.0;
                // Cap at standard 8 hours
                dailyHours = Math.min(dailyHours, HOURS_PER_DAY);
                totalHours += dailyHours;
            } catch (Exception e) {
                // Skip malformed rows
                System.err.println("Skipping malformed attendance row: " + row[0]);
            }
        }
        return totalHours;
    }

    /**
     * Computes total late/undertime minutes for an employee in a pay period.
     * Late is defined as arriving more than 10 minutes after 8:00 AM.
     * Each late minute is deducted as (hourly rate / 60) from gross pay.
     *
     * @param employeeNumber The employee ID
     * @param startDate      Start of pay period (YYYY-MM-DD)
     * @param endDate        End of pay period (YYYY-MM-DD)
     * @return Total late/undertime in minutes as a double
     */
    public double computeLateMinutes(String employeeNumber, String startDate, String endDate) {
        List<String[]> records = csvReader.readCSV("data/attendance.csv");
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("H:mm");

        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate end   = LocalDate.parse(endDate,   DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        double totalLateMinutes = 0.0;

        for (String[] row : records) {
            if (row.length < 6) continue;
            if (!row[0].trim().equals(employeeNumber)) continue;

            try {
                LocalDate date = LocalDate.parse(row[3].trim(), dateFmt);
                if (date.isBefore(start) || date.isAfter(end)) continue;
                int dow = date.getDayOfWeek().getValue();
                if (dow >= 6) continue;

                LocalTime timeIn = LocalTime.parse(row[4].trim(), timeFmt);
                // Minutes late = timeIn - 8:00 AM (only if beyond grace period)
                long minutesLate = ChronoUnit.MINUTES.between(STANDARD_START, timeIn);
                if (minutesLate > LATE_THRESHOLD_MIN) {
                    totalLateMinutes += minutesLate;
                }
            } catch (Exception e) {
                System.err.println("Skipping malformed attendance row: " + row[0]);
            }
        }
        return totalLateMinutes;
    }
}
