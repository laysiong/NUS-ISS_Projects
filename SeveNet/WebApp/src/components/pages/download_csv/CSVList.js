
import React from 'react';
import CSVService from '../../../services/CSVService';
import { Container, Button, Table } from 'react-bootstrap';

const CSVList = () => {

    const downloadCsv = async (type) => {
        try {
            let response;
            
            switch (type) {
                case 'users':
                    response = await CSVService.getUserCsv();
                    break;
                case 'tags':
                    response = await CSVService.getPC_tags();
                    break;
                case 'reports':
                    response = await CSVService.getReports();
                    break;
                case 'labels':
                    response = await CSVService.getLabels();
                    break;
                default:
                    throw new Error('Unknown CSV type');
            }
    
            // Create a URL for the blob and trigger the download
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `${type}.csv`); // Filename based on type
            document.body.appendChild(link);
            link.click();
    
            // Clean up
            link.parentNode.removeChild(link);
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error('Error during CSV download:', error);
        }
    };

    const downloadAllCsv = async () => {
        const types = ['users', 'tags', 'reports', 'labels'];
    
        // Execute all downloads concurrently
        const downloadPromises = types.map(type => downloadCsv(type));
        
        try {
            await Promise.all(downloadPromises);
        } catch (error) {
            console.error('Error downloading one or more CSVs:', error);
        }
    };

    return (
        <Container className="contentDiv" style={{width:'25vh'}}>
        <h2 className="mb-4">CSV Lists</h2>
        <Table bordered hover>
            <thead>
                <tr>
                    <th>Data Info</th>
                    <th>Download</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>User CSV</td>
                    <td>
                        <Button onClick={() => downloadCsv('users')} className="ml-auto">
                            Download Users CSV
                        </Button>
                    </td>
                </tr>
                <tr>
                    <td>Messages Tags</td>
                    <td>
                        <Button onClick={() => downloadCsv('tags')} className="ml-auto">
                            Download Messages CSV
                        </Button>
                    </td>
                </tr>
                <tr>
                    <td>Reports</td>
                    <td>
                        <Button onClick={() => downloadCsv('reports')} className="ml-auto">
                            Download Reports CSV
                        </Button>
                    </td>
                </tr>
                <tr>
                    <td>Labels</td>
                    <td>
                        <Button onClick={() => downloadCsv('labels')} className="ml-auto">
                            Download Label CSV
                        </Button>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <Button onClick={downloadAllCsv} className="ml-auto">
                            Download All
                        </Button>
                    </td>
                </tr>

            </tbody>
        </Table>
    </Container>
    );
};

export default CSVList;