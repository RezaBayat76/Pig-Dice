package me.rezabayat.pigdice.dto;

import lombok.Data;

@Data
public class CommentOnUserDTO {

    private Long score;

    private String text;

    private Long mentionUserID;
}
