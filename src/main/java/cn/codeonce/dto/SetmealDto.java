package cn.codeonce.dto;

import cn.codeonce.pojo.Setmeal;
import cn.codeonce.pojo.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
