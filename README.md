# CCTV Out

CCTV Out은 등록 된 CCTV의 상태를 실시간으로 감시하여 문제 발생 시 담당자에게 자동알림을 발송하는 프로그램입니다.

# 서비스 시나리오

CCTV 네트워크 이상감지 프로젝트로 관리자가 관제하고싶은 CCTV IP를 등록을 하게되면 자동으로 ICMP와 Hls를 확인하여 회선의 문제인지 카메라 자체의 문제인지 자동판별 후 각각의 담당자에게 전달을 진행한다. 이후 

## 기능적 요구사항

	1.	담당자가 네트워크에 연결된 CCTV 카메라의 IP 주소를 등록/수정/삭제할 수 있다.
	2.	시스템이 일정 시간마다 각 IP에 대해 핑(ICMP) 테스트를 실행한다.
	3.	ICMP 핑 응답에 문제가 발생하면 회선 담당자에게 자동으로 알림을 보낸다.
	4.	HLS 스트리밍 상태를 주기적으로 점검하며, 문제가 발생하면 CCTV 카메라 업체에게 알림을 보낸다.
	5.	네트워크 또는 카메라 문제 해결 후 담당자에게 정상화 알림을 보낸다.

## 비기능적 요구사항

 트랜잭션
  - ICMP, HLS 핑 테스트 실패 시 알림 전송이 이루어져야 한다. (Sync 호출)
 장애격리
  - 네트워크 점검 및 알림 시스템은 365일 24시간 중단 없이 작동해야 한다. (Async, Eventual Consistency)
  - 핑 테스트가 실패한 경우에도 다른 카메라 장치에 대한 테스트는 계속되어야 한다. (Circuit breaker, fallback)
 성능
  - 모든 CCTV 장치에 대한 네트워크 상태와 알림 내역을 실시간으로 확인할 수 있어야 한다. (CQRS)
  - 네트워크 장애가 발생할 때마다 즉시 담당자에게 알림이 전달되어야 한다. (Event driven)


## 분석 및 설계

### 이벤트스토밍
- 각 도메인의 이벤트를 명확하게 정의하고, 헥사고날 아키텍처와의 연계를 설계에 반영하고 있는가?
- 네트워크 문제 발생 시 이벤트가 적절하게 트리거되는지 확인하였는가?
- 핑 테스트 및 알림 전송이 ACID 트랜잭션 단위로 안전하게 처리되고 있는가?
- 기능적 요구사항과 비기능적 요구사항을 모두 반영하였는가?
### 서브 도메인, 바운디드 컨텍스트 분리
- 네트워크 점검, CCTV 장치 관리, 알림 시스템을 적절하게 분리하여 설계하였는가?
- 마이크로 서비스 간의 관심사를 분리하고 각 도메인에 맞는 기술 스택을 선택하였는가?
- ACID 트랜잭션이 필요한 부분과 eventual consistency가 허용되는 부분을 구분하였는가?
### 컨텍스트 매핑 / 이벤트 드리븐 아키텍처
- 네트워크 장애 감지 서비스와 알림 서비스 간의 관계를 명확하게 구분하였는가?
- 이벤트 드리븐 방식으로 핑 테스트 결과에 따른 알림을 적절하게 처리하고 있는가?
- 네트워크 또는 장치 장애가 발생했을 때 다른 서비스에 영향 없이 독립적으로 작동할 수 있는가?

# 분석/설계

