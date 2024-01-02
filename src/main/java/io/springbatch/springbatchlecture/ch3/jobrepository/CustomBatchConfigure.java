package io.springbatch.springbatchlecture.ch3.jobrepository;

//@Configuration
//public class CustomBatchConfigure extends BasicBatchConfigurer {
//
//    private final DataSource dataSource;
//
//    public CustomBatchConfigure(BatchProperties properties, DataSource dataSource, TransactionManagerCustomizers transactionManagerCustomizers) {
//        super(properties, dataSource, transactionManagerCustomizers);
//        this.dataSource = dataSource;
//    }
//
//    @Override
//    protected JobRepository createJobRepository() throws Exception {
//
//        JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
//        factoryBean.setDataSource(dataSource);
//        factoryBean.setTransactionManager(getTransactionManager());
//        factoryBean.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
//        factoryBean.setTablePrefix("SYSTEM_");
//
//        return factoryBean.getObject();
//    }
//}
