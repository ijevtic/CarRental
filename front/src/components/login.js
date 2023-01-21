import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { profileInfo } from "../recoil/atom/loggedAtom";
import { useRecoilState } from 'recoil';
function Login() {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [profile, setProfile] = useRecoilState(profileInfo);
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();

        fetch(process.env.REACT_APP_USER_SERVICE_URL + '/login', {
          'method': 'POST',
          // 'mode': 'no-cors',
          headers: {
            'Content-Type': 'application/json',
            // 'Authorization': token,
          },
          body: JSON.stringify({
            "username": username,
            "password": password
          })
        }).then(res => res.json())
        .then(res => {
          console.log(res);
          if(res.statusCode != 200) {
            alert(res.message)
          }
          else {
            setProfile({'loggedIn':'true', 'jwt': 'Bearer '+res.data.token, 'role': res.data.role, 'data': null})
            navigate('/')
          }
        })
        .catch(error => console.error('Error:', error));
    }

    return (
        <div>
            <h1>Login</h1>
            <form onSubmit = {handleSubmit}>
                <label>
                    Username:
                    <input type="text" name="Email" required value={username} 
                    onChange={(e) => setUsername(e.target.value)}/>
                </label>
                <label>
                    Password:
                    <input type="password" name="password" required value={password}
                    onChange={(e) => setPassword(e.target.value)}/>
                </label>
                <input type="submit" value="Submit" />
            </form>
        </div>
    );
}

export default Login;