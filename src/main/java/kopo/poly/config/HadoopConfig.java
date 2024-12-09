package kopo.poly.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HadoopConfig {

    @Value("${hadoop.master.ip}")
    String namenodeHost;

    // 네임노드 포트
    @Value("${hadoop.master.namenode.port}")

    String namenodePort;

    @Value("${hadoop.master.resourcemanager.port}") // 얀 포트
    String yarnPort;

    @Bean
    public org.apache.hadoop.conf.Configuration getHadoopConfiguration() {

        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();

        //fs.defaultFS 설정 값 : hdfs://192.168.243.128:8020
        conf.set("fs.defaultFS", "hdfs://" + namenodeHost + ":" + namenodePort);

        // 리소스 매니저 접속 설정 : 192.168.243.128:8032
        conf.set("yarn.resourcemanager.address", namenodeHost + ":" + yarnPort);

        return conf;
    }
}
