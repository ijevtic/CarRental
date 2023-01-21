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

  const fetchLocations = async () => {
    const res = await fetch(process.env.REACT_APP_RENT_SERVICE_URL + '/getLocations', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': profile.token,
        },
        }).then(res => res.json())
        .then(res => {
          console.log(res);
          if(res.statusCode != 200) {
            alert(res.message)
          }
          return res.data;
        }
        )
        .catch(error => console.error('Error:', error));
        return res;
  }

  const navigate = useNavigate();

  const fetchUser = () => {
    fetch(process.env.REACT_APP_USER_SERVICE_URL+'/getUserData', {
      'method': 'GET',
      // 'mode': 'no-cors',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': profile.jwt,
      },
    }).then(res => res.json())
    .then(res => {
      console.log(res);
      if(res.statusCode != 200) {
        alert(res.message)
      }
      setProfile((profile) => {
        return({
          ...profile,
          data: res.data
        });
      });
    })
    .catch(error => console.error('Error:', error));

  }

  useEffect(() => {
    if(profile.loggedIn == 'false') {
      navigate('/login');
      return;
    }
    fetchUser();
    console.log(profile);
  }, []);

  return (
    <div>
      <Logout />
      <h1>Home</h1>
      {profile.data && profile.role == 'ADMIN' && <Admin fetchUser = {fetchUser}/>}
      {profile.data && profile.role == 'USER' && <User fetchUser = {fetchUser} fetchLocations = {fetchLocations}/>}
      {profile.data && profile.role == 'MANAGER' && <Manager fetchUser = {fetchUser} fetchLocations = {fetchLocations}/>}
    </div>
  )
}

export default Home;