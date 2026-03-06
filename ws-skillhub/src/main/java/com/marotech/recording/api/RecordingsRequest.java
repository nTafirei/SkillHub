package com.marotech.recording.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.marotech.recording.gson.GsonExcludeField;
import com.marotech.recording.util.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class RecordingsRequest extends BaseRequest {

    private LocalDate startDate;
    private LocalDate endDate;
    private Page page;

    @GsonExcludeField
    @JsonIgnore
    public boolean isValid() {

        if(StringUtils.isEmpty(mobileNumber) || StringUtils.isEmpty(password)){
            return false;
        }

        if (startDate != null && endDate != null) {
            if (endDate.isBefore(startDate)) {
                return false;
            }
        }
        return true;
    }
}