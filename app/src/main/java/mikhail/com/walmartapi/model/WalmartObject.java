package mikhail.com.walmartapi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Root JSon object
 */
public class WalmartObject {

   private int totalProducts;
   private int pageNumber;
   private int pageSize;
   private List<Products> products;

   public List<Products> getProducts() {
      return products;
   }

   public int getTotalProducts() {
      return totalProducts;
   }

   public int getPageNumber() {
      return pageNumber;
   }

   public int getPageSize() {
      return pageSize;
   }
}

