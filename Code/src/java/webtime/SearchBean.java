package webtime;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="searchBean")
@SessionScoped
public class SearchBean{
    
    private String searchText;
    private List<Result> autocompleteResults;
    
    public void search() {
        // perform search action with searchText value
    }
    
    public SearchBean() {
        autocomplete();
    }
    
    @PostConstruct
    public void init() {
        autocomplete();
    }
    
    // add results based on searchText value
    public void autocomplete() {
        autocompleteResults = new ArrayList<>();
        autocompleteResults.add(new Result("Hydrogen", "Pages/learn.xhtml#Hydrogen"));
        autocompleteResults.add(new Result("Helium", "Pages/about.xhtml#Helium"));
        autocompleteResults.add(new Result("Lithium", "Pages/contact.xhtml#Lithium"));
        autocompleteResults.add(new Result("Beryllium", "Pages/gallery.xhtml#Beryllium"));
        autocompleteResults.add(new Result("Boron", "Pages/home.xhtml#Boron"));
        autocompleteResults.add(new Result("Carbon", "Pages/services.xhtml#Carbon"));
        autocompleteResults.add(new Result("Nitrogen", "Pages/testimonials.xhtml#Nitrogen"));
        autocompleteResults.add(new Result("Oxygen", "Pages/portfolio.xhtml#Oxygen"));
        autocompleteResults.add(new Result("Fluorine", "Pages/pricing.xhtml#Fluorine"));
        autocompleteResults.add(new Result("Neon", "Pages/learn.xhtml#Neon"));
        autocompleteResults.add(new Result("Sodium", "Pages/about.xhtml#Sodium"));
        autocompleteResults.add(new Result("Magnesium", "Pages/contact.xhtml#Magnesium"));
        autocompleteResults.add(new Result("Aluminum", "Pages/gallery.xhtml#Aluminum"));
        autocompleteResults.add(new Result("Silicon", "Pages/home.xhtml#Silicon"));
        autocompleteResults.add(new Result("Phosphorus", "Pages/services.xhtml#Phosphorus"));
        autocompleteResults.add(new Result("Sulfur", "Pages/testimonials.xhtml#Sulfur"));
        autocompleteResults.add(new Result("Chlorine", "Pages/portfolio.xhtml#Chlorine"));
        autocompleteResults.add(new Result("Argon", "Pages/pricing.xhtml#Argon"));
    
    }
    
    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
    
    public List<Result> getAutoCompleteResults() {
        List<Result> filtered = new ArrayList<>();
        for (Result p : autocompleteResults) {
            if (searchText == null || searchText.trim().isEmpty() ||
                    p.getLabel().toLowerCase().contains(searchText.toLowerCase())) {
                filtered.add(p);
            }
        }
        return filtered;
    }
    
    public static class Result {
        private String label;
        private String link;
        
        public Result(String label, String link) {
            this.label = label;
            this.link = link;
        }

        public String getLabel() {return label;}
        public void setLabel(String label) {this.label = label;}
        public String getLink() {return link;}
        public void setLink(String link) {this.link = link;}
    }
}
