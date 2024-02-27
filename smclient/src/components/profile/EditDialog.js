import React from 'react';
import Dialog from '@material-ui/core/Dialog';
import EditForm from './EditForm';

const EditDialog = ({ open, handleClose}) => {
    let form;
    form = <EditForm handleClose={handleClose} />

  return (
    // props received from App.js
    <Dialog open={open} onClose={handleClose} >
      {form}
    </Dialog>
  );
};

export default EditDialog;