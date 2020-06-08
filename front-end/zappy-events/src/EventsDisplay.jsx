import React, { useState, useEffect } from 'react';
import { EventRow } from './EventRow.jsx';
import './EventsDisplay.css';

export const EventsDisplay = () => {
    const [events, setEvents] = useState([]);

    const fetchEvents = () => {
        fetch('https://0u9awxaptb.execute-api.us-east-1.amazonaws.com/dev/events')
            .then(res => res.json())
            .then(res => {
                setEvents(res.events)
            });
    }

    useEffect(fetchEvents, []);

    return (
        <div className="eventsDisplay">
            <table>
                <tbody>
                    <EventRow isHeader={true}/>
                    {events
                        .sort((a, b) => new Date(a.date) - new Date(b.date))
                        .map((event, ind) => <EventRow key={event.id} odd={ind % 2} id={event.id} org={event.organizer} venue={event.venue} date={event.date} isHeader={false}/>)}
                </tbody>
            </table>
            <div className="button">
                <button className="bigButton" onClick={fetchEvents}>
                    Refresh
                </button>
            </div>
        </div>
    );
}
