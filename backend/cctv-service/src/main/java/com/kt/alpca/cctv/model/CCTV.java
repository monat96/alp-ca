package com.kt.alpca.cctv.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cctv")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CCTV {
  @Id
  private String cctvId;

  @Column(nullable = false)
  private String ipAddress;

  private double longitude;

  private double latitude;

  private String locationName;

  private String locationAddress;

  private String hlsAddress;
}
