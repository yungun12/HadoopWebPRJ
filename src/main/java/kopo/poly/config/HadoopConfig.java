package kopo.poly.config;

import org.apache.hadoop.conf.Configuration;
import org.springframework.context.annotation.Bean;

/**
 * 하둡 접속을 위한 공통 설정을 추상한 객체로 정의
 */
public class HadoopConfig {

    // 네임노드가 설치된 마스터 서버 IP
    String namenodeHost = "192.168.2.136";

    // 네임노드 포트
    String namemodePort = "9000";

    // 얀 포트
    String yarnPort = "8080";

    /*
     * 하둡 접속 설정
     * */
    @Bean
    public Configuration getHadoopConfiguration() {

        Configuration conf = new Configuration();

        // fs.defaultFS 설정 값 : hdfs://192.168.2.136:9000
        conf.set("fs.defaultFS", "hdfs://" + namenodeHost + ":" + namemodePort);

        // yarn 주소 설정
        conf.set("yarn.resourcemanager.address", namenodeHost + ":" + yarnPort);

        return conf;
    }
}