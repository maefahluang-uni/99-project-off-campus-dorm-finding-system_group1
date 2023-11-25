const eye = document.querySelector(".show-hide")
const password = document.querySelector("#password")

eye.addEventListener("click", () => {
    if(password.type === "password"){
        password.type = "text"
        eye.classList.replace("bx-hide", "bx-show");
        eye.style.color = "black";
    }
    else{
        eye.classList.replace("bx-show", "bx-hide");
        eye.style.color = "#919191"
        password.type = "password";
    }
})