//package com.kt.nms.job;
//
//import com.kt.nms.cctv.model.CCTV;
//import com.kt.nms.cctv.repository.CCTVRepository;
//import com.kt.nms.ping.model.Ping;
//import com.kt.nms.ping.service.PingService;
//import com.kt.nms.requestTime.model.RequestTime;
//import com.kt.nms.requestTime.repository.RequestTimeRepository;
//import com.kt.nms.util.PingCommand;
//import com.kt.nms.util.PingCommandResult;
//import lombok.AllArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.data.RepositoryItemReader;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.Sort;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//
//@Configuration
//@AllArgsConstructor
//public class PingJobConfig {
//    private final JobRepository jobRepository;
//    private final PlatformTransactionManager transactionManager;
//    private final CCTVRepository cctvRepository;
//    private final PingService pingService;
//    private final RequestTimeRepository requestTimeRepository;
//
//    @Bean
//    public Job pingJob() {
//        return new JobBuilder("pingJob", jobRepository)
//                .start(pingStep())
//                .build();
//    }
//
//    @Bean
//    public Step pingStep() {
//        RequestTime requestTime = RequestTime.builder().requestTime(LocalDateTime.now()).build();
//        requestTimeRepository.save(requestTime);
//        return new StepBuilder("pingStep", jobRepository)
//                .<CCTV, Ping>chunk(10, transactionManager)
//                .reader(cctvReader())
//                .processor(pingProcessor(requestTime))
//                .writer(pingWriter())
//                .build();
//    }
//
//    @Bean
//    public ItemReader<CCTV> cctvReader() {
//        RepositoryItemReader<CCTV> reader = new RepositoryItemReader<>();
//        reader.setRepository(cctvRepository);
//        reader.setMethodName("findAll");
//        reader.setSort(Collections.singletonMap("cctvId", Sort.Direction.ASC));
//        return reader;
//    }
//
//    @Bean
//    public ItemProcessor<CCTV, Ping> pingProcessor(RequestTime requestTime) {
//        return cctv -> {
//            PingCommandResult result = PingCommand.exec(cctv.getIpAddress());
//
//            return Ping.builder().cctv(cctv)
//                    .rttAvg(result.getRtt().getAvg())
//                    .rttMax(result.getRtt().getMax())
//                    .rttMin(result.getRtt().getMin())
//                    .packetLossRate(result.getPacketLossRate())
//                    .status(result.getStatus())
//                    .requestTime(requestTime)
//                    .build();
//        };
//    }
//
//    @Bean
//    public ItemWriter<Ping> pingWriter() {
//        return chunk -> pingService.saveAll(chunk.getItems());
//    }
//}
//
