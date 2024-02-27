import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import { useNavigate, Redirect } from "react-router-dom";
import ErrorForm from './ErrorForm';
import Navbar from './Navbar'
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

async function getResponse(signUpInfo) {
  return await fetch('http://localhost:8080/signup', {
    method: 'POST',
    headers: {"Content-Type": "text/html", "Origin": "http://localhost:3000"},
    mode: "cors",
    body: JSON.stringify(signUpInfo)
  }).then((response) => response.text())
      .then((responseText)=>{return responseText})
}
async function getRequestInfo(username) {
  return await fetch('http://localhost:8080/getProfileInfo', {
    method: 'POST',
    headers: {"Content-Type": "application/json"},
    mode: "cors",
    body: username
  }).then((response) => response.text())
      .then((responseText) => {return responseText});
}

const Form = ({ handleClose }) => {
  let navigate = useNavigate(); 
  const routeChange = (user) =>{ 
    let path = `/profile`; 
    navigate(path, {state:user});
  }
  const throwError = (error) =>{ 
    let path = `/error`; 
    navigate(path, {state:error});
  }
  const classes = useStyles();
  // create state variables for each input
  const [username, setusername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async e => {
    e.preventDefault();

    if (username == '') {
      throwError("Username field cannot be blank")
    } 
    else if (password === '' || password.length > 15 || password.length < 5) {
      throwError("Password length incorrect")
    }
    else if (email == '' ) {
      throwError("Please enter an email")
    }else {
      /*var bcrypt = require('bcryptjs');
      var salt = bcrypt.genSaltSync(10);
      var hash = bcrypt.hashSync(password, salt);*/
      var CryptoJS= require("crypto-js")
      var hash = CryptoJS.SHA256(password).toString(CryptoJS.enc.Hex);
        // Store hash in your password DB.
      setPassword(null)
      // Same as LogIn info
      // Add server send
      console.log(username, email, password);
      const text = await getResponse({username, email, hash});
      console.log("Check Text: ", text);
      if (text.indexOf("Error") !== -1) {
        throwError(text)
      } else {
        let user = JSON.parse(text)
        console.log("The Response: ", user);
        sessionStorage.setItem('user', username);
        sessionStorage.setItem('userInfo', (text))
        sessionStorage.setItem('logged', true);
        const reqInfo = await getRequestInfo(username);
        sessionStorage.setItem('requestInfo', reqInfo);
        sessionStorage.setItem('bio', JSON.parse(reqInfo).bio);
        // wait for its response
        // potential keyword await
        routeChange(username);
      }
    }
  };

  return (
    <div>
      <Navbar/>
    <form className={classes.root} onSubmit={handleSubmit}>
      <TextField
        label="Username"
        variant="filled"
        required
        value={username}
        onChange={e => setusername(e.target.value)}
      />
      <TextField
        label="Email"
        variant="filled"
        type="email"
        required
        value={email}
        onChange={e => setEmail(e.target.value)}
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

        <Button type="submit" variant="contained" color="primary" onClick={handleSubmit}> 
          Signup
        </Button>
      </div>
    </form>
    </div>
  );
};

export default Form;