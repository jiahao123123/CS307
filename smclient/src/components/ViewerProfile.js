import { Avatar, Button } from "@material-ui/core";
import React from "react";
import { useState, useEffect } from 'react';
import Header from './profile/Header'
import Nav from './profile/Nav'
import { useNavigate, Redirect, useLocation } from "react-router-dom";
import Divider from '@mui/material/Divider';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import { styled } from '@mui/material/styles';
import { Box, makeStyles } from '@material-ui/core';
import Navbar from './Navbar';

const useStyles = makeStyles(theme => ({
    root: {
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'center',
      alignItems: 'center',
      padding: theme.spacing(2),
    },
  }));
const Item = styled(Paper)(({ theme }) => ({
    backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
    color: theme.palette.text.secondary,
}));
async function getUserline() {
  let userInfo;
  if (sessionStorage.getItem('logged')) {
      userInfo = JSON.parse(sessionStorage.getItem('visit')).username;
      console.log("User is logged in!");
  }
  else {
    console.log("User is not logged in!");
      return "user not logged in";
  }
  return await fetch('http://localhost:8080/userline', {
      method: 'POST',
      headers: {"Content-Type": "text/html", "Origin": "http://localhost:3000"},
      mode: "cors",
      body: userInfo
  }).then((response) => response.text())
      .then((responseText)=>{return responseText})
}
async function getResponse(follow, to) {
    const username = sessionStorage.getItem('user');
    return await fetch('http://localhost:8080/followUser', {
      method: 'POST',
      headers: {"Content-Type": "application/json", "origin" : "localhost:3000"},
      mode: "cors",
      body: to + ", " + username + ", " + follow
    }).then((response) => response.text())
        .then((responseText) => {return responseText});
  }
  async function blockResponse(block, blockee) {
    const username = sessionStorage.getItem('user');
    return await fetch('http://localhost:8080/blockUser', {
      method: 'POST',
      headers: {"Content-Type": "application/json", "origin" : "localhost:3000"},
      mode: "cors",
      body: blockee + ", " + username + ", " + block
    }).then((response) => response.text())
        .then((responseText) => {return responseText});
  }
  async function getInteract() {
    const username = sessionStorage.getItem('user');
    return await fetch('http://localhost:8080/allInteract', {
      method: 'POST',
      headers: {"Content-Type": "application/json", "origin" : "localhost:3000"},
      mode: "cors",
      body: username 
    }).then((response) => response.text())
        .then((responseText) => {return responseText});
  }
  async function getDM(visit) {
    const username = JSON.parse(sessionStorage.getItem('userInfo'));
    return await fetch('http://localhost:8080/getDM', {
      method: 'POST',
      headers: {"Content-Type": "application/json", "origin" : "localhost:3000"},
      mode: "cors",
      body: username.id +", " + visit
    }).then((response) => response.text())
        .then((responseText) => {return responseText});
  }
