import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { FieldInput } from './FieldInput.jsx';
import './Modal.css';

export const UpdateModal = ({ id, org, venue, date, formattedDate, setShowModal }) => {
    const [newOrg, setNewOrg] = useState(org);
    const [newVenue, setNewVenue] = useState(venue);
    const [newDate, setNewDate] = useState(date);
    const [error, setError] = useState("");

    const re = /((0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])-[12]\d{3})/;

    const validateInputs = () => {
        if (!newOrg || !newVenue || !newDate) {
            setError("Please fill all fields before adding an event.");
        } else if (!re.test(newDate)) {
            setError("Please match your date format to mm-dd-yyyy.");
        } else {
            setError("");
            return true;
        }
        return false;
    }

    const updateEvent = () => {
        const dateArray = newDate.split('-');
        const dateObject = new Date(dateArray[2], dateArray[0], dateArray[1]);
        if (validateInputs()) {
            fetch('https://0u9awxaptb.execute-api.us-east-1.amazonaws.com/dev/events', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ id: id, date: date, newOrg: newOrg, newVenue: newVenue, newDate: dateObject})});
                setShowModal(false);
        }
    }

    return (
        <div className="modal">
            <FieldInput label="ORGANIZER" defaultValue={org} onChange={setNewOrg}></FieldInput>
            <FieldInput label="VENUE" defaultValue={venue} onChange={setNewVenue}></FieldInput>
            <FieldInput label="DATE" defaultValue={formattedDate} onChange={setNewDate}></FieldInput>
            <div className="errorMessage">{error}</div>
            <div className="buttonRow">
                <button className="updateModalButton" onClick={updateEvent}>UPDATE</button>
                <button className="cancelModalButton" onClick={() => setShowModal(false)}>CANCEL</button>
            </div>
        </div>
    );
}

UpdateModal.propTypes = {
    id: PropTypes.string.isRequired,
    org: PropTypes.string.isRequired,
    venue: PropTypes.string.isRequired,
    date: PropTypes.string.isRequired, 
    formattedDate: PropTypes.string.isRequired, 
    setShowModal: PropTypes.func.isRequired,
}