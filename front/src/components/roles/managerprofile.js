import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { profileInfo } from '../../recoil/atom/loggedAtom';
import { useRecoilState } from 'recoil';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';

function ManagerProfile(props) {
  const [profile, setProfile] = useRecoilState(profileInfo);
  const [username, setUsername] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [birthDate, setBirthDate] = useState('');
  const [startWorkDate, setStartWorkDate] = useState('');
  const [showCompanyName, setShowCompanyName] = useState('');
  const [companyName, setCompanyName] = useState('');
  const [password, setPassword] = useState('');

  const navigate = useNavigate();

  const fetchCompanyInfo = () => {
    fetch(process.env.REACT_APP_RENT_SERVICE_URL + '/getCompanyInfo/' + profile.data.companyId, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': profile.jwt,
      }
    }).then(res => res.json())
    .then(res => {
      console.log(res);
      if(res.statusCode != 200) {
        return;
      }
      setShowCompanyName(res.data.companyName);
    })
  }

  useEffect(() => {
    if(profile.loggedIn == 'false') {
      navigate('/login');
      return;
    }
    fetchCompanyInfo();
  }, []);

  const changeProfile = () => {
    fetch(process.env.REACT_APP_USER_SERVICE_URL + '/changeManager', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': profile.jwt,
      },
      body: JSON.stringify({
        "username": username !== '' ? username : null,
        "email": email !== '' ? email : null,
        "phoneNumber": phoneNumber !== '' ? phoneNumber : null,
        "birthDate": birthDate !== '' ? birthDate : null,
        "firstName": firstName !== '' ? firstName : null,
        "lastName": lastName !== '' ? lastName : null,
        "password": password !== '' ? password : null,
        "companyName": companyName !== '' ? companyName : null,
        "startWorkDate": startWorkDate !== '' ? startWorkDate : null,
      })
    }).then(res => res.json())
    .then(res => {
      props.fetchUser();
      fetchCompanyInfo();
    })
    .catch(error => console.error('Error:', error));
  }

  return (
    <div>
      <h1>User Profile</h1>
      <p>Username: <b>{profile.data.username}</b> new username: <input value={username} onChange={evt => setUsername(evt.target.value)} type="text" name="name" /></p>
      <p>Password: <b>{profile.data.password}</b> new password: <input value={password} onChange={evt => setPassword(evt.target.value)} type="text" name="name" /></p>
      <p>birthDate: <b>{profile.data.birthDate}</b> new birthDate: <input value={birthDate} onChange={evt => setBirthDate(evt.target.value)} type="text" name="name" /></p>
      <p>firstName: <b>{profile.data.firstName}</b> new firstName: <input value={firstName} onChange={evt => setFirstName(evt.target.value)} type="text" name="name" /></p>
      <p>lastName: <b>{profile.data.lastName}</b> new lastName: <input value={lastName} onChange={evt => setLastName(evt.target.value)} type="text" name="name" /></p>
      <p>startWorkDate: <b>{profile.data.startWorkDate}</b> new startWorkDate: <input value={startWorkDate} onChange={evt => setStartWorkDate(evt.target.value)} type="text" name="name" /></p>
      <p>Email: <b>{profile.data.email}</b> new Email: <input value={email} onChange={evt => setEmail(evt.target.value)} type="text" name="name" /></p>
      <p>PhoneNumber: <b>{profile.data.phoneNumber}</b> new Phone number: <input value={phoneNumber} onChange={evt => setPhoneNumber(evt.target.value)} type="text" name="name" /></p>
      <p>Company Name: <b>{showCompanyName}</b> new Company name: <input value={companyName} onChange={evt => setCompanyName(evt.target.value)} type="text" name="name" /></p>
      <button onClick={changeProfile}>button</button>
    </div>
  )
}

export {
  ManagerProfile
}