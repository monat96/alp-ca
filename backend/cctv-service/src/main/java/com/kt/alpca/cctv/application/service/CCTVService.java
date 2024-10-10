package com.kt.alpca.cctv.application.service;

import com.kt.alpca.cctv.common.KafkaBindingNames;
import com.kt.alpca.cctv.domain.event.CCTVRegisteredEvent;
import com.kt.alpca.cctv.domain.model.CCTV;
import com.kt.alpca.cctv.infra.repository.CCTVRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import static com.kt.alpca.cctv.domain.enums.CSVColumn.CCTV_ID;
import static com.kt.alpca.cctv.domain.enums.CSVColumn.LONGITUDE;
import static com.kt.alpca.cctv.domain.enums.CSVColumn.LATITUDE;
import static com.kt.alpca.cctv.domain.enums.CSVColumn.LOCATION_NAME;
import static com.kt.alpca.cctv.domain.enums.CSVColumn.LOCATION_ADDRESS;
import static com.kt.alpca.cctv.domain.enums.CSVColumn.HLS_ADDRESS;


@Service
@RequiredArgsConstructor
public class CCTVService {
    private final CCTVRepository cctvRepository;
    private final StreamBridge streamBridge;

    public void upload(MultipartFile file) throws IOException {
        try (
                BufferedReader fileReader = new BufferedReader(
                        new InputStreamReader(file.getInputStream(), "MS949")
                );
                CSVParser csvParser = new CSVParser(
                        fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim()
                );
        ) {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            List<CCTV> cctvs = StreamSupport
                    .stream(csvRecords.spliterator(), false)
                    .map(this::convertToCctv)
                    .toList();

            cctvRepository.saveAll(cctvs);
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 1) // 15분마다
    public void checkCCTV() {
        cctvRepository.findAll().stream().map(this::convertToCctvRegisteredEvent)
                .forEach(event -> streamBridge.send(KafkaBindingNames.CCTV_EVENT_OUT, event));
    }

    private CCTV convertToCctv(CSVRecord csvRecord) {
        return CCTV.builder()
                .cctvId(csvRecord.get(CCTV_ID.getColumn()))
                .ipAddress("8.8.8.8")
                .longitude(Double.parseDouble(csvRecord.get(LONGITUDE.getColumn())))
                .latitude(Double.parseDouble(csvRecord.get(LATITUDE.getColumn())))
                .locationName(csvRecord.get(LOCATION_NAME.getColumn()))
                .locationAddress(csvRecord.get(LOCATION_ADDRESS.getColumn()))
                .hlsAddress(csvRecord.get(HLS_ADDRESS.getColumn()))
                .build();
    }

    private CCTVRegisteredEvent convertToCctvRegisteredEvent(CCTV cctv) {
        return CCTVRegisteredEvent.builder()
                .cctvId(cctv.getCctvId())
                .ipAddress(cctv.getIpAddress())
                .hlsAddress(cctv.getHlsAddress())
                .build();
    }
}
