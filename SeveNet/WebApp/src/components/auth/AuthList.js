import React, { useState, useEffect } from 'react';
import AuthService from '../../services/AuthService';
import { useNavigate } from 'react-router-dom';
import { Container, Button, Table } from 'react-bootstrap';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import CreateIcon from '@mui/icons-material/Create';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';

const AuthList = () => {
    const [auths, setAuths] = useState([]);
    const [loading, setLoading] = useState(true); // loading status
    const navigate = useNavigate();
    const fixedAuthIds = [1, 2, 4, 5, 6, 8, 9, 10]; // Store IDs of existing auths

    useEffect(() => {
        loadAuths();
    }, []);

    const loadAuths = () => {
        AuthService.getAllAuths()
            .then((response) => {
                const authsWithParsedMenu = response.data.map(auth => ({
                    ...auth,
                    parsedMenu: JSON.parse(auth.menuViewJason || '[]'),
                    isExpanded: false
                }));
                setAuths(authsWithParsedMenu);
                setLoading(false);
            })
            .catch(error => {
                console.error('Failed to load auths:', error);
                setLoading(false);
            });
    };

    const addAuth = () => {
        navigate('/auths/add');
    };

    const editAuth = (id) => {
        navigate(`/auths/edit/${id}`);
    };

    const deleteAuth = (id) => {
        // can not delete the fixed auths
        if (fixedAuthIds.includes(id)) {
            alert('This auth cannot be deleted.');
            return;
        }

        AuthService.deleteAuth(id)
            .then(() => {
                setAuths(prevAuths => prevAuths.filter(auth => auth.id !== id));
            })
            .catch(error => {
                console.error('Failed to delete auth:', error);
            });
    };

    // const toggleExpand = (index) => {
    //     setAuths(prevAuths =>
    //         prevAuths.map((auth, i) => (
    //             i === index ? { ...auth, isExpanded: !auth.isExpanded } : auth
    //         ))
    //     );
    // };

    return (
        <Container className="contentDiv" style={{width:'50%'}}>
            <h2>Auth List</h2>
            <Button className='mb-3 btn-dark'  onClick={addAuth}><AddCircleIcon className='icon-thing'/>Add Auth</Button>
            {loading ? (
                <p>Loading...</p>
            ) : auths.length === 0 ? (
                <p>No auths found.</p>
            ) : (
                <Table bordered hover>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Rank</th>
                        <th>Menu View JSON</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {auths.map((auth, index) => (
                        <tr key={auth.id}>
                            <td>{auth.id}</td>
                            <td>{auth.rank}</td>
                            <td>
                                {/*<button onClick={() => toggleExpand(index)}>*/}
                                {/*    {auth.isExpanded ? 'Collapse' : 'Expand'}*/}
                                {/*</button>*/}
                                {/*{auth.isExpanded && (*/}
                                {/*    <ul>*/}
                                {/*        {auth.parsedMenu.map((menuItem, i) => (*/}
                                {/*            <li key={i}>*/}
                                {/*                {menuItem.menuName}*/}
                                {/*                {menuItem.children && (*/}
                                {/*                    <ul>*/}
                                {/*                        {menuItem.children.map((child, j) => (*/}
                                {/*                            <li key={j}>{child.menuName}</li>*/}
                                {/*                        ))}*/}
                                {/*                    </ul>*/}
                                {/*                )}*/}
                                {/*            </li>*/}
                                {/*        ))}*/}
                                {/*    </ul>*/}
                                {/*)}*/}

                                <ul>
                                    {auth.parsedMenu.map((menuItem, i) => (
                                        <li key={i}>
                                            {menuItem.menuName}
                                            {menuItem.children && (
                                                <ul>
                                                    {menuItem.children.map((child, j) => (
                                                        <li key={j}>{child.menuName}</li>
                                                    ))}
                                                </ul>
                                            )}
                                        </li>
                                    ))}
                                </ul>
                            </td>
                            <td>
                                <Button className='me-2 mb-2 w-100' onClick={() => editAuth(auth.id)}> <CreateIcon className='icon-thing'/>Edit</Button>
                                <Button className='btn-danger w-100' onClick={() => deleteAuth(auth.id)}> <DeleteOutlineIcon className='icon-thing'/>Delete</Button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            )}
        </Container>
    );
};

export default AuthList;
