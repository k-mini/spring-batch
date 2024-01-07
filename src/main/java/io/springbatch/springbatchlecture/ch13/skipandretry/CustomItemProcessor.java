package io.springbatch.springbatchlecture.ch13.skipandretry;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Integer, String> {

    int count = 0;

    @Override
    public String process(Integer item) throws Exception {

        if (count < 2) {
            if (count % 2 == 0) {
                count++;
            }
            else if (count % 2 == 1){
                count++;
                throw new CustomRetryException("retry process failed");
            }
        }
        return String.valueOf(item);
    }
}
