package Baloot.Service;

import Baloot.Model.*;
import Baloot.Repository.*;
import Baloot.Util.HttpRequest;
import Baloot.Util.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MarketService {

    final UserRepository users;

    final ProviderRepository providers;

    final CommodityRepository commodities;

    final CommentRepository comments;

    final DiscountRepository discounts;

    final CategoryRepository categories;

    final BuyItemRepository buyItems;

    final RatingRepository ratings;

    public MarketService(UserRepository users, ProviderRepository providers, CommodityRepository commodities, CommentRepository comments, DiscountRepository discounts, CategoryRepository categories, BuyItemRepository buyItems, RatingRepository ratings) {
        this.users = users;
        this.providers = providers;
        this.commodities = commodities;
        this.comments = comments;
        this.discounts = discounts;
        this.categories = categories;
        this.buyItems = buyItems;
        this.ratings = ratings;
    }

    public void clear() {
        users.deleteAll();
        providers.deleteAll();
        commodities.deleteAll();
        comments.deleteAll();
        discounts.deleteAll();
        categories.deleteAll();
        buyItems.deleteAll();
    }

    public void init() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String usersJson = HttpRequest.getHttpResponse("http://5.253.25.110:5000/api/users");
            JSONArray usersArray = JsonParser.parseJsonArray(usersJson);
            for (Object obj : usersArray) {
                try {
                    JSONObject jsonObject = (JSONObject) obj;
                    String username = (String) jsonObject.get("username");
                    String password = (String) jsonObject.get("password");
                    String email = (String) jsonObject.get("email");
                    Date birthDate = dateFormat.parse((String) jsonObject.get("birthDate"));
                    String address = (String) jsonObject.get("address");
                    int credit = (int) (long) jsonObject.get("credit");
                    addUser(username, password, email, birthDate, address, credit);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            String providersJson = HttpRequest.getHttpResponse("http://5.253.25.110:5000/api/v2/providers");
            JSONArray providersArray = JsonParser.parseJsonArray(providersJson);
            for (Object obj : providersArray) {
                try {
                    JSONObject jsonObject = (JSONObject) obj;
                    int id = (int) (long) jsonObject.get("id");
                    String name = (String) jsonObject.get("name");
                    String imageUrl = (String) jsonObject.get("image");
                    Date registryDate = dateFormat.parse((String) jsonObject.get("registryDate"));
                    addProvider(id, name, registryDate, imageUrl);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            String commoditiesJson = HttpRequest.getHttpResponse("http://5.253.25.110:5000/api/v2/commodities");
            JSONArray commoditiesArray = JsonParser.parseJsonArray(commoditiesJson);
            for (Object obj : commoditiesArray) {
                try {
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
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            String discountJson = HttpRequest.getHttpResponse("http://5.253.25.110:5000/api/discount");
            JSONArray discountArray = JsonParser.parseJsonArray(discountJson);
            for (Object obj : discountArray) {
                try {
                    JSONObject jsonObject = (JSONObject) obj;
                    String code = (String) jsonObject.get("discountCode");
                    int percent = (int) (long) jsonObject.get("discount");
                    addDiscount(code, percent);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            String commentsJson = HttpRequest.getHttpResponse("http://5.253.25.110:5000/api/comments");
            JSONArray commentsArray = JsonParser.parseJsonArray(commentsJson);
            for (Object obj : commentsArray) {
                try {
                    JSONObject jsonObject = (JSONObject) obj;
                    String username = getUserByEmail((String) jsonObject.get("userEmail")).getUsername();
                    int commodityId = (int) (long) jsonObject.get("commodityId");
                    String comment = (String) jsonObject.get("text");
                    Date date = dateFormat.parse((String) jsonObject.get("date"));
                    int id = (int) (comments.count() + 1);
                    addComment(id, username, commodityId, comment, date);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void login(String username, String password) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null)
            throw new RuntimeException("Username not found");
        if (!user.checkPassword(password))
            throw new RuntimeException("Wrong password");
    }

    public void signup(String username, String password, String email, String birthdate, String address) throws RuntimeException {
        if (findUserByUsername(username) != null)
            throw new RuntimeException("Username already exists");
        if (users.findByEmail(email) != null)
            throw new RuntimeException("Email already exists");
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate = dateFormat.parse(birthdate);
            addUser(username, password, email, birthDate, address, 0);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format");
        }
    }

    private User findUserByUsername(String username) {
        try {
            return users.findById(username).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private Provider findProviderById(int id) {
        try {
            return providers.findById(id).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private Commodity findCommodityById(int id) {
        try {
            return commodities.findById(id).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private Discount findDiscountByCode(String code) {
        try {
            return discounts.findById(code).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private Category findCategoryByName(String categoryName) {
        try {
            return categories.findById(categoryName).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private Comment findCommentById(int id) {
        try {
            return comments.findById(id).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public void addUser(String username, String password, String email, Date birthDay, String address, int credit) throws RuntimeException {
        CharSequence[] invalidChars = {" ", "‌", "!", "@", "#", "$", "%", "^", "&", "*"};
        for (CharSequence invalidChar : invalidChars) {
            if (username.contains(invalidChar)) {
                throw new RuntimeException("Invalid character in username");
            }
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        users.save(new User(username, hashedPassword, email, birthDay, address, credit));
    }

    public void addProvider(int id, String name, Date registryDate, String imageUrl) throws RuntimeException {
        Provider provider = findProviderById(id);
        if (provider != null) {
            throw new RuntimeException("This id is already registered");
        }
        providers.save(new Provider(id, name, registryDate, imageUrl));
    }

    public void addCommodity(int id, String name, int providerId, int price, ArrayList<String> commodityCategories, float rating, int inStock, String imageUrl) throws RuntimeException {
        Provider provider = findProviderById(providerId);
        if (provider == null) {
            throw new RuntimeException("Provider id not found");
        }
        Commodity commodity = findCommodityById(id);
        if (commodity != null) {
            throw new RuntimeException("This id is already registered");
        }
        List<Category> categoriesList = new ArrayList<>();
        for (String categoryName : commodityCategories) {
            Category category = findCategoryByName(categoryName);
            if (category == null) {
                category = new Category(categoryName);
                categories.save(category);
                categoriesList.add(category);
            } else {
                categoriesList.add(category);
            }
        }
        commodities.save(new Commodity(id, name, providerId, price, categoriesList, rating, inStock, imageUrl));
    }

    public void addComment(int id, String username, int commodityId, String comment, Date date) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Commodity commodity = findCommodityById(commodityId);
        if (commodity == null) {
            throw new RuntimeException("Commodity not found");
        }
        comments.save(new Comment(id, username, commodityId, comment, date));
    }

    public void addComment(String username, int commodityId, String comment) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Commodity commodity = findCommodityById(commodityId);
        if (commodity == null) {
            throw new RuntimeException("Commodity not found");
        }
        comments.save(new Comment((int) (comments.count() + 1), username, commodityId, comment, new Date()));
    }

    public void addDiscount(String code, int percent) throws RuntimeException {
        if (percent < 0 || percent > 100) {
            throw new RuntimeException("Invalid discount percent");
        }
        discounts.save(new Discount(code, percent));
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

    public User getUserByEmail(String email) throws RuntimeException {
        User user = users.findByEmail(email);
        if (user != null) {
            return user;
        }
        throw new RuntimeException("User not found");
    }

    public Comment getCommentById(int id) throws RuntimeException {
        Comment comment = findCommentById(id);
        if (comment != null) {
            return comment;
        }
        throw new RuntimeException("Comment with the given Id doesn't exist!");
    }

    public List<Commodity> getCommoditiesList() {
        return commodities.findAll();
    }

    public List<Commodity> getCommoditiesByCategory(String category) {
        return commodities.findByCategory(category);
    }

    public List<Commodity> getCommoditiesByName(String name) {
        return commodities.findByName(name);
    }

    public List<Commodity> getCommoditiesByProvider(int providerId) {
        return commodities.findByProviderId(providerId);
    }

    public List<Commodity> getCommoditiesByProvider(String providerName) {
        return commodities.findByProviderName(providerName);
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

    //TODO: write it with database
    public List<Commodity> getSuggestedCommodities(int commodityId) throws RuntimeException {
        Commodity criterionCommodity = getCommodityById(commodityId);
        List<Commodity> suggestedCommodities = new ArrayList<>();
        List<Float> scores = new ArrayList<>();
        boolean addCondtion = true;
        for (Commodity com : commodities.findAll()) {
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

    public List<Comment> getCommentListForCommodityById(int commodityId) {
        return comments.findByCommodityId(commodityId);
    }

    public List<Comment> getCommentList() {
        return comments.findAll();
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
        ratings.save(new Rating(username, commodityId, score));
        commodity.addRating(score, username);
        commodities.save(commodity);
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
        buyItems.save(new BuyItem(commodity, user, 1));
        user.addToBuyList(commodity);
    }

    @Transactional
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
        buyItems.deleteBuyItem(commodityId, username);
    }

    @Transactional
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
                if (buyItem.getQuantity() == 1) {
                    removeFromBuyList(username, commodityId);
                }
                else {
                    buyItem.setQuantity(buyItem.getQuantity() - 1);
                    buyItems.save(buyItem);
                }
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
                buyItems.save(buyItem);
                return;
            }
        }
        throw new RuntimeException("Item does not exist.");
    }

    public boolean haveCommonCategory(Commodity commodity1, Commodity commodity2) {
        for (Category category : commodity1.getCategories()) {
            if (commodity2.getCategories().contains(category)) {
                return true;
            }
        }
        return false;
    }

    public void purchase(String username, String discountCode) throws RuntimeException {
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
        totalPrice *= ((double) (100 - discount.getPercent()) / 100);
        for (BuyItem buyItem : buyList) {
            if (this.getCommodityById(buyItem.getCommodity().getId()).getInStock() < buyItem.getQuantity()) {
                throw new RuntimeException("Out of stoke");
            }
        }
        System.out.println(totalPrice);
        System.out.println(user.getCredit());
        if (buyList.isEmpty()) {
            throw new RuntimeException("Buy list is empty");
        }
        if (user.getCredit() < totalPrice) {
            throw new RuntimeException("Not enough credit");
        }
        for (BuyItem buyItem : buyList) {
            buyItem.setPurchased(true);
            buyItems.save(buyItem);
        }
        user.setCredit(user.getCredit() - totalPrice);
        users.save(user);
        for (BuyItem buyItem : buyList) {
            this.getCommodityById(buyItem.getCommodity().getId()).pickFromStock(buyItem.getQuantity());
        }
        discount.use(user);
        discounts.save(discount);
    }

    public void purchase(String username) throws RuntimeException {
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
        if (buyList.isEmpty()) {
            throw new RuntimeException("Buy list is empty");
        }
        for (BuyItem buyItem : buyList) {
            buyItem.setPurchased(true);
            buyItems.save(buyItem);
        }
        user.setCredit(user.getCredit() - totalPrice);
        users.save(user);
        for (BuyItem buyItem : buyList) {
            this.getCommodityById(buyItem.getCommodity().getId()).pickFromStock(buyItem.getQuantity());
        }
    }

    public void addCreditToUser(String username, int credit) throws RuntimeException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (credit <= 0) {
            throw new RuntimeException("Invalid credit");
        }
        user.addCredit(credit);
        users.save(user);
    }

    public void vote(String username, int userVote, int commentId) throws RuntimeException {
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

        comments.save(comment);
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
