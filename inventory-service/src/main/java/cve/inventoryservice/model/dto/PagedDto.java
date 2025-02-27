package cve.inventoryservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagedDto<ResponseDto> {
    private int pageNo;
    private int pageSize;
    private int totalPage;
    private String sort;

    private List<ResponseDto> items;
}