const ViewerProfile = () => {
    const classes = useStyles();
    let navigate = useNavigate(); 
    let location = JSON.parse(useLocation().state);
    let profile = location.profile;
    let publicSex = profile.sex.publicInfo;
    let publicAge = profile.age.publicInfo;
    let publicAbout = profile.bio.publicInfo;
    const postlength = profile.numPosts;
    const followerlength = profile.userFollowers.length;
    const followinglength = profile.userFollowing.length;
    const [follow, setFollow] = useState(false);
    const [block, setBlock] = useState(false);
    const followingTopiclength = profile.followingTopicsList.length;
    let username = location.username;
    const show = sessionStorage.getItem('logged');
    useEffect(() => {
        if (sessionStorage.getItem('logged')) {
          let user = JSON.parse(sessionStorage.getItem('userInfo')); 
            for (const name of user.profile.usersThisUserHasBlocked) {
              if (name === username) {
                setBlock(true)
                console.log('start blocked')
              }
            }
            for (const name of user.profile.userFollowing) {
              if (name === username) {
                setFollow(true)
                console.log('start follow')
              }
            }
          }
      }, []);
    const swapHome = async e => {
        let path = `/userline`;
        const pass = await getUserline();
        console.log("Before:" + pass)
        //add timelime getResponse
        navigate(path, {state:pass});
      }
    const swapInteract = async e => {
        let path = `/interactions`;
        const pass = await getInteract();
        console.log("Before:" + pass)
        //add timelime getResponse
        navigate(path, {state:pass});
      }
      const swapDM = async e => {
        if (checkFollow()) {
          let path = `/dm`;
          const pass = await getDM(location.id);
          //add timelime getResponse
          navigate(path, {state:pass});
        }
      }
    const handleFollow = async (bool) => {
        // If called with true we want to add the topic to users followed, if called with false we want to remove topic from users followed
        console.log("Follow: " + bool);
        setFollow(bool)
        let user = JSON.parse(await getResponse(bool, username));
        sessionStorage.setItem('userInfo', JSON.stringify(user))
      }
      const handleBlock = async (bool) => {
        // If called with true we want to add the topic to users followed, if called with false we want to remove topic from users followed
        console.log("Block: " + bool);
        setBlock(bool)
        let ret = JSON.parse(await blockResponse(bool, username));
        sessionStorage.setItem('userInfo', JSON.stringify(ret))
      }
      const checkFollow = () =>
      {
        if (sessionStorage.getItem('logged')) {
          if (!location.profile.dmStatus) {
            for (const name of location.profile.userFollowing) {
              if (name === sessionStorage.getItem('user')) {
                return true;
              }
            }
            return false;
          }
        }
        return true;
      }
    // let age = profile.age;
    return(
        <div className="Topic">
        <Navbar/>
        <div style={{display:'flex', justifyContent:'center', alignItems:'center'}}>
            <h1>{username}</h1>
        </div>
        {show &&
        <div style={{display:'flex', justifyContent:'space-between', alignItems:'center', padding:'15px'}}>
        {!follow && <Button variant="contained" size="small" color="primary" onClick={() => handleFollow(true)}>
              Follow
          </Button>}
          {follow && <Button variant="outlined" size="small" color="primary" onClick={() => handleFollow(false)}> 
              Unfollow
          </Button>}
          {!block && <Button variant="contained" size="small" color="inherit" onClick={() => handleBlock(true)}>
              Block
          </Button>}
          {block && <Button variant="outlined" size="small" color="inherit" onClick={() => handleBlock(false)}> 
              Unblock
          </Button>}
           <Button variant="outlined" size="small" color="primary" onClick={swapDM}> 
              Direct Message
          </Button>
          </div>}
        <Stack
                direction="row"
                divider={<Divider orientation="vertical" flexItem />}
                spacing={2}
                justifyContent="center"
                >
                <Item>Gender: {publicSex ? profile.sex.content: 'Private'}</Item>
                <Item>Age: {publicAge ? profile.age.content: 'Private'}</Item>
        </Stack>
        <div style={{display: "flex", justifyContent: "center ", justifyContent: "space-evenly"}}>
                    <h3>{" "}{postlength} posts{" "}</h3>
                    <h3>{" "}{followerlength} followers{" "}</h3>
                    <h3>{"     "}{followinglength} following{" "}</h3>
                    <h3>{"     "}{followingTopiclength} following Topic{" "}</h3>
        </div>
        <div style={{display:'flex', justifyContent:'center', alignItems:'center'}}>
            <h1>about me</h1>
        </div>
        <Box sx={{ textAlign: 'center', m: 1 }}>{publicAbout ? profile.bio.content: 'Private'}</Box>
        {show &&
        <div style={{display:'flex', justifyContent:'center', alignItems:'center'}}>
        <Button variant="outlined" size="small" color="primary" onClick={swapHome} > 
              User's created posts
          </Button>
          <Button variant="outlined" size="small" color="primary" onClick={swapInteract}> 
              User's Interactions
          </Button>
          </div>}
        </div>  
        );
}
export default ViewerProfile;
