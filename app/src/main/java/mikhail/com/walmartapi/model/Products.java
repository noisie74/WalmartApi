package mikhail.com.walmartapi.model;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * JSon data
 */
public class Products {

    private String productId;
    private String productName;
    private String shortDescription;
    private String longDescription;
    private String price;
    @SerializedName("productImage")
    private String image;
    @SerializedName("reviewRating")
    private double rating;
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

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public double getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public boolean isInStock() {
        return inStock;
    }

}


