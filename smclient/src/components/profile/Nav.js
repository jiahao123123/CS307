import React, { Component } from 'react'
import '../profile.css'
import Button from '@material-ui/core/Button';
import ModalDialog from '../ModalDialog';
import { useNavigate, Redirect } from "react-router-dom";
import { useState } from 'react';
import EditDialog from './EditDialog';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles(theme => ({
  menuButton: {
    marginRight: theme.spacing(2),
  },
  title: {
    flexGrow: 1,
  },
}));

function HandleClick() {
  let navigate = useNavigate(); 
  const routeChange = () =>{ 
    let path = `/`; 
    navigate(path);
  }
  routeChange();
}


const Navbar = () => {

const [open, setOpen] = useState(false);
  const handleOpen = () => {
    setOpen(true);
  };
  let navigate = useNavigate(); 
  const HandleClick = () => {
    let path = `/`; 
    navigate(path);
  }
  // function to handle modal close
  const handleClose = () => {
    setOpen(false);
  };
  const handleOpenPrivate = () => {
    if (sessionStorage.getItem('private') == true) {
      sessionStorage.setItem('private', true);
    } else {
      sessionStorage.setItem('private', true);
    }
  };
  const classes = useStyles();

  return (
    <div className='navbar'> 
    <EditDialog open={open} handleClose={handleClose}/>
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" className={classes.title}>PurdueCircleApp</Typography>
        <Button color="inherit" onClick={HandleClick}>Home</Button>
        <Button color="inherit" onClick={handleOpen}>Edited</Button>
      </Toolbar>
    </AppBar> 
      </div>
  )
}

export default Navbar