## Event Storming 결과
* MSAEz 로 모델링한 이벤트스토밍 결과:  [https://www.msaez.io/#/storming/nmsservice]


## 이벤트 도출
![image](https://github.com/monat96/alp-ca/blob/main/image/1_event.png)
- CCTV IP 등록 / 수정 / 삭제: 관리자가 CCTV 장치의 IP를 등록, 수정, 삭제하는 작업은 주요 이벤트입니다. 이는 시스템에서 CCTV의 상태를 추적하기 위해 필수적인 이벤트로 간주됩니다.
- CCTV 개별 테스트: 각 CCTV 장치에 대해 개별적인 테스트가 수행되며, 네트워크 상태와 연결 상태를 점검합니다.
- ICMP 확인: ICMP 핑 테스트는 CCTV가 정상적으로 네트워크에 연결되어 있는지 확인하는 이벤트입니다.
- HLS 확인: HLS 스트리밍 상태를 주기적으로 점검하며, 비정상적인 상태를 탐지합니다.
- (문제가 생긴 CCTV에 대해) 해당지역방문, 문제해결, 알람: CCTV 문제가 확인되면 담당자에게 알림을 보냅니다.
- (관리자) 결과 확인: 문제가 해결되었을 때, 시스템이 자동으로 확인 후 결과를 관리자에게 전달합니다.

## 부적격 이벤트 탈락
![image](https://github.com/monat96/alp-ca/blob/main/image/2_event_remove.png)
- CCTV 개별 테스트: UI 이벤트로 실제 업무적 의미가 부족하므로 제외되었습니다.
- 개별 ICMP, HLS 확인: 이 역시 UI 이벤트로 업무적 의미가 부족하여 제외되었습니다.

## 액터, 커맨드 부착하여 읽기 좋게
![image](https://github.com/monat96/alp-ca/blob/main/image/3_actor.png)
- 관리자: 시스템 전체를 관리하며, CCTV 네트워크의 상태를 모니터링하고 이상이 발생할 시 담당자에게 알림을 보냅니다.
- CCTV 담당자: CCTV 장치 관련 문제 발생 시, 해당 문제를 해결하고 시스템에 결과를 기록합니다.
- 네트워크(NW) 담당자: 네트워크 관련 문제 발생 시, 이를 해결하는 역할을 담당합니다.

## 어그리게잇으로 묶기
![image](https://github.com/monat96/alp-ca/blob/main/image/4_aggregate.png)

   - CCTV 등록 및 상태 체크: CCTV 등록, 수정, 삭제 이벤트는 하나의 어그리게잇으로 묶여 관리됩니다. 이는 트랜잭션 단위로 관리되어야 하며, 일관성을 유지하는 것이 중요합니다.
- 건강 상태 체크: ICMP 및 HLS 테스트와 관련된 이벤트는 네트워크 상태 확인과 문제 해결에 대한 중요한 트랜잭션을 포함합니다.
- 문제 해결 및 알림: 네트워크 또는 CCTV 문제 해결 후, 해당 알림을 보내는 이벤트가 묶여 있습니다.


## 바운디드 컨텍스트로 묶기

![image](https://github.com/monat96/alp-ca/blob/main/image/5_%E1%84%8C%E1%85%A5%E1%86%BC%E1%84%8E%E1%85%A2%E1%86%A8.png)

도메인 서열 분리 
       - CCTV 관리(CCTV Management): CCTV IP 등록, 수정, 삭제 및 상태 점검 등 핵심 기능으로, 네트워크 감지 시스템의 핵심 서비스입니다. 이 도메인의 안정성은 시스템 전체 가동에 매우 중요한 역할을 하며, 연간 가동률(SLA) 목표는 99.999%입니다. 배포 주기는 변경 사항 발생 시 1개월 1회 이하입니다.
	- 네트워크 상태 확인(Network Health Check): CCTV 네트워크 연결 상태(핑 테스트, HLS 체크)를 확인하는 핵심 도메인입니다. 시스템의 주요 부분으로 네트워크 장애가 발생할 시 빠르게 대응할 수 있어야 하며, 이 역시 99.999%의 가동률을 목표로 합니다. 배포 주기는 네트워크 모니터링과 관련한 기능에 따라 1개월 1회 이하로 유지됩니다.

## 완성된 모형

![image](https://github.com/monat96/alp-ca/blob/main/image/6.%20%E1%84%92%E1%85%AA%E1%84%89%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD.png)


# 구현진행

분석/설계 단계에서 만들어진 내용대로 구현을 진행된다.
  1. cctv-service: CCTV IP등록 및 삭제가 가능하다.
  2. health-check-service: 등록 된 CCTV IP를 15분에 한번씩 ICMP, HLS 점검을 진행 후 문제가있는경우 issue로 전달한다.
  3. issue-service: ICMP, HLS 문제상황 별 처리를 진행하며 결과를 notification으로 전달한다.
  4. notification-service: IP 별 상황을 기록한다.

각각 실행은 아래 코드로 실행이 가능하다.
1) 각각 실행 (각각 폴더안에서 실행)
```bash
    ./gradlew bootRun
```
2) 한번에 실행 (alp-ca 위치에서 실행)
```bash
    docker-compose up
```
## DDD적용  

ㅇㅇㅇㅇㅇ

## CQRS 

CQRS(Command Query Responsibility Segregation)구현을 위해 쓰기와 읽기를 구분하여 개발진행.
CSV파일을 읽고 CCTV데이터를 변환하여 이벤트를 발생시키는 로직 구현(쓰기)
![image](https://github.com/monat96/alp-ca/blob/main/image/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA%202024-10-10%20%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE%204.33.32.png)


## API게이트웨이

gateway 스프링부트 App을 추가 후 application.yaml내에 각 마이크로 서비스의 routes 를 추가하고 gateway 서버의 포트를 8080 으로 설정함

상용버전과 개발버전을 다르게 하기위하여 application.yaml에는 profiles의 active를 활용

application.yaml
```yaml
spring:
  application:
    name: gateway
  profiles:
    active: dev
```

각각 서비스별로 매칭을 시키는 작업을 진행한다.

application-dev.yaml
```yaml
 spring:
  cloud:
    gateway:
      routes:
        - id: cctv-service
          uri: http://localhost:8081
          predicates:
            - Path=/cctvs/**
        - id: health-check-service
          uri: http://localhost:8083
          predicates:
            - Path=/health-checks/**
        - id: issue-service
          uri: http://localhost:8084
          predicates:
            - Path=/issues/**
        - id: notification-service
          uri: http://localhost:8085
          predicates:
            - Path=/notifications/**
      globalcors:
        cors-configurations:
            '[/**]':
                allowed-origin-patterns:
                  -  "*"
                allowedMethods:
                    - GET
                    - POST
                    - PUT
                    - DELETE
                allowedHeaders:
                    - "*"
                allowCredentials: false
                maxAge: 3600
      default-filters:
        - DedupeResponseHeader=Access-Control-Allow-Origin Access-Control-Allow-Credentials
```



## RestAPI 적용 결과
RestAPI는 크게 4가지의 서비스의 소통을 통해 진행이된다. --> API를 통해 전달되는 과정을 확인 한 후 붙여넣는다.


1. 엑셀 파일을 활용한 데이터 등록
2. 등록 된 데이터 삭제 기능
3. ICMP, RLS 검사진행 / 문제가 있을 시 담당자에게 IP 전달(자동)

- 상태는 ICMP 4개, HLS 3개로 총 7개의 상태가 존재한다. 상태는 다음과같다.
```java
  public enum ICMPStatus {
      SUCCESS,
      LOSS,
      TIMEOUT,
      FAIL
  }

  public enum HLSStatus {
      SUCCESS,
      FAIL,
      ERROR
  }
```

4. ICMP, RLS 문제 발생 시 해결진행 (단계 별 API 전달)
5. 에러가 발생한 CCTV의 상태 현 상태 관리

## 서킷브레이킹

## 부하테스트



# 운영

## 무정지 재배포

## 오토스케일 아웃

## CI/CD설정
