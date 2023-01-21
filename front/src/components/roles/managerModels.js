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
import Select from '@mui/material/Select'
import MenuItem from "@mui/material/MenuItem";
import Button from '@mui/material/Button';

function ManagerModels(props) {
  const [profile, setProfile] = useRecoilState(profileInfo);
  const [models, setModels] = useState([]);
  const [newModelName, setNewModelName] = useState('');
  const [newModelType, setNewModelType] = useState('SUV');
  const types = ['SUV', 'Sedan', 'Hatchback'];
  const [price, setPrice] = useState(0);

  

  useEffect(() => {
    
    props.fetchModels()
    .then(res => { setModels(res) });
    
  }, []);

  const addNewModel = () => {
    console.log(newModelType);
    // console.log(filteredOptions[index])
    fetch(process.env.REACT_APP_RENT_SERVICE_URL+'/addModel', {
      'method': 'POST',
      // 'mode': 'no-cors',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': profile.jwt,
      },
      body: JSON.stringify({
        'modelName': newModelName,
        'carType': newModelType,
        'price': price
      })
    }).then(res => res.json())
    .then(res => {
      console.log(res);
      if(res.statusCode != 201) {
        alert(res.message)
      }
      else
      props.fetchModels()
      .then(res => { setModels(res) });
    })
  }

  return (
    <div>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} aria-label="simple table">
          <TableHead>
            <TableRow>
              <TableCell>Car model</TableCell>
              <TableCell align="right">Car type</TableCell>
              <TableCell align="right">Price</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {models.map((row, index) => (
              <TableRow
                key={index}
                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
              >
                <TableCell component="th" scope="row">
                  {row.modelName}
                </TableCell>
                <TableCell align="right">{row.carType}</TableCell>
                <TableCell align="right">{row.price}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <h2>Add new Model</h2>
      <p>Name <input onChange={(evt) => setNewModelName(evt.target.value)} type="text" value={newModelName}></input></p>
      <p>Price <input onChange={(evt) => setPrice(evt.target.value)} type="text" value={price}></input></p>
      <Select value={newModelType} onChange={(evt) => setNewModelType(evt.target.value)}>
        {types.map((type) => {
          return (
            <MenuItem key={type} value={type}>{type}</MenuItem>
          )
        })}
      </Select>
      <Button variant="contained" onClick={addNewModel}>Add new model</Button>

    </div>
  )
}
export{
  ManagerModels
}