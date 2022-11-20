package kopo.poly.controller;

import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/*
 * Controller 선언해야만 Spring 프레임워크에서 Controller인지 인식 가능
 * 자바 서블릿 역할 수행
 * */
@Slf4j
@RequestMapping(value = "/hdfs")
@RequiredArgsConstructor
@RestController
public class HdfsFileUploadController {

    private final Configuration hadoopConfiguration;

    // HDFS에 저장되는 폴더 시작 위치
    private String hdfsUploadDir = "/01";

    /**
     * 게시판 리스트 보여주기
     */
    @PostMapping(value = "uploadFile")
    public String uploadFile(HttpServletRequest request,
                             @RequestParam(value = "fileUpload") MultipartFile mf) throws Exception {

        log.info(this.getClass().getName() + ".uploadFile Start!");

        // 업로드하는 실제 파일명
        // 다운로드 기능 구현시, 임의로 정의된 파일명을 원래대로 만들어주기 위한 목적
        String originalFileName = mf.getOriginalFilename();

        // 파일 확장자 가져오기(파일 확장자를 포함한 전체 이름(myimage.jpg)에서 뒤쪽부터 .이 존재하는 위치 찾기
        String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1,
                originalFileName.length()).toLowerCase();

        log.info("originalFileName : " + originalFileName);
        log.info("ext : " + ext);

        // HDFS에 저장되는 파일명(사용자가 업로드하는 파일명에 한글 등 특수문자가 존재할 수 있기 때문에
        // 실제 저장되는 이름은 영어, 숫자만 사용함
        String hadoopUploadFileName = DateUtil.getDateTime("HHmmss") + "." + ext;

        // CentOS에 설치된 하둡 분산 파일 시스템 연결 및 설정하기
        FileSystem hdfs = FileSystem.get(hadoopConfiguration);

        // 하둡에 파일을 업로드할 폴더
        // 예 : /01/2022/11/20
        String hadoopUploadFilePath = hdfsUploadDir + "/" + DateUtil.getDateTime("yyyy/MM/dd");

        log.info("hadoopUploadFilePath : " + hadoopUploadFilePath);

        // 하둡에 폴더 생성하기
        // hadoop fs -mkdir -p /01/2022/11/20
        hdfs.mkdirs(new Path(hadoopUploadFilePath));

        // 하둡분산파일시스템에 저장될 파일경로 및 폴더명
        // 예 : hadoop fs -put access_log.gz /01/02/access_log.gz
        String hadoopFile = hadoopUploadFilePath + "/" + hadoopUploadFileName;

        // 하둡분산파일시스템에 저장가능한 객체로 변환
        Path path = new Path(hadoopFile);

        if (hdfs.exists(path)) { // 하둡분산파일시스템에 파일 존재하면...
            // 기존 업로드되어 있는 파일 삭제하기
            // hadoop fs -rm -r /01/2022/11/20/203030
            hdfs.delete(path, true);

        }

        // HDFS에 파일 만들기
        FSDataOutputStream outputStream = hdfs.create(path);

        // HDFS에 파일 내용 기록하기
        IOUtils.copyBytes(mf.getInputStream(), outputStream, hadoopConfiguration);

        hdfs.close();

        log.info(this.getClass().getName() + ".uploadFile End!");

        return "Success";
    }

}
