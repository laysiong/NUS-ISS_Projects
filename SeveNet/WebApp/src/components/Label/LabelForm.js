import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import LabelService from "../../services/LabelService";
import {Row, Col, Container, Form, Button, Card} from 'react-bootstrap';

const LabelForm = () => {
    const {id} = useParams();
    const navigate = useNavigate();
    const [taglabel, setLabel] = useState({
        label:'',
        penaltyScore:'',
        colorCode: '#ffffff' 
    });
    const [error, setError] = useState('');


    useEffect(() => {
        if(id){
            LabelService.getLabelsById(id).then((response) => {
                let label = response.data;
                console.log("API Response:", label);
                setLabel({
                    label: label.label || '',
                    penaltyScore: label.penaltyScore || '',
                    colorCode: label.colorCode || '#ffffff' 
                })
            }).catch(error => {
                console.error("There was an error retrieving the label!", error);
            });
        }
    },[id]);

    const handleChange = (event) => {
        const { name, value } = event.target;
        setLabel(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const saveOrUpdateLabel = (e) => {
        e.preventDefault(); // Prevent default form submission

        if (id) {
            LabelService.updateLabels(id, taglabel).then(() => {
                navigate('/labellist');
            }).catch(error => {
                if (error.response && error.response.status === 409) {
                    setError(error.response.data);
                } else {
                    console.error("There was an error updating the label", error);
                }
            });
        } else {
            LabelService.createLabels(taglabel).then(() => {
                navigate('/labellist');
            }).catch(error => {
                if (error.response && error.response.status === 409) {
                    setError(error.response.data);
                } else {
                    console.error("There was an error creating the label!", taglabel);
                }
            });
        }
    };


    const cancel = () => {
        navigate('/labellist');
    }

    return(
        <Container className="d-flex justify-content-center">
        <Card style={{ width: '100%', maxWidth: '50vh' }}>

            <Card.Header><h2>{id ? 'Update Label' : 'Add Label'}</h2></Card.Header>
            <Form className='p-3'>
                <Form.Group>
                    <Form.Label>Label: </Form.Label>
                    <Form.Control  type="text" name="label" value={taglabel.label} onChange={handleChange}/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Penalty Score: </Form.Label>
                    <Form.Control  type="number" name="penaltyScore" value={taglabel.penaltyScore} onChange={handleChange}/>
                </Form.Group>
                <Form.Group>
                    <Form.Label>Color: </Form.Label>
                    <Row className="align-items-center">
                        <Col xs="auto">
                            <Form.Control  
                                type="color" 
                                name="colorCode" 
                                value={taglabel.colorCode}
                                onChange={handleChange} 
                            />
                        </Col>
                        <Col xs="auto">
                            <Form.Control  
                                type="text" 
                                value={taglabel.colorCode} 
                                readOnly 
                                style={{ width: '80px' }}
                            />
                        </Col>
                    </Row>
                </Form.Group>
                {error && <div style={{ color: 'red' }}>{error}</div>}
                <Button className='me-2 mt-2' onClick={saveOrUpdateLabel}>{id ? 'Update' : 'Save'}</Button>
                <Button className='me-2 mt-2' onClick={cancel}>Cancel</Button>
            </Form>
        </Card>
        </Container>
    );

};

export default LabelForm;