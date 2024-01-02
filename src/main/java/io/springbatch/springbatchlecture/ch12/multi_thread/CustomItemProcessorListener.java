package io.springbatch.springbatchlecture.ch12.multi_thread;

import io.springbatch.springbatchlecture.ch12.async.Customer;
import org.springframework.batch.core.ItemProcessListener;

public class CustomItemProcessorListener implements ItemProcessListener<Customer, Customer> {

    @Override
    public void beforeProcess(Customer item) {

    }

    @Override
    public void afterProcess(Customer item, Customer result) {
        System.out.println("Thread : " + Thread.currentThread().getName() + ", process item : " + item.getId());
        System.out.println();
    }

    @Override
    public void onProcessError(Customer item, Exception e) {

    }
}
