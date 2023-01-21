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
import { ManagerProfile } from './managerprofile';
import { ManagerReservations } from './managerReservations';
import { ManagerModels } from './managerModels';
import { ManagerVehicles } from './managerVehicles';

function Manager(props) {
  const [profile, setProfile] = useRecoilState(profileInfo);
  let sent = false;

  const navigate = useNavigate();

  const fetchModels = async () => {
    const models = await fetch(process.env.REACT_APP_RENT_SERVICE_URL + "/getCompanyModels", {
      method: 'GET',
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
      return res.data;
    })
    .catch(error => console.error('Error:', error))
    return models;
  }

  useEffect(() => {
    if(profile.loggedIn == 'false') {
      navigate('/login');
      return;
    }
    
  }, []);

  return (
    <div>
      <h1>Manager</h1>
      <Tabs>
        <TabList>
          <Tab>Profile</Tab>
          <Tab>Models</Tab>
          <Tab>Vehicles</Tab>
          <Tab>Reservations</Tab>
        </TabList>

        <TabPanel>
          <ManagerProfile fetchUser={props.fetchUser}/>
        </TabPanel>
        <TabPanel>
          <ManagerModels fetchModels={fetchModels}/>
        </TabPanel>
        <TabPanel>
          <ManagerVehicles fetchModels={fetchModels} fetchLocations={props.fetchLocations}/>
        </TabPanel>
        <TabPanel>
          <ManagerReservations />
        </TabPanel>
      </Tabs>
    </div>
  )
}

export{
  Manager
}