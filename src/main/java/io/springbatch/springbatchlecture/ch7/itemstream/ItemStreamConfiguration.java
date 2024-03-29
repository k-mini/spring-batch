package io.springbatch.springbatchlecture.ch7.itemstream;

import io.springbatch.springbatchlecture.ch7.reader_processor_writer.CustomItemProcessor;
import io.springbatch.springbatchlecture.ch7.reader_processor_writer.CustomItemReader;
import io.springbatch.springbatchlecture.ch7.reader_processor_writer.CustomItemWriter;
import io.springbatch.springbatchlecture.ch7.reader_processor_writer.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
//@Configuration
public class ItemStreamConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob() {
        return jobBuilderFactory.get("batchJob")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(5)
                .reader(itemReader())
                .writer(itemWriter())
                .build();
    }
    @Bean
    public ItemReader<? extends String> itemReader() {
        List<String> items = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            items.add(String.valueOf(i));
        }
        return new CustomItemStreamReader(items);
    }
    @Bean
    public ItemWriter<? super String> itemWriter() {
        return new CustomItemStreamWriter();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> step2 has executed");
//                    throw new RuntimeException("step2 was failed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
