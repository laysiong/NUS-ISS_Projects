import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '@fortawesome/fontawesome-free/css/all.min.css';

const SearchBar = () => {
    const [keyword, setKeyword] = useState('');
    const navigate = useNavigate();

    const handleSearch = (e) => {
        e.preventDefault();
        if (keyword.trim()) {
            navigate(`/search?keyword=${keyword}`);
        }
    };

    return (
        <form onSubmit={handleSearch}>
            <div className="input-group">
                <input
                    type="text"
                    value={keyword}
                    className="form-control mb-3 me-2"
                    placeholder="Enter keyword..."
                    onChange={(e) => setKeyword(e.target.value)}
                />
                <div className="input-group-append">
                    <button type="submit" className="btn btn-primary">
                        <i className="fas fa-search"></i>
                    </button>
                </div>
            </div>
        </form>
    );
};

export default SearchBar;
