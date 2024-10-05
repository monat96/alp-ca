package com.kt.alpca.ping.service;

import com.kt.alpca.ping.repository.RequestTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestTimeService {
    private final RequestTimeRepository requestTimeRepository;
}
