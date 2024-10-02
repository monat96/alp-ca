package com.kt.nms.cctv.service;

import com.kt.nms.cctv.dto.CCTVDto;
import com.kt.nms.cctv.dto.CreateRequest;
import com.kt.nms.cctv.model.CCTV;
import com.kt.nms.cctv.repository.CCTVRepository;
import com.kt.nms.ping.model.Ping;
import com.kt.nms.ping.repository.PingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.scope.StepScope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.kt.nms.cctv.enums.CSVColumn.CCTV_ID;
import static com.kt.nms.cctv.enums.CSVColumn.LONGITUDE;
import static com.kt.nms.cctv.enums.CSVColumn.LATITUDE;
import static com.kt.nms.cctv.enums.CSVColumn.LOCATION_NAME;
import static com.kt.nms.cctv.enums.CSVColumn.LOCATION_ADDRESS;
import static com.kt.nms.cctv.enums.CSVColumn.HLS_ADDRESS;


@Slf4j
@Service
@RequiredArgsConstructor
public class CCTVService {
    private final CCTVRepository cctvRepository;
    private final PingRepository pingRepository;

    public CCTV create(CreateRequest request) {
        var cctv = this.convertToCctv(request);
        return cctvRepository.save(cctv);
    }

    public List<CCTVDto> list() {
        var cctvList = cctvRepository.findAll();

        if (cctvList.isEmpty()) {
            return List.of();
        }

        return cctvList.stream().map(cctv -> {
            Ping latestPing = pingRepository.findTopByCctvOrderByCreatedAtDesc(cctv)
                    .orElse(null);
            return this.convertToCctvDto(cctv, latestPing);
        }).toList();
    }

    public CCTVDto detail(String id) {
        var cctv = cctvRepository
                .findByCctvId(id)
                .orElseThrow();
        Ping latestPing = pingRepository.findTopByCctvOrderByCreatedAtDesc(cctv)
                .orElse(null);

        return this.convertToCctvDto(cctv, latestPing);
    }

    public void update() {
    }

    public void delete(String id) {
        cctvRepository
                .findByCctvId(id)
                .ifPresent(cctvRepository::delete);
    }

    public List<CCTV> uploadCSV(MultipartFile file) throws IOException {
        List<CCTV> cctvList = new ArrayList<>();

        try (
                BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "MS949"));
                CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                var cctv = this.convertToCctv(csvRecord);
                cctvList.add(cctv);
            }
            cctvRepository.saveAll(cctvList);
        }

        return cctvList;
    }

    private CCTV convertToCctv(CreateRequest request) {
        return CCTV.builder()
                .cctvId(request.getCctvId())
                .ipAddress(request.getIpAddress())
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .locationName(request.getLocationName())
                .locationAddress(request.getLocationAddress())
                .hlsAddress(request.getHlsAddress())
                .build();
    }

    private CCTV convertToCctv(CSVRecord csvRecord) {
        return CCTV.builder()
                .cctvId(csvRecord.get(CCTV_ID.getColumn()))
                .ipAddress("8.8.8.8") // FIXME: IP 주소는 어떻게 처리할 것인가?
                .longitude(Double.parseDouble(csvRecord.get(LONGITUDE.getColumn())))
                .latitude(Double.parseDouble(csvRecord.get(LATITUDE.getColumn())))
                .locationName(csvRecord.get(LOCATION_NAME.getColumn()))
                .locationAddress(csvRecord.get(LOCATION_ADDRESS.getColumn()))
                .hlsAddress(csvRecord.get(HLS_ADDRESS.getColumn()))
                .build();
        }

    private CCTVDto convertToCctvDto(CCTV cctv, Ping ping) {
        CCTVDto dto =CCTVDto.builder()
                .cctvId(cctv.getCctvId())
                .ipAddress(cctv.getIpAddress())
                .longitude(cctv.getLongitude())
                .latitude(cctv.getLatitude())
                .locationName(cctv.getLocationName())
                .locationAddress(cctv.getLocationAddress())
                .hlsAddress(cctv.getHlsAddress())
                .build();

        if (ping != null) {
            dto.setStatus(ping.getStatus());
        }
        return dto;
    }
}
