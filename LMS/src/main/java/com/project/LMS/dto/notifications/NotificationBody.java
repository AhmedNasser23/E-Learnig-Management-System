package com.project.LMS.dto.notifications;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationBody {
    int userId;
    String message;
    String role;
}
