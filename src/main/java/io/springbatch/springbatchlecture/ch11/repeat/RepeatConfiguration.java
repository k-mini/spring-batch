package io.springbatch.springbatchlecture.ch11.repeat;

import io.springbatch.springbatchlecture.ch10.classifier.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.batch.repeat.exception.SimpleLimitExceptionHandler;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
//@Configuration
public class RepeatConfiguration {

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
                .build();
    }

    @Bean
    public ItemProcessor<? super String, ? extends String> customItemProcessor() {
        return new ItemProcessor<String, String>() {
            RepeatTemplate repeatTemplate = new RepeatTemplate();
            int i = 0;
            @Override
            public String process(String item) throws Exception {

//                repeatTemplate.setCompletionPolicy(new SimpleCompletionPolicy(3));
//                repeatTemplate.setCompletionPolicy(new TimeoutTerminationPolicy(3000));

                CompositeCompletionPolicy completionPolicy = new CompositeCompletionPolicy();
                CompletionPolicy[] completionPolicies =
                        new CompletionPolicy[]{
//                                                new SimpleCompletionPolicy(3),
                                                new TimeoutTerminationPolicy(3000)};
                completionPolicy.setPolicies(completionPolicies);
//                repeatTemplate.setCompletionPolicy(completionPolicy); // or 조건

                repeatTemplate.setExceptionHandler(simpleLimitExceptionHandler());

                repeatTemplate.iterate(new RepeatCallback() {
                    @Override
                    public RepeatStatus doInIteration(RepeatContext context) throws Exception {
                        System.out.println("repeatTemplate is testing item = " + item);
//                        Thread.sleep(500);
                        i ++;
                        if (i % 10 == 0) {
                            throw new RuntimeException("Exception is occured i = " + i + " item = " + item);
                        } else {
                            return RepeatStatus.CONTINUABLE;
                        }
                    }
                });

                return item;
            }
        };
    }

    @Bean
    public ExceptionHandler simpleLimitExceptionHandler() {
        return new SimpleLimitExceptionHandler(3); // 예외 3번 발생까지 허용
    }

    @Bean
    public ItemReader<? extends String> customItemReader() {
        return new ItemReader<String>() {
            int i = 0;
            @Override
            public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                i++;
                return i > 3 ? null : "item" + i;
            }
        };
    }

    @Bean
    public ItemWriter<? super String> customItemWriter() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> items) throws Exception {
                System.out.println("items = " + items);
                System.out.println();
            }
        };
    }

}
