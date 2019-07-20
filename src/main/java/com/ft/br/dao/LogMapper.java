package com.ft.br.dao;

import com.ft.dao.stock.mapper.LogDOMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * LogMapper
 *
 * @author shichunyang
 */
@Component
@Mapper
public interface LogMapper extends LogDOMapper {
}
