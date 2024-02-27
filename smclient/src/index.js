import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Profile from "./components/Profile";
import Timeline from "./components/Timeline";
import CreateTopic from './components/CreateTopic';
import ErrorForm from './components/ErrorForm';
import Topic from './components/Topic';
import Search from './components/Search';
import Post from './components/Post';
import LogIn from './components/LogIn';
import Form from './components/Form';
import Private from './components/profile/Private';
import CreatePost from './components/CreatePost';
import ViewerProfile from './components/ViewerProfile';
import ProfileTopic from './components/ProfileTopic';
import Userline from './components/Userline';
import Interactions from './components/Interactions'
import DM from './components/DM'
ReactDOM.render(
  <Router>
  <Routes>
    <Route path="/" element={<App />} />
    <Route path="/profile" element={<Profile />} />
    <Route path="/timeline" element={<Timeline />} />
    <Route path="/userline" element={<Userline />} />
    <Route path="/interactions" element={<Interactions/>}/>
    <Route path="/createtopic" element={<CreateTopic />} />
    <Route path="/error" element={<ErrorForm />} />
    <Route path="/topic" element={<Topic />} />
    <Route path="/search" element={<Search />} />
    <Route path="/post" element={<Post />} />
    <Route path="/login" element={<LogIn />} />
    <Route path="/signup" element={<Form />} />
    <Route path="/private" element={<Private />} />
    <Route path="/createPost" element={<CreatePost />} />
    <Route path="/viewerProfile" element={<ViewerProfile />} />
    <Route path="/profileTopic" element={<ProfileTopic />} />
    <Route path="/dm" element={<DM />} />
  </Routes>
</Router>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
