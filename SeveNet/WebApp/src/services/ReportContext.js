import React, { createContext, useContext, useState, useEffect } from 'react';
import useCurrentUser from "../components/customhook/CurrentUser";
import ReportService from "./ReportService";
import { BASE_WS_URL } from './config';

const ReportContext = createContext();

export const useReport = () => {
    return useContext(ReportContext);
};

export const ReportProvider = ({ children }) => {
    const [reports, setReports] = useState();

    const currentUser = useCurrentUser();

    useEffect(() => {
        if (currentUser) {
            const moderatorUri = currentUser.role.type === "Moderator" ? "&role=Moderator" : "";
            const socket = new WebSocket(`${BASE_WS_URL}/reports?userId=${currentUser.id}${moderatorUri}`);

            socket.onopen = () => {
                console.log('Report WebSocket connection established');
            };

            socket.onmessage = (event) => {
                if (event.data === 'Your report has been updated') {
                    fetchReportsByUserId(currentUser.id).then();
                } else if (event.data === 'A report has been updated. Please review it.') {
                    fetchAllReports().then();
                }
            };

            socket.onclose = () => {
                console.log('Report WebSocket connection closed');
            };
            //once added manager, it does not work.
            if (currentUser.role.type !== 'Moderator') {
                fetchReportsByUserId(currentUser.id).then();
            } else {
                fetchAllReports().then();
            }
        }
    }, [currentUser]);


    const fetchReportsByUserId = async (uid) => {
        try {
            const response = await ReportService.getReportByUserId(uid);
            setReports(response.data);
        } catch (error) {
            console.error('Error fetching ReportsByUserId', error);
        }
    };

    const fetchAllReports = async () => {
        try {
            const response = await ReportService.getAllReports();
            setReports(response.data);
        } catch (error) {
            console.error('Error fetching all reports', error);
        }
    };

    return (
        <ReportContext.Provider value={{ reports }}>
            {children}
        </ReportContext.Provider>
    );
};