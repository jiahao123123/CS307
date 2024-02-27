import React, { useEffect, useState } from 'react';
import { Box, makeStyles } from '@material-ui/core';
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

async function getResponse(follow, topic) {
  const username = sessionStorage.getItem('user');
  return await fetch('http://localhost:8080/followedTopic', {
    method: 'POST',
    headers: {"Content-Type": "application/json", "origin" : "localhost:3000"},
    mode: "cors",
    body: JSON.stringify(username + ", " + topic + ", " + follow)
  }).then((response) => response.text())
      .then((responseText) => {return responseText});
}

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

const Topic = () => {
  const classes = useStyles();
  let location = JSON.parse(useLocation().state);
  const [follow, setFollow] = useState(false);
  let user
  if (sessionStorage.getItem('logged')) {
    user = JSON.parse(sessionStorage.getItem('userInfo')); 
  }
  let navigate = useNavigate(); 
  //change when user info is correct
  useEffect(() => {
    if (sessionStorage.getItem('logged')) {
      user = JSON.parse(sessionStorage.getItem('userInfo')); 
      for (const name of user.profile.followingTopicsList) {
        if (name.name === location.name) {
          setFollow(true)
          console.log('following')
        }
      }
    }
  }, []);

  const routeChange = async (post) =>{
      console.log("This is the post: " + JSON.stringify(post));

      if (post.hasImage === true) {
        post.image = await decodeString(post.image);

        console.log("This is the new post: " + JSON.stringify(post));
      }

      let path = `/post`; 
      navigate(path, {state:post});
  }
  const handleFollow = async (bool) => {
    // If called with true we want to add the topic to users followed, if called with false we want to remove topic from users followed
    console.log("Bool: " + bool);
    setFollow(bool)
    user = JSON.parse(await getResponse(bool, location.name));
    sessionStorage.setItem('userInfo', JSON.stringify(user))
  }
  const checkBlock = (item) =>
  {
    if (sessionStorage.getItem('logged')) {
      for (const name of user.profile.usersThisUserHasBlocked) {
        if (name === item.creatorName) {
          return false;
        }
      }
      for (const name2 of user.profile.usersThatBlockedThisUser) {
        if (name2 === item.creatorName) {
          return false;
        }
      }
    }
    return true;
  }
  const dispTime = (item) =>
    {
      return "Date: " + item[1] + "/" + item[2]
    }
  let anon = "Anon"
  //get the post list here, the name of the topic is in the variable location
  //const obj = await getResponse({location});
  return (
      <div className="Topic">
        <Navbar/>
        <form className={classes.root}>
          <h1 >Topic: {location.name}</h1>
          {!follow && <Button variant="contained" size="small" color="primary" onClick={() => handleFollow(true)}>
              Follow
          </Button>}
          {follow && <Button variant="outlined" size="small" color="primary" onClick={() => handleFollow(false)}> 
              Unfollow
          </Button>}
          <List sx={{ width: '100%', padding: 5, bgcolor: 'background.paper' }}>
         {location.sortedPosts.filter(check => checkBlock(check)).map((item) => {
            return(
              <ListItem button divider onClick={() => routeChange(item)}>
                <ListItemText   primary = {item.title}
                  />
                  {!item.isAnonymous &&<ListItemText   primary = {item.creatorName}
                  />}
                  {item.isAnonymous && <ListItemText   primary = {anon}
                  />}
              <ListItemText secondary={item.topicName}/>
                  <ListItemText secondary={dispTime(item.creationTime)}/>
              </ListItem>
             )
        })}</List></form>
      </div>
  );
};

export default Topic;