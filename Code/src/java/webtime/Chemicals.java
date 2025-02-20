package webtime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class Chemicals {
    private String searchText;
    private List<Product> products;
    private Cart cart;
    
    //Getters and Setters
    public String getSearchText() {return searchText;}
    public void setSearchText(String searchText) {this.searchText = searchText;}
    public Cart getCart() {return cart;}
    
    public String getTotal() {
        double total = 0;
        for (Product p : cart.getCartItems()) {
            total += p.getPrice() * p.getQuantity();
        }
        return String.format("%.2f", total);
    }


    public Chemicals() throws SQLException {
    
    //Connecting to DataBase
    String url = "jdbc:derby://localhost:1527/Chemicals";
    Connection connection = DriverManager.getConnection(url, "APP", "APP");

    PreparedStatement data = connection.prepareStatement("SELECT * FROM APP.PRODUCTS");
    ResultSet rs = data.executeQuery();
    products = new ArrayList<>();

    while (rs.next()) {
        String name = rs.getString("name");
        double price = rs.getDouble("price");
        int stock = rs.getInt("stock");
        products.add(new Product(name, price, stock));
    }
    cart = new Cart();//Creating variable instance to store products
}


    public List<Product> getAutofill() {
        List<Product> filtered = new ArrayList<>();
        for (Product p : products) {
            if (searchText == null || searchText.trim().isEmpty() ||
                    p.getName().toLowerCase().contains(searchText.toLowerCase())){
                filtered.add(p);
            }
        }
        return filtered;
    }
    
    public void addToCart(Product product) {cart.addToCart(product);}
    public void removeFromCart(Product product) {cart.removeFromCart(product);}
    
    public static class Product {
        private String name;
        private double price;
        private int stock;
        private int quantity;

        public Product(String name, double price, int stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
            this.quantity = 0;
        }

        //Getters and Setters
        public String getName() {return name;}
        public void setName(String name) {this.name = name;}
        public double getPrice() {return price;}
        public void setPrice(double price) {this.price = price;}
        public int getStock() {return stock;}
        public void setStock(int stock) {this.stock = stock;}
        public int getQuantity() {return quantity;}
        public void setQuantity(int quantity) {this.quantity = quantity;}
    }
    
    public class Cart implements Serializable {
        private Map<String, Product> shoppingCart = new HashMap<>();

        //Adds 1 of selected chemical from basket when actioned (doesnt display duplicate chemicals)
        public void addToCart(Product product) {
            String productName = product.getName();
            //if product im adding to is in the Map then dont repeat chemical on table and quantity +1
            if (shoppingCart.containsKey(productName)) {
                Product existingProduct = shoppingCart.get(productName);
                existingProduct.setQuantity(existingProduct.getQuantity() + 1);
            } else {
                product.setQuantity(1);
                shoppingCart.put(productName, product);
            }
        }

        //Removes 1 of selected chemical from basket when actioned 
        public void removeFromCart(Product product) {
            String productName = product.getName();
            if (shoppingCart.containsKey(productName)) {
                Product existingProduct = shoppingCart.get(productName);
                if (existingProduct.getQuantity() > 1) {
                    existingProduct.setQuantity(existingProduct.getQuantity() - 1);
                } else {
                    shoppingCart.remove(productName);
                }
            }
        }

        public List<Product> getCartItems() {
            return new ArrayList<>(shoppingCart.values());
        }
    }  
    public String viewCart() {
        // perform checkout logic here
        return "checkout?faces-redirect=true";
    }
    
    public void checkout() throws SQLException {
        // Connecting to Database
        String url = "jdbc:derby://localhost:1527/Chemicals";
        Connection connection = DriverManager.getConnection(url, "APP", "APP");

        try {
            PreparedStatement newStock = connection.prepareStatement("UPDATE APP.PRODUCTS SET STOCK = STOCK - ? WHERE NAME = ?");

            for (Product item : cart.getCartItems()) {
                newStock.setInt(1, item.getQuantity());
                newStock.setString(2, item.getName());
                newStock.executeUpdate();
            }
        } finally {
            connection.close();// end connection to database
        }
        cart = null;// empty the basket
    }
    
}
