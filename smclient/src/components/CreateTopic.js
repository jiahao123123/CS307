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

async function getResponse(topicName) {
    return await fetch('http://localhost:8080/topic', {
        method: 'POST',
        headers: {"Content-Type": "application/json", "origin": "localhost:3000"},
        mode: "cors",
        body: topicName + ", " + sessionStorage.getItem('user')
    }).then((response) => response.text())
        .then((responseText) => {return responseText});
}
const CreateTopic = () => {
    const classes = useStyles();
    // create state variables for each input
    const [topicName, setTopicName] = useState('');
    let navigate = useNavigate(); 
    const routeChange = (name) =>{ 
      let path = `/topic`; 
      navigate(path, {state:name});
    }
    const throwError = (error) =>{ 
      let path = `/error`; 
      navigate(path, {state:error});
    }
    const handleSubmit = async e => {
      e.preventDefault();
      console.log(topicName);

      const temp = await getResponse(topicName);
      console.log(temp)
      const ret = temp

      if (ret.indexOf("Error") !== -1) {
        throwError(ret)
      } else {
        console.log("Returned from Server: ", ret);
        routeChange(ret);
      }
      //handleClose();
    };
    return(
      
        <div className="CreateTopic" ><Navbar/>
        <form className={classes.root}><TextField
        label="Topic Name"
        variant="filled"
        required
        value={topicName}
        onChange={e => setTopicName(e.target.value)}
      /><Button variant="contained" color="primary" onClick={handleSubmit}>
      Create
    </Button></form>
        </div>
        );
}
export default CreateTopic;