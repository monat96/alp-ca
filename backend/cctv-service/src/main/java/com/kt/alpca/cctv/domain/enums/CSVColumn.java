package com.kt.alpca.cctv.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CSVColumn {
  CCTV_ID("CCTV관리번호"),
  LONGITUDE("경도"),
  LATITUDE("위도"),
  LOCATION_NAME("설치위치명"),
  LOCATION_ADDRESS("설치위치주소"),
  HLS_ADDRESS("스트리밍 프로토콜(HTTP)주소");

  private final String column;
}