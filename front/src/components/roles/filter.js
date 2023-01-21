import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { profileInfo } from '../../recoil/atom/loggedAtom';
import { useRecoilState } from 'recoil';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import styled from 'styled-components';
import 'react-tabs/style/react-tabs.css';
import { UserProfile } from './userprofile';
import Select from '@mui/material/Select'
import MenuItem from "@mui/material/MenuItem";
import { styled as styled2} from '@mui/material/styles';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import TextField from '@mui/material/TextField';
import dayjs from 'dayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import Button from '@mui/material/Button';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

function createData(name, calories, fat, carbs, protein) {
  return { name, calories, fat, carbs, protein };
}

function Filter(props) {
  const [startTime, setStartTime] = React.useState(dayjs('2023-01-22T21:11:54'));
  const [filteredOptions, setFilteredOptions] = React.useState([]);
  const [endTime, setEndTime] = React.useState(dayjs('2023-01-24T21:11:54'));
  const [profile, setProfile] = useRecoilState(profileInfo);
  const [locationSelected, setLocationSelected] = useState(-1);
  const [companySelected, setCompanySelected] = useState(-1);
  const [locations, setLocations] = useState([{id:-1, city:"No city chosen"}]);
  const [companies, setCompanies] = useState([{id:-1, companyName:"No company chosen"}]);

  const navigate = useNavigate();
  const rows = [
    createData('Frozen yoghurt', 159, 6.0, 24, 4.0),
    createData('Ice cream sandwich', 237, 9.0, 37, 4.3),
    createData('Eclair', 262, 16.0, 24, 6.0),
    createData('Cupcake', 305, 3.7, 67, 4.3),
    createData('Gingerbread', 356, 16.0, 49, 3.9),
  ];
  

  const fetchLocations = () => {
    fetch(process.env.REACT_APP_RENT_SERVICE_URL + '/getLocations', {
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
          setLocations(locations => [{id:-1, city:"No city chosen"}, ...res.data]);
        }
        )
        .catch(error => console.error('Error:', error));
  }

  const fetchCompanies = () => {
    fetch(process.env.REACT_APP_RENT_SERVICE_URL + '/getCompanies', {
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
          setCompanies(companies => [{id:-1, companyName:"No company chosen"}, ...res.data]);
        }
        )
        .catch(error => console.error('Error:', error));
  }

  useEffect(() => {
    fetchLocations();
    fetchCompanies();
  }, []);

  const search = () => {
    if(startTime.unix() > endTime.unix()) {
      alert("Start time cannot be after endTime!")
      return;
    }
    const now = Date.now() / 1000;
    if(startTime.unix() < now || endTime.unix() < now) {
      alert("Times are in the past!")
      return
    }
    fetch(process.env.REACT_APP_RENT_SERVICE_URL+'/filterVehicles', {
      'method': 'POST',
      // 'mode': 'no-cors',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': profile.jwt,
      },
      body: JSON.stringify({
        "companyId": companySelected < 0 ? null : companySelected,
        "locationId": locationSelected < 0 ? null : locationSelected,
        'startTime': startTime.unix(),
        'endTime': endTime.unix(),
      })
    }).then(res => res.json())
    .then(res => {
      console.log(res);
      if(res.statusCode != 200) {
        alert(res.message)
      }
      setFilteredOptions(res.data);
    })
    .catch(error => console.error('Error:', error));

  }
  
  const handleLocationChange = (event) => {
    setLocationSelected(event.target.value);
  };
  const handleCompanyChange = (event) => {
    setCompanySelected(event.target.value);
  };
  const handleStartChange = (newValue) => {
    setStartTime(newValue);
  };
  const handleEndChange = (newValue) => {
    setEndTime(newValue);
  };

  const reserve = (index) => {
    console.log(filteredOptions[index])
    fetch(process.env.REACT_APP_RENT_SERVICE_URL+'/addReservation', {
      'method': 'POST',
      // 'mode': 'no-cors',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': profile.jwt,
      },
      body: JSON.stringify({
        'carModelId': filteredOptions[index].modelId,
        // 'startTime': startTime.unix(),
        // 'endTime': endTime.unix(),
        'startTime': startTime.unix(),
        'endTime': endTime.unix(),

        'locationId': locationSelected < 0 ? null : locationSelected,
      })
    }).then(res => res.json())
    .then(res => {
      console.log(res);
      if(res.statusCode != 200) {
        alert(res.message)
      }
      else
        alert("Reserved!")
    })
  }

  return (
    <div>
      <Select value={locationSelected} onChange={handleLocationChange}>
        {locations.map((location) => {
          return (
            <MenuItem key={location.id} value={location.id}>{location.city}</MenuItem>
          )
        })}

      </Select>
      <Select value={companySelected} onChange={handleCompanyChange}>
        {companies.map((company) => {
          return (
            <MenuItem key={company.id} value={company.id}>{company.companyName}</MenuItem>
          )
        })}

      </Select>
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <DateTimePicker
          label="Pickup time"
          value={startTime}
          onChange={handleStartChange}
          renderInput={(params) => <TextField {...params} />}
        />
        <DateTimePicker
          label="Drop-off time"
          value={endTime}
          onChange={handleStartChange}
          renderInput={(params) => <TextField {...params} />}
        />
        </LocalizationProvider>
        <Button variant="contained" onClick={search}>Search</Button>
    
        <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>Car model</TableCell>
            <TableCell align="right">Company</TableCell>
            <TableCell align="right">Location</TableCell>
            <TableCell align="right">Type</TableCell>
            <TableCell align="right">Price</TableCell>
            <TableCell align="right">Price with membership discount</TableCell>
            <TableCell align="right"></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {filteredOptions.map((row, index) => (
            <TableRow
              key={index}
              sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
            >
              <TableCell component="th" scope="row">
                {row.modelName}
              </TableCell>
              <TableCell align="right">{row.companyName}</TableCell>
              <TableCell align="right">{row.locationName}</TableCell>
              <TableCell align="right">{row.typeName}</TableCell>
              <TableCell align="right">{row.price}</TableCell>
              <TableCell align="right">{profile.rank != null ? row.price*(1-profile.rank.discountAmount) : row.price}</TableCell>
              <TableCell align="right">{}<Button variant="outlined" onClick={() => reserve(index)}>reserve</Button></TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
    
    </div>

    
  )
}

export{
  Filter
}