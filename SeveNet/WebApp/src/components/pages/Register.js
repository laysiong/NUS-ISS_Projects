// src/components/auth/RegisterForm.js
import React, {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import LoginService from '../../services/LoginService';
import CountryService from "../../services/CountryService";
import { Row, Col, Container, Form, Button, Card} from 'react-bootstrap';

const RegisterForm = () => {
    const [name, setName] = useState('');
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [phoneNum, setPhoneNum] = useState('');
    const [gender, setGender] = useState('');
    const [country, setCountry] = useState('');
    const [countries, setCountries] = useState([]);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const genderOptions = ['Male', 'Female', 'Other'];

    // get country list
    useEffect(() => {
        CountryService.getAllCountries().then((response) => {
            const countryNames = response.data.map(country => country.name.common);
            setCountries(countryNames);
        });
    },[]);

    const handleRegister = (e) => {
        e.preventDefault();
        LoginService.register(name,username, email, password,phoneNum).then(() => {
            navigate('/login');
        }).catch(() => {
            setError('Failed to register');
        });
    };

    return (
        <Container className="d-flex justify-content-center">
        <Card style={{ width: '100%', maxWidth: '500px' }}>

            <Card.Header><h2>Register</h2></Card.Header>
            <Form onSubmit={handleRegister} className='p-3'>
                <Form.Group>
                    <Form.Label>Name: </Form.Label>
                    <Form.Control type="text" value={name} onChange={(e) => setName(e.target.value)} required/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Password: </Form.Label>
                    <Form.Control type="password" value={password} onChange={(e) => setPassword(e.target.value)} required/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Username: </Form.Label>
                    <Form.Control type="text" value={username} onChange={(e) => setUsername(e.target.value)} required/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Email: </Form.Label>
                    <Form.Control type="email" value={email} onChange={(e) => setEmail(e.target.value)} required/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Phone Number: </Form.Label>
                    <Form.Control type="text" value={phoneNum} onChange={(e) => setPhoneNum(e.target.value)} required/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Gender: </Form.Label>
                    <select className="form-select" name="gender" value={gender} onChange={(e) => setGender(e.target.value)}>
                        <option value="">Select Gender</option>
                        {genderOptions.map((gender) => (
                            <option key={gender} value={gender}>
                                {gender}
                            </option>
                        ))}
                    </select>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Country: </Form.Label>
                    <select className="form-select" name="country" value={country} onChange={(e) => setCountry(e.target.value)}>
                        <option value="">Select Country</option>
                        {countries.map((country) => (
                            <option key={country} value={country}>
                                {country}
                            </option>
                        ))}
                    </select>
                </Form.Group>
                {error && <div style={{color: 'red'}}>{error}</div>}
                <Button className="my-3" type="submit">Register</Button>
            </Form>

        </Card>
        </Container>
    );
};

export default RegisterForm;
