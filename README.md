# CCTV Out
![image](https://github.com/monat96/alp-ca/blob/main/image/cctv.jfif)


CCTV Out은 등록 된 CCTV의 상태를 실시간으로 감시하여 문제 발생 시 담당자에게 자동알림을 발송하는 프로그램입니다.

# 서비스 시나리오

CCTV 네트워크 이상감지 프로젝트로 관리자가 관제하고싶은 CCTV IP를 등록을 하게되면 자동으로 ICMP와 Hls를 확인하여 회선의 문제인지 카메라 자체의 문제인지 자동판별 후 각각의 담당자에게 전달을 진행합니다.  
이후 담당자가 해당 이슈상황을 처리하게되면 알림을 자동으로 보내며 서비스가 끝나게됩니다.


## 기능적 요구사항

	1.	담당자가 네트워크에 연결된 CCTV 카메라의 IP 주소를 등록/수정/삭제할 수 있다.
	2.	시스템이 일정 시간마다 각 IP에 대해 핑(ICMP) 테스트를 실행한다.
	3.	ICMP 핑 응답에 문제가 발생하면 회선 담당자에게 자동으로 알림을 보낸다.
	4.	HLS 스트리밍 상태를 주기적으로 점검하며, 문제가 발생하면 CCTV 카메라 업체에게 알림을 보낸다.
	5.	네트워크 또는 카메라 문제 해결 후 담당자에게 정상화 알림을 보낸다.

## 비기능적 요구사항

 트랜잭션
  - ICMP, HLS 핑 테스트 실패 시 알림 전송이 이루어져야 한다.
 장애격리
  - 네트워크 점검 및 알림 시스템은 365일 24시간 중단 없이 작동해야 한다.
  - 핑 테스트가 실패한 경우에도 다른 카메라 장치에 대한 테스트는 계속되어야 한다.
 성능
  - 모든 CCTV 장치에 대한 네트워크 상태와 알림 내역을 실시간으로 확인할 수 있어야 한다.
  - 네트워크 장애가 발생할 때마다 즉시 담당자에게 알림이 전달되어야 한다. 


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

## AS-IS

![image](https://github.com/monat96/alp-ca/blob/main/image/ASIS.png)

기존에는 CCTV에 문제가 발생하면 관제사가 해당 CCTV를 확인한 후, KT 담당자에게 요청을 합니다.  
이후 KT 담당자가 현장에 출동해 문제의 원인이 KT 회선인지, CCTV 기기 자체에 있는지 파악합니다.  
만약 CCTV 기기 문제로 확인되면, 해당 기기의 담당자를 불러 문제를 해결하는 방식으로 작업이 마무리됩니다.  

## TO-BE
![image](https://github.com/monat96/alp-ca/blob/main/image/TOBE.png)

TO-BE 시나리오에서는, 관제사가 CCTV IP를 등록하면 시스템이 자동으로 15분 간격으로 ICMP와 HLS 상태를 점검하게 됩니다.  
문제가 발생할 경우, ICMP 문제는 KT 담당자에게, HLS 문제는 CCTV 담당자에게 자동으로 알림이 전송됩니다.  
문제가 해결되면 관제사에게 알림을 보내 문제 해결을 인식할 수 있도록 합니다.  


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

![image](https://github.com/monat96/alp-ca/blob/main/image/event_%E1%84%8B%E1%85%AA%E1%86%AB%E1%84%89%E1%85%A5%E1%86%BC.png)


# 구현 진행

이 프로젝트는 분석/설계 단계에서 정의된 내용에 따라 구현됩니다.

1. **cctv-service**: CCTV IP를 등록 및 삭제할 수 있습니다.
2. **health-check-service**: 등록된 CCTV IP에 대해 15분마다 ICMP 및 HLS 점검을 진행하며, 문제가 발생하면 issue-service에 문제를 전달합니다.
3. **issue-service**: ICMP 및 HLS 문제 상황에 따라 문제를 처리하고, 결과를 notification-service에 전달합니다.
4. **notification-service**: 각 IP의 상태와 이력을 기록합니다.

### 실행 방법

아래의 명령어로 각각의 서비스를 실행할 수 있습니다.

1) 각 서비스 개별 실행 (각 서비스 폴더에서 실행):
```bash
  ./gradlew bootRun
```
2) 한번에 실행 (alp-ca 위치에서 실행)
```bash
    docker-compose up
```

## CQRS 

이 프로젝트는 CQRS 패턴을 적용하여 쓰기 작업과 읽기 작업을 분리하여 구현되었습니다. CSV 파일을 읽어 CCTV 데이터를 변환하고 이벤트를 발생시키는 쓰기 로직이 구현되어 있습니다.

```java
// CSV파일을 읽어서 CCTV데이터 변환 및 이벤트 발생
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

    @Scheduled(fixedRate = 1000 * 60 * 10) // 15분마다
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
```


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
RestAPI는 크게 4가지의 서비스의 소통을 통해 진행이 됩니다.


1. 엑셀 파일을 활용한 CCTV 데이터 등록
![image](https://github.com/monat96/alp-ca/blob/main/image/API_cctv.png)

관제사가 CCTV 데이터를 대량으로 관리할 수 있도록, 엑셀 파일을 이용해 CCTV IP 주소 및 기타 관련 정보를 시스템에 일괄 등록할 수 있습니다.  
이 기능을 통해 관리자는 효율적으로 CCTV 장치 데이터를 업데이트하고, 실시간 모니터링을 위한 데이터를 준비할 수 있습니다.


2. 등록된 CCTV 데이터 삭제 기능
![image](https://github.com/monat96/alp-ca/blob/main/image/API_Delete.png)

더 이상 사용하지 않는 CCTV 장치 또는 불필요한 장치 데이터를 시스템에서 삭제할 수 있는 기능을 제공합니다.  
이로 인해 삭제된 CCTV 장치는 이후 검사 및 모니터링 대상에서 제외되어, 불필요한 리소스 소모를 방지할 수 있습니다.  
관제사는 불필요한 데이터를 주기적으로 정리함으로써 시스템의 효율성을 높일 수 있습니다.


3. ICMP 및 HLS 검사 진행 및 문제 발생 시 담당자에게 IP 전달
![image](https://github.com/monat96/alp-ca/blob/main/image/API_health.png)

시스템은 CCTV 네트워크 상태를 주기적으로 확인하며, ICMP(인터넷 제어 메시지 프로토콜)와 HLS(HTTP Live Streaming) 상태를 검사합니다.  
검사 결과는 각각 다음과 같은 7가지 상태로 구분됩니다:

ICMP 상태: SUCCESS, LOSS, TIMEOUT, FAIL (4개 상태)
HLS 상태: SUCCESS, FAIL, ERROR (3개 상태)
각 상태에 따라 네트워크 이상 여부를 판단하며, 문제가 발생하면 담당자에게 알려줍니다.  
이를 통해 담당자는 신속하게 문제를 파악하고 해결할 수 있습니다.

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
4. ICMP, HLS 각각의 이슈 발생 시 해당 데이터를 적재하고 이후 이슈해결을 진행
![image](https://github.com/monat96/alp-ca/blob/main/image/API_issue.png)

ICMP 또는 HLS 검사 중 하나에서 이슈가 발생하면, 해당 CCTV 장치의 관련 데이터를 시스템에 자동으로 적재합니다.  
적재된 데이터는 네트워크 장애 및 CCTV 장치 문제에 대한 분석 자료로 활용됩니다.

5. 에러가 발생한 CCTV의 상태 현 상태 관리
![image](https://github.com/monat96/alp-ca/blob/main/image/API_noti.png)

에러가 발생한 CCTV 장치에 대한 상태를 시스템에서 지속적으로 관리합니다.  
각 CCTV 장치의 현재 상태는 실시간으로 업데이트되며, 문제가 해결될 때까지 시스템에서 해당 장치를 주의 상태로 표시합니다.

## 재시도 매커니즘
```yaml
  cloud:
    function:
      definition: healthCheckEvent
    stream:
      kafka:
        binder:
          brokers: localhost:9092
      bindings:
        health-check-event-in:
          destination: health-check-event-in
          contentType: application/json
          group: health-check-service
        health-check-event-out:
          destination: issueEvent-in-0
          contentType: application/json
          producer:
            retry:
              maxAttempts: 5
              backOffInitialInterval: 1000
              backOffMaxInterval: 5000
```
health-check-event-out 프로듀서는 issueEvent-in-0이라는 대상(destination)으로 Kafka 메시지를 전송합니다.  
만약 메시지 전송 중 네트워크 장애나 Kafka 브로커의 일시적인 오류로 인해 메시지 전송이 실패할 경우, 재시도 매커니즘이 작동하여 메시지를 다시 전송하도록 합니다.

주요 설정:

- maxAttempts: 5: 메시지 전송 실패 시 최대 5번까지 재시도합니다. 첫 번째 시도가 실패할 경우 5회까지 추가 시도를 허용하며, 재시도 횟수가 5번을 넘으면 재시도는 중단됩니다.
- backOffInitialInterval: 1000: 첫 번째 재시도는 실패 후 1초(1000ms) 뒤에 이루어집니다. 즉, 메시지 전송이 실패할 경우, 1초 후에 첫 번째 재시도를 시도합니다.
- backOffMaxInterval: 5000: 재시도 간 간격이 점차 늘어나며, **최대 5초(5000ms)**까지 증가합니다. 초기 재시도 간격은 1초이지만, 각 재시도마다 간격이 점차 증가하여 최대 5초까지 대기 후 재시도가 이루어집니다.



# 배포

배포는 다음과 같은 순서로 진행을합니다.

[Docker]
1. Docker 파일 생성
2. Gradle 빌드
3. Docker 이미지 생성
4. Docker 컨테이너 실행
5. Docker 이미지 배포

[k8s]
1. Azure 로그인
2. AKS 클러스터 연결
3. k8s 배포 설정 파일 작성
4. k8s 애플리케이션 배포
5. 애플리케이션 상태확인


##  Docker 

1. Docker 파일 생성 (Dockerfile)
```docker
FROM gradle:jdk21-alpine AS build
WORKDIR /project

COPY gradlew /project/
COPY gradle /project/gradle
COPY build.gradle settings.gradle /project/

RUN chmod +x gradlew && ./gradlew build --no-daemon -x test || return 0

COPY src /project/src

RUN ./gradlew clean build --no-daemon

FROM openjdk:21-jdk-slim

WORKDIR /project

COPY --from=build /project/build/libs/*.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "/project/app.jar"]
```

2. Gradle 빌드


```bash
./gradlew build
```

3. Docker 이미지 생성
```bash
docker build -t cctv-service .
```

4. Docker 컨테이너 실행
```bash
docker run -p 8081:8081 cctv-service
```
5. Docker 이미지 배포

```bash
docker login
docker tag cctv-service {dockerhub-name}/cctv-service
docker push {dockerhub-name}/cctv-service
```

![image](https://github.com/monat96/alp-ca/blob/main/image/docker.png)

Docker hub에 배포된 것 확인이 가능하다.  
마찬가지로 나머지 서비스들도 배포를 진행할 수 있다.

## K8S 배포

1. Azure 로그인

```bash
az login --use-device-code
```
2. AKS 클러스터 연결
Azure에서 리소스그룹과 k8s 서비스를 만든 후 진행한다. 

```bash
az aks get-credentials --resource-group userXX-rsrcgrp --name userXX-aks
```

3. k8s 배포 설정 파일 작성

Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: cctv-service
  labels:
    app: cctv-service
spec:
  replicas: 1
  selector:
    matchLabels:
      app: cctv-service
  template:
    metadata:
      labels:
        app: cctv-service
    spec:
      containers:
        - name: cctv-service
          image: "monat96/alp-ca-cctv-service:latest"
          ports:
            - containerPort: 8080
          resources:
            requests:
              cpu: 200m
```

Service

```yaml
apiVersion: v1
kind: Service
metadata:
  name: cctv-service
  labels:
    app: cctv-service
spec:
  selector:
    app: cctv-service
  ports:
    - port: 8080
      targetPort: 8080
```
4. k8s 애플리케이션 배포

```bash
#!/bin/bash

services=(
  "gateway"
  "cctv-service"
  "health-check-service"
  "issue-service"
  "notification-service"
)

for service in "${services[@]}"; do
  kubectl apply -f "./backend/${service}/kubernetes/deployment.yaml"
  kubectl apply -f "./backend/${service}/kubernetes/service.yaml"
done

```

5. 애플리케이션 상태확인

```bash
kubectl get pods
kubectl get service
```

