// src/components/auth/LoginForm.js
import React, {useEffect, useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import LoginService from '../../services/LoginService';
import {useAuth} from "../../services/AuthContext";
import { Container, Form, Button, Card} from 'react-bootstrap';

const LoginForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const { currentUser,setCurrentUser } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (currentUser) {
            navigate(currentUser.role.type === "User" ? '/mainmenu'  :'/dashboard');
        }
    }, [currentUser,navigate]);

    const handleLogin = (e) => {
        e.preventDefault();
        LoginService.login(username, password).then((response) =>
            {
                setCurrentUser(response);
            }).catch((e) => {
                setError(e.response.data.message);
            });

    };

    return (
        // <Container  className="contentDiv">
        //     <h2>Login</h2>
        //     <Form onSubmit={handleLogin}>
        //         <Form.Group>
        //             <Form.label>Username: </Form.label>
        //             <Form.Control type="text" value={username} onChange={(e) => setUsername(e.target.value)} required />
        //         </Form.Group>
        //         <Form.Group>
        //             <Form.label>Password: </Form.label>
        //             <Form.Control type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
        //         </Form.Group>
        //         {error && <div style={{ color: 'red' }}>{error}</div>}
        //         <Button variant="primary" type="submit">Login</Button>

        //         <Link to={"/register"}>Haven't Signed in yet? Register Now!</Link>
        //     </Form>
        // </Container >
        
        <Container className="d-flex justify-content-center">
        <Card style={{ width: '100%', maxWidth: '400px' }}>
          <Card.Body>
            <h2 className="text-center mb-4">Login</h2>
            <Form onSubmit={handleLogin}>
              <Form.Group>
                <Form.Label>Username</Form.Label>
                <Form.Control
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                />
              </Form.Group>
              <Form.Group>
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </Form.Group>
              {error && <div style={{ color: 'red' }}>{error}</div>}
              <Button variant="primary" type="submit" className="w-100 mt-3">
                Login
              </Button>
            </Form>
            <div className="text-center mt-3">
              <Link to="/register">Haven't Signed in yet? Register Now!</Link>
            </div>
          </Card.Body>
        </Card>
      </Container>

    );
};

export default LoginForm;
