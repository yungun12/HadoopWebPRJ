package kopo.poly.persistance.mapper;

import kopo.poly.dto.HdfsDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IHdfsMapper {

    void insertHdfsInfo(HdfsDTO pDTO) throws Exception;

    List<HdfsDTO> getHdfsInfoList() throws Exception;

}
