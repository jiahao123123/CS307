import * as React from 'react';
import Navbar from "./Navbar";
import { useEffect } from 'react';
import { useLocation, useNavigate } from "react-router-dom";
import {useState} from 'react'
import Button from '@material-ui/core/Button';
import { TextField } from "@mui/material";
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Box from '@material-ui/core/Box';
import LoadingButton from '@mui/lab/LoadingButton';
import Heart from "react-animated-heart";
import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@material-ui/icons/Delete'
import Airplane from '@mui/icons-material/AirplanemodeActive'
import Anchor from '@mui/icons-material/Anchor'
async function getResponse(comment, post) {
    const username = sessionStorage.getItem('user');
    return await fetch('http://localhost:8080/addComment', {
        method: 'POST',
        headers: {"Content-Type": "application/json", "origin" : "localhost:3000"},
        mode: "cors",
        body: comment + ", " + sessionStorage.getItem('user')+ ", " + post  
    }).then((response) => response.text())
        .then((responseText) => {return responseText});
}
async function removeComment(comment, post) {
  const username = sessionStorage.getItem('user');
  return await fetch('http://localhost:8080/removeComment', {
      method: 'POST',
      headers: {"Content-Type": "application/json", "origin" : "localhost:3000"},
      mode: "cors",
      body: comment + ", " + post + ", " + username
  }).then((response) => response.text())
      .then((responseText) => {return responseText});
}

async function addReaction(reaction, post, type) {
    console.log("I HAD ADDED THE REACTION");
    return await fetch('http://localhost:8080/postReactions', {
        method: 'POST',
        headers: {"Content-Type": "application/json", "origin" : "localhost:3000"},
        mode: "cors",
        body: reaction + ", " + post + ", " + sessionStorage.getItem('user') +", " + type
    }).then((response) => response.text())
        .then((responseText) => {return responseText});
}

async function removeReaction(username, post) {
    return await fetch('http://localhost:8080/removeReaction', {
        method: 'POST',
        headers: {"Content-Type": "application/json", "origin" : "localhost:3000"},
        mode: "cors",
        body: username + ", " + post
    }).then((response) => response.text())
        .then((responseText) => {return responseText});
}

const Post = (title, content) => {
    const location = useLocation().state;
    console.log(location)
    const [open, setOpen] = useState(false);
    const [comment, setComment] = useState('');
    const [show, setShow] = useState('');
    const [disp, setDisp] = useState(false);
    const [isClick, setClick] = useState(false);
    const [isUp, setUp] = useState(false);
    const [isDown, setDown] = useState(false);
    const [count, setCount] = useState(0);
    var postImage = null;

    if (location.hasImage === true) {
        postImage = URL.createObjectURL(location.image)
    }
    useEffect(() => {
      setCount(0)
      if (sessionStorage.getItem('logged')) {
        let user2 = sessionStorage.getItem('user'); 
        let user3 = JSON.parse(sessionStorage.getItem('userInfo')); 
        let one = 0;
        for (const name of location.interactions) {
          if (name.type === "DISLIKE") {
            console.log("dislike")
            one = one -1;
          }
          if (name.type === "LIKE") {
            console.log(count)
            one = one +1
          }
          if (name.creatorName === user2) {
            if (name.type === "DISLIKE") {
              setDown(true);
            }
            else if (name.type === "LIKE") {
              setUp(true);
            }
          }
        }
        for (const check of user3.profile.savedPostIds) {
            console.log(check +": " + location.id)
            if (check === location.id) {
              setClick(true);
            }
        }
        setCount(one)
      }
    }, []);
    // function to handle modal open
    const handleOpen = () => {
      setOpen(true);
    };
    let user = JSON.parse(sessionStorage.getItem('userInfo')); 
    const handleSubmit = async (e) => {
      //posts
      e.preventDefault();
      if (sessionStorage.getItem('logged')) {
        setDisp(true);
        setShow(comment);
        console.log(await getResponse(comment, location.id));
      }
    }
    const handleDelete = async (ke) => {
      if (ke.creatorName === user.username) {
        removeComment(ke.id, location.id)
        console.log("deleted comment "+ ke.id)
      }
      else {
        console.log(ke.creatorName + ", " + user.username)
      }
    }
    const reactSubmit = async (click, type) => {
      //posts
      if (sessionStorage.getItem('logged')) {
        console.log(click)
        if (type == "LIKE") {
          setUp(click)
        }
        else if (type == "DISLIKE") {
          setDown(click)
        }
        else {
          setClick(click)
        }
        if (click === false && type != "SAVE") {
          reactDelete()
        }
        else {
          const ret = await addReaction(click, location.id, type);
          if (ret.indexOf("OK") === -1) {
            sessionStorage.setItem('userInfo', ret);
          }
          
        }
      }
    }
    const reactDelete = async () => {
      removeReaction(sessionStorage.getItem('user'), location.id);
    }
    let anon = "Anonymous";
    return(
        <div className="Post">
                <Navbar/>
        <form className="Post">
          <h1>Post by: {!location.isAnonymous && location.creatorName}
                  {location.isAnonymous && anon}</h1>
          <img className="post_image" src={postImage} alt="" />
          <h2>{location.caption}</h2>
          <Box display="flex">
            <div>
            <TextField
              label="Comment"
              variant="outlined"
              size="small"
              required
              value={comment}
              onChange={e => setComment(e.target.value)}
            />
              <Button type="submit" variant="contained" color="primary" disabled={disp} onClick={handleSubmit}> 
                Add Comment
              </Button>
            </div>            
            <IconButton onClick={() => reactSubmit(!isUp, "LIKE")}><Airplane/></IconButton>
            <IconButton onClick={() => reactSubmit(!isDown, "DISLIKE")}><Anchor/></IconButton>
          </Box>
          <h1 align="center">Planes vs Anchors: {count}</h1>
          <Heart isClick={isClick} onClick={() => reactSubmit(!isClick, "SAVE")} />
          <List >
          
                {location.comments.map((item) => {
                return(
              <ListItem divider secondaryAction={
                <IconButton edge="end" aria-label="delete" onClick={() => handleDelete(item)}>
                  <DeleteIcon />
                </IconButton>
              }>
                  <ListItemText
                    primary={item.text}
                    secondary={item.creatorName}
                  />
                </ListItem>)
            })}
            {disp && 
          <ListItem divider>
                  <ListItemText
                    primary={show}
                    secondary={user.username}
                  />
                </ListItem>}  
            </List>
          
          </form>
        </div>
    );
}
export default Post;