import React from 'react';
import PropTypes from 'prop-types';
import './Modal.css';

export const DeleteModal = ({ id, date, setShowModal }) => {

    const deleteEvent = () => {
        fetch('https://0u9awxaptb.execute-api.us-east-1.amazonaws.com/dev/events', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ id: id, date: date })});
            setShowModal(false);
    }

    return (
        <div className="modal">
            Are you sure you want to delete this event?
            <div className="buttonRow">
                <button className="deleteModalButton" onClick={deleteEvent}>DELETE</button>
                <button className="cancelModalButton" onClick={() => setShowModal(false)}>CANCEL</button>
            </div>
        </div>
    );
}

DeleteModal.propTypes = {
    id: PropTypes.string.isRequired,
    date: PropTypes.string.isRequired, 
    setShowModal: PropTypes.func.isRequired,
}