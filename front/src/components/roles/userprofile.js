import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { profileInfo } from '../../recoil/atom/loggedAtom';
import { useRecoilState } from 'recoil';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';

function UserProfile(props) {
  const [profile, setProfile] = useRecoilState(profileInfo);
  const [rank, setRank] = useState('');
  const [username, setUsername] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [birthDate, setBirthDate] = useState('');
  const [passportNumber, setPassportNumber] = useState('');
  const [password, setPassword] = useState('');

  const navigate = useNavigate();

  useEffect(() => {
    if(profile.loggedIn == 'false') {
      navigate('/login');
      return;
    }
    fetch(process.env.REACT_APP_USER_SERVICE_URL + '/getRank/' + profile.data.id, {
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
      setRank(res.data.rankName);
    })
  }, []);

  const changeProfile = () => {
    fetch(process.env.REACT_APP_USER_SERVICE_URL + '/changeUser', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': profile.jwt,
      },
      body: JSON.stringify({
        "username": username != '' ? username : null,
        "email": email != '' ? email : null,
        "phoneNumber": phoneNumber != '' ? phoneNumber : null,
        "birthDate": birthDate != '' ? birthDate : null,
        "passportNumber": passportNumber != '' ? passportNumber : null,
        "firstName": firstName != '' ? firstName : null,
        "lastName": lastName != '' ? lastName : null,
        "password": password != '' ? password : null,
      })
    }).then(res => res.json())
    .then(res => {
      props.fetchUser();
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
      <p>passportNumber: <b>{profile.data.passportNumber}</b> new passportNumber: <input value={passportNumber} onChange={evt => setPassportNumber(evt.target.value)} type="text" name="name" /></p>
      <p>Email: <b>{profile.data.email}</b> new Email: <input value={email} onChange={evt => setEmail(evt.target.value)} type="text" name="name" /></p>
      <p>PhoneNumber: <b>{profile.data.phoneNumber}</b> new Phone number: <input value={phoneNumber} onChange={evt => setPhoneNumber(evt.target.value)} type="text" name="name" /></p>
      <p>totalRentDays: <b>{profile.data.totalRentDays}</b></p>
      <p>rank: <b>{rank}</b></p>
      <button onClick={changeProfile}>button</button>
    </div>
  )
}

export {
  UserProfile
}