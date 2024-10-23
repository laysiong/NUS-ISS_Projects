import React, {useEffect, useState} from 'react';
import EmployeeService from "../../services/EmployeeService";
import NotificationService from "../../services/NotificationService.";
import {useParams} from "react-router-dom";
import { Container, Form, Button, Card} from 'react-bootstrap';

const SendNotification = () => {
    const [query, setQuery] = useState('');
    const [userOptions, setUserOptions] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    const [title, setTitle] = useState('');
    const [message, setMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const { id } = useParams();

    const handleSearch = async (e) => {
        setQuery(e.target.value);
        // to prevent too much api requests
        if (e.target.value.length > 2) {
            setIsLoading(true);
            // find matched users
            await EmployeeService.getEmployeeByKeyWord(e.target.value).then((response) => {
                setUserOptions(response.data);
            }).catch((error) => {
                console.error('Error fetching users:', error);
            }).finally(()=>setIsLoading(false));
        } else {
            setUserOptions([]);
        }
    };

    const handleSelectUser = (user) => {
        setSelectedUser(user);
        setQuery(user.username); // or setQuery(user.name) based on preference
        setUserOptions([]);
    };

    const handleSendNotification = async () => {
        if (selectedUser && title && message) {
            // Verify that the selectedUser corresponds to the current query
            if (query !== `${selectedUser.username}`) {
                alert('Selected user does not match the input. Please select a user from the dropdown.');
                return;
            }
            const notification = {
                notificationUser: { id: selectedUser.id },
                title: title,
                message: message,
                // other notification fields if needed
            };
            await NotificationService.saveNotification(notification).then((response) => {
                alert('Notification sent successfully');
                // clean the notification form
                setTitle('');
                setMessage('');
                setSelectedUser(null);
                setQuery('');
            }).catch((error) => {
                console.error('Error sending notification:', error);
                alert('Failed to send notification');
            });
        } else {
            alert('Please select a user, enter a title, and a message');
        }
    };

    useEffect(()=>{
        if (id) {
            EmployeeService.getEmployeeById(id).then((response)=>{
                setQuery(response.data.username);
                setSelectedUser(response.data);
            }).catch((error) => {
                console.log("Error fetching user with id: " + id, error)
                alert("Error fetching user with id: " + id);
            });
        }
    },[id]);

    return (
        <Container className="d-flex justify-content-center">
            <Card style={{ width: '100%', maxWidth: '50vh' }}>

            <Card.Header><h2>Send Notification</h2></Card.Header>
            <Form className='p-3'>
            <div style={styles.formGroup}>
                <label>User:</label>
                <input
                    type="text"
                    value={query}
                    onChange={handleSearch}
                    placeholder="Search user by name or username"
                    style={styles.input}
                />
                {isLoading && <p>Loading...</p>}
                {userOptions.length > 0 && (
                    <ul style={styles.dropdown}>
                        {userOptions.map(user => (
                            <li key={user.id} onClick={() => handleSelectUser(user)} style={styles.dropdownItem}>
                                {user.username} - {user.name}
                            </li>
                        ))}
                    </ul>
                )}
            </div>
            <div style={styles.formGroup}>
                <label>Title:</label>
                <input
                    type="text"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    placeholder="Enter notification title"
                    style={styles.input}
                />
            </div>
            <div style={styles.formGroup}>
                <label>Message:</label>
                <textarea
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    placeholder="Enter your notification message"
                    style={styles.textarea}
                />
            </div>
            <button onClick={handleSendNotification} style={styles.button}>Send Notification</button>
            </Form>
            </Card>

        </Container>
    );
};

const styles = {
    container: {
        width: '80%',
        margin: '0 auto',
        padding: '20px',
        border: '1px solid #ccc',
        borderRadius: '5px',
    },
    formGroup: {
        marginBottom: '15px',
        position: 'relative',
    },
    input: {
        width: '100%',
        padding: '8px',
        boxSizing: 'border-box',
    },
    textarea: {
        width: '100%',
        height: '100px',
        padding: '8px',
        boxSizing: 'border-box',
    },
    button: {
        padding: '10px 15px',
        backgroundColor: '#28a745',
        color: '#fff',
        border: 'none',
        borderRadius: '3px',
        cursor: 'pointer',
    },
    dropdown: {
        listStyle: 'none',
        padding: '0',
        margin: '0',
        border: '1px solid #ccc',
        maxHeight: '150px',
        overflowY: 'auto',
        position: 'absolute',
        width: '100%',
        backgroundColor: '#fff',
        zIndex: 1,
    },
    dropdownItem: {
        padding: '8px',
        cursor: 'pointer',
    },
};

export default SendNotification;
