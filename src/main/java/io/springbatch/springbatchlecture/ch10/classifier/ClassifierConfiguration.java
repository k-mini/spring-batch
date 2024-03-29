package io.springbatch.springbatchlecture.ch10.classifier;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
//@Configuration
public class ClassifierConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private int chunkSize = 10;

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
                .<ProcessorInfo, ProcessorInfo>chunk(chunkSize)
                .reader(customItemReader())
                .processor(customItemProcessor())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public ItemProcessor<? super ProcessorInfo, ? extends ProcessorInfo> customItemProcessor() {
        ClassifierCompositeItemProcessor<ProcessorInfo, ProcessorInfo> processor =
                new ClassifierCompositeItemProcessor<>();

        ProcessorClassifier<ProcessorInfo, ItemProcessor<?, ? extends ProcessorInfo>> classifier =
                new ProcessorClassifier<>();

        Map<Integer, ItemProcessor<? super ProcessorInfo,? extends ProcessorInfo>> processorMap = new HashMap<>();
        processorMap.put(1, new CustomItemProcessor1());
        processorMap.put(2, new CustomItemProcessor2());
        processorMap.put(3, new CustomItemProcessor3());
        classifier.setProcessorMap(processorMap);

        processor.setClassifier(classifier);

        return processor;
    }

    @Bean
    public ItemReader<? extends ProcessorInfo> customItemReader() {
        return new ItemReader<ProcessorInfo>() {
            int i = 0;

            @Override
            public ProcessorInfo read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                i++;
                ProcessorInfo processorInfo = ProcessorInfo.builder()
                        .id(i)
                        .build();


                return i > 3 ? null : processorInfo;
            }
        };
    }

    @Bean
    public ItemWriter<? super ProcessorInfo> customItemWriter() {
        return new ItemWriter<ProcessorInfo>() {
            @Override
            public void write(List<? extends ProcessorInfo> items) throws Exception {
                System.out.println("items = " + items);
                System.out.println();
            }
        };
    }
}
