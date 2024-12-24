package com.project.LMS.dto.material;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialDto {
    private String courseName;
    private String title;
    private String url;
}
