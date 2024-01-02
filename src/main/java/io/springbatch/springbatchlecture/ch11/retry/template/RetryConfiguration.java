package io.springbatch.springbatchlecture.ch11.retry.template;

import io.springbatch.springbatchlecture.ch11.retry.RetryableException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .<String, Customer>chunk(chunkSize)
//                .reader(new ItemReader<String>() {
//                    int cnt = 0;
//                    @Override
//                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
//                        cnt++;
//                        if (cnt == 1) {
//                            throw new RetryableException("read failed");
//                        }
//                        return cnt <= 30 ? String.valueOf(cnt) : null;
//                    }
//                })
                .reader(customItemReader())
                .processor(customItemProcessor())
                .writer(customItemWriter())
                .faultTolerant()
//                .skip(RetryableException.class)
//                .skipLimit(2)
//                .skipPolicy(new AlwaysSkipItemSkipPolicy())
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
    public ItemProcessor<String, Customer> customItemProcessor() {
        return new RetryItemProcessor2();
    }

    private ItemWriter<? super Customer> customItemWriter() {
        return items -> {
//            items.forEach(item -> System.out.println("item = " + item));
            System.out.println("items = " + items);
//            for (Customer item : items) {
//                if (item.getItem().equals("0")) {
//                    System.out.println("0 존재");
//                    throw new RetryableException("0 포함되면 예외 발생;");
//                }
//            }
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

    @Bean
    public RetryTemplate retryTemplate() {
        Map<Class<? extends Throwable>, Boolean> exceptionClass = new HashMap<>();
        exceptionClass.put(RetryableException.class, true);

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(2000);

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(2, exceptionClass);

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(simpleRetryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }

}





