package com.kitHub.Facilities_info.dto.community;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCommentRequest {
    private String content;
    private Long postId;
}
