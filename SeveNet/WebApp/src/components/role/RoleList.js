// src/components/RoleList.js
import React, { Component } from 'react';
import RoleService from '../../services/RoleService';
import { useNavigate } from 'react-router-dom';
import { Container, Button, Table } from 'react-bootstrap';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import CreateIcon from '@mui/icons-material/Create';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';

const fixedRoleIds = [1, 3, 4]; // Store IDs of existing auths

class RoleList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            roles: []
        };
    }

    componentDidMount() {
        this.loadRoles();
    }

    loadRoles() {
        RoleService.getAllRoles().then((response) => {
            this.setState({ roles: response.data });
        }).catch((error) => {
            console.error('There was an error!', error);
        });
    }

    deleteRole(id) {
        if (fixedRoleIds.includes(id)) {
            alert('This role cannot be deleted.');
            return;
        }

        RoleService.deleteRole(id).then(() => {
            this.loadRoles();
        }).catch((error) => {
            console.error('There was an error!', error);
        });
    }


    render() {
        const { navigate } = this.props;
        return (
            <Container className='contentDiv'>
                <h2>Role List</h2>
                <Button className='mb-3 btn-dark' onClick={() => navigate('/roles/add')}> <AddCircleIcon className='icon-thing'/> Add Role</Button>
                <Table bordered hover className="w-30">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Type</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.state.roles.map(role => (
                        <tr key={role.id}>
                            <td>{role.id}</td>
                            <td>{role.type}</td>
                            <td>
                                <Button className='me-2' onClick={() => navigate(`/roles/edit/${role.id}`)}><CreateIcon className='icon-thing'/>Edit</Button>
                                <Button onClick={() => this.deleteRole(role.id)}><DeleteOutlineIcon className='icon-thing'/>Delete</Button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                    </Table>
            </Container>
        );
    }
}

const RoleListWithNavigate = (props) => {
    const navigate = useNavigate();
    return <RoleList {...props} navigate={navigate} />;
};

export default RoleListWithNavigate;
