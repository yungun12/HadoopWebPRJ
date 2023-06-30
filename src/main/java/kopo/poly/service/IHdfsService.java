package kopo.poly.service;

import kopo.poly.dto.HdfsDTO;

import java.util.List;

public interface IHdfsService {

    List<HdfsDTO> insertHdfsFileInfo(HdfsDTO pDTO) throws Exception;

}

