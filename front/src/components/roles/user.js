import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { profileInfo } from "../../recoil/atom/loggedAtom";
import { useRecoilState } from 'recoil';
import styled from 'styled-components';

function User(props) {
  const [profile, setProfile] = useRecoilState(profileInfo);
  let sent = false;

  const navigate = useNavigate();

  useEffect(() => {
    if(profile.loggedIn == 'false') {
      navigate('/login');
      return;
    }
    
  }, []);

  return (
    <div>
      <h1>User</h1>
    </div>
  )
}

export{
  User
}