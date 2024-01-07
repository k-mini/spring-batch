package io.springbatch.springbatchlecture.ch15.batch.classifier;

import io.springbatch.springbatchlecture.ch15.batch.domain.ApiRequestVO;
import io.springbatch.springbatchlecture.ch15.batch.domain.ProductVO;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class ProcessorClassifier<C, T> implements Classifier<C,T> {

    private Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap = new HashMap<>();

    public void setProcessorMap(Map<String, ItemProcessor<ProductVO, ApiRequestVO>> processorMap) {
        this.processorMap = processorMap;
    }

    @Override
    public T classify(C classifiable) {
        return (T) processorMap.get(((ProductVO) classifiable).getType());
    }
}
