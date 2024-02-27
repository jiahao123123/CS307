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

async function getResponse() {
    return await fetch('http://localhost:8080/editProfile', {
      method: 'POST',
      headers: {"Content-Type": "text/html", "Origin": "http://localhost:3000"},
      mode: "cors",
      body: sessionStorage.getItem('requestInfo')
    }).then((response) => response.text())
        .then((responseText)=>{return responseText})
}

const Item = styled(Paper)(({ theme }) => ({
    backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
    color: theme.palette.text.secondary,
}));
async function deleteAccount() {
    //sessionStorage.clear();
    return await fetch('http://localhost:8080/deleteUser', {
        method: 'POST',
        headers: {"Content-Type": "application/json", "Origin": "http://localhost:3000"},
        mode: "cors",
        body: sessionStorage.getItem('user')
    }).then((response) => response.text())
        .then((responseText) => {return responseText});
}

const Profile = () => {

    let navigate = useNavigate();
    sessionStorage.setItem("test", false);
    const temp = sessionStorage.getItem("user");
    const routeChange = () => {
        let path ="/";
        navigate(path);
    }
    const handleSubmit = async e => {
        setisTrue(!isTrue);
        const obj = JSON.parse(sessionStorage.getItem('requestInfo'));
        obj.bioIsPublic = !isTrue;
        sessionStorage.setItem('requestInfo', JSON.stringify(obj));
        const text = await getResponse(); 
    }
    const handleSubmitSex = async e => {
        setisSex(!isSex);
        const obj = JSON.parse(sessionStorage.getItem('requestInfo'));
        obj.sexIsPublic = !isSex;
        sessionStorage.setItem('requestInfo', JSON.stringify(obj));
        const text = await getResponse(); 
        console.log(text);
    }
    const handleSubmitAge = async e => {
        setisAge(!isAge);
        obj = JSON.parse(sessionStorage.getItem('requestInfo'));
        obj.ageIsPublic = !isAge;
        sessionStorage.setItem('requestInfo', JSON.stringify(obj));
        const text = await getResponse(); 
    }


    const handleSubmitDm = async e => {
        setisDm(!isDm);
        obj = JSON.parse(sessionStorage.getItem('requestInfo'));
        obj.dmStatus = !isDm;
        sessionStorage.setItem('requestInfo', JSON.stringify(obj));
        const text = await getResponse();  
    }

    const handleDelete = async e => {
        console.log("deleting")
        deleteAccount();
        sessionStorage.clear();
        routeChange();
    }

    let obj = JSON.parse(sessionStorage.getItem('requestInfo'));
    
    const bioPublic = obj.bioIsPublic;
    const sexPublic = obj.sexIsPublic;
    const agePublic = obj.ageIsPublic;
    const dmPublic = obj.dmStatus;
    const [isTrue, setisTrue] = useState(bioPublic ? true : false);
    const [isSex, setisSex] = useState(sexPublic ? true : false);
    const [isAge, setisAge] = useState(agePublic ? true : false);
    const [isDm, setisDm] = useState(dmPublic ? true : false);
    console.log("Profile info: " + JSON.stringify(obj));
    sessionStorage.setItem("publicBio", bioPublic);
    const current = JSON.parse(sessionStorage.getItem('userInfo'));
    const postlength = obj.numPosts;
    const followerlength = current.profile.userFollowers.length;
    const followinglength = current.profile.userFollowing.length;
    const followingTopiclength = obj.followedTopics.length;
    const userSex = obj.sex;
    const userAge = obj.age;
    const handleTopics = async e => {
        let path ="/profileTopic";
        navigate(path);
    }
    return (
        <div className="Profile">
            <Header/>
            <hr/>
            <Nav/>
            <br/>
            <div style={{display:'flex', justifyContent:'center', alignItems:'center'}}>
            <h1>{temp}</h1>
            </div>
            <div>
      
                <img alt="Avatar" src="https://pic.onlinewebfonts.com/svg/img_550783.png" className="avatar"/>
                <Stack
                direction="row"
                divider={<Divider orientation="vertical" flexItem />}
                spacing={2}
                justifyContent="center"
                >
                    <Item>Email: {obj.email}</Item>
                    <Item>Gender: {userSex}</Item>
                    <Item>Age: {userAge}</Item>
                    <button onClick={(handleSubmitSex)}>{isSex ? 'Sex is public' : 'Sex private'}</button>
                    <button onClick={(handleSubmitAge)}>{isAge ? 'Age is public' : 'Age is private'}</button>
                    <button onClick={(handleSubmit)}>{isTrue ? 'About me is public' : 'About me is private'}</button>
                    <button onClick={(handleSubmitDm)}>{isDm ? 'Anyone can DM' : 'Only Users I Follow can DM'}</button>
                </Stack>
            </div>
            <div>
                
            </div>
            <div>
                <h4 style={{display: "flex", fontSize: 30, content: "username"}}></h4>
                <div style={{display: "flex", justifyContent: "center ", justifyContent: "space-evenly"}}>
                    <h3>{" "}{postlength} posts{" "}</h3>
                    <h3>{" "}{followerlength} followers{" "}</h3>
                    <h3>{"     "}{followinglength} following{" "}</h3>
                    <h3>{"     "}{followingTopiclength} following Topic{" "}</h3>
                    <Button type="submit" variant="contained" color="inherit"  onClick={handleDelete}> 
                        Delete Account
                    </Button>
                </div>
                <div style={{display: "center", justifyContent: "center", textAlign: 'center'}}>
                    <Button variant="contained" onClick={handleTopics}> Followed Topic and User </Button>
                </div>
                
            </div>
            <br/>

            <section id="container-about" className="container-about">
            <h1>About Me</h1>
            <div>
            <h1>{obj.bio}</h1>
            </div>
            </section> 
            
        </div>
    );
}
export default Profile;
