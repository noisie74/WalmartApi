package mikhail.com.walmartapi.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mikhail on 6/21/16.
 */
public class Products {

    private String productId;
    private String productName;
    private String shortDescription;
    private String longDescription;
    private float price;
    @SerializedName("productImage")
    private String image;
    @SerializedName("reviewRating")
    private float rating;
    private int reviewCount;
    private boolean inStock;

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public float getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public float getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public boolean isInStock() {
        return inStock;
    }

    @Override
    public String toString() {
        return String.format("$%", price);

    }
}


