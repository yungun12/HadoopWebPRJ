package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HadoopDTO {

    // 하둡에 업로드되는 파일 이름
    private String hadoopUploadFileName;

    // 하둡에 업로드되는 파일 경로
    private String hadoopUploadPath;

    // 하둡에 업로드할 내 컴퓨터에 존재하는 파일 이름
    private String localUploadFileName;

    // 하둡에 업로드할 내 컴퓨터에 존재하는 파일 경로
    private String localUploadPath;

    // GZ 파일 내용 읽기, 업로드 할 라인수
    long lineCnt;

    // GZ 파일 내용
    List<String> contentList;

    // 정규식 표현
    String regExp;

}

