package item;

//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lenovo-pc on 27/02/2017.
 */
public class ItemModel {

    private String type;

    private String id;

    private int size;

    private int price;

    private String face;

    private int stock;

    private List<String> tags;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getFace() {
        return face;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStock() {
        return stock;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }
}
