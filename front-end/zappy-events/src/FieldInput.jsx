import React from 'react';
import PropTypes from 'prop-types';
import './FieldInput.css';

export const FieldInput = ({ label, defaultValue, onChange }) => {

    return (
        <div className="fieldInput">
            <div className="label">{label}: </div>
            <input className="inputBox" type="text" defaultValue={defaultValue} onChange={e => onChange(e.target.value)}></input>
        </div>
    );
}

FieldInput.propTypes = {
    label: PropTypes.string.isRequired,
    defaultValue: PropTypes.string,
    onChange: PropTypes.func.isRequired,
}