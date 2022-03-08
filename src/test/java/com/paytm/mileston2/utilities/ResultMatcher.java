package com.paytm.mileston2.utilities;

import com.paytm.mileston2.DTO.CustomReturnType;

import java.io.IOException;

public class ResultMatcher {


    public static boolean isMatched(String actualJson,String ExpectedOutputFile) throws IOException {
        CustomReturnType actualResponse = (CustomReturnType) FileUtilities.getObjectFromjsonString(actualJson, CustomReturnType.class);
        CustomReturnType expectedResponse = (CustomReturnType) FileUtilities.getObjectFromFile(ExpectedOutputFile, CustomReturnType.class);

        if(expectedResponse.getMsg().compareTo(actualResponse.getMsg())!=0 || expectedResponse.getStatus().compareTo(actualResponse.getStatus())!=0)
           return false;

        return true;
    }

    public static boolean Matched(String actualJson,String ExpectedOutputFile) throws IOException {

        String Expectedjson=FileUtilities.getJsonStringFromFile(ExpectedOutputFile);

        if(actualJson.compareTo(Expectedjson)!=0)
            return false;
        return true;

    }
}
