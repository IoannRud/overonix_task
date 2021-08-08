package com.overonix.nominatim.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AddressInfoStored {
    public String lat;
    public String lon;
    public String display_name;
    public JsonNode address;
}
