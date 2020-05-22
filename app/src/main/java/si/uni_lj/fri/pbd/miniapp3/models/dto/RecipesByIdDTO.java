package si.uni_lj.fri.pbd.miniapp3.models.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;

public class RecipesByIdDTO {

    @SerializedName("meals")
    @Expose
    private List<RecipeDetails> recipe;

    public List<RecipeDetails> getRecipesById(){
        return recipe;
    }
}
