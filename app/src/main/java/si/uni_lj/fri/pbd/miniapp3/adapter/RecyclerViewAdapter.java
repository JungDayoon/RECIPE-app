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

    Bitmap bm;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView itemImage;
        public TextView itemTitle;


        public MyViewHolder(View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.image_view);
            itemTitle = itemView.findViewById(R.id.text_view_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "position: " + getAdapterPosition(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    //detail_activity
                    Intent detailActivity = new Intent(view.getContext(), DetailsActivity.class);


                    System.out.println("meal id in adapter: " + recipeDetails.get(getAdapterPosition()).getIdMeal());
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
            Thread mThread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(recipeDetails.get(position).getStrMealThumb());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        bm = BitmapFactory.decodeStream(is);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            mThread.start();

            try{
                mThread.join();
                holder.itemImage.setImageBitmap(bm);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            holder.itemTitle.setText(recipeDetails.get(position).getStrMeal());
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return (null != recipeDetails ? recipeDetails.size() : 0);
    }

}