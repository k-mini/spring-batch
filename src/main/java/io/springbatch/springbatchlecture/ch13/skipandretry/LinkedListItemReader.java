package io.springbatch.springbatchlecture.ch13.skipandretry;

import org.springframework.aop.support.AopUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.LinkedList;
import java.util.List;

public class LinkedListItemReader<T> implements ItemReader<T> {

    private List<T> list;

    public LinkedListItemReader(List<T> list) {
        if (AopUtils.isAopProxy(list)) {
            this.list = list;
        }
        else {
            this.list = new LinkedList<>(list);
        }
    }

    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if (!list.isEmpty()) {
            T remove = (T) list.remove(0);
            if ((Integer) remove == 3) {
                throw new CustomSkipException("read skipped : " + remove);
            }
            System.out.println("read : " + remove);
            return remove;
        }
        return null;
    }
}
