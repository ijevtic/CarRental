import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { profileInfo } from '../../recoil/atom/loggedAtom';
import { useRecoilState } from 'recoil';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import styled from 'styled-components';
import 'react-tabs/style/react-tabs.css';
import { UserProfile } from './userprofile';
import { Filter } from './filter';
import { Reservations } from './reservations';

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
      <Tabs>
        <TabList>
          <Tab>Profile</Tab>
          <Tab>Filter</Tab>
          <Tab>Reservations</Tab>
        </TabList>

        <TabPanel>
          <UserProfile fetchUser={props.fetchUser}/>
        </TabPanel>
        <TabPanel>
          <Filter />
        </TabPanel>
        <TabPanel>
        <Reservations />
        </TabPanel>
      </Tabs>
    </div>
  )
}

export{
  User
}