package si.uni_lj.fri.pbd.miniapp3.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;
import si.uni_lj.fri.pbd.miniapp3.ui.DetailsActivity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    public String fragment;
    public List<RecipeDetails> recipeDetails;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemTitle;


        public MyViewHolder(View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.image_view);
            itemTitle = itemView.findViewById(R.id.text_view_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //detail_activity
                    Intent detailActivity = new Intent(view.getContext(), DetailsActivity.class);

                    System.out.println("meal id in adapter: " + recipeDetails.get(getAdapterPosition()).getIdMeal());

                    //send mealId info and fragment info to detailActivity
                    detailActivity.putExtra("mealId", Long.parseLong(recipeDetails.get(getAdapterPosition()).getIdMeal()));
                    detailActivity.putExtra("fragment", fragment);

                    view.getContext().startActivity(detailActivity);
                }
            });
        }
    }
    public void setRecipeList(List<RecipeDetails> recipes)
    {
        recipeDetails = recipes;
        notifyDataSetChanged();
    }

    public RecyclerViewAdapter(List<RecipeDetails> recipeDetails, String fragment)
    {
        this.recipeDetails = recipeDetails;
        this.fragment = fragment;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_grid_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if(holder.getAdapterPosition()!=RecyclerView.NO_POSITION)
        {
            Glide.with(holder.itemView).load(recipeDetails.get(position).getStrMealThumb()).override(400,400).into(holder.itemImage);
            holder.itemTitle.setText(recipeDetails.get(position).getStrMeal());
        }


    }

    // Return the size of the data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return (null != recipeDetails ? recipeDetails.size() : 0);
    }

}