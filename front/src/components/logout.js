import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { profileInfo } from "../recoil/atom/loggedAtom";
import { useRecoilState } from 'recoil';
import styled from 'styled-components';


function Logout(props) {

  const [profile, setProfile] = useRecoilState(profileInfo);
  const navigate = useNavigate();
  const logout = () => {
    setProfile({'loggedIn':'false', 'jwt': null, 'role': 'null'})
    navigate('/login');
  }

  return(
    <button onClick={logout}>
      Logout
    </button>
  )
}

export {
  Logout
}