// FilterableUserDetails.js
import React, { useState } from 'react';
import AllUserDetails from './AllUserDetails'; // Ensure the path is correct

const FilterableUserDetails = () => {
    const [statusFilter, setStatusFilter] = useState('All');

    const handleStatusChange = (event) => {
        setStatusFilter(event.target.value);
    };

    return (
        <div className="contentDiv">
            <div className="filter">
                <label htmlFor="statusFilter">Filter by Status: </label>
                <select id="statusFilter" value={statusFilter} onChange={handleStatusChange}>
                    <option value="All">All</option>
                    <option value="Default">Default</option>
                    <option value="Blocked">Blocked</option>
                    {/* Add other statuses as needed */}
                </select>
            </div>
            <AllUserDetails statusFilter={statusFilter} />
        </div>
    );
};

export default FilterableUserDetails;
