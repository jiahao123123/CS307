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

async function getResponseUser(follow, to) {
  const username = sessionStorage.getItem('user');
  return await fetch('http://localhost:8080/followUser', {
    method: 'POST',
    headers: {"Content-Type": "application/json", "origin" : "localhost:3000"},
    mode: "cors",
    body: to + ", " + username + ", " + follow
  }).then((response) => response.text())
      .then((responseText) => {return responseText});
}




const ProfileTopic = () => {
    const reptiles = JSON.parse(sessionStorage.getItem('userInfo'));
    const posts = reptiles.profile.followingTopicsList;
    const useFollow = reptiles.profile.userFollowing;
    console.log(reptiles.profile.followingTopicsList[0]);
    const [follow, setFollow] = useState(false);
    let user
    if (sessionStorage.getItem('logged')) {
      user = JSON.parse(sessionStorage.getItem('userInfo')); 
    }
    const handleFollow = async (bool, name) => {
      // If called with true we want to add the topic to users followed, if called with false we want to remove topic from users followed
      console.log("Bool: " + bool);
      setFollow(bool)
      user = JSON.parse(await getResponse(bool, name));
      sessionStorage.setItem('userInfo', JSON.stringify(user))
    }
    const handleFollowUser = async (bool, username) => {
      // If called with true we want to add the topic to users followed, if called with false we want to remove topic from users followed
      console.log("Follow: " + bool);
      setFollow(bool)
      let user = JSON.parse(await getResponseUser(bool, username));
      sessionStorage.setItem('userInfo', JSON.stringify(user))
    }
    const User = ({ name, creatorName }) => (
      <div>
        <div style={{display:'flex', justifyContent:'center', alignItems:'center'}}>
          <h1>Topic Name:{name}</h1>
          {<Button variant="outlined" size="small" color="primary" onClick={() => handleFollow(false, name)}> 
              Unfollow
          </Button>}
        </div>
      </div>
    );
    const UserFollowing = ({name}) => (
      <div>
        <div style={{display:'flex', justifyContent:'center', alignItems:'center'}}>
          <h1>Name:{name} </h1>
          {<Button variant="outlined" size="small" color="primary" onClick={() => handleFollowUser(false, name)}> 
              Unfollow
          </Button>}
        </div>
      </div>
    );
    const topic = () => {
      const length = reptiles.profile.followingTopicsList.length;
      const userList = [];
      for (let i = 0; i < length; i++) {
        let name = posts[i].name;
        let creator = posts[i].creatorName;
        userList.push(<User name={name} creatorName={creator}/>);
      };
      return userList;
    }
    const followingUser = () => {
      const length = reptiles.profile.userFollowing.length;
      const userList = [];
      for (let i = 0; i < length; i++) {
        let name = useFollow[i];
        userList.push(<UserFollowing name={name}/>);
      };
      return userList;
    }
    return (
      <div className="ProfileTopic">
        <Navbar/>
        <div>
          <h1 style={{display:'flex', justifyContent:'center', alignItems:'center'}}>Followed Topics</h1>
          {topic()}
          <h1 style={{display:'flex', justifyContent:'center', alignItems:'center'}}>Followed User</h1>
          {followingUser()}
        </div>
      </div>
    );
};

export default ProfileTopic;