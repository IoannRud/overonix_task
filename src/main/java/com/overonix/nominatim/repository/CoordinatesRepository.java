package com.overonix.nominatim.repository;

import com.overonix.nominatim.entity.AddressInfoStored;
import com.overonix.nominatim.entity.CoordinatesHolder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CoordinatesRepository {

    void saveCoordinates(@Param("pojo") AddressInfoStored pojo);

    List<CoordinatesHolder> getCoordinates();

    Object getAddressByCoordinates(@Param("lat") String lat, @Param("lon") String lon);

}
