import React from 'react';
import { Link } from 'react-router-dom'
import styled from 'styled-components';

const Title = styled.title`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    font-size: 1.5em;
    color: palevioletred;
    `;

const StyledNavbar = styled.nav`
    display: flex;
    justify-content: space-between;
    margin: auto;
    text-align: center;
    color: palevioletred;
    `;

function Navbar() {
    return (
        <Title>
            <h1>Car Rental</h1>
            <StyledNavbar>
                <Link to="/">Home</Link>
                <Link to="/register">Register</Link>
                <Link to="/login">Login</Link>
            </StyledNavbar>
        </Title>
    );
}

export default Navbar;