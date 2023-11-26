package th.mfu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import th.mfu.Repos.DormitoryRepository;
import th.mfu.Repos.LanalordRepository;
import th.mfu.Repos.RatingRepository;
import th.mfu.Repos.TenantRepository;
import th.mfu.Repos.WishListRepository;
import th.mfu.domain.Dormitory;
import th.mfu.domain.Landlord;
import th.mfu.domain.City;
import th.mfu.domain.Rating;
import th.mfu.domain.Tenant;
import th.mfu.domain.WishList;


@Controller
public class TenantController {
    
    //TODO: creat objects from the repository classes
    @Autowired
    DormitoryRepository dormRepo;
    @Autowired
    LanalordRepository landLordRepo;
    @Autowired
    TenantRepository tenantRepository;
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    WishListRepository wishListRepository;
    //to store the wishlist of the tenant for some checking process
    HashMap<String,List<Integer>> tenantWish = new HashMap<>();
    //to check which user is logged in and which is not
    private HashMap<String,Boolean> isLogged = new HashMap<>();
    //to check which dorms each individual user has rated
    private HashMap<String,List<Integer>> ratedMap = new HashMap<>();
    //private HashSet<Integer> dormIdHashSet = new HashSet<>();
    //To encode and decode password
    private PasswordEncoder pwEncoder = new BCryptPasswordEncoder();

    public TenantController(DormitoryRepository dormRepo, LanalordRepository landLordRepo,
            TenantRepository tenantRepository, RatingRepository ratingRepository,
            WishListRepository wishListRepository) 
    {
        this.dormRepo = dormRepo;
        this.landLordRepo = landLordRepo;
        this.tenantRepository = tenantRepository;
        this.ratingRepository = ratingRepository;
        this.wishListRepository = wishListRepository;
    }
    //the index login page
    @GetMapping("/")
    public String goToLogin(@CookieValue(name="email",defaultValue = "none") String cookieValue, HttpServletRequest request)
    {
        try
        {
            if(tenantRepository.findByEmail(cookieValue).isPresent())
            {
        
                isLogged.put(cookieValue, true);
                return "redirect:/homepage";
            }    
            else
                return "Login";
        }catch(NoSuchElementException e)
        {
            return "Login";
        }
    }
    //for logging in
    @PostMapping("/login")
    public String validate(@RequestParam String email, @RequestParam String password,RedirectAttributes re,@RequestParam(value = "rememberMe", defaultValue = "false") boolean rememberMe,
                            HttpSession session, HttpServletResponse response,@CookieValue(name="email",defaultValue = "none") String cookieValue)
    {
        Tenant temp;
        try
        {
            temp = tenantRepository.findById(email).get();
            if(pwEncoder.matches(password, temp.getPassword()))   
                {
                    //if all details are true, set the cookie of user and allow them to proceed
                    isLogged.put(email, true);
                    Cookie cookie = new Cookie("email", email);
                    cookie.setPath("/");
                    cookie.setMaxAge(3600*24);
                    response.addCookie(cookie);
                    return "redirect:/homepage";
                }
            else{
                //if not show error
                re.addFlashAttribute("error", true);
                return "redirect:/";
            }
        }catch(NoSuchElementException e)
        {
            re.addFlashAttribute("error", true);
            return "redirect:/";
        }
    }
    //to generate view for registration form
    @GetMapping("/register")
    public String goToReg()
    {
        return "SignUp";
    }
    //for new users to register
    @PostMapping("/register")
    public String register(@RequestParam String firstName,
    @RequestParam String lastName,
    @RequestParam String email,
    @RequestParam String phone,
    @RequestParam String password,
    @RequestParam String gender,
    @RequestParam String region,
    RedirectAttributes re,
    HttpServletResponse response,
    @CookieValue(name="email",defaultValue = "none") String cookieValue) 
    {
        //TODO: store the user data in the database
        if(tenantRepository.findById(email).isPresent())
        {
            re.addFlashAttribute("error", true);
            return "redirect:/register";
        }
        else
        {
            String ph=region+phone;
            String encodedPw = pwEncoder.encode(password);
            Tenant t = new Tenant(firstName, lastName, email, gender, ph, encodedPw);
            tenantRepository.save(t);
           
            Cookie cookie = new Cookie("email", email);
            cookie.setPath("/");
            cookie.setMaxAge(24*3600);
            response.addCookie(cookie);
            isLogged.put(email, true);
            return "redirect:/homepage";
        }
    }
   //to show all the details of one dorm based on ID
   @GetMapping("/dorm/{id}")
   public String showDormDetail(@PathVariable int id, Model model,@CookieValue(name="email",defaultValue = "none") String cookieValue)
   {
        if(!isLoggedIn(cookieValue))
            return "redirect:/";
        int dormId = (Integer)id;
        Dormitory dorm = dormRepo.findById(dormId).get();
        Landlord l= landLordRepo.findById(dorm.getLandlord().getEmail()).get();
        String phone = l.getTelephone();
        model.addAttribute("dorm", dorm);
        model.addAttribute("phone", phone);
        model.addAttribute("rating", dorm.getRating().calcRating());
        System.out.println(dorm.getRating().calcRating());
        //checking if the dorm is already in wished list and if it is already rated by the current user 
        //and send attribute to the view to adjust dynamically based on the value
        if(containsWish(cookieValue, dormId))
            model.addAttribute("isWished",true);
        else
            model.addAttribute("isWished",false);
        if(isValuePresent(ratedMap, cookieValue, dormId))
            model.addAttribute("alreadyRated",true);
        else
            model.addAttribute("alreadyRated",false);
        return "DormDetail";
   }
   //to show user account details such as name. email and phone number
   @GetMapping("/userDetail")
   public String showUserDetail(Model model,@CookieValue(name="email",defaultValue = "none") String cookieValue)
   {
        model.addAttribute("tenant", tenantRepository.findById(cookieValue).get());
        return "Profile";
   }
   //showing wishlist
    @GetMapping("/wishlist")
    public String wishlist(Model model,@CookieValue(name="email",defaultValue = "none") String cookieValue)
    {
        if(!isLoggedIn(cookieValue))
            return "redirect:/";
        ArrayList<Dormitory> dorms = new ArrayList<>();
        for (WishList item : wishListRepository.findByTenant(tenantRepository.findById(cookieValue).get())) {
            dorms.add(item.getDormitory());
        }
        model.addAttribute("dormList", dorms);
        return "WishList";
    }
    //adding to wish list
    @GetMapping("/add/{id}")
    public String addToWishList(@PathVariable int id,@CookieValue(name="email",defaultValue = "none") String cookieValue)
    {
        if(!isLoggedIn(cookieValue))
            return "redirect:/";
        //to go back after the process is done
        String path = "/dorm/"+id;
        path.trim();
        WishList wish = new WishList();
        wish.setDormitory(dormRepo.findById(id).get());
        wish.setTenant(tenantRepository.findById(cookieValue).get());
        //to check if the dorm is already in the wishlist and if true, return without doing anything 
        for (WishList list : wishListRepository.findByDormitoryAndTenant(wish.getDormitory(), tenantRepository.findById(cookieValue).get())) {
            if (wish.equals(list))
                return "redirect:/dorm/"+id;
        }
        wishListRepository.save(wish);
        addValue(tenantWish, cookieValue, id);
        return "redirect:/dorm/"+id;
    }
    //removing dorms from wish list
    @GetMapping("/remove/{id}")
    @Transactional
    public String removeItem(@CookieValue(name="email",defaultValue = "none") String cookieValue,@PathVariable int id, HttpServletRequest request)
    {
        if(!isLoggedIn(cookieValue))
            return "redirect:/";
        //to go back to a page beforet this request by getting referer from page header
        String referer = request.getHeader("Referer");
        Dormitory dorm = dormRepo.findById(id).get();
        wishListRepository.deleteByDormitory(dorm);
        //removing from hashmap
        removeValue(tenantWish, cookieValue, id);
        if (referer != null && !referer.isEmpty())
        {
            System.out.println(referer);
            return "redirect:"+referer;
        }
        return "redirect:/wishlist";
    }
    //the dashboard of the website
    @GetMapping("/homepage")
    public String homePage(Model model,@CookieValue(name="email",defaultValue = "none") String cookieValue)
    {
        if(!isLoggedIn(cookieValue))
            return "redirect:/";
        ArrayList<Dormitory> dorms = new ArrayList<>();
        dorms= (ArrayList<Dormitory>) dormRepo.findAll();
        model.addAttribute("dormList", dorms);
        model.addAttribute("cityList", City.values());
        //to notify the view that there are no dorms currently
        if(dorms.isEmpty())
            model.addAttribute("empty",true);
        else
            model.addAttribute("empty",false);
        return"HomePage";
    }
    //for logging out
    @GetMapping("/logout")
    public String logout(HttpServletResponse response,@CookieValue(name="email",defaultValue = "none") String cookieValue)
    {
        //reset and clear cookies
        isLogged.put(cookieValue, false);
        Cookie cookie = new Cookie("email", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "Login";
    }
    //For searching dorms with city
    @GetMapping("/city/{city}")
    public String searchByProvince(@PathVariable String city,Model model,@CookieValue(name="email",defaultValue = "none") String cookieValue,RedirectAttributes re)
    {
         if(!isLoggedIn(cookieValue))
            return "redirect:/";
        System.out.println(city);
        ArrayList<Dormitory> dorms = dormRepo.findByCity(city);
        model.addAttribute("dormList", dorms);
        model.addAttribute("cityList", City.values());
        //to notify the view if the result is empty and show a message accordingly
         if(dorms.isEmpty())
            model.addAttribute("empty",true);
        else
            model.addAttribute("empty",false);
        return"HomePage";
    }
    //For searching dorms with names
    @PostMapping("/search")
    public String searchByName(@RequestParam String dormitoryName,Model model,@CookieValue(name="email",defaultValue = "none") String cookieValue)
    {
         if(!isLoggedIn(cookieValue))
            return "redirect:/";
        ArrayList<Dormitory> dorms = dormRepo.findByNameIgnoreCaseContaining(dormitoryName.trim());
        model.addAttribute("dormList", dorms);
        model.addAttribute("cityList", City.values());
        //to notify the view if the result is empty and show a message accordingly
         if(dorms.isEmpty())
            model.addAttribute("empty",true);
        else
            model.addAttribute("empty",false);
        return"HomePage";
    }
    //for rating the dorms
    @Transactional
    @PostMapping("/rate")
    public String rating(@RequestParam int rating, @RequestParam int ratingId, @RequestParam int dormId,@CookieValue(name="email",defaultValue = "none") String cookieValue)
    {
         if(!isLoggedIn(cookieValue))
            return "redirect:/";
        switch (rating) {
            case 2: ratingRepository.increaseTwoCountById(ratingId);break;
            case 3: ratingRepository.increaseThreeCountById(ratingId);break;
            case 4: ratingRepository.increaseFourCountById(ratingId);break;
            case 5: ratingRepository.increaseFiveCountById(ratingId);break;
            default: ratingRepository.increaseOneCountById(ratingId);
            break;
        }
        addValue(ratedMap, cookieValue, dormId);
        return "redirect:/dorm/"+dormId;
    }
    //for adding a value from a list of a key
    public static <K, V> void addValue(Map<K, List<V>> map, K key, V value) {
        // If the key is not in the map, create a new list for it
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }
    private Boolean containsWish(String email,int id)
    {
        if(tenantWish.containsKey(email))
        {
            List<Integer> values=tenantWish.get(email);
            if(values.contains(id))
                return true;
        }
        return false;
    }
    //remove value from list of a key
    public static <K, V> void removeValue(Map<K, List<V>> map, K key, V value) {
        // Check if the key is present in the map
        if (map.containsKey(key)) {
            // Get the list associated with the key
            List<V> values = map.get(key);

            // Remove the specified value from the list
            values.remove(value);

            // If the list is now empty, remove the key from the map
            if (values.isEmpty()) {
                map.remove(key);
            }
        }
    }
    //For checking whether a value is present in a list of a key
     public static <K, V> Boolean isValuePresent(Map<K, List<V>> map, K key, V value) {
        // Check if the key is present in the map
        if (map.containsKey(key)) {
            // Get the list associated with the key
            List<V> values = map.get(key);
            if(values.contains(value))
                return true;
        }
            return false;
    }
    //For checking if the user is logged in or not with cookies before entering a page for better security
    private Boolean isLoggedIn(String email)
    {
        try{
            if(isLogged.containsKey(email))
                {if(isLogged.get(email))
                        {
                            return true;
                        }
                }
            else
                System.out.println("No logged in has been made");
        }catch(NullPointerException e)
        {
            return false;
        }
        return false;
    }
}   

