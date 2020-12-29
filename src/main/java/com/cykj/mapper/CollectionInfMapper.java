package com.cykj.mapper;

import com.cykj.bean.CollectionInf;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionInfMapper {
    int deleteByPrimaryKey(Integer collectionid);

    int insert(CollectionInf record);

    int insertSelective(CollectionInf record);

    CollectionInf selectByPrimaryKey(Integer collectionid);

    int updateByPrimaryKeySelective(CollectionInf record);

    int updateByPrimaryKey(CollectionInf record);
}