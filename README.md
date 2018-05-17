## Demo elastic-job with spring boot
Elastic-Job-Lite Architecture
![Elastic-Job-Lite Architecture](https://kekekeke.sh1a.qingstor.com/elastic_job_lite.png)

### use zookeeper image
```
docker run --name my-zookeeper --restart always -p 2181:2181 -d zookeeper
```

### Use H2 or Mysql to store JOB_EXECUTION_LOG and JOB_STATUS_TRACE_LOG

```
 docker run --name my-mysql -e MYSQL_ROOT_PASSWORD=root -d -p 3306:3306 mysql:5.7
 docker exec -it my-mysql bash  #进入my-mysql容器
 mysql -uroot -proot #创建test数据库
 mysql> create database test character set utf8 collate utf8_general_ci;
 ```
 
### job 配置
```yaml
simpleJob:
  cron: 0/5 * * * * ?
  shardingTotalCount: 3
  shardingItemParameters: 0=Beijing,1=Shanghai,2=Guangzhou
``` 
### job分片执行情况
#### 先启动一个节点

```
mvn spring-boot:run 
```
job执行情况
```
[INFO ] 2018-05-17 16:48:00,224 --inner-job-com.ml.elasticjobdemo.job.MySimpleJob-1-- [com.ml.elasticjobdemo.job.MySimpleJob] ------Thread ID: 55, 任务总片数: 3, 当前分片项: 0, 分片参数：Beijing
[INFO ] 2018-05-17 16:48:00,227 --inner-job-com.ml.elasticjobdemo.job.MySimpleJob-2-- [com.ml.elasticjobdemo.job.MySimpleJob] ------Thread ID: 56, 任务总片数: 3, 当前分片项: 1, 分片参数：Shanghai
[INFO ] 2018-05-17 16:48:00,227 --inner-job-com.ml.elasticjobdemo.job.MySimpleJob-3-- [com.ml.elasticjobdemo.job.MySimpleJob] ------Thread ID: 57, 任务总片数: 3, 当前分片项: 2, 分片参数：Guangzhou
```
#### 启动第二个节点

```
mvn spring-boot:run -Dserver.port=8091
```

节点一输出
```
[INFO ] 2018-05-17 16:50:45,099 --com.ml.elasticjobdemo.job.MySimpleJob_Worker-1-- [com.ml.elasticjobdemo.job.MySimpleJob] ------Thread ID: 30, 任务总片数: 3, 当前分片项: 1, 分片参数：Shanghai
[INFO ] 2018-05-17 16:50:50,060 --com.ml.elasticjobdemo.job.MySimpleJob_Worker-1-- [com.ml.elasticjobdemo.job.MySimpleJob] ------Thread ID: 30, 任务总片数: 3, 当前分片项: 1, 分片参数：Shanghai
[INFO ] 2018-05-17 16:50:55,062 --com.ml.elasticjobdemo.job.MySimpleJob_Worker-1-- [com.ml.elasticjobdemo.job.MySimpleJob] ------Thread ID: 30, 任务总片数: 3, 当前分片项: 1, 分片参数：Shanghai

```
节点而输出
```
[INFO ] 2018-05-17 16:51:20,189 --inner-job-com.ml.elasticjobdemo.job.MySimpleJob-4-- [com.ml.elasticjobdemo.job.MySimpleJob] ------Thread ID: 64, 任务总片数: 3, 当前分片项: 0, 分片参数：Beijing
[INFO ] 2018-05-17 16:51:20,189 --inner-job-com.ml.elasticjobdemo.job.MySimpleJob-3-- [com.ml.elasticjobdemo.job.MySimpleJob] ------Thread ID: 63, 任务总片数: 3, 当前分片项: 2, 分片参数：Guangzhou
[INFO ] 2018-05-17 16:51:25,159 --inner-job-com.ml.elasticjobdemo.job.MySimpleJob-5-- [com.ml.elasticjobdemo.job.MySimpleJob] ------Thread ID: 65, 任务总片数: 3, 当前分片项: 0, 分片参数：Beijing
[INFO ] 2018-05-17 16:51:25,159 --inner-job-com.ml.elasticjobdemo.job.MySimpleJob-6-- [com.ml.elasticjobdemo.job.MySimpleJob] ------Thread ID: 66, 任务总片数: 3, 当前分片项: 2, 分片参数：Guangzhou

```

### 运维平台 elastic-job-lite-console
下载代码
```
git clone https://github.com/elasticjob/elastic-job-lite
```
编译代码
```
mvn clean install -Dmaven.test.skip=true
```
解压 ../elastic-job-lite-console-3.0.0.M1-SNAPSHOT.tag.gz
```
cd ./elastic-job-lite-console/target
tar -zxf elastic-job-lite-console-3.0.0.M1-SNAPSHOT.tar.gz
```
启动
```
cd elastic-job-lite-console-3.0.0.M1-SNAPSHOT
./bin/start.sh  
or ./bin/start/bat # Win
```
登陆运维平台，可以修改job配置