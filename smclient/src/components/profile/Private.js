import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import { useNavigate, Redirect, useParams } from "react-router-dom";
import ErrorForm from '../ErrorForm';
import Dialog from '@material-ui/core/Dialog';
import Navbar from '../Navbar';
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

async function getResponse(loginInfo) {
  return await fetch('http://localhost:8080/login', {
    method: 'POST',
    headers: {"Content-Type": "application/json"},
    mode: "cors",
    body: JSON.stringify(loginInfo)
  }).then((response) => response.text())
      .then((responseText) => {return responseText});
}

const Private = ({ handleClose }) => {

  let navigate = useNavigate(); 
  const routeChange = (user) =>{ 
    let path = `/userline`; 
    navigate(path, {state:[user]});
  }
  const throwError = (error) =>{ 
    let path = `/error`; 
    navigate(path, {state:[error]});
  }
  const classes = useStyles();
  // create state variables for each input
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async e => {
    e.preventDefault();
    console.log(username, password);
    const loginInfo = {username, password};
    // Error Checking for blank password
    if (username === '') {
      throwError("Username field cannot be blank")
    }
    else if (password === ''  || password.length < 5) {
      throwError("Password length incorrect")
    }
    else  {
     /* var bcrypt = require('bcryptjs');
      //var salt = bcrypt.genSaltSync(10);
      var hash = bcrypt.hashSync(password, 0);*/
      var CryptoJS= require("crypto-js")
      var hash = CryptoJS.SHA256(password).toString(CryptoJS.enc.Hex);
        // Store hash in your password DB.
      setPassword(null)
      const obj = await getResponse({username, hash});
      if (obj.indexOf("Error") !== -1) {
        throwError(obj)
      } else {
        //let user = JSON.parse(obj)
        console.log("The Response: ", obj);
        sessionStorage.setItem('user', obj);
        sessionStorage.setItem('logged', true)
        routeChange(username);
      }
    //handleClose();
    }
  };

  return (
    <div>
    <form className={classes.root} onSubmit={handleSubmit}>
      <TextField
        label="Username"
        variant="filled"
        required
        value={username}
        onChange={e => setUsername(e.target.value)}
      />
      <TextField
        label="Password"
        variant="filled"
        type="password"
        required
        value={password}
        onChange={e => setPassword(e.target.value)}
      />
      <div>
        <Button variant="contained" onClick={handleClose}>
          Cancel
        </Button>
        <Button type="submit" variant="contained" color="primary" onClick={handleSubmit}>
          Done
        </Button>
      </div>
    </form>
    </div>
  );
};

export default Private;