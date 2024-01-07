package io.springbatch.springbatchlecture.ch14.test;


import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest(classes = {SimpleJobConfiguration.class, TestBatchConfig.class})
public class SimpleJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void clear() {
        jdbcTemplate.execute("delete from customer3");
    }

    @Test
    void simpleJob_test() throws Exception {

        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("name", "user1")
                .addLong("date", new Date().getTime())
                .toJobParameters();

        // when
//        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        JobExecution jobExecution1 = jobLauncherTestUtils.launchStep("step1");

        // then
//        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
//        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        StepExecution stepExecution = ((List<StepExecution>) jobExecution1.getStepExecutions()).get(0);
        assertThat(stepExecution.getCommitCount()).isEqualTo(11);
        assertThat(stepExecution.getReadCount()).isEqualTo(100);
        assertThat(stepExecution.getWriteCount()).isEqualTo(100);
        assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(stepExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

}
