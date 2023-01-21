import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { profileInfo } from '../../recoil/atom/loggedAtom';
import { useRecoilState } from 'recoil';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';


function Reservations() {
  const [profile, setProfile] = useRecoilState(profileInfo);
  const [reservations, setReservations] = useState([]);

  const fetchReservations = () => {
    fetch(process.env.REACT_APP_RENT_SERVICE_URL + "/getUserReservations", {
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
      setReservations(res.data);
    })
    .catch(error => console.error('Error:', error))
  }

  useEffect(() => {
    
    fetchReservations();
    
  }, []);

  const cancelReservation = (index) => {
    // console.log(filteredOptions[index])
    fetch(process.env.REACT_APP_RENT_SERVICE_URL+'/removeReservation', {
      'method': 'POST',
      // 'mode': 'no-cors',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': profile.jwt,
      },
      body: JSON.stringify({
        'id': reservations[index].reservationId,
      })
    }).then(res => res.json())
    .then(res => {
      console.log(res);
      if(res.statusCode != 200) {
        alert(res.message)
      }
      else
        fetchReservations();
    })
  }

  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>Car model</TableCell>
            <TableCell align="right">Company</TableCell>
            <TableCell align="right">Location</TableCell>
            <TableCell align="right">Type</TableCell>
            <TableCell align="right">Pick-up time</TableCell>
            <TableCell align="right">Drop-off time</TableCell>
            <TableCell align="right">Price</TableCell>
            <TableCell align="right"></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {reservations.map((row, index) => (
            <TableRow
              key={index}
              sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
            >
              <TableCell component="th" scope="row">
                {row.modelName}
              </TableCell>
              <TableCell align="right">{row.companyName}</TableCell>
              <TableCell align="right">{row.city}</TableCell>
              <TableCell align="right">{row.typeName}</TableCell>
              <TableCell align="right">{row.startTime}</TableCell>
              <TableCell align="right">{row.endTime}</TableCell>
              <TableCell align="right">{profile.rank != null ? row.price*(1-profile.rank.discountAmount) : row.price}</TableCell>
              <TableCell align="right">{}<Button variant="outlined" onClick={() => cancelReservation(index)}>Cancel</Button></TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  )
}
export {
  Reservations
}