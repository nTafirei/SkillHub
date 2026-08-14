package com.marotech.skillhub.api;

import com.marotech.skillhub.model.Category;
import com.marotech.skillhub.model.Suburb;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;

@Data
public class JobDTO extends BaseRequest {

    private String title;
    private String desc;
    private String category;
    private String city;
    private String suburb;
}
