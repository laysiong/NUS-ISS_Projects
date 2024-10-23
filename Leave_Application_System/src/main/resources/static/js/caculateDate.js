/* function calculateDateDifference(startDateInput, endDateInput, leaveDurationInput) {
    const startDate = new Date(startDateInput.value);
    const endDate = new Date(endDateInput.value);

    if (startDate && endDate && !isNaN(startDate) && !isNaN(endDate)) {
        const timeDiff = endDate - startDate;
        const daysDiff = timeDiff / (1000 * 3600 * 24);
        leaveDurationInput.value = daysDiff >= 0 ? daysDiff + " days" : "Invalid dates";
    } else {
        leaveDurationInput.value = "";
    }
}

function initializeDateDifferenceCalculation() {
    const startDateInput = document.getElementById("startDate");
    const endDateInput = document.getElementById("endDate");
    const leaveDurationInput = document.getElementById("leaveDuration");

    if (startDateInput && endDateInput && leaveDurationInput) {
        startDateInput.addEventListener("change", function() {
            calculateDateDifference(startDateInput, endDateInput, leaveDurationInput);
        });
        endDateInput.addEventListener("change", function() {
            calculateDateDifference(startDateInput, endDateInput, leaveDurationInput);
        });
    }
}

document.addEventListener("DOMContentLoaded", function() {
    initializeDateDifferenceCalculation();
}); */
const publicHolidays = [
    "2024-06-24",
    "2024-06-25",
    "2024-06-26"
];

function parseDate(input) {
    const parts = input.split("-");
    return new Date(Date.UTC(parts[0], parts[1] - 1, parts[2])); // Adjust month index and use UTC
}

function isWeekendOrHoliday(date) {
    const dayOfWeek = date.getUTCDay();
    const formattedDate = date.toISOString().split('T')[0];
    console.log(`Checking date: ${formattedDate}, Day of Week: ${dayOfWeek}`);
    return dayOfWeek === 0 || dayOfWeek === 6 || publicHolidays.includes(formattedDate);
}

function calculateDateDifference(startDateInput, endDateInput, leaveDurationInput) {
    const startDate = parseDate(startDateInput.value);
    const endDate = parseDate(endDateInput.value);

    if (startDate && endDate && !isNaN(startDate) && !isNaN(endDate) && endDate >= startDate) {
        let totalDays = 1;
        let businessDays = 1;

        for (let date = new Date(startDate); date <= endDate; date.setUTCDate(date.getUTCDate() + 1)) {
            const formattedDate = date.toISOString().split('T')[0];
            totalDays++;
            if (!isWeekendOrHoliday(new Date(date))) {
                businessDays++;
            }
            console.log(`Date: ${formattedDate}, Total Days: ${totalDays}, Business Days: ${businessDays}`);
        }

        const resultDays = totalDays > 14 ? totalDays : businessDays;
        leaveDurationInput.value = resultDays + " days";
        console.log(`Final Result: ${resultDays} days`);
    } else {
        leaveDurationInput.value = "Invalid dates";
    }
}

function initializeDateDifferenceCalculation() {
    const startDateInput = document.getElementById("startDate");
    const endDateInput = document.getElementById("endDate");
    const leaveDurationInput = document.getElementById("leaveDuration");

    const updateLeaveDuration = () => {
        calculateDateDifference(startDateInput, endDateInput, leaveDurationInput);
    };

    if (startDateInput && endDateInput && leaveDurationInput) {
        startDateInput.addEventListener("change", updateLeaveDuration);
        endDateInput.addEventListener("change", updateLeaveDuration);
    }
}

document.addEventListener("DOMContentLoaded", function() {
    initializeDateDifferenceCalculation();
});
 