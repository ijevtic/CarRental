import './App.css';
import Login from './components/login.js'
import styled from 'styled-components';
import React from 'react';
import { BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Navbar from './components/navbar';
import Home from './components/home';
import {
  RecoilRoot,
} from 'recoil';
import Register from './components/register';

const Content = styled.div`
    // text-align: center;
    // color: palevioletred;
    width: 90%;
    margin: auto;
`;

function App() {
  return (
    <RecoilRoot>
    <Router>
      <div className="App">
          <Navbar />
          <Content>
              <Routes>
                  <Route exact path="/" element={<Home />} />
                  <Route exact path="/login" element={<Login />} />
                  <Route exact path="/register" element={<Register />} />
              </Routes>
          </Content>
      </div>
    </Router>
    </RecoilRoot>
  );
}

export default App;
