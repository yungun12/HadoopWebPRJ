package kopo.poly.service.impl;

import kopo.poly.dto.HdfsDTO;
import kopo.poly.persistance.mapper.IHdfsMapper;
import kopo.poly.service.IHdfsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HdfsService implements IHdfsService {

    private final IHdfsMapper hdfsMapper;

    @Override
    public List<HdfsDTO> insertHdfsFileInfo(HdfsDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".insertHdfsFileInfo Start!");

        // HDFS 업로드된 파일 정보를 DB 저장하기
        hdfsMapper.insertHdfsInfo(pDTO);

        // HDFS 업로드된 전체 파일 정보를 DB 조회하기
        List<HdfsDTO> rList = Optional.ofNullable(hdfsMapper.getHdfsInfoList()).orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + ".insertHdfsFileInfo End!");

        return rList;
    }

}

