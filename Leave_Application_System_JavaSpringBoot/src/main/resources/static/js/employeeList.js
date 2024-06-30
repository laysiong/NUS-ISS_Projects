/*<![CDATA[*/

// Function to filter the employees
function filterEmployees() {
	var selectedDepartmentId = document.getElementById('departmentFilter').value;
	var selectedSupervisorId = document.getElementById('supervisorFilter').value;
	var selectedRoleId = document.getElementById('roleFilter').value;
	var allSupervisors = /*[[${dsupervisorsJson}]]*/'[]';
	allSupervisors = JSON.parse(allSupervisors);
	console.log("allSupervisors", allSupervisors);
	var rows = document.querySelectorAll('tr[data-department-id]');

	// Loop through each row in the table body
	rows.forEach(function(row) {
		var departmentId = row.getAttribute('data-department-id');
		var supervisorId = row.getAttribute('data-supervisor-id');
		var roleId = row.getAttribute('data-role-id');

		// Determine whether to show or hide the row
		var showRow = true;

		if (selectedDepartmentId !== '' && departmentId !== selectedDepartmentId) {
			showRow = false;
		}

		if (selectedSupervisorId !== '' && supervisorId !== selectedSupervisorId) {
			showRow = false;
		}

		if (selectedRoleId !== '' && roleId !== selectedRoleId) {
			showRow = false;
		}

		// Show or hide the row
		row.style.display = showRow ? '' : 'none';
	});
}

// Function to filter supervisors based on selected role and department
function filterSupervisors() {
	var selectedRoleId = document.getElementById('roleFilter').value;
	var selectedDepartmentId = document.getElementById('departmentFilter').value;

	var supervisorSelect = document.getElementById('supervisorFilter');
	supervisorSelect.innerHTML = '<option value="">All Supervisors</option>'; // Clear existing options


	allSupervisors.forEach(function(supervisor) {
		if ((selectedRoleId === '' || supervisor.role.id == selectedRoleId) &&
			(selectedDepartmentId === '' || supervisor.department.id == selectedDepartmentId)) {
			var option = document.createElement('option');
			option.value = supervisor.id;
			option.text = supervisor.name;
			supervisorSelect.appendChild(option);
		}
	});

	// Call filterEmployees to apply the filter after updating supervisors
	filterEmployees();
}

// Add event listeners to the filters
document.getElementById('departmentFilter').addEventListener('change', function() {
	filterSupervisors();
});
document.getElementById('supervisorFilter').addEventListener('change', filterEmployees());
document.getElementById('roleFilter').addEventListener('change', function() {
	filterSupervisors();
});
// reset button
document.getElementById('resetButton').addEventListener('click', function() {
	window.location.href = '/employee';
});

filterSupervisors();
/*]]>*/