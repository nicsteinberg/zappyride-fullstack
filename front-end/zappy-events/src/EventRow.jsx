import React, { useState } from 'react';
import { DeleteModal } from './DeleteModal.jsx';
import { UpdateModal } from './UpdateModal.jsx';
import PropTypes from 'prop-types';
import './EventRow.css';

export const EventRow = ({ odd, id, org, venue, date, isHeader }) => {
    const rowClass = odd ? "oddRow" : "evenRow";
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [showUpdateModal, setShowUpdateModal] = useState(false);

    let formattedDate = "";
    if (date) {
        const dateArray = date.split(/-|T/);
        formattedDate = dateArray[1].concat('-').concat(dateArray[2]).concat('-').concat(dateArray[0]);
    }

    return (
        <React.Fragment>
            {showDeleteModal && <DeleteModal id={id} date={date} setShowModal={setShowDeleteModal}></DeleteModal>}
            {showUpdateModal && <UpdateModal id={id} org={org} venue={venue} date={date} formattedDate={formattedDate} setShowModal={setShowUpdateModal}></UpdateModal>}
            {isHeader ?
                <tr key="header">
                    <th>
                        ORGANIZER
                    </th>
                    <th>
                        VENUE
                    </th>
                    <th>
                        DATE
                    </th>
                    <th></th>
                </tr>
            :
                <tr className={rowClass}>
                    <td>
                        {org}
                    </td>
                    <td>
                        {venue}
                    </td>
                    <td>
                        {formattedDate}
                    </td>
                    <td>
                        <button className="updateButton" onClick={() => setShowUpdateModal(true)}>UPDATE</button>
                        <button className="deleteButton" onClick={() => setShowDeleteModal(true)}>DELETE</button>
                    </td>
                </tr>}
        </React.Fragment>
    );
}

EventRow.propTypes = {
    odd: PropTypes.number,
    id: PropTypes.string,
    org: PropTypes.string,
    venue: PropTypes.string,
    date: PropTypes.string,
    isHeader: PropTypes.bool.isRequired,
}