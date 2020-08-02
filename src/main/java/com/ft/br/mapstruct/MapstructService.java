package com.ft.br.mapstruct;

import com.ft.util.DateUtil;
import com.ft.util.SpringContextUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Created by shichunyang on 2020/8/1.
 */
@Mapper(componentModel = SpringContextUtil.SPRING)
public interface MapstructService {

    @Mappings({
            @Mapping(source = "alId", target = "blId"),
            @Mapping(source = "alName", target = "blName"),
            @Mapping(source = "alDate", target = "blDate", dateFormat = DateUtil.DATE_FORMAT_MINUTE),
            @Mapping(target = "blSum", expression = "java(amo.getAlId() * 100)"),
            @Mapping(target = "blbb", constant = "别bb")
    })
    Bmo convert(Amo amo);
}
