import React, { Component, Fragment } from 'react'

import '../profile.css'
import EditForm from './EditForm'
import { useNavigate, Redirect, useLocation } from "react-router-dom";


const Bios = () =>  {
    let navigate = useNavigate(); 
    const location = {about:""};
    if (sessionStorage.getItem('private') == true) {
        location.about = "About Me is private";
    } else {
        location.about = sessionStorage.getItem('about');
    }
    
    
    return (
        <section id="container-about" className="container-about">
            <h1>About Me</h1> 

            <Fragment>{location.about}</Fragment>
            
        </section> 
    )
    
}

export default Bios