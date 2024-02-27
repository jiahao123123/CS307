import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core';
import Button from '@material-ui/core/Button';
import { useNavigate, Redirect, useLocation } from "react-router-dom";
import Navbar from './Navbar';
const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    padding: theme.spacing(2),

    '& .MuiTextField-root': {
      margin: theme.spacing(1),
      width: '300px',
    },
    '& .MuiButtonBase-root': {
      margin: theme.spacing(2),
    },
  },
}));


const ErrorForm = ({ error }) => {
  const classes = useStyles();

  let navigate = useNavigate(); 
  const location = useLocation();
  const routeChange = () =>{ 
      let path = `/`; 
      navigate(path);
  }
  return (
  
      <div className="ErrorForm">
          <form className={classes.root}>
        <h1>{location.state}</h1>
      <Button variant="contained" color="primary" onClick={routeChange}>
      Return
    </Button> </form>
      </div>
  );
};

export default ErrorForm;