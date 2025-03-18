package com.learnova.classedge.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequestDto {


    private int page = 1;

    private int size = 10;

}
