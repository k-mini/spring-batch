package io.springbatch.springbatchlecture.ch11.faulttolerant;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
//@Configuration
public class FaultTolerantConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private int chunkSize = 5;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(chunkSize)
                .reader(customItemReader())
                .processor(customItemProcessor())
                .writer(customItemWriter())
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skip(RuntimeException.class)
                // RuntimeException 과 IllegalArumentException 발생횟수 합쳐서 skipLimit 를 적용
                .skipLimit(2) // skipCount 는 reader , processor, writer 전체적용. 다르게 하고 싶으면 직접 생성 (builder말고)
                .retry(IllegalStateException.class)
                .retryLimit(2)
                .build();
    }

    @Bean
    public ItemReader<String> customItemReader() {
        return new ItemReader<String>() {
            int i = 0;
            @Override
            public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                i ++;
                if (i == 1) {
                    throw new IllegalArgumentException("this exception is skipped");
                } else if (i == 2) {
                    throw new RuntimeException("this runtime exception is skipped");
                }
                return i > 10 ? null : "item" + i;
            }
        };
    }

    @Bean
    public ItemProcessor<? super String, String> customItemProcessor() {
        return new ItemProcessor<String, String>() {
            @Override
            public String process(String item) throws Exception {
//                throw new IllegalStateException("this exception is retried");
                return item;
            }
        };
    }

    private ItemWriter<? super String> customItemWriter() {
        return items -> System.out.println("items = " + items);
    }

}
