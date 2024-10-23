function calculateDateDifference(startDateInput, endDateInput, leaveDurationInput) {
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
});
