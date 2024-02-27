import React from "react";
import { makeStyles } from '@material-ui/core';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import { useNavigate, Redirect } from "react-router-dom";
import { useState } from "react";
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

async function getDMs(to, from) {
    return await fetch('http://localhost:8080/getDM', {
        method: 'POST',
        headers: {"Content-Type": "application/json", "origin" : "localhost:3000"},
        mode: "cors",
        body: to + ", " + from
    }).then((response) => response.text())
        .then((responseText) => {return responseText});
}

async function getResponse(topicName) {
    return await fetch('http://localhost:8080/searchTopic', {
        method: 'POST',
        headers: {"Content-Type": "application/json"},
        mode: "cors",
        body: topicName
    }).then((response) => response.text())
        .then((responseText) => {return responseText});
}
async function getUser(userName) {
  return await fetch('http://localhost:8080/searchUser', {
      method: 'POST',
      headers: {"Content-Type": "application/json"},
      mode: "cors",
      body: userName
  }).then((response) => response.text())
      .then((responseText) => {return responseText});
}

const Search = () => {
    const classes = useStyles();
    // create state variables for each input
    const [topicName, setTopicName] = useState('');
    const [userName, setUserName] = useState('');
    let navigate = useNavigate(); 
    const routeChange = (name) =>{ 
      let path = `/topic`;
      navigate(path, {state:name});
    }
    const userChange = (name) =>{ 
      let path = `/viewerProfile`; //Edit this
      navigate(path, {state:name});
    }
    const throwError = (error) =>{ 
      let path = `/error`; 
      navigate(path, {state:error});
    }
    const handleSubmit = async e => {
      e.preventDefault();
      console.log(topicName);

      const ret = await getResponse(topicName);
      if (ret.indexOf("Error") !== -1) {
        throwError(ret)
      } else {
        console.log("Returned from Server: ", ret);
        routeChange(ret);
      }
      //handleClose();
    };
    const viewSearch = async e => {
      e.preventDefault();
      console.log(userName);
      if (sessionStorage.getItem('logged')) {
        for (const name of JSON.parse(sessionStorage.getItem('userInfo')).profile.usersThatBlockedThisUser) {
          console.log(name + ": " + userName)
          if (name === userName) {
            throwError("Error: This user has blocked you")
            return;
          }
        }
      }
      const ret = await getUser(userName);
      if (ret.indexOf("Error") !== -1) {
        throwError(ret)
      } else {
        console.log("Returned from Server: ", ret);
        sessionStorage.setItem('visit', ret)

        userChange(ret);
      }
      //handleClose();
    };
    return(
      
        <div className="Search" ><Navbar/>
        <form className={classes.root}><TextField
        label="Topic Name"
        variant="filled"
        required
        value={topicName}
        onChange={e => setTopicName(e.target.value)}
      /><Button variant="contained" color="primary" onClick={handleSubmit}>
      Search Topic
    </Button>
    <TextField
        label="Username"
        variant="filled"
        value={userName}
        onChange={e => setUserName(e.target.value)}
      /><Button variant="contained" color="primary" onClick={viewSearch}>
      Search User
    </Button></form>
        </div>
        );
}
export default Search;