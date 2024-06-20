package com.example.minionlinemarket.Model.Dto.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.example.minionlinemarket.Model.*;

@Data
@Getter
@Setter
public class ReviewAdminDto {
    private Long id;
    private String buyer;
    private String content;
    private int rating;

    public ReviewAdminDto(Review review) {
        this.id = review.getId();
        this.buyer = review.getBuyer().getUsername();
        this.content = review.getContent();
        this.rating = review.getRating();
    }
}
