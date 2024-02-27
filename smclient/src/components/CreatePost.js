import * as React from 'react';
import { makeStyles } from '@material-ui/core';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import { useNavigate, Redirect } from "react-router-dom";
import { useState } from "react";
import { useRef } from "react";
import Navbar from './Navbar'
import { styled } from '@mui/material/styles';
import Checkbox from '@mui/material/Checkbox';
import FormControlLabel from '@mui/material/FormControlLabel';
import * as PropTypes from "prop-types";
import FileUploader from './FileUploader'

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

const Input = styled('input')({
    display: 'none',
});

async function getResponse(topicName, postTitle, postInfo, setChecked, img) {
    const creator = sessionStorage.getItem('user');

    console.log(creator)
    return await fetch('http://localhost:8080/post', {
        method: 'POST',
        headers: {"Content-Type": "text/html", "Origin": "http://localhost:3000"},
        mode: "cors",
        body: JSON.stringify(topicName + ", " + postTitle + ", " + postInfo + ", " + setChecked + ", "
            + img + ", " + creator)
    }).then((response) => response.text())
        .then((responseText) => {return responseText});
}

function imageUploaded(file) {
    return new Promise(function (resolve, reject) {
        var reader = new FileReader();

        reader.onload = function () {
            resolve(reader.result);
        }
        reader.onerror = reject;
        reader.readAsDataURL(file);
    });
}

const CreatePost = () => {
    const classes = useStyles();
    // create state variables for each input
    const [topicName, setTopicName] = useState('');
    const [selectedFile, setSelectedFile] = useState("null");
    const [postInfo, setPostInfo] = useState('');
    const [postTitle, setPostTitle] = useState('');
    let navigate = useNavigate(); 
    const [checked, setChecked] = React.useState(false);

    const routeChange = (name) =>{ 
      let path = `/topic`; 
      navigate(path, {state:name});
    }
    const throwError = (error) =>{
      let path = `/error`; 
      navigate(path, {state:error});
    }
    const handleChange = (event) => {
      setChecked(event.target.checked);
    }
    const handleSubmit = async e => {
      e.preventDefault();

      console.log("File Uploaded: " + selectedFile);

      console.log("Post: " + topicName + " " + postInfo + " " + sessionStorage.getItem('user'));

      let imgB64 = "null";

      if (selectedFile !== "null") {
          var promise = imageUploaded(selectedFile);
          imgB64 = await promise;

          console.log("Img Promise: ", imgB64);
      }

      if (topicName === '') {
        throwError("topicName field cannot be blank")
      } else if (postTitle === '') {
        throwError("postTitle field cannot be blank")
      } else if (postInfo === '') {
        throwError("postInfo field cannot be blank")
      } else {
        const ret = await getResponse(topicName, postTitle, postInfo, checked, imgB64);
          if (ret.indexOf("Error") !== -1) {
              throwError(ret)
          } else {
              console.log("Returned from Server: ", ret);
              sessionStorage.setItem('img', null);
              routeChange(ret);
          }
          //handleClose();
        }
    };
    
    return(
      
        <div className="CreatePost" ><Navbar/>
        <form className={classes.root}><TextField
        label="Topic Name"
        variant="filled"
        required
        value={topicName}
        onChange={e => setTopicName(e.target.value)}
      />
      <TextField
        label="Post Title"
        variant="filled"
        required
        value={postTitle}
        onChange={e => setPostTitle(e.target.value)}
      />
      <TextField
        label="Post Info"
        variant="filled"
        required
        value={postInfo}
        onChange={e => setPostInfo(e.target.value)}
        multiline
        minRows = {6}
        maxRows = {10}
      />
      <FileUploader
          onFileSelectSuccess={(file) => setSelectedFile(file)}
          onFileSelectError={({ error }) => alert(error)}
      />
    <FormControlLabel label="Anonymous"
        control={<Checkbox
      checked={checked}
      onChange={handleChange}
      inputProps={{ 'aria-label': 'controlled' }}
      label = "private"
    />}/>
    <img className="preview" src={sessionStorage.getItem('img')} alt="" />
      <Button variant="contained" color="primary" onClick={handleSubmit}>
      Create
    </Button>
    </form>
        </div>
        );
}
export default CreatePost;