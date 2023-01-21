package org.example.mapper;

import org.example.domain.Location;
import org.example.dto.LocationDto;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
    public LocationDto locationToLocationDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setId(location.getId());
        locationDto.setCity(location.getCity());
        return locationDto;
    }

}
