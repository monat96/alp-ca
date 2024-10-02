package com.kt.nms.requestTime.service;

import com.kt.nms.requestTime.model.RequestTime;
import com.kt.nms.requestTime.repository.RequestTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RequestTimeService {
    private final RequestTimeRepository requestTimeRepository;

    public void saveRequestTime() {
        requestTimeRepository.save(RequestTime.builder().requestTime(LocalDateTime.now()).build());
    }
}
