import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import RouteSelectionForm from './RouteSelectionForm';
import {useAuth} from "../../services/AuthContext";
import {getAllRoutes} from "../../utils/allRoutes";
import { Container, Form, Button, Card} from 'react-bootstrap';

const AuthForm = () => {
    const { id } = useParams();
    const { currentUser,setCurrentUser } = useAuth();
    const navigate = useNavigate();
    const fixedAuthIds = [1,2,4,5,6,8,9,10]; // Store IDs of existing auths
    const [auth, setAuth] = useState({
        rank: '',
        menuViewJason: '[]',
    });

    // routes controls the whole routes
    // using the structure below:
    const [routes] = useState(getAllRoutes);
    const [selectedRoutes, setSelectedRoutes] = useState([]);

    useEffect(() => {
        if (id) {
            AuthService.getAuthById(id).then((response) => {
                const authData = response.data;
                setAuth(authData);
                setSelectedRoutes(JSON.parse(authData.menuViewJason));
            });
        }
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        if (name === 'rank' && fixedAuthIds.includes(Number(id))) {
            // Prevent rank change for existing auths
            return;
        }
        setAuth((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handleRoutesChange = (newRoutesJson) => {
        setSelectedRoutes(JSON.parse(newRoutesJson));
    };

    const saveOrUpdateAuth = (e) => {
        e.preventDefault();
        const updatedAuth = { ...auth, menuViewJason: JSON.stringify(selectedRoutes) };
        if (id) {
            AuthService.updateAuth(id, updatedAuth).then(() => {
                // update user session if the updated user is the one who logged in
                // if(JSON.stringify(currentUser.auth.id) === id){
                //     currentUser.auth = updatedAuth;
                //     console.log("newUser",currentUser);
                //     setCurrentUser(JSON.stringify(currentUser));
                //     sessionStorage.setItem('currentUser', JSON.stringify(currentUser));
                // }
                navigate('/auths');
            });
        } else {
            AuthService.createAuth(updatedAuth).then(() => {
                navigate('/auths');
            });
        }
    };

    const cancel = () => {
        navigate('/auths');
    };

    const getAllPaths = (routes) => {
        return routes.flatMap(route => {
            const childrenPaths = route.children ? getAllPaths(route.children) : [];
            return [route.path, ...childrenPaths];
        });
    };

    return (
        <Container className="d-flex justify-content-center">
            <Card style={{ width: '100%', maxWidth: '500px' }}>

            <Card.Header><h2>{id ? 'Update Auth' : 'Add Auth'}</h2></Card.Header>
            <Form className='p-3' onSubmit={saveOrUpdateAuth}>
                <Form.Group>
                    <Form.Label>Rank: </Form.Label>
                    <Form.Control
                        type="text"
                        name="rank"
                        value={auth.rank}
                        onChange={handleChange}
                        disabled={id && fixedAuthIds.includes(Number(id))} // Disable if existing auth
                    />
                </Form.Group>
                <Form.Group>
                    <RouteSelectionForm
                        routes={routes}
                        initialSelectedRoutes={getAllPaths(selectedRoutes)}
                        onChange={handleRoutesChange}
                    />
                </Form.Group>
                <Button className="me-2" type="submit">{id ? 'Update' : 'Save'}</Button>
                <Button type="button" onClick={cancel}>Cancel</Button>
            </Form>
            </Card>
        </Container>
    );
};

export default AuthForm;
