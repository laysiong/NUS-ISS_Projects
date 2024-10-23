import {Button, Modal} from "react-bootstrap";
import React from "react";
import TagLists from "../Classess/taglists";

const ReportDetailModal = ({ show, onHide, selectedReport }) => {
    return (
        <>
            {/*Report Detail Modal*/}
            <Modal show={show} onHide={onHide} size="lg">
                <Modal.Header closeButton>
                    <Modal.Title>Report Detail</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {selectedReport ? (
                        <>
                            <h4>Report Information</h4>
                            <p><strong>Report User</strong> {selectedReport.reportUser.username}</p>
                            <p><strong>Reported ID:</strong> {selectedReport.reportedId}</p>
                            <p><strong>Reason:</strong> {selectedReport.reason}</p>
                            <p><strong>Status:</strong> {selectedReport.status}</p>
                            <p style={{ display: 'flex', alignItems: 'center' }}><strong>Labels:</strong><TagLists style={{ marginLeft: '8px' }} tagsString={selectedReport.label.label} /></p>
                            <p><strong>Report Time:</strong> {new Date(selectedReport.reportDate.replace(' ', 'T') + '.000Z').toLocaleString()}</p>
                            <p><strong>CaseClose
                                Time:</strong> { selectedReport.caseCloseDate ? new Date(selectedReport.caseCloseDate.replace(' ', 'T') + '.000Z').toLocaleString() : 'N/A'}
                            </p>

                            <h4>Reply Stream</h4>
                            <div style={{maxHeight: '300px', overflowY: 'auto'}}>
                                {selectedReport.remarks ?
                                    JSON.parse(selectedReport.remarks).map((remark, index) => (
                                        <div key={index} style={{marginBottom: '15px'}}>
                                            <p>
                                                <strong>{remark.user}</strong> ({new Date(remark.timestamp).toLocaleString()}):
                                            </p>
                                            <p>{remark.text}</p>
                                        </div>
                                    )) :
                                    <p>No remarks available.</p>
                                }
                            </div>
                        </>
                    ) : (
                        <p>No report selected.</p>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={onHide}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default ReportDetailModal;