import React, { useEffect, useState } from 'react';
import { Box, Divider, makeStyles } from '@material-ui/core';
import Button from '@material-ui/core/Button';
import { useLocation, useNavigate, BrowserRouter as Router, Route } from "react-router-dom";
import Navbar from './Navbar';
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

function decodeString(data) {
    var arr = data.split(','),
        mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]),
        n = bstr.length,
        u8arr = new Uint8Array(n);

    while(n--) {
        u8arr[n] = bstr.charCodeAt(n);
    }

    return new File([u8arr], "Post_Img", {type:mime});
}

const Interactions = () => {
    const classes = useStyles();
    let navigate = useNavigate();


    const routeChange = async (post) =>{
        if (post.hasImage === true) {
            post.image = await decodeString(post.image);

            console.log("This is the new post: " + JSON.stringify(post));
        }

        let path = `/post`; 
        navigate(path, {state:post});
    }
    let show = sessionStorage.getItem('logged')
    let user;
    let location = useLocation().state;
    console.log(location);
    if (show) {
      location = JSON.parse(location)
    }
    if (show) {
      user = JSON.parse(sessionStorage.getItem('userInfo')); 
         
    }
    const dispTime = (item) =>
    {
      return item[1] + "/" + item[2]
    }
    const dispReaction = (item) =>
    {
      return "Reaction: " + item.type;
    }
    let anon = "Anonymous";
    return(
        <div className="Userline">
            <Navbar/>
            { show &&<form className={classes.root}>
          <h1 >Userline for: {location.username}</h1>
          <Box sx={{display: 'inline-flex',
          flexDirection:'column',
          p: 1,
          m: 1}}>
            <List sx={{ width: '100%', padding: 5, bgcolor: 'background.paper' }}>
         {location.map((item) => {
            return(
                <ListItem button divider onClick={() => routeChange(item.post)}>
                <ListItemText  inset primary = {dispReaction(item)}
                  />
                <ListItemText  inset primary = {item.post.title}
                  />
                  {!item.post.isAnonymous &&<ListItemText  inset primary = {item.post.creatorName}
                  />}
                  {item.post.isAnonymous &&<ListItemText  inset primary = {anon}
                  />} 
              <ListItemText inset secondary={item.post.topicName}/>
                  <ListItemText inset secondary={dispTime(item.post.creationTime)}/>
              </ListItem>
             )
        })}</List></Box></form>}
        {!show && <h1>not Logged</h1>}
        </div>
        );
}
export default Interactions;
