package io.springbatch.springbatchlecture.ch15.batch.chunk.processor;

import io.springbatch.springbatchlecture.ch15.batch.domain.Product;
import io.springbatch.springbatchlecture.ch15.batch.domain.ProductVO;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.ui.Model;

public class FileItemProcessor implements ItemProcessor<ProductVO, Product> {

    @Override
    public Product process(ProductVO item) throws Exception {

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(item, Product.class);
    }
}
