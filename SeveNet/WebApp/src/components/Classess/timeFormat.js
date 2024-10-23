import React from 'react';

const TimeFormat = ({msgtimeStamp}) => {
    const formatDate = (dateString) => {
        const options = { 
            year: "numeric", 
            month: "long", 
            day: "numeric",
            hour: '2-digit', 
            minute: '2-digit', 
            hour12: false 
        };
        return new Date(dateString).toLocaleDateString(undefined, options)
    }

    return (
        <p className="text-muted" style={{ margin: '0', padding: '0' }}>{formatDate(msgtimeStamp)}</p>
    );
};

export default TimeFormat;