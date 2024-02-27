import React, { useEffect, useState } from 'react';
import { Box, Divider, makeStyles } from '@material-ui/core';
import Button from '@material-ui/core/Button';
import { useLocation, useNavigate, BrowserRouter as Router, Route } from "react-router-dom";
import Navbar from './Navbar';
import { TextField } from "@mui/material";
import TestPost from './TestPost.json'
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
const useStyles = makeStyles(theme => ({
    root: {
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'center',
      alignItems: 'center',
      padding: theme.spacing(2),
    },
  }));

async function getDMs(to, from) {
    return await fetch('http://localhost:8080/sendMessage', {
        method: 'POST',
        headers: {"Content-Type": "application/json", "origin" : "localhost:3000"},
        mode: "cors",
        body: to + "," + from
    }).then((response) => response.text())
        .then((responseText) => {return responseText});
}

  async function getResponse(comment, from, to) {
    const username = sessionStorage.getItem('user');
    return await fetch('http://localhost:8080/sendMessage', {
        method: 'POST',
        headers: {"Content-Type": "application/json", "origin" : "localhost:3000"},
        mode: "cors",
        body: comment + ", " + from+ ", " + to  
    }).then((response) => response.text())
        .then((responseText) => {return responseText});
}
const DM = () => {
    const classes = useStyles();
    let user = JSON.parse(sessionStorage.getItem('userInfo'));
    let see = JSON.parse(sessionStorage.getItem('visit'));
    const [comment, setComment] = useState('');
    const [show, setShow] = useState('');
    const [disp, setDisp] = useState(false);
    let location = JSON.parse(useLocation().state);
    console.log(location);
    console.log(comment);
    const handleSubmit = async (e) => {
        //posts
        e.preventDefault();
        setDisp(true);
        setShow(comment);
        console.log(await getResponse(comment, user.id, see.id));
        //const dm = await getDMs(user.id, see.id);
        //sessionStorage.setItem('dm', dm);
    }
      //const messages = sessionStorage.getItem('dm');
    return(<div><Navbar/>
    <h1>DM from {user.username} to {see.username}</h1>
    <br/><Box display="flex">
    <div>
    <TextField
      label="Message"
      variant="outlined"
      size="small"
      required
      value={comment}
      onChange={e => setComment(e.target.value)}
    />
      <Button type="submit" variant="contained" color="primary" disabled={disp} onClick={handleSubmit}> 
        Send DM
      </Button>
    </div>
  </Box>
  <List >
         
                {location.map((item) => {
                return(
              <ListItem divider >
                  <ListItemText
                    primary={item.text}
                    secondary={item.senderId === user.id ? user.username: see.username}
                  />
                </ListItem>)
            })}  {disp && 
              <ListItem divider>
                      <ListItemText
                        primary={show}
                        secondary={user.username}
                      />
                    </ListItem>} 
            </List></div>);
}
export default DM;