package com.ozay.web.rest.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naofumiezaki on 5/2/15.
 */
public class JsonResponse {

    private String code;

    private boolean success;

    private Object response;

    private List<FieldErrorDTO> fieldErrorDTOs;

    public JsonResponse(){
        this.success = true;
        this.code = "200";
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<FieldErrorDTO> getFieldErrorDTOs() {
        return fieldErrorDTOs;
    }

    public void setFieldErrorDTOs(List<FieldErrorDTO> fieldErrorDTOs) {
        this.fieldErrorDTOs = fieldErrorDTOs;
    }

    public void addFieldErrorDTO(FieldErrorDTO fieldErrorDTO){
        if(this.getFieldErrorDTOs() == null){
            this.setFieldErrorDTOs(new ArrayList<FieldErrorDTO>());
        }
        this.getFieldErrorDTOs().add(fieldErrorDTO);
    }

}
