package org.example.mapper;

import org.example.domain.*;
import org.example.dto.AddModelDto;
import org.example.dto.ReviewDto;
import org.example.repository.ReservationRepository;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    private ReservationRepository reservationRepository;

    public ReviewMapper(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    public Review reviewDtoToReview(ReviewDto reviewDto, Long userId) {
        CarModel carModel = new CarModel();
        Review review = new Review();
        review.setReservation(reservationRepository.findReservationById(reviewDto.getReservationId()).orElse(null));
        review.setComment(reviewDto.getComment());
        review.setMark(reviewDto.getMark());
        return review;
    }
}