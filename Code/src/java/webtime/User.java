package webtime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name="User")
@SessionScoped
public class User {
    //Instance variables that represent one user
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private String password;
    private boolean viewState; //Check if user is logged in
    
    //Getters and Setters
    public String getFirstName(){return firstName;}
    public void setFirstName( String firstName ){this.firstName = firstName;}
    public String getLastName(){return lastName;}
    public void setLastName( String lastName ){this.lastName = lastName;}
    public String getUsername(){return username;}
    public void setUsername( String username ){this.username = username;} 
    public String getEmail(){return email;}
    public void setEmail( String email ){this.email = email;}
    public String getPhone(){return phone;} 
    public void setPhone( String phone ){this.phone = phone;} 
    public String getPassword(){return password;}
    public void setPassword( String password ){this.password = password;} 
    
    public boolean getViewState() {return viewState;}
    public void setViewState(boolean viewState) {this.viewState = viewState;}
    
        // Method to initialize form
    public void init() {
        // Code to initialize form fields
    }
    
    //Saves user information
    public String save() throws SQLException {
        
        //Connecting to DataBase
        String url = "jdbc:derby://localhost:1527/Chemicals";
        Connection connection = DriverManager.getConnection(url, "APP", "APP");
        
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM APP.USERS WHERE username = ?");
        ps.setString(1, username);
        ResultSet rs1 = ps.executeQuery();
        ps = connection.prepareStatement("SELECT * FROM APP.USERS WHERE email = ?");
        ps.setString(1, email);
        ResultSet rs2 = ps.executeQuery();
        ps = connection.prepareStatement("SELECT * FROM APP.USERS WHERE phone = ?");
        ps.setString(1, phone);
        ResultSet rs3 = ps.executeQuery();
            
        if (rs1.next()){
            return "Username Already in Use";
        }else if(rs2.next()){
            return "Email Already in Use";
        }else if(rs3.next()){
            return "Phone Number Already in Use";
        }else {
            if (connection == null) //Obtain connection 
                throw new SQLException("Unable to connect to DataSource"); //check if connection was successfull
                try {
                    // Create a PreparedStatement to insert the user info data into the database
                    ps = connection.prepareStatement("INSERT INTO APP.USERS (FIRSTNAME, LASTNAME, USERNAME, EMAIL, PHONE, PASSWORD) VALUES (?, ?, ?, ?, ?, ?)");

                    //Specify the PreparedStatements arguments
                    ps.setString(1, getFirstName());
                    ps.setString(2, getLastName());
                    ps.setString(3, getUsername());
                    ps.setString(4, getEmail());
                    ps.setString(5, getPhone());
                    ps.setString(6, getPassword());

                    ps.executeUpdate(); //insert the data
                    return "login";
                } finally {
                    connection.close(); //Return connection to pool
                }
        }
    }
    
    //Update User Information
    public String update() throws SQLException {
        //Connecting to DataBase
        String url = "jdbc:derby://localhost:1527/Chemicals";
        Connection connection = DriverManager.getConnection(url, "APP", "APP");
        if (connection == null) //Obtain connection 
                throw new SQLException("Unable to connect to DataSource"); //check if connection was successfull
                try {
                PreparedStatement ps = connection.prepareStatement("UPDATE APP.USERS SET FIRSTNAME=?, LASTNAME=?, EMAIL=?, PHONE=?, PASSWORD=? WHERE USERNAME=?");

                //Specify the PreparedStatements arguments
                ps.setString(1, getFirstName());
                ps.setString(2, getLastName());
                ps.setString(3, getEmail());
                ps.setString(4, getPhone());
                ps.setString(5, getPassword());
                ps.setString(6, username);
                
                ps.executeUpdate();
                return "profile";
            } finally {
                connection.close(); //Return connection to pool
            }
    
    }


    
    //Login to Website
    public String login() throws SQLException {
        
        // Connect to the database
        String url = "jdbc:derby://localhost:1527/Chemicals";
        Connection connection = DriverManager.getConnection(url, "APP", "APP");
        try {
            // Prepare the query to check if the user's credentials are valid
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM APP.USERS WHERE USERNAME = ? AND PASSWORD = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            // Validation - Success means it worked else no
            if (rs.next()) {
                viewState = true;
                return "learn";
            } else {
                // If the query returns no results, the user's credentials are invalid
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid username or password", null));
                return "";
            }
        } finally {
            
        }
    }
    
    // Method to log out user and invalidate session
    public String logout() {
        viewState = false;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getContextPath() + "login";
    }
    
    //To Delete Users Account
    public String delete() throws SQLException {
        // Connect to the database
        String url = "jdbc:derby://localhost:1527/Chemicals";
        Connection connection = DriverManager.getConnection(url, "APP", "APP");
        try {
            // Prepare the query to delete the user's data from the database
            PreparedStatement ps = connection.prepareStatement("DELETE FROM APP.USERS WHERE USERNAME = ?");
            ps.setString(1, username);

            ps.executeUpdate();
            logout();
            return"login";
        } finally {
            connection.close(); // Return connection to pool
        }
    }

}
