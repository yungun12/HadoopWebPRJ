package kopo.poly.controller;

import kopo.poly.dto.HadoopDTO;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.ui.ModelMap;
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

    /**
     * 게시판 리스트 보여주기
     */
    @PostMapping(value = "uploadFile")
    public String uploadFile(HttpServletRequest request,
                             @RequestParam(value = "fileUpload") MultipartFile mf, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".uploadFile Start!");

        HadoopDTO pDTO = new HadoopDTO();

        // CentOS에 설치된 하둡 분산 파일 시스템 연결 및 설정하기
        FileSystem hdfs = FileSystem.get(hadoopConfiguration);

        // 하둡에 파일을 업로드할 폴더
        // 예 : /01/02
        String hadoopUploadFilePath = CmmUtil.nvl(pDTO.getHadoopUploadPath());

        // 하둡에 업로드할 파일명
        // 예 : access_log.gz
        String hadoopUploadFileName = CmmUtil.nvl(pDTO.getHadoopUploadFileName());

        // 하둡에 폴더 생성하기
        // hadoop fs -mkdir -p /01/02
        hdfs.mkdirs(new Path(hadoopUploadFilePath));

        // 하둡분산파일시스템에 저장될 파일경로 및 폴더명
        // 예 : hadoop fs -put access_log.gz /01/02/access_log.gz
        String hadoopFile = hadoopUploadFilePath + "/" + hadoopUploadFileName;

        // 하둡분산파일시스템에 저장가능한 객체로 변환
        Path path = new Path(hadoopFile);

        // 기존 하둡에 존재하는지 로그 찍어보기
        log.info("HDFS Exists : " + hdfs.exists(path));

        if (hdfs.exists(path)) { // 하둡분산파일시스템에 파일 존재하면...

            // 기존 업로드되어 있는 파일 삭제하기
            // hadoop fs -rm -r /01/02/access_log.gz
            hdfs.delete(path, true);

        }

        // 예 : c:/hadoop_data/access_log.gz
        Path localPath = new Path(
                CmmUtil.nvl(pDTO.getLocalUploadPath()) +
                        "/" + CmmUtil.nvl(pDTO.getLocalUploadFileName()));

        // HDFS에 파일 만들기
        FSDataOutputStream outputStream = hdfs.create(path);

        // HDFS에 파일 내용 기록하기
        IOUtils.copyBytes(mf.getInputStream(), outputStream, hadoopConfiguration);

        hdfs.close();

        log.info(this.getClass().getName() + ".uploadFile End!");

        return "Success";
    }

}
