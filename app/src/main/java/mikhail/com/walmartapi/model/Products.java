package mikhail.com.walmartapi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created by Mikhail on 6/21/16.
 */
public class Products implements Parcelable{

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    //    @Override
//    public String toString() {
//        return String.format(DecimalFormat.getCurrencyInstance(Locale.US).format(price));
//
//    }


//    @Override
//    public String toString() {
//        return String.format("%s",rating);
//    }
}


