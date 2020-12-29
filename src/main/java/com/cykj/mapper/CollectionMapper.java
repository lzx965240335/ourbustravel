package com.cykj.mapper;

import com.cykj.bean.Collection;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionMapper {
    int deleteByPrimaryKey(Integer collectionid);

    int insert(Collection record);

    int insertSelective(Collection record);

    Collection selectByPrimaryKey(Integer collectionid);

    int updateByPrimaryKeySelective(Collection record);

    int updateByPrimaryKey(Collection record);
}