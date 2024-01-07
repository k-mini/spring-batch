package io.springbatch.springbatchlecture.ch13.jobandstep;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class CustomStepExecutionListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        System.out.println("stepName = " + stepName);
        stepExecution.getExecutionContext().put("name", "user1");
        System.out.println();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        ExitStatus exitStatus = stepExecution.getExitStatus();
        System.out.println("exitStatus = " + exitStatus);
        BatchStatus status = stepExecution.getStatus();
        System.out.println("status = " + status);
        String name = stepExecution.getExecutionContext().getString("name");
        System.out.println("name = " + name);

        return ExitStatus.COMPLETED;
    }

}
