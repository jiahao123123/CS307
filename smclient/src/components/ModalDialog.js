import React from 'react';
import Dialog from '@material-ui/core/Dialog';
import Form from './Form';
import LogIn from './LogIn'
import AddComment from './AddComment'
const ModalDialog = ({ open, handleClose, which }) => {
    let form;
    if (which===0) {
        form = <Form handleClose={handleClose} />
    }
    else if (which === 1) {
        form = <LogIn handleClose={handleClose} />
    }
    else {
      form = <AddComment handleCLose={handleClose}/>
    }
  return (
    // props received from App.js
    <Dialog open={open} onClose={handleClose} which={which} >
      {form}
    </Dialog>
  );
};

export default ModalDialog;