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
    //for the current user so that we will not always need to run the findById funciton
    //Tenant tenant = new Tenant();
    HashMap<String,List<Integer>> tenantWish = new HashMap<>();
    //private Boolean isLoggedin=false;
    private HashMap<String,Boolean> isLogged = new HashMap<>();
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
                    isLogged.put(email, true);
                    if (rememberMe) {
                        Cookie cookie = new Cookie("email", email);
                        cookie.setPath("/");
                        cookie.setMaxAge(3600*24);
                        response.addCookie(cookie);
                    }
                    
                    return "redirect:/homepage";
                }
            else{
                re.addFlashAttribute("error", true);
                return "redirect:/";
            }
        }catch(NoSuchElementException e)
        {
            re.addFlashAttribute("error", true);
            return "redirect:/";
        }
        
    }

    @GetMapping("/register")
    public String goToReg()
    {
        return "SignUp";
    }
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
            isLogged.put(cookieValue, true);
            return "redirect:/homepage";
        }
    }

   @GetMapping("/home")
   public String home(Model model,HttpSession session,@CookieValue(name="email",defaultValue = "none") String cookieValue)
   {
        
        model.addAttribute("error", true);
        model.addAttribute("tenants", tenantRepository.findById(cookieValue).get());
        model.addAttribute("hasSession", "false");
        //TODO: get the user detail and display the data of dorms with 10 results per page
        return "home";
   }
   
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
        if(containsWish(cookieValue, dormId))
            model.addAttribute("isWished",true);
        else
            model.addAttribute("isWished",false);
        return "DormDetail";
   }
   @GetMapping("/userDetail")
   public String showUserDetail(Model model,@CookieValue(name="email",defaultValue = "none") String cookieValue)
   {
        model.addAttribute("tenant", tenantRepository.findById(cookieValue).get());
        return "Profile";
   }
   //TODO: add functions for searching, user account page
    @GetMapping("/wishlist")
    public String wishlist(Model model,@CookieValue(name="email",defaultValue = "none") String cookieValue)
    {
         if(!isLoggedIn(cookieValue))
            return "redirect:/";
        ArrayList<Dormitory> dorms = new ArrayList<>();
        for (WishList item : wishListRepository.findByTenant(tenantRepository.findById(cookieValue).get())) {
            dorms.add(item.getDormitory());
        }
        // for (Dormitory dorm: dorms)
        // {
        //     System.out.println(dorm.getName());
        // }
        model.addAttribute("dormList", dorms);
        return "WishList";
    }
    @GetMapping("/add/{id}")
    public String addToWishList(@PathVariable int id,@CookieValue(name="email",defaultValue = "none") String cookieValue)
    {
         if(!isLoggedIn(cookieValue))
            return "redirect:/";

        String path = "/dorm/"+id;
        path.trim();
        WishList wish = new WishList();
        wish.setDormitory(dormRepo.findById(id).get());
        wish.setTenant(tenantRepository.findById(cookieValue).get());
        for (WishList list : wishListRepository.findByDormitoryAndTenant(wish.getDormitory(), tenantRepository.findById(cookieValue).get())) {
            if (wish.equals(list))
                return "redirect:/dorm/"+id;
        }
        wishListRepository.save(wish);
        addValue(tenantWish, cookieValue, id);
        return "redirect:/dorm/"+id;
    }
    @GetMapping("/remove/{id}")
    @Transactional
    public String removeItem(@CookieValue(name="email",defaultValue = "none") String cookieValue,@PathVariable int id, HttpServletRequest request)
    {
         if(!isLoggedIn(cookieValue))
            return "redirect:/";
        String referer = request.getHeader("Referer");
        Dormitory dorm = dormRepo.findById(id).get();
        wishListRepository.deleteByDormitory(dorm);
        removeValue(tenantWish, cookieValue, id);
        if (referer != null && !referer.isEmpty())
        {
            System.out.println(referer);
            return "redirect:"+referer;
        }
        return "redirect:/wishlist";
    }

    @GetMapping("/homepage")
    public String homePage(Model model,@CookieValue(name="email",defaultValue = "none") String cookieValue)
    {
         if(!isLoggedIn(cookieValue))
            return "redirect:/";
        ArrayList<Dormitory> dorms = new ArrayList<>();
        dorms= (ArrayList<Dormitory>) dormRepo.findAll();
        model.addAttribute("dormList", dorms);
        model.addAttribute("cityList", City.values());
        return"HomePage";
    }
    @GetMapping("/logout")
    public String logout(HttpServletResponse response,@CookieValue(name="email",defaultValue = "none") String cookieValue)
    {
        
        isLogged.put(cookieValue, false);
        Cookie cookie = new Cookie("email", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "Login";
    }
    @GetMapping("/city/{city}")
    public String searchByProvince(@PathVariable String city,Model model,@CookieValue(name="email",defaultValue = "none") String cookieValue)
    {
         if(!isLoggedIn(cookieValue))
            return "redirect:/";
        System.out.println(city);
        ArrayList<Dormitory> dorms = dormRepo.findByCity(city);
        model.addAttribute("dormList", dorms);
        model.addAttribute("cityList", City.values());
        return"HomePage";
    }
    @PostMapping("/search")
    public String searchByName(@RequestParam String dormitoryName,Model model,@CookieValue(name="email",defaultValue = "none") String cookieValue)
    {
         if(!isLoggedIn(cookieValue))
            return "redirect:/";
        ArrayList<Dormitory> dorms = dormRepo.findByNameIgnoreCaseContaining(dormitoryName.trim());
        model.addAttribute("dormList", dorms);
        model.addAttribute("cityList", City.values());
        return"HomePage";
    }
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
        System.out.println("rated"+rating);
        return "redirect:/dorm/"+dormId;
    }
    
    private Integer generateRandom()
    {
        int random =(int)(Math.random()*10000);
        return random;
    }
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
    private Boolean isLoggedIn(String email)
    {
        if(isLogged.get(email))
            return true;
        return false;
    }
}   

