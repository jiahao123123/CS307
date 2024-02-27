import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import MenuItem from '@mui/material/MenuItem';
import Menu from '@mui/material/Menu';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/material/Menu';
import AccountCircle from '@material-ui/icons/AccountCircle'
import { useNavigate, Redirect } from "react-router-dom";
import {useState} from 'react'
import ModalDialog from './ModalDialog'
import Button from '@material-ui/core/Button';
import { Box } from '@material-ui/core';
const useStyles = makeStyles(theme => ({
    menuButton: {
      marginRight: theme.spacing(2),
    },
    title: {
      flexGrow: 1,
    },
  }));

async function getRequestInfo() {
    const username = sessionStorage.getItem('user');
    return await fetch('http://localhost:8080/getProfileInfo', {
        method: 'POST',
        headers: {"Content-Type": "application/json"},
        mode: "cors",
        body: username
    }).then((response) => response.text())
        .then((responseText) => {
            return responseText
        });
}

async function getResponse() {
    let userInfo;
    if (sessionStorage.getItem('logged')) {
        userInfo = sessionStorage.getItem('user');
        console.log("User is logged in!");
    }
    else {
      console.log("User is not logged in!");
        return "user not logged in";
    }
    return await fetch('http://localhost:8080/timeline', {
        method: 'POST',
        headers: {"Content-Type": "text/html", "Origin": "http://localhost:3000"},
        mode: "cors",
        body: userInfo
    }).then((response) => response.text())
        .then((responseText)=>{return responseText})
}

async function sendResponse() {
    const obj = await getResponse();

    console.log("navbar response: " + obj);
    sessionStorage.setItem('timeline', obj)
}

const Navbar = () => {
    // declare a new state variable for modal open
    const [open, setOpen] = useState(false);
    const [which, setWhich] = useState(false);

 
    const [anchorEl, setAnchorEl] = React.useState(null);
    let navigate = useNavigate(); 
    const handleMenu = (event) => {
      setAnchorEl(event.currentTarget);
    };
  
    const handleCloseM = () => {
      setAnchorEl(null);
    };
    // function to handle modal close
    const handleClose = () => {
      setOpen(false);
    };
        // function to handle modal open
    // Which true == Sign up
    const handleOpen = () => {
      let path = `/signup`;
      navigate(path);
    };
    // Which true == Log in
    const handleOpen2 = () => {
      let path = `/login`;
      navigate(path);
    };
    const swapHome = async e => {

      let path = `/timeline`; 
      const obj = await getResponse();

      //add timelime getResponse
      navigate(path, {state:obj});
    }
    const swapProfile = async () => {
      const prof = await getRequestInfo();
      sessionStorage.setItem('requestInfo', prof);
      console.log("Freshly set requestInfo");
      console.log(prof);
      let path = `/profile`; 
        navigate(path);
    }
    const swapLogout = () => {
      let path = `/`; 
      sessionStorage.clear();
      navigate(path);
    }
    const routeChange = () =>{ 
        let path = `/createtopic`; 
        navigate(path);
    }
    const swapCreatePost = () =>{ 
      let path = `/createpost`; 
      navigate(path);
    }
    const searchPage = () =>{ 
      let path = `/search`; 
      navigate(path);
    }
    const createTopic = () => {
        routeChange();
    };
    const classes = useStyles();
    let auth = sessionStorage.getItem("logged") ? <Box sx={{display: 'inline-flex',
    flexDirection:'column'}}><MenuItem onClick={swapProfile}>Profile</MenuItem><MenuItem onClick={swapLogout}>Logout</MenuItem></Box>: <Box sx={{display: 'inline-flex',
    flexDirection:'column'}}><MenuItem variant="contained" color="secondary" onClick={handleOpen2}>
    Log in
  </MenuItem>
  <MenuItem variant="contained" color="primary" onClick={handleOpen}>
    Sign up
  </MenuItem></Box>;
  return (
    <AppBar position="static">
    <Toolbar>
      <Typography variant="h6" className={classes.title}>PurdueCircleApp</Typography>
      <Button color="inherit" onClick={swapHome}>Home</Button>
      <Button color="inherit" onClick={searchPage}>Search</Button>
      <Button color="inherit" onClick={createTopic}>Create Topic</Button>
      <Button color="inherit" onClick={swapCreatePost}>Create Post</Button>
            <div>
              <IconButton
                size="large"
                aria-label="account of current user"
                aria-controls="menu-appbar"
                aria-haspopup="true"
                onClick={handleMenu}
                color="inherit"
              >
                <AccountCircle />
              </IconButton>
              <Menu
                id="menu-appbar"
                anchorEl={anchorEl}
                anchorOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
                }}
                open={Boolean(anchorEl)}
                onClose={handleCloseM}
              >
              {auth}
              </Menu>
            </div>
    </Toolbar>
  </AppBar>

  );
};
export default Navbar;