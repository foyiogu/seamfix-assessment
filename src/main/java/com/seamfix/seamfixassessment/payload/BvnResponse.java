package com.seamfix.seamfixassessment.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BvnResponse {

    private String message;
    private String code;
    private String bvn;
    private String imageDetail;
    private String basicDetail;

}
