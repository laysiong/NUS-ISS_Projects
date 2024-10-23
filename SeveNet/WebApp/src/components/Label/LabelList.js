import React, { Component } from 'react';
import { useNavigate } from 'react-router-dom';
import LabelService from '../../services/LabelService';

import { Container, Button, Table } from 'react-bootstrap';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import CreateIcon from '@mui/icons-material/Create';
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';

class LabelList extends Component {
    constructor(props){
        super(props);
        this.state = {
            labels:[]
        }
    }

    componentDidMount(){
        this.loadLabel();
    }

    loadLabel(){
        LabelService.getAllLabels().then((response) => {
            this.setState({labels: response.data});
        }).catch((error) => {
            console.error('There was an error!', error);
        });
    }

    handleColorChange = (id, event) => {
        const newColor = event.target.value;
        this.setState((prevState) => ({
            labels: prevState.labels.map((label) =>
                label.id === id ? { ...label, colorCode: newColor } : label
            ),
        }),() => {
            const updatedLabel = this.state.labels.find(label => label.id === id);

            // Send the updated label to the backend
            LabelService.updateLabels(id, updatedLabel).then(() => {
                console.log('Color updated successfully');
            }).catch((error) => {
                console.error('Error updating color:', error);
            });
        });
    }

    render () {
        const { navigate } = this.props;
        return(
            <Container className="contentDiv" style={{width:'40%'}}>
                <h2>Label List</h2>
                <Button className='mb-3 btn-dark' onClick={() => navigate('/labels/add')}><AddCircleIcon className='icon-thing'/>Add New Label</Button>
                <Table bordered hover>
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Label</th>
                            <th>Penalty Score</th>
                            <th>Color Code</th>
                        </tr>
                    </thead>
                    <tbody>
                    {this.state.labels.map(label => (
                        <tr key={label.id}>
                            <td>{label.id}</td>
                            <td>{label.label}</td>
                            <td>{label.penaltyScore}</td>
                            <td>
                                <input
                                    type="color"
                                    value={label.colorCode}
                                    onChange={(event) => this.handleColorChange(label.id, event)}
                                />
                            </td>
                            <td>
                                <Button className='me-2'  onClick={() => navigate(`/labels/edit/${label.id}`)}> <CreateIcon className='icon-thing'/>Edit</Button>
                                <Button className='btn-danger'  onClick={() => this.deleteLabel(label.id)}> <DeleteOutlineIcon className='icon-thing'/>Delete</Button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                    </Table>
        </Container>
        );
    }

    deleteLabel(id){
        LabelService.deleteLabels(id).then(() => {
            this.loadLabel();
        });
    }
};

const LabelListWithNavigate = (props) => {
    const navigate = useNavigate();
    return <LabelList {...props} navigate={navigate}/>;
}

export default LabelListWithNavigate;

