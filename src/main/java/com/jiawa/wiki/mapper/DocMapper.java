package com.jiawa.wiki.mapper;

import com.jiawa.wiki.domain.Doc;
import com.jiawa.wiki.domain.DocExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DocMapper {
    long countByExample(DocExample example);

    int deleteByExample(DocExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Doc row);

    int insertSelective(Doc row);

    List<Doc> selectByExample(DocExample example);

    Doc selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Doc row, @Param("example") DocExample example);

    int updateByExample(@Param("row") Doc row, @Param("example") DocExample example);

    int updateByPrimaryKeySelective(Doc row);

    int updateByPrimaryKey(Doc row);
}