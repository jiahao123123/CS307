import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import { useNavigate, Redirect, useHistory } from "react-router-dom";



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

async function getResponse() {
  return await fetch('http://localhost:8080/editProfile', {
    method: 'POST',
    headers: {"Content-Type": "text/html", "Origin": "http://localhost:3000"},
    mode: "cors",
    body: sessionStorage.getItem('requestInfo')
  }).then((response) => response.text())
      .then((responseText)=>{return responseText})
}



export const EditForm = ({ handleClose }) => {
  let navigate = useNavigate(); 
  const routeChange = (aboutt) =>{ 
    let path = `/profile`; 
    navigate(path, {state:aboutt});
  }
  const classes = useStyles();
  // create state variables for each input
  let [username, setusername] = useState('');
  let [about, setAbout] = useState('');
  let [gender, setGender] = useState('');
  let [age, setAge] = useState('');
  
  const update=()=> {
    return about
  }

  const handleSubmit = async e => {
    e.preventDefault();
    console.log(username, about);
    const EnditInfo = {username, about};
    let err = 0
    
    const throwError = (error, value) =>{ 
      let path = `/error`; 
      navigate(path, {state:[error,value]});
    }
    const throwPrifle = async (about, gender, age) =>{
      let path = `/profile`;
      sessionStorage.setItem('bio', about);
      const p = JSON.parse(sessionStorage.getItem('requestInfo'));

      console.log("New About: " + about)
      console.log("New Gender: " + gender)
      console.log("New Age: " + age)

      if (about.length > 0) {
        p.bio = about;
      }
      if (gender.length > 0) {
        p.sex = gender;
      }
      if (age.length > 0) {
        p.age = age;
      }
      sessionStorage.setItem('requestInfo', JSON.stringify(p));
      console.log("New request info:" + sessionStorage.getItem('requestInfo'));
      const text = await getResponse();
      console.log("The Response: ", text);
      navigate(path, {state:[about, gender, age]});
    }
    console.log(about);

    console.log("About Length: " + about.length)
    console.log("Gender Length: " + gender.length)
    console.log("Age Length: " + age.length)

    if (about.length == 0 && gender.length == 0 && age.length == 0) {
      throwError("Error: Can not have everything empty", "/profile");
      err = 1
      console.log("about");
    }
    if (about.length > 0) {
      if (about.length > 60 || about.length < 5) {
        throwError("Error: about me length incorrect", "/profile");
        err = 1
        console.log("About Error")
      } 
    }
    if (age.length > 0) {
      if (isNaN(age)) {
        throwError("Error: enter a number","/profile");
        err = 1
      }
    }

    console.log("Gender Check")
    let newGender = gender

    if (gender == 'male' || gender == 'Male' || gender == 'MALE') {
      console.log("In Male")
      setGender('MALE')
      newGender = 'MALE'
    }
    else if (gender == 'female' || gender == 'Female' || gender == 'FEMALE') {
      console.log("In Female")
      setGender('FEMALE')
      newGender = 'FEMALE'
    }
    else if (gender == 'trans' || gender == 'Trans' || gender == 'TRANS' || gender == 'transgender' || gender == 'Transgender' || gender == 'TRANSGENDER') {
      console.log("In Trans")
      setGender('TRANSGENDER')
      newGender = 'TRANSGENDER'
    }
    else if (gender == 'neutral' || gender == 'Neutral' || gender == 'NEUTRAL') {
      console.log("In Neutral")
      setGender('NEUTRAL')
      newGender = 'NEUTRAL'
    }
    else if (gender.length > 0) {
      throwError("Error: please enter male, female, transgender, or netural as gender", "/profile")
      err = 1
      console.log("Gender Error")
    }

    if (err == 0) {
      await throwPrifle(about, newGender, age);
      handleClose();
    }

    // Same as LogIn info
    // Add server send

    // handleClose();
  };

  return (
    <form className={classes.root} onSubmit={handleSubmit}>
      {/* <TextField
        label="New Username"
        variant="filled"
        required
        value={username}
        onChange={e => setusername(e.target.value)}
      /> */}
      <TextField
        label="About Me"
        variant="filled"
        type="about"
        required
        value={about}
        onChange={e => setAbout(e.target.value)}
      />
      <TextField
        label="Gender (Male, Female, Trans, Neutral)"
        variant="filled"
        type="gender"
        required
        value={gender}
        onChange={e => setGender(e.target.value)}
      />
      <TextField
        label="Age"
        variant="filled"
        type="age"
        required
        value={age}
        onChange={e => setAge(e.target.value)}
      />
      <div>
        <Button variant="contained" onClick={handleClose}>
          Cancel
        </Button>
        <Button type="submit" variant="contained" color="primary" onClick={handleSubmit}> 
          Submit
        </Button>
      </div>
    </form>
  );
};


export default EditForm;