package com.kt.alpca.cctv.service;

import com.kt.alpca.cctv.model.CCTV;
import com.kt.alpca.cctv.repository.CCTVRepository;
import lombok.RequiredArgsConstructor;
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

import static com.kt.alpca.cctv.enums.CSVColumn.CCTV_ID;
import static com.kt.alpca.cctv.enums.CSVColumn.LONGITUDE;
import static com.kt.alpca.cctv.enums.CSVColumn.LATITUDE;
import static com.kt.alpca.cctv.enums.CSVColumn.LOCATION_NAME;
import static com.kt.alpca.cctv.enums.CSVColumn.LOCATION_ADDRESS;
import static com.kt.alpca.cctv.enums.CSVColumn.HLS_ADDRESS;


@Service
@RequiredArgsConstructor
public class CCTVService {
    private final CCTVRepository cctvRepository;

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
}
