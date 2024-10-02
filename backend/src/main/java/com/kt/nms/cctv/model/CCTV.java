package com.kt.nms.cctv.model;

import com.kt.nms.common.BaseTime;
import com.kt.nms.ping.model.Ping;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "cctv")
@Entity
@Getter
@Setter
@ToString(exclude = "pingList")
@EqualsAndHashCode(callSuper = true, exclude = "pingList")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CCTV extends BaseTime {
  @Id
  private String cctvId; // CCTV 관리번호

  @Column(nullable = false)
  private String ipAddress; // IP 주소

  private double longitude; // 경도

  private double latitude; // 위도

  private String locationName; // 설치위치명

  private String locationAddress; // 설치위치주소

  private String hlsAddress; // RTSP 주소

  @OneToMany(mappedBy = "cctv", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Ping> pingList;
}
