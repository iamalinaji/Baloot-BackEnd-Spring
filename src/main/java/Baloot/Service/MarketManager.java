package Baloot.Service;

import Baloot.Model.*;
import Baloot.Util.HttpRequest;
import Baloot.Util.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MarketManager {
    private final ArrayList<User> users = new ArrayList<>();
    private final ArrayList<Provider> providers = new ArrayList<>();
    private final ArrayList<Commodity> commodities = new ArrayList<>();
    private final ArrayList<Comment> comments = new ArrayList<>();
    private final ArrayList<Discount> discounts = new ArrayList<>();
    private final ArrayList<Category> categories = new ArrayList<>();
    private static MarketManager marketManagerInstance = null;

    private String loggedInUser = "";

    private MarketManager() {

    }

    public boolean isUserLoggedIn() {
        return !loggedInUser.equals("");
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    public void login(String username, String password) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null || !user.checkPassword(password)) {
            throw new RuntimeException("Invalid username or password");
        } else {
            loggedInUser = username;
        }
    }

    public boolean signup(String username, String password, String email, String address, String birthday) throws RuntimeException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate;
        try {
            birthDate = dateFormat.parse(birthday);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        addUser(username, password, email, birthDate, address, 0);
        loggedInUser = username;
        return true;
    }

    public void logout() {
        if (loggedInUser.equals("")) {
            throw new RuntimeException("No user is logged in");
        }
        loggedInUser = "";
    }

    public void clear() {
        users.clear();
        providers.clear();
        commodities.clear();
        comments.clear();
        loggedInUser = "";
    }

    public void init() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String usersJson = HttpRequest.getHttpResponse("http://5.253.25.110:5000/api/users");
            JSONArray usersArray = JsonParser.parseJsonArray(usersJson);
            for (Object obj : usersArray) {
                JSONObject jsonObject = (JSONObject) obj;
                String username = (String) jsonObject.get("username");
                String password = (String) jsonObject.get("password");
                String email = (String) jsonObject.get("email");
                Date birthDate = dateFormat.parse((String) jsonObject.get("birthDate"));
                String address = (String) jsonObject.get("address");
                int credit = (int) (long) jsonObject.get("credit");
                addUser(username, password, email, birthDate, address, credit);
            }

            String providersJson = HttpRequest.getHttpResponse("http://5.253.25.110:5000/api/v2/providers");
            JSONArray providersArray = JsonParser.parseJsonArray(providersJson);
            for (Object obj : providersArray) {
                JSONObject jsonObject = (JSONObject) obj;
                int id = (int) (long) jsonObject.get("id");
                String name = (String) jsonObject.get("name");
                String imageUrl = (String) jsonObject.get("image");
                Date registryDate = dateFormat.parse((String) jsonObject.get("registryDate"));
                addProvider(id, name, registryDate, imageUrl);
            }

            String commoditiesJson = HttpRequest.getHttpResponse("http://5.253.25.110:5000/api/v2/commodities");
            JSONArray commoditiesArray = JsonParser.parseJsonArray(commoditiesJson);
            for (Object obj : commoditiesArray) {
                JSONObject jsonObject = (JSONObject) obj;
                int id = (int) (long) jsonObject.get("id");
                String name = (String) jsonObject.get("name");
                int providerId = (int) (long) jsonObject.get("providerId");
                int price = (int) (long) jsonObject.get("price");
                ArrayList<String> categories = JsonParser.parseCategory((JSONArray) jsonObject.get("categories"));
                float rating = (float) (double) jsonObject.get("rating");
                int inStock = (int) (long) jsonObject.get("inStock");
                String imageUrl = (String) jsonObject.get("image");
                addCommodity(id, name, providerId, price, categories, rating, inStock, imageUrl);
            }

            String discountJson = HttpRequest.getHttpResponse("http://5.253.25.110:5000/api/discount");
            JSONArray discountArray = JsonParser.parseJsonArray(discountJson);
            for (Object obj : discountArray) {
                JSONObject jsonObject = (JSONObject) obj;
                String code = (String) jsonObject.get("discountCode");
                int percent = (int) (long) jsonObject.get("discount");
                addDiscount(code, percent);
            }

            String commentsJson = HttpRequest.getHttpResponse("http://5.253.25.110:5000/api/comments");
            JSONArray commentsArray = JsonParser.parseJsonArray(commentsJson);
            for (Object obj : commentsArray) {
                JSONObject jsonObject = (JSONObject) obj;
                String username = Objects.requireNonNull(findUserByEmail((String) jsonObject.get("userEmail"))).getUsername();
                int commodityId = (int) (long) jsonObject.get("commodityId");
                String comment = (String) jsonObject.get("text");
                Date date = dateFormat.parse((String) jsonObject.get("date"));
                int id = comments.size() + 1;
                addComment(id, username, commodityId, comment, date);
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static MarketManager getInstance() {
        if (marketManagerInstance == null)
            marketManagerInstance = new MarketManager();

        return marketManagerInstance;
    }

    private User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    private User findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email))
                return user;
        }
        return null;
    }

    private Provider findProviderById(int id) {
        for (Provider provider : providers) {
            if (provider.getId() == id) {
                return provider;
            }
        }
        return null;
    }

    private Commodity findCommodityById(int id) {
        for (Commodity commodity : commodities) {
            if (commodity.getId() == id) {
                return commodity;
            }
        }
        return null;
    }

    private Discount findDiscountByCode(String code) {
        for (Discount discount : discounts) {
            if (discount.getCode().equals(code)) {
                return discount;
            }
        }
        return null;
    }

    public void addUser(String username, String password, String email, Date birthDay, String address, int credit) throws RuntimeException {
        CharSequence[] invalidChars = {" ", "‌", "!", "@", "#", "$", "%", "^", "&", "*"};
        for (CharSequence invalidChar : invalidChars) {
            if (username.contains(invalidChar)) {
                throw new RuntimeException("Invalid character in username");
            }
        }
        User user = findUserByUsername(username);
        if (user == null) {
            users.add(new User(username, password, email, birthDay, address, credit));
            return;
        }
        user.updateUser(password, email, birthDay, address, credit);
    }

    public void addProvider(int id, String name, Date registryDate, String imageUrl) throws RuntimeException {
        Provider provider = findProviderById(id);
        if (provider != null) {
            throw new RuntimeException("This id is already registered");
        }
        providers.add(new Provider(id, name, registryDate, imageUrl));
    }

    public void addCommodity(int id, String name, int providerId, int price, ArrayList<String> categories, float rating, int inStock, String imageUrl) throws RuntimeException {
        Provider provider = findProviderById(providerId);
        if (provider == null) {
            throw new RuntimeException("Provider id not found");
        }
        Commodity commodity = findCommodityById(id);
        if (commodity != null) {
            throw new RuntimeException("This id is already registered");
        }
        List<Category> categoriesList = new ArrayList<>();
        for (String categoryName : categories) {
            Category category = findCategoryByName(categoryName);
            if (category == null) {
                category = new Category(categoryName);
                categoriesList.add(category);
                categories.add(categoryName);
            } else {
                categoriesList.add(category);
            }
        }
        commodities.add(new Commodity(id, name, providerId, price, categoriesList, rating, inStock, imageUrl));
    }

    private Category findCategoryByName(String categoryName) {
        for (Category category : categories) {
            if (category.getName().equals(categoryName)) {
                return category;
            }
        }
        return null;
    }

    public List<Commodity> getCommoditiesList() {
        return Collections.unmodifiableList(commodities);
    }

    public void rateCommodity(String username, int commodityId, int score) throws RuntimeException {
        if (score < 1 || score > 10) {
            throw new RuntimeException("Invalid score");
        }
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Commodity commodity = findCommodityById(commodityId);
        if (commodity == null) {
            throw new RuntimeException("Commodity not found");
        }
        commodity.addRating(score, username);
    }

    public void addToBuyList(String username, int commodityId) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Commodity commodity = findCommodityById(commodityId);
        if (commodity == null) {
            throw new RuntimeException("Commodity not found");
        }
        if (commodity.getInStock() <= 0) {
            throw new RuntimeException("Out of stoke");
        }
        user.addToBuyList(commodity);
    }

    public void removeFromBuyList(String username, int commodityId) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Commodity commodity = findCommodityById(commodityId);
        if (commodity == null) {
            throw new RuntimeException("Commodity not found");
        }
        user.removeFromBuyList(commodityId);
    }

    public void decrementBuyItem(String username, int commodityId) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Commodity commodity = findCommodityById(commodityId);
        if (commodity == null) {
            throw new RuntimeException("Commodity not found");
        }
        for (BuyItem buyItem : user.getBuyList()) {
            if (buyItem.getCommodity().getId() == commodityId) {
                if (buyItem.getQuantity() == 1)
                    removeFromBuyList(username, commodityId);
                else
                    buyItem.setQuantity(buyItem.getQuantity() - 1);
                return;
            }
        }
        throw new RuntimeException("Item does not exist.");
    }

    public void incrementBuyItem(String username, int commodityId) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Commodity commodity = findCommodityById(commodityId);
        if (commodity == null) {
            throw new RuntimeException("Commodity not found");
        }
        for (BuyItem buyItem : user.getBuyList()) {
            if (buyItem.getCommodity().getId() == commodityId) {
                buyItem.setQuantity(buyItem.getQuantity() + 1);
                return;
            }
        }
        throw new RuntimeException("Item does not exist.");
    }

    public Commodity getCommodityById(int id) throws RuntimeException {
        Commodity commodity = findCommodityById(id);
        if (commodity == null) {
            throw new RuntimeException("Commodity not found");
        }
        return commodity;
    }

    public Provider getProviderById(int id) throws RuntimeException {
        Provider provider = findProviderById(id);
        if (provider == null) {
            throw new RuntimeException("Provider not found");
        }
        return provider;
    }

    public User getUserByUsername(String username) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    public List<Commodity> getCommoditiesByCategory(String category) {
        List<Commodity> temp = new ArrayList<>();
        for (Commodity commodity : commodities) {
            if (commodity.getCategories().contains(category)) {
                temp.add(commodity);
            }
        }
        return temp;
    }

    public List<Commodity> getCommoditiesByName(String name) {
        List<Commodity> temp = new ArrayList<>();
        for (Commodity commodity : commodities) {
            if (commodity.getName().contains(name)) {
                temp.add(commodity);
            }
        }
        return temp;
    }

    public List<Commodity> getCommoditiesByProvider(String providerName) {
        List<Commodity> temp = new ArrayList<>();
        for (Commodity commodity : commodities) {
            Provider provider = findProviderById(commodity.getProviderId());
            if (provider != null && provider.getName().contains(providerName)) {
                temp.add(commodity);
            }
        }
        return temp;
    }

    public List<Commodity> getCommoditiesByProvider(int providerId) {
        List<Commodity> temp = new ArrayList<>();
        for (Commodity commodity : commodities) {
            if (commodity.getProviderId() == providerId) {
                temp.add(commodity);
            }
        }
        return temp;
    }

    public List<Commodity> getCommoditiesSortedByRate() {
        List<Commodity> temp = new ArrayList<>(commodities);
        temp.sort(Comparator.comparing(Commodity::getRating));
        return temp;
    }

    public List<Commodity> getCommoditiesWithinPrice(int startPrice, int endPrice) {
        List<Commodity> temp = new ArrayList<>();
        for (Commodity commodity : commodities) {
            if (commodity.getPrice() >= startPrice && commodity.getPrice() <= endPrice) {
                temp.add(commodity);
            }
        }
        return temp;
    }

    public List<BuyItem> getBuyList(String username) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        List<BuyItem> buyList = user.getBuyList();
        return Collections.unmodifiableList(buyList);
    }


    public List<BuyItem> getPurchasedList(String username) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        List<BuyItem> purchasedList = user.getPurchasedList();
        return Collections.unmodifiableList(purchasedList);
    }

    public List<Commodity> getSuggestedCommodities(int commodityId) throws RuntimeException {
        Commodity criterionCommodity = findCommodityById(commodityId);
        List<Commodity> suggestedCommodities = new ArrayList<>();
        List<Float> scores = new ArrayList<>();
        boolean addCondtion = true;
        for (Commodity com : commodities) {
            if (com.getId() == criterionCommodity.getId()) continue;
            boolean isInSimilarCategory = haveCommonCategory(com, criterionCommodity);
            float score = 11 * (isInSimilarCategory ? 1 : 0) + com.getRating();
            for (int i = 0; i < scores.size(); i++) {
                if (score >= scores.get(i)) {
                    scores.add(i, score);
                    suggestedCommodities.add(i, com);
                    addCondtion = false;
                    break;
                }
            }
            if (addCondtion) {
                scores.add(score);
                suggestedCommodities.add(com);
            }
        }
        return suggestedCommodities.subList(0, Math.min(suggestedCommodities.size(), 4));
    }

    public static boolean haveCommonCategory(Commodity commodity1, Commodity commodity2) {
        for (Category category : commodity1.getCategories()) {
            if (commodity2.getCategories().contains(category)) {
                return true;
            }
        }
        return false;
    }

    public boolean purchase(String username, String discountCode) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Discount discount = findDiscountByCode(discountCode);
        if (discount == null) {
            throw new RuntimeException("Discount not found");
        }
        if (discount.cantUse(username)) {
            throw new RuntimeException("Discount can't be used");
        }

        List<BuyItem> buyList = user.getBuyList();
        int totalPrice = 0;
        for (BuyItem buyItem : buyList) {
            totalPrice += this.getCommodityById(buyItem.getCommodity().getId()).getPrice() * buyItem.getQuantity();
        }
        totalPrice -= totalPrice * discount.getPercent() / 100;
        for (BuyItem buyItem : buyList) {
            if (this.getCommodityById(buyItem.getCommodity().getId()).getInStock() < buyItem.getQuantity()) {
                throw new RuntimeException("Out of stoke");
            }
        }
        user.purchase(totalPrice);
        for (BuyItem buyItem : buyList) {
            this.getCommodityById(buyItem.getCommodity().getId()).pickFromStock(buyItem.getQuantity());
        }
        discount.use(username);
        return true;
    }

    public boolean purchase(String username) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        List<BuyItem> buyList = user.getBuyList();
        int totalPrice = 0;
        for (BuyItem buyItem : buyList) {
            totalPrice += this.getCommodityById(buyItem.getCommodity().getId()).getPrice() * buyItem.getQuantity();
        }
        if (user.getCredit() < totalPrice) {
            throw new RuntimeException("Not enough credit");
        }
        for (BuyItem buyItem : buyList) {
            if (this.getCommodityById(buyItem.getCommodity().getId()).getInStock() < buyItem.getQuantity()) {
                throw new RuntimeException("Out of stoke");
            }
        }
        user.purchase(totalPrice);
        for (BuyItem buyItem : buyList) {
            this.getCommodityById(buyItem.getCommodity().getId()).pickFromStock(buyItem.getQuantity());
        }
        return true;
    }

    public boolean addCreditToUser(String username, int credit) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (credit <= 0) {
            throw new RuntimeException("Invalid credit");
        }
        user.addCredit(credit);
        return true;
    }

    public boolean addComment(int id, String username, int commodityId, String comment, Date date) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Commodity commodity = findCommodityById(commodityId);
        if (commodity == null) {
            throw new RuntimeException("Commodity not found");
        }
        comments.add(new Comment(id, username, commodityId, comment, date));
        return true;
    }

    public boolean addComment(String username, int commodityId, String comment) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Commodity commodity = findCommodityById(commodityId);
        if (commodity == null) {
            throw new RuntimeException("Commodity not found");
        }
        comments.add(new Comment(comments.size() + 1, username, commodityId, comment, new Date()));
        return true;
    }

    public ArrayList<Comment> getCommentListForCommodityById(int commodityId) {
        ArrayList<Comment> commentsToBeReturned = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.getCommodityId() == commodityId)
                commentsToBeReturned.add(comment);
        }
        return commentsToBeReturned;
    }

    public List<Comment> getCommentList() {
        return Collections.unmodifiableList(comments);
    }

    public Comment getCommentById(int id) throws RuntimeException {
        for (Comment comment : comments) {
            if (comment.getId() == id)
                return comment;
        }
        throw new RuntimeException("Comment with the given Id doesn't exist!");
    }

    public boolean vote(String username, int userVote, int commentId) throws RuntimeException {
        Comment comment = getCommentById(commentId);
        if (comment == null) {
            throw new RuntimeException("Comment not found");
        }
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (userVote == 1) {
            comment.upVote(user);
        } else if (userVote == -1) {
            comment.downVote(user);
        } else if (userVote == 0) {
            comment.removeVote(user);
        } else {
            throw new RuntimeException("Invalid vote");
        }
        return true;
    }

    public boolean addDiscount(String code, int percent) throws RuntimeException {
        if (percent < 0 || percent > 100) {
            throw new RuntimeException("Invalid discount percent");
        }
        discounts.add(new Discount(code, percent));
        return true;
    }

    public boolean canUserUseDiscount(String username, String discountCode) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Discount discount = findDiscountByCode(discountCode);
        if (discount == null) {
            throw new RuntimeException("Discount not found");
        }
        if (discount.cantUse(username)) {
            throw new RuntimeException("User can't use this discount");
        }
        return true;
    }

    public int getDiscountPercent(String discountCode) throws RuntimeException {
        Discount discount = findDiscountByCode(discountCode);
        if (discount == null) {
            throw new RuntimeException("Discount not found");
        }
        return discount.getPercent();
    }
}
