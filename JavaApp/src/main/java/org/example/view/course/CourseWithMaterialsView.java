package org.example.view.course;

import lombok.Builder;
import lombok.Data;
import org.example.enums.CourseType;
import org.example.view.TeacherView;
import org.example.view.TheoryMaterialView;

import java.util.List;

@Data
@Builder
public class CourseWithMaterialsView {

    private Long id;

    private CourseType courseType;

    private String description;

    private TeacherView teacher;

    private int materialsCount;

    private Long testId;

    private List<TheoryMaterialView> theoryMaterials;
}
