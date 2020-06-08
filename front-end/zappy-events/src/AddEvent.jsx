import React, { useState } from 'react';
import { FieldInput } from './FieldInput.jsx';
import './AddEvent.css';

export const AddEvent = () => {
    const [organizer, setOrganizer] = useState("");
    const [venue, setVenue] = useState("");
    const [date, setDate] = useState("");
    const [error, setError] = useState("");

    const re = /((0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])-[12]\d{3})/;

    const validateInputs = () => {
        if (!organizer || !venue || !date) {
            setError("Please fill all fields before adding an event.");
        } else if (!re.test(date)) {
            setError("Please match your date format to mm-dd-yyyy.");
        } else {
            setError("");
            return true;
        }
        return false;
    }

    const addEvent = () => {
        if (validateInputs()) {
            const dateArray = date.split('-');
            const dateObject = new Date(dateArray[2], dateArray[0], dateArray[1]);
            console.log(dateObject);
            fetch('https://0u9awxaptb.execute-api.us-east-1.amazonaws.com/dev/events', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ id: "", organizer: organizer, venue: venue, date: dateObject })});
        }
    }

    return (
        <div className="addEvent">
            <FieldInput label="ORGANIZER" onChange={setOrganizer}></FieldInput>
            <FieldInput label="VENUE" onChange={setVenue}></FieldInput>
            <FieldInput label="DATE" onChange={setDate}></FieldInput>
            <div className="errorMessage">{error}</div>
            <button className="bigButton" onClick={addEvent}>
                Add Event
            </button>
        </div>
    );
}
