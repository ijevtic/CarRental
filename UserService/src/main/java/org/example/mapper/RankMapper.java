package org.example.mapper;

import org.example.domain.UserRank;
import org.example.dto.RankDto;
import org.springframework.stereotype.Component;

@Component
public class RankMapper {
    public RankDto rankToDto(UserRank userRank) {
        RankDto rankDto = new RankDto();
        rankDto.setRankName(userRank.getRankName());
        rankDto.setDiscountAmount(userRank.getDiscountAmount());
        rankDto.setMinDays(userRank.getMinDays());
        rankDto.setMaxDays(userRank.getMaxDays());
        return rankDto;
    }
}
