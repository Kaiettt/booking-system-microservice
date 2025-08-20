package com.booking.userservice.common;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserContext {
    private  String userId;
    private  String username;
}
