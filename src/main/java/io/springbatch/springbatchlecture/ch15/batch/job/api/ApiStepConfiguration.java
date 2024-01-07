package io.springbatch.springbatchlecture.ch15.batch.job.api;

import io.springbatch.springbatchlecture.ch15.batch.chunk.processor.ApiItemProcessor1;
import io.springbatch.springbatchlecture.ch15.batch.chunk.processor.ApiItemProcessor2;
import io.springbatch.springbatchlecture.ch15.batch.chunk.processor.ApiItemProcessor3;
import io.springbatch.springbatchlecture.ch15.batch.chunk.writer.ApiItemWriter1;
import io.springbatch.springbatchlecture.ch15.batch.chunk.writer.ApiItemWriter2;
import io.springbatch.springbatchlecture.ch15.batch.chunk.writer.ApiItemWriter3;
import io.springbatch.springbatchlecture.ch15.batch.classifier.ProcessorClassifier;
import io.springbatch.springbatchlecture.ch15.batch.classifier.WriterClassifier;
import io.springbatch.springbatchlecture.ch15.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.ch15.batch.domain.ProductVO;
import io.springbatch.springbatchlecture.ch15.batch.partition.ProductPartitioner;
import io.springbatch.springbatchlecture.ch15.service.ApiService1;
import io.springbatch.springbatchlecture.ch15.service.ApiService2;
import io.springbatch.springbatchlecture.ch15.service.ApiService3;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class ApiStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private int chunkSize = 10;
    private final ApiService1 apiService1;
    private final ApiService2 apiService2;
    private final ApiService3 apiService3;

    @Bean
    public Step apiMasterStep() throws Exception {
        return stepBuilderFactory.get("apiMasterStep")
                .partitioner(apiSlaveStep().getName(), partitioner())
                .step(apiSlaveStep())
                .gridSize(3)
                .taskExecutor(taskExecutor())
                .build();

    }
    @Bean
    public Step apiSlaveStep() throws Exception {
        return stepBuilderFactory.get("apiSlaveStep")
                .<ProductVO, ApiRequestVO>chunk(chunkSize)
                .reader(itemReader(null))
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(6);
        taskExecutor.setThreadNamePrefix("api-thread-");
        return taskExecutor;
    }

    @Bean
    public ProductPartitioner partitioner() {
        ProductPartitioner productPartitioner = new ProductPartitioner();
        productPartitioner.setDataSource(dataSource);
        return productPartitioner;
    }

    @Bean
    @StepScope
    public ItemReader<ProductVO> itemReader(
            @Value("#{stepExecutionContext['product']}") ProductVO productVO
    ) throws Exception {
        JdbcPagingItemReader<ProductVO> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(dataSource);
        reader.setPageSize(chunkSize);
        reader.setRowMapper(new BeanPropertyRowMapper<>(ProductVO.class));

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, name, price, type");
        queryProvider.setFromClause("from product");
        queryProvider.setWhereClause("where type = :type");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("id", Order.DESCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setParameterValues(QueryGenerator.getParameterForQuery("type", productVO.getType()));
        reader.setQueryProvider(queryProvider);
        reader.afterPropertiesSet();

        return reader;
    }

    @Bean
    public ItemProcessor<? super ProductVO,? extends ApiRequestVO> itemProcessor() {
        ClassifierCompositeItemProcessor<ProductVO, ApiRequestVO> processor =
                new ClassifierCompositeItemProcessor<>();

        ProcessorClassifier<ProductVO, ItemProcessor<?,? extends ApiRequestVO>> classifier =
                new ProcessorClassifier<>();

        Map<String, ItemProcessor<ProductVO,ApiRequestVO>> processorMap = new HashMap<>();
        processorMap.put("1", new ApiItemProcessor1());
        processorMap.put("2", new ApiItemProcessor2());
        processorMap.put("3", new ApiItemProcessor3());
        classifier.setProcessorMap(processorMap);

        processor.setClassifier(classifier);
        return processor;
    }

    @Bean
    public ItemWriter<? super ApiRequestVO> itemWriter() throws Exception {
        ClassifierCompositeItemWriter<ApiRequestVO> writer =
                new ClassifierCompositeItemWriter<>();

        WriterClassifier<ApiRequestVO, ItemWriter<? super ApiRequestVO>> classifier =
                new WriterClassifier<>();

        Map<String, ItemWriter<ApiRequestVO>> writerMap = new HashMap<>();
        ApiItemWriterFactory apiItemWriterFactory = new ApiItemWriterFactory();
        String writeDirectory = "C:\\workspace\\Java\\springbatchlecture\\src\\main\\resources\\";

        for (int i = 1; i <= 3; i++) {
            FlatFileItemWriter<ApiRequestVO> itemWriter = apiItemWriterFactory.getItemWriter(i);
            itemWriter.setResource(
                    new FileSystemResource(writeDirectory + "product" + i + ".txt")
            );
            itemWriter.setLineAggregator(new DelimitedLineAggregator<>());
            itemWriter.setAppendAllowed(true);
            itemWriter.afterPropertiesSet();
            writerMap.put(String.valueOf(i), itemWriter);
        }

        classifier.setWriterMap(writerMap);

        writer.setClassifier(classifier);
        return writer;
    }

    class ApiItemWriterFactory {

        FlatFileItemWriter<ApiRequestVO> getItemWriter(int typeId) {
            if (typeId == 1) {
                return new ApiItemWriter1(ApiStepConfiguration.this.apiService1);
            }
            else if (typeId == 2) {
                return new ApiItemWriter2(ApiStepConfiguration.this.apiService2);
            }
            else if (typeId == 3) {
                return new ApiItemWriter3(ApiStepConfiguration.this.apiService3);
            }
            throw new RuntimeException("not Found Proper ApiItemWriter..");
        }
    }
}
