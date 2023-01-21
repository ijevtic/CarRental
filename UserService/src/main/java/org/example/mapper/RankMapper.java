package org.example.mapper;

import org.example.domain.Rank;
import org.example.dto.RankDto;
import org.springframework.stereotype.Component;

@Component
public class RankMapper {
    public RankDto rankToDto(Rank rank) {
        RankDto rankDto = new RankDto();
        rankDto.setRankName(rank.getRankName());
        rankDto.setDiscountAmount(rank.getDiscountAmount());
        rankDto.setMinDays(rank.getMinDays());
        rankDto.setMaxDays(rank.getMaxDays());
        return rankDto;
    }
}
