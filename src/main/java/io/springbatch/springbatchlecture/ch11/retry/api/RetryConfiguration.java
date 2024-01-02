package io.springbatch.springbatchlecture.ch11.retry.api;

import io.springbatch.springbatchlecture.ch11.retry.RetryableException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;

import java.util.*;

@RequiredArgsConstructor
//@Configuration
public class RetryConfiguration {

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
                .skip(RetryableException.class)
                .skipLimit(2)
                .retry(RetryableException.class)
                .retryLimit(2)
//                .retryPolicy(retryPolicy())
                .build();
    }

    @Bean
    public ItemReader<String> customItemReader() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            items.add(String.valueOf(i));
        }
//        return new ListItemReader<>(new ArrayList<>(Arrays.asList("1","1")));
        return new ListItemReader<>(items);
    }

    @Bean
    public ItemProcessor<String, String> customItemProcessor() {
        return new RetryItemProcessor();
    }

    private ItemWriter<? super String> customItemWriter() {
        return items -> {
            items.forEach(item -> System.out.println("item = " + item));
            System.out.println();
        };
    }

    @Bean
    public RetryPolicy retryPolicy() {

        Map<Class<? extends Throwable>, Boolean> exceptionClass = new HashMap<>();
        exceptionClass.put(RetryableException.class, true);

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(2, exceptionClass);
        return simpleRetryPolicy;
    }
}





