package io.springbatch.springbatchlecture.ch9.jpa;


import io.springbatch.springbatchlecture.ch9.xml.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Customer, Customer3> {

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public Customer3 process(Customer item) throws Exception {
        return modelMapper.map(item, Customer3.class);
    }
}
