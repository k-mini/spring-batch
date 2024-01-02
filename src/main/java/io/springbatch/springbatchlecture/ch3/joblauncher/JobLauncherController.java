package io.springbatch.springbatchlecture.ch3.joblauncher;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;

@RequiredArgsConstructor
//@RestController
public class JobLauncherController {

    private final Job job;
    private final JobLauncher simpleJobLauncher;
    private final BasicBatchConfigurer basicBatchConfigurer;

//    @PostMapping("/batch")
//    public String launch(@RequestBody Member member) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("id", member.getId())
//                .addDate("date", new Date())
//                .toJobParameters();
//
//        SimpleJobLauncher jobLauncher = (SimpleJobLauncher) basicBatchConfigurer.getJobLauncher();
//        SimpleJobLauncher jobLauncher = (SimpleJobLauncher) simpleJobLauncher;
//        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
//        jobLauncher.run(job, jobParameters);
//
//        return "batch completed";
//    }
}
