import React, { Component } from 'react';
import TextField from '@mui/material/TextField';
import { makeStyles } from '@mui/material/styles';
import Button from '@mui/material/Button';

export class Test extends Component {
    
  // create state variables for each input

   handleSubmit = e => {
    e.preventDefault();
    console.log("submitted");
  };  
    render() {
        return (
            <form  onSubmit={this.handleSubmit}>
                <TextField 
                    label="First Name"
                    variant="filled"
                    required
                />
                <br/>
                <TextField
                    label="Last Name"
                    variant="filled"
                    required
                />
                <br/>
                <TextField
                    label="Email"
                    variant="filled"
                    type="email"
                    required
                />
                <br/>
                <TextField
                    label="Password"
                    variant="filled"
                    type="password"
                    required
                />
                <br/>
                <Button type="submit" variant="contained" color="primary" onClick={this.handleSubmit}>
                    Sign Up
                </Button>
            </form>
        )
    }
}
export default Test;