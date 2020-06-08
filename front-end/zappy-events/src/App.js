import React from 'react';
import { EventsDisplay } from './EventsDisplay.jsx';
import { AddEvent } from './AddEvent.jsx';
import './App.css';

function App() {
  return (
    <React.Fragment>
      <div className="header">
        Ride-and-Drive Events
      </div>
      <div className="body">
        <AddEvent></AddEvent>
        <EventsDisplay></EventsDisplay>
      </div>
    </React.Fragment>
  );
}

export default App;
