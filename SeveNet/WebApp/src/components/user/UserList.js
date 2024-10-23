import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import EmployeeService from '../../services/EmployeeService';
import Modal from '../common/Modal';
import './UserList.css';
import LoginService from "../../services/LoginService";

import { Container, Button, Table } from 'react-bootstrap';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import RemoveCircleOutlineIcon from '@mui/icons-material/RemoveCircleOutline';
import CreateIcon from '@mui/icons-material/Create';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import CircleNotificationsIcon from '@mui/icons-material/CircleNotifications';
import PageviewIcon from '@mui/icons-material/Pageview';
import currentUser from "../customhook/CurrentUser";
import useCurrentUser from "../customhook/CurrentUser";
import {useAuth} from "../../services/AuthContext";

const EmployeeList = () => {
    const [employees, setEmployees] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [modalAction, setModalAction] = useState(null);
    const navigate = useNavigate();
    const {currentUser} = useAuth();

    useEffect(() => {
        loadEmployees();
    }, []);

    const loadEmployees = () => {
        EmployeeService.getAllEmployees().then((response) => {
            // hide those users been deleted
            setEmployees(response.data);
        });
    };

    const deleteEmployee = (id) => {
        setModalAction(() => () => {
            EmployeeService.updateEmployeeStatus(id,"delete").then(() => {
                setEmployees(employees.filter(employee => employee.id !== id));
                closeModal();
            });
            // if moderator is now deleting him/herself once confirm he no longer has the permission to access
            // we will navigate him to login page
            if (id === currentUser.id) {
                LoginService.logout();
                navigate('/login');
                window.location.reload();
            }
        });
        setIsModalOpen(true);
    };

    const banEmployee = (id,status) => {
        setModalAction(() => () => {
            EmployeeService.updateEmployeeStatus(id,status === "ban" ? "active" : "ban").then(() => {
                loadEmployees();
                closeModal();
            });
        });
        setIsModalOpen(true);
    };

    const editEmployee = (id) => {
        navigate(`/users/edit/${id}`);
    };

    const addEmployee = () => {
        navigate('/users/add');
    };

    const viewEmployee = (id) => {
        navigate(`/userProfile/${id}`);
    };

    const notifyEmployee = (id) => {
      navigate(`/sendNotification/${id}`);
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setModalAction(null);
    };

    const confirmModalAction = () => {
        if (modalAction) {
            modalAction();
        }
    };

    return (
        <>
        <Container style={{width:'100%'}}>
        <h2>User List</h2>
                    <Button className='mb-3 btn-dark'  onClick={addEmployee}><AddCircleIcon className='icon-thing'/>Add User</Button>
             
                {employees.length === 0 ? (
                    <p>No users found</p>
                ) : (
                    <Table bordered hover>
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>SocialScore</th>
                            <th>Username</th>
                            <th>JoinDate</th>
                            <th>Role</th>
                            <th>Authorization</th>
                            <th>Status</th>
                            <th>Detail</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {employees.map(employee => (
                            <tr key={employee.id}>
                                <td>{employee.name}</td>
                                <td>{employee.socialScore}</td>
                                <td>{employee.username}</td>
                                <td>{employee.joinDate}</td>
                                <td>{employee.role ? employee.role.type : 'No Role'}</td>
                                <td>{employee.auth ? employee.auth.rank : 'No Auth'}</td>
                                <td>{employee.status}</td>
                                <td>
                                    <Button className="btn view-btn me-2" onClick={() => viewEmployee(employee.id)}>
                                        <PageviewIcon className='icon-thing'/>
                                        Detail</Button>
                                </td>
                                <td>
                                    <Button className="me-2" onClick={() => editEmployee(employee.id)}>
                                        <CreateIcon className='icon-thing'/>Edit
                                    </Button>
                                    <Button className="btn-success me-2" onClick={() => notifyEmployee(employee.id)}>
                                        <CircleNotificationsIcon className='icon-thing'/>
                                        Notify
                                    </Button>
                                    <Button className='btn-danger me-2'
                                            onClick={() => deleteEmployee(employee.id)}> 
                                            <DeleteOutlineIcon className='icon-thing'/>Delete
                                    </Button>
                                    <Button className="btn-dark"
                                            onClick={() => banEmployee(employee.id, employee.status)}>
                                        <RemoveCircleOutlineIcon className='icon-thing'/>
                                        {employee.status === "ban" ? "Activate" : "Ban"}
                                    </Button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </Table>
                )}
                <Modal
                    isOpen={isModalOpen}
                    onClose={closeModal}
                    onConfirm={confirmModalAction}
                    title="Confirm Action"
                >
                    <p>Are you sure you want to proceed with this action?</p>
                </Modal>
            </Container>
        </>
    );
};

export default EmployeeList;
