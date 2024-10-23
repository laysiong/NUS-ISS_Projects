// LatestReports.js
import React, { useEffect, useState } from 'react';
import { Card, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import DashboardService from '../../services/DashboardService';
import ReportDetailModal from "../report/ReportDetailModal";

const LatestReports = () => {
  const [latestReports, setLatestReports] = useState([]);
  const [selectedReport, setSelectedReport] = useState(null);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);
  const navigate = useNavigate();

  const truncateText = (text, length) => {
    return text.length > length ? text.substring(0, length) + '...' : text;
  };
  
  useEffect(() => {
    const fetchLatestReports = async () => {
      try {
        const response = await DashboardService.getLatest5Report();
        setLatestReports(response.data);
      } catch (error) {
        console.error('Error fetching latest reports:', error);
      }
    };

    fetchLatestReports();
  }, []);

  const closeDetailModal = () => {
    setIsDetailModalOpen(false);
    setSelectedReport(null);
  };

  const handleReportClick = (report) => {
    setSelectedReport(report);
    setIsDetailModalOpen(true);
  };

  return (
      <>
        <div className="grid-item">
          <h4>Latest Reports</h4>
          {latestReports.length > 0 ? (
              latestReports.slice(0, 5).map(report => (
                  <Card key={report.id} className="mb-3">
                    <Card.Body>
                      <div className="row">
                        <div className="col-9" style={{textAlign: 'left'}}>
                          <Card.Title>{truncateText(report.reason, 20)}</Card.Title>
                          <Card.Text>
                            {new Date(report.startDate).toLocaleString()}
                            <br/>
                            Status: {report.status}
                          </Card.Text>
                        </div>
                        <div className="col-3 d-flex align-items-center justify-content-end">
                          <Button variant="primary" onClick={() => handleReportClick(report)}>View Report</Button>
                        </div>
                      </div>
                    </Card.Body>
                  </Card>
              ))
          ) : (
              <p>No latest reports available</p>
          )}
          <Button variant="link" onClick={() => navigate('/reports')}>View All</Button>
        </div>
        <ReportDetailModal
            show={isDetailModalOpen}
            onHide={closeDetailModal}
            selectedReport={selectedReport}
        />
      </>
  );
};

export default LatestReports;
