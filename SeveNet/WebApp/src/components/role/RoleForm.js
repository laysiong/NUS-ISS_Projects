import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import RoleService from '../../services/RoleService';
import { Container, Form, Button, Card} from 'react-bootstrap';

const RoleForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [type, setType] = useState('');
    const [error, setError] = useState('');
    const fixedRoleId = [1,3,4]; // Store IDs of existing roles

    useEffect(() => {
        if (id) {
            RoleService.getRoleById(id).then((response) => {
                let role = response.data;
                setType(role.type);
            }).catch(error => {
                console.error("There was an error retrieving the role!", error);
            });
        }
    }, [id]);

    const saveOrUpdateRole = (e) => {
        e.preventDefault();
        let role = { type: type };
        if (id) {
            RoleService.updateRole(id, role).then(() => {
                navigate('/roles');
            }).catch(error => {
                if (error.response && error.response.status === 409) {
                    setError(error.response.data);
                } else {
                    console.error("There was an error updating the role!", error);
                }
            });
        } else {
            RoleService.createRole(role).then(() => {
                navigate('/roles');
            }).catch(error => {
                // in spring boot we create DuplicateRoleTypeException
                // and use HttpStatus.CONFLICT (409) to represent something
                // like DuplicateRole error
                if (error.response && error.response.status === 409) {
                    setError(error.response.data);
                } else {
                    console.error("There was an error creating the role!", error);
                }
            });
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        if (name === 'type' && fixedRoleId.includes(Number(id))) {
            // Prevent rank change for existing auths
            return;
        }
        setType(value);
    };

    const cancel = () => {
        navigate('/roles');
    };

    return (
        <Container className="d-flex justify-content-center">
        <Card style={{ width: '100%', maxWidth: '400px' }}>
            <Card.Header><h2>{id ? 'Update Role' : 'Add Role'}</h2></Card.Header>
            <Form className='p-2'>
                <Form.Group>
                    <Form.Label>Type Of Role: </Form.Label>
                    <Form.Control
                        type="text"
                        value={type}
                        onChange={handleChange}
                        disabled={id && fixedRoleId.includes(Number(id))} // Disable if existing auth
                    />
                </Form.Group>
                {error && <div style={{ color: 'red' }}>{error}</div>}
                <Button className='me-2 mt-2' onClick={saveOrUpdateRole}>{id ? 'Update' : 'Save'}</Button>
                <Button className='me-2 mt-2' onClick={cancel}>Cancel</Button>
            </Form>
        </Card>
      </Container>
    );
};

export default RoleForm;
