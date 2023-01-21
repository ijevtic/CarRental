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
import Select from '@mui/material/Select'
import MenuItem from "@mui/material/MenuItem";


function ManagerVehicles(props) {
  const [profile, setProfile] = useRecoilState(profileInfo);
  const [vehicles, setVehicles] = useState([]);
  const [models, setModels] = useState([]);
  const [model, setModel] = useState('');
  const [location, setLocation] = useState('');
  const [locations, setLocations] = useState([]);

  const fetchVehicles = () => {
    fetch(process.env.REACT_APP_RENT_SERVICE_URL + "/getVehicles", {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': profile.jwt,
      },
    }).then(res => res.json())
    .then(res => {
      console.log(res);
      // if(res.statusCode != 201) {
      //   alert(res.message)
      // }
      setVehicles(res.data);
    })
    .catch(error => console.error('Error:', error))
  }
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

  useEffect(() => {
    fetchLocations().
    then(res => {setLocations(locations => res);});
    fetchVehicles();
    props.fetchModels()
    .then(res => { 
      setModels(res) 
      console.log(res);
    });
  }, []);

  const addNewVehicle = () => {
    console.log(model, location);
    // console.log(filteredOptions[index])
    fetch(process.env.REACT_APP_RENT_SERVICE_URL+'/addVehicle', {
      'method': 'POST',
      // 'mode': 'no-cors',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': profile.jwt,
      },
      body: JSON.stringify({
        'modelId': model,
        'locationId': location,
      })
    }).then(res => res.json())
    .then(res => {
      console.log(res);
      if(res.statusCode != 201) {
        alert(res.message)
      }
      
        fetchVehicles();
    })
  }

  return (
    <div>
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell align="right">Model</TableCell>
            <TableCell align="right">Type</TableCell>
            <TableCell align="right">Price</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {vehicles.map((row, index) => (
            <TableRow
              key={index}
              sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
            >
              <TableCell align="right">{row.modelName}</TableCell>
              <TableCell align="right">{row.typeName}</TableCell>
              <TableCell align="right">{row.price}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
    <h2>Add new Vehicle</h2>
      <Select value={model.modelName} onChange={(evt) => setModel(evt.target.value)}>
        {models.map((model) => {
          return (
            <MenuItem key={model.id} value={model.id}>{model.modelName}</MenuItem>
          )
        })}
      </Select>
      <Select value={location.city} onChange={(evt) => setLocation(evt.target.value)}>
        {locations.map((location) => {
          return (
            <MenuItem key={location.id} value={location.id}>{location.city}</MenuItem>
          )
        })}
      </Select>
      <Button variant="contained" onClick={addNewVehicle}>Add new vehicle</Button>
    </div>
  )
}
export {
  ManagerVehicles
}