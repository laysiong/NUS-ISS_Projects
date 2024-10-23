import React, { useState, useEffect } from 'react';
import { Container, Form, Button, Card} from 'react-bootstrap';

const RouteSelectionForm = ({ routes, initialSelectedRoutes, onChange }) => {
    const [selectedRoutes, setSelectedRoutes] = useState([]);

    useEffect(() => {
        if (routes && routes.length > 0) {
            const selected = routes.map(route => ({
                ...route,
                selected: initialSelectedRoutes.includes(route.path),
                children: route.children ? route.children.map(child => ({
                    ...child,
                    selected: initialSelectedRoutes?.includes(child.path)
                })) : []
            }));
            setSelectedRoutes(selected);
        }
    }, [routes, initialSelectedRoutes]);

    const updateSelectedRoutes = (updatedRoutes) => {
        setSelectedRoutes(updatedRoutes);

        const selectedRoutesJson = updatedRoutes.map(route => ({
            path: route.path,
            component: route.component,
            menuName: route.menuName,
            children: route.children.filter(child => child.selected).map(child => ({
                path: child.path,
                menuName: child.menuName
            })),
            selected: route.selected
        })).filter(route => route.selected || route.children.length > 0);

        onChange(JSON.stringify(selectedRoutesJson, null, 2));
    };

    const handleCheckboxChange = (index, isChild, childIndex) => {
        const updatedRoutes = [...selectedRoutes];
        if (isChild) {
            updatedRoutes[index].children[childIndex].selected = !updatedRoutes[index].children[childIndex].selected;
        } else {
            updatedRoutes[index].selected = !updatedRoutes[index].selected;
            // If the parent is selected, select all its children
            if (updatedRoutes[index].children && updatedRoutes[index].children.length > 0) {
                updatedRoutes[index].children = updatedRoutes[index].children.map(child => ({
                    ...child,
                    selected: updatedRoutes[index].selected
                }));
            }
        }
        updateSelectedRoutes(updatedRoutes);
    };

    const handleSelectAll = () => {
        const updatedRoutes = selectedRoutes.map(route => ({
            ...route,
            selected: true,
            children: route.children ? route.children.map(child => ({
                ...child,
                selected: true
            })) : []
        }));
        updateSelectedRoutes(updatedRoutes);
    };

    const handleDeselectAll = () => {
        const updatedRoutes = selectedRoutes.map(route => ({
            ...route,
            selected: false,
            children: route.children ? route.children.map(child => ({
                ...child,
                selected: false
            })) : []
        }));
        updateSelectedRoutes(updatedRoutes);
    };

    return (
        <div>
            <h3>Select Routes:</h3>
            <Button className="me-2" type="button" onClick={handleSelectAll}>Select All</Button>
            <Button type="button" onClick={handleDeselectAll}>Deselect All</Button>
            {selectedRoutes.map((route, index) => (
                <div className ="mt-2 mb-2"key={index}>
                    <input
                        class="form-check-input me-1"
                        type="checkbox"
                        id={`route-${index}`}
                        checked={route.selected}
                        onChange={() => handleCheckboxChange(index, false)}
                    />
                    <label class="form-check-label" htmlFor={`route-${index}`}> <b>{route.menuName}</b></label>
                    {route.children && route.children.map((child, childIndex) => (
                        <div key={childIndex} style={{ paddingLeft: '20px' }}>
                            <input
                                class="form-check-input me-1"
                                type="checkbox"
                                id={`child-route-${index}-${childIndex}`}
                                checked={child.selected}
                                onChange={() => handleCheckboxChange(index, true, childIndex)}
                            />
                            <label htmlFor={`child-route-${index}-${childIndex}`}>{child.menuName}</label>
                        </div>
                    ))}
                </div>
            ))}
        </div>
    );
};

export default RouteSelectionForm;
