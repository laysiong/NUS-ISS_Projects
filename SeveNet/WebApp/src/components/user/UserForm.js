// src/components/employee/EmployeeForm.js
import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import EmployeeService from '../../services/EmployeeService';
import CheckUserThenBanService from '../../services/CheckUserThenBanService';
import RoleService from '../../services/RoleService';
import AuthService from '../../services/AuthService';
import CountryService from '../../services/CountryService';
import {useAuth} from "../../services/AuthContext";
import PasswordUtils from '../../utils/pwdEncryption';
import {getUserAllStatus} from "../../utils/userAllStatus";
import { Row, Col, Container, Form, Button, Card} from 'react-bootstrap';
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';


const EmployeeForm = () => {
    const { currentUser,setCurrentUser } = useAuth();
    const [statuses, setStatuses]  = useState(getUserAllStatus);
    const { id } = useParams();
    const navigate = useNavigate();
    const [employee, setEmployee] = useState({
        name: '',
        email: '',
        username: '',
        country: '',
        blockList: '',
        gender: '',
        socialScore: 120,
        status: 'active',
        password: '',
        phoneNum: '',
        joinDate: '',
        role: { id: '' },
        auth: { id: '' },
    });
    const [roles, setRoles] = useState([]);
    const [auths, setAuths] = useState([]);
    const [countries, setCountries] = useState([]);
    const [showPassword, setShowPassword] = useState(false);
    const [plainPassword, setPlainPassword] = useState('');
    const [isScoreChanged, setIsScoreChanged] = useState(false);
    const [isAuthChanged, setIsAuthChanged] = useState(false);
    const [originalSocialScore, setOriginalSocialScore] = useState(employee.socialScore);
    const [originalAuth, setOriginalAuth] = useState(employee.auth);

    const genderOptions = ['Male', 'Female', 'Other'];

    useEffect(() => {
        RoleService.getAllRoles().then((response) => {
            setRoles(response.data);
        });

        AuthService.getAllAuths().then((response) => {
            setAuths(response.data);
        });

        CountryService.getAllCountries().then((response) => {
            const countryNames = response.data.map(country => country.name.common);
            setCountries(countryNames);
        });

        if (id) {
            EmployeeService.getEmployeeById(id).then((response) => {
                let employeeData = response.data;
                setEmployee({
                    ...employeeData,
                    role: employeeData.role || { id: '' },
                    auth: employeeData.auth || { id: '' },
                });
                setOriginalSocialScore(employeeData.socialScore); // Store original score
                setOriginalAuth(employeeData.auth);
            });
        } else {
            // when creating new user, his/her status can only be active
            setStatuses(["active"])
        }
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;

        if (name === "socialScore" && parseInt(value) !== originalSocialScore) {
            setIsScoreChanged(true);
        } else if (name === "socialScore" && parseInt(value) === originalSocialScore) {
            setIsScoreChanged(false);
        }

        setEmployee((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    const handlePwdChange = (e) => {
        const pwd = e.target.value;
        setPlainPassword(pwd);
        setEmployee((prevState) => ({
            ...prevState,
            password: PasswordUtils.encryptUserPassword(pwd),
        }));
    };

    const handleRoleChange = (e) => {
        const roleId = e.target.value;
        const selectedRole = roles.find(role => role.id === parseInt(roleId));
        setEmployee((prevState) => ({
            ...prevState,
            role: selectedRole || { id: '' },
        }));
    };

    const handleAuthChange = (e) => {
        const authId = e.target.value;
        const selectedAuth = auths.find(auth => auth.id === parseInt(authId));

        if (selectedAuth && selectedAuth.id !== originalAuth.id) {
            setIsAuthChanged(true);
        } else if (selectedAuth && selectedAuth.id === originalAuth.id) {
            setIsAuthChanged(false);
        }

        setEmployee((prevState) => ({
            ...prevState,
            auth: selectedAuth || { id: '' },
        }));
    };

    const toggleShowPassword = () => {
        setShowPassword(!showPassword);
    };

    // in situations like when the admin login the system and want to change his/her
    // own auth ,the navigation bar and url should change immediately when he clicks
    // the update button, in other words:
    // the employee should be the one who has logged and also the one being updated
    const saveOrUpdateEmployee = (e) => {
        e.preventDefault();
        if (id) {
            if (isScoreChanged) {
                console.log("here dude");
                EmployeeService.updateEmployee(id, employee).then(() => {
                    // add changeAndBan APi
                    CheckUserThenBanService.updateUserAuthAndNotify(id).then(() => {}).catch((error) => {
                        console.log("Error updating Authorization with userId: "+ id,error);
                    });
                    // EmployeeService.getEmployeeById(id).then((response) => {
                    //     const { auth,role, username } = response.data;  // retrieve id & auth
                    //     // if the updated employee is the one who logging in
                    //     if(currentUser.username === username){
                    //         setCurrentUser(JSON.stringify({ id, auth,role, username }));
                    //         sessionStorage.setItem('currentUser', JSON.stringify({ id, auth,role, username }));
                    //     }
                    //     navigate('/users');
                    // }).catch((error) => {
                    //     console.log("Error fetching User with id: "+ id,error);
                    // });
                    navigate('/users');
                }).catch((error) => {
                    console.log("Error Updating User with id: "+ id,error);
                });
            } else {
                console.log("here dude2");
                EmployeeService.updateEmployee(id, employee).then(() => {
                    navigate('/users');
                }).catch((error) => {
                    console.log("Error Updating User with id: "+ id,error);
                });
            }
        } else {
            EmployeeService.createEmployee(employee).then(() => {
                navigate('/users');
            });
        }
    };

    const cancel = () => {
        navigate('/users');
    };

    return (
        <Container className="d-flex justify-content-center">
        <Card style={{ width: '100%', maxWidth: '500px' }}>
           
            <Card.Header><h2>{id ? 'Update User' : 'Add User'}</h2></Card.Header>
            <Form className='p-3'>
                <Form.Group>
                    <Form.Label>Name: </Form.Label>
                    <Form.Control type="text" name="name" value={employee.name} onChange={handleChange}/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Gender: </Form.Label>
                    <select className="form-select"  name="gender" value={employee.gender} onChange={handleChange}>
                        <option value="">Select Gender</option>
                        {genderOptions.map((gender) => (
                            <option key={gender} value={gender}>
                                {gender}
                            </option>
                        ))}
                    </select>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Email: </Form.Label>
                    <Form.Control type="email" name="email" value={employee.email} onChange={handleChange}/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Username: </Form.Label>
                    <Form.Control type="text" name="username" value={employee.username} onChange={handleChange}/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Password:</Form.Label>
                    <Row>
                        <Col xs={9} className="pr-1">
                            <Form.Control
                                type={showPassword ? 'text' : 'password'}
                                name="password"
                                value={plainPassword ? plainPassword : PasswordUtils.decryptUserPassword(employee.password)}
                                onChange={handlePwdChange}
                            />
                        </Col>
                        <Col xs={3}>
                            <Button type="button" onClick={toggleShowPassword} className="w-100">
                                {showPassword ? <VisibilityOffIcon /> : <VisibilityIcon />}
                            </Button>
                        </Col>
                    </Row>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Phone Number: </Form.Label>
                    <Form.Control type="text" name="phoneNum" value={employee.phoneNum} onChange={handleChange}/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Country: </Form.Label>
                    <select className="form-select" name="country" value={employee.country} onChange={handleChange}>
                        <option value="">Select Country</option>
                        {countries.map((country) => (
                            <option key={country} value={country}>
                                {country}
                            </option>
                        ))}
                    </select>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Join Date: </Form.Label>
                    <Form.Control type="date" name="joinDate" value={employee.joinDate} onChange={handleChange}/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Role: </Form.Label>
                    <select  className="form-select" name="role" value={employee.role.id} onChange={handleRoleChange}>
                        <option value="">Select Role</option>
                        {roles.map(role => (
                            <option key={role.id} value={role.id}>{role.type}</option>
                        ))}
                    </select>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Auth: </Form.Label>
                    <select  className="form-select" name="auth" value={employee.auth.id} onChange={handleAuthChange}>
                        <option value="">Select Auth</option>
                        {auths.map(auth => (
                            <option key={auth.id} value={auth.id}>{auth.rank}</option>
                        ))}
                    </select>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Social Score: </Form.Label>
                    <Form.Control type="text" name="socialScore" value={employee.socialScore == null ? 0 : employee.socialScore} onChange={handleChange}/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Status: </Form.Label>
                    <select className="form-select" name="status" value={employee.status} onChange={handleChange}>
                        <option value="">Select Status</option>
                        {statuses.map((s) => (
                            <option key={s} value={s}>
                                {s}
                            </option>
                        ))}
                    </select>
                </Form.Group>
                <Button className="my-3" onClick={saveOrUpdateEmployee}>{id ? 'Update' : 'Save'}</Button>
                <Button className="mx-2 my-3" onClick={cancel}>Cancel</Button>
            </Form>
            
        </Card>
        </Container>
    );
};

export default EmployeeForm;
