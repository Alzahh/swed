package com.example.swed.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminPageResponse {
    private List<UserDataResponse> users;
}
