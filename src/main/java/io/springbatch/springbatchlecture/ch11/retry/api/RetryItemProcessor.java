package io.springbatch.springbatchlecture.ch11.retry.api;

import org.springframework.batch.item.ItemProcessor;

public class RetryItemProcessor implements ItemProcessor<String, String> {

    private int cnt = 0;

    @Override
    public String process(String item) throws Exception {

//        if (item.equals("2") || item.equals("3")) {
//            cnt++;
//            System.out.println("예외발생 item : " + item);
//            throw new RetryableException("failed item : " + item + " cnt : " + cnt);
//        }
        return item;
    }
}
