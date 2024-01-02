package io.springbatch.springbatchlecture.ch8.jdbccursor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

@RequiredArgsConstructor
//@Configuration
public class JdbcCursorConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private int chunksize = 10;
    private final DataSource dataSource;

    @Bean
    public Job batchJob() {
        return jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Customer, Customer>chunk(chunksize)
                .reader(customItemReader())
                .writer(new ItemWriter<Customer>() {
                    @Override
                    public void write(List<? extends Customer> items) throws Exception {
                        items.forEach(System.out::println);
                        System.out.println();
                    }
                })
                .build();
    }
    @Bean
    public ItemReader<? extends Customer> customItemReader() {
        return new JdbcCursorItemReaderBuilder<Customer>()
                .name("jdbcCursorItemReader")
                .fetchSize(5) // db로부터 결과값을 가져올때 한번에 최대 몇개씩 가져와서 메모리에 올릴건지
//                .sql("select id, firstName, lastName, birthdate from customer where firstName like ? order by lastName, firstName")
                .sql("select id, firstName, lastName, birthdate from customer")
                .beanRowMapper(Customer.class)
//                .queryArguments("A%")
                .dataSource(dataSource)
//                .maxRows(3) // 결과값을 최대 몇개 가져올 수 있나?
//                .currentItemCount(5)
//                .maxItemCount(5)
                .build();
    }

}
