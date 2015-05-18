package com.ozay.web.rest.dto;

import java.util.List;

/**
 * Created by naofumiezaki on 5/18/15.
 */
public class SearchDTO {
    private String type;
    private List<?> list;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
