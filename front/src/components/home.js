import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { profileInfo } from "../recoil/atom/loggedAtom";
import { useRecoilState } from 'recoil';
import { Logout } from './logout';
import styled from 'styled-components';
import { Admin } from './roles/admin';
import { User } from './roles/user';
import { Manager } from './roles/manager';


function Home(props) {
  const [profile, setProfile] = useRecoilState(profileInfo);
  let sent = false;

  const navigate = useNavigate();

  useEffect(() => {
    if(profile.loggedIn == 'false') {
      navigate('/login');
      return;
    }
    
    console.log(profile);
  }, []);

  return (
    <div>
      <h1>Home</h1>
      {profile.role == 'ADMIN' && <Admin />}
      {profile.role == 'USER' && <User />}
      {profile.role == 'MANAGER' && <Manager />}
      <Logout />
    </div>
  )
}

export default Home